package repository;

import exceptions.*;
import io.IOManager;
import loader.Loader;
import repository.builder.DirectoryBuilder;
import storage.StorageManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * Klasa direktorijuma. Čuva korenski direktorijum. Predstavlja podstablo čitavog skladišta.
 */
public class Directory extends INode {

    public static final String ROOT_DIRECTORY = INODE_ROOT;

    /**
     * Za sinhronizaciju.
     */
    private static final Object lock = new Object();

    /**
     * Podčvorovi.
     */
    private Collection<INode> children;

    /**
     * Konstruktor na osnovu postojećeg direktorijuma.
     *
     * @param parent Roditeljski direktorijum.
     * @param name   Naziv direktorijuma.
     */
    public Directory(Directory parent, String name) {
        super(parent, name, INodeType.DIRECTORY);

        children = new HashSet<>();
        if (!name.equals(ROOT_DIRECTORY)) {
            if (parent == null)
                throw new INodeFatalException("Non-root node have null parent.");
            if (name.contains("/"))
                throw new INodeFatalException("Node cannot contain illegal character '/' in name.");
        } else {
            if (parent != null)
                throw new INodeFatalException("Root node cannot have a parent.");
        }
        if (parent != null)
            IOManager.getIOHandler().makeDirectory(getPath());
    }

    /**
     * Konstruktor na osnovu bilder klase.
     *
     * @param parent           Roditeljski direktorijum.
     * @param directoryBuilder Bilder.
     */
    public Directory(Directory parent, DirectoryBuilder directoryBuilder) {
        super(parent, directoryBuilder.getName(), INodeType.DIRECTORY);

        children = new HashSet<>();
        if (!directoryBuilder.getName().equals(ROOT_DIRECTORY)) {
            if (parent == null)
                throw new INodeFatalException("Non-root node cannot have null parent.");
            if (directoryBuilder.getName().contains("/"))
                throw new INodeFatalException("Node cannot contain illegal character '/' in name.");
        } else {
            if (parent != null)
                throw new INodeFatalException("Root node cannot have a parent.");
        }
    }

    /**
     * Vraća podčvorove direktorijuma.
     *
     * @return Podčvorovi (i direktorijumi i fajlovi.)
     */
    public Collection<INode> getChildren() {
        return children;
    }

    /**
     * Kreira novi čvor.
     *
     * @param name Naziv čvora.
     * @param type Tip čvora.
     * @return Novonapravljeni čvor.
     * @throws DirectoryMakeNodeNameInvalidException     Greška ukoliko nije ispravan naziv čvora.
     * @throws DirectoryMakeNodeNameNotUniqueException   Greška ukoliko već postoji čvor sa datim nazivom istog tipa.
     * @throws DirectoryMakeNodeInvalidNodeTypeException Greška ukoliko je dati tip čvora null.
     */
    private INode makeNode(String name, INodeType type) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException {
        // #OGRANIČENJE
        // proveri da li je naziv čvora ispravan, tj. ne završava se sa "/"
        if (name == null ||
                name.substring(name.length() - 1).equals("/") ||
                name.equals(ROOT_DIRECTORY))
            throw new DirectoryMakeNodeNameInvalidException(name);

        // #OGRANIČENJE
        // proveri da li već postoji čvor istog tipa
        for (INode i : children) {
            if (i.getName().equals(name) && i.getType().equals(type))
                throw new DirectoryMakeNodeNameNotUniqueException(name);
        }

        // #OGRANIČENJE
        // proveri da li je validan INodeType
        if (type == null) {
            throw new DirectoryMakeNodeInvalidNodeTypeException();
        }

        // inicijalizuj čvor
        INode i;
        if (type.equals(INodeType.DIRECTORY)) {
            i = new Directory(this, name);
        } else {
            i = new File(this, name);
        }

        children.add(i);
        return i;
    }

    /**
     * Kreira novi direktorijum.
     *
     * @param name Naziv direktorijuma.
     * @return Novonapravljeni direktorijum.
     * @throws DirectoryMakeNodeNameInvalidException     Greška ukoliko nije ispravan naziv direktorijuma.
     * @throws DirectoryMakeNodeNameNotUniqueException   Greška ukoliko već postoji direktorijum sa datim nazivom.
     * @throws DirectoryMakeNodeInvalidNodeTypeException Greška ukoliko je dati tip čvora null.
     */
    public Directory makeDirectory(String name) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException {
        return (Directory) makeNode(name, INodeType.DIRECTORY);
    }

    /**
     * Kreira novi fajl.
     *
     * @param name Naziv fajla.
     * @return Novonapravljeni fajl.
     * @throws DirectoryMakeNodeNameInvalidException     Greška ukoliko nije ispravan naziv fajla.
     * @throws DirectoryMakeNodeNameNotUniqueException   Greška ukoliko već postoji fajl sa datim nazivom.
     * @throws DirectoryMakeNodeInvalidNodeTypeException Greška ukoliko je dati tip čvora null.
     */
    public File makeFile(String name) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException {
        return (File) makeNode(name, INodeType.FILE);
    }

    @Override
    public void delete() {
        // ako je korenski čvor, nema brisanja
        if (getParent() == null)
            throw new INodeUnsupportedOperationException("Cannot delete root directory.");

        // obriši sebe
        IOManager.getIOHandler().deleteDirectory(getPath());

        // obriši iz roditelja
        ((Directory) getParent()).unlinkNode(this);
    }

    @Override
    public void delete(String path) throws INodeRootNotInitializedException {
        INode target = resolvePath(path);

        // ako nije pozvan na meti za brisanje, prebaci na tu instancu
        if (target != this) {
            target.delete();
            return;
        }

        delete();
    }

    @Override
    public void move(String path) throws INodeRootNotInitializedException {
        INode destNode = resolvePath(path);

        // ako je korenski čvor, nema pomeranja
        if (getParent() == null)
            throw new INodeUnsupportedOperationException("Cannot move root directory.");

        // ako se pomera u isti čvor, samo vrati
        if (destNode == this) return;

        // ako je destinacija fajl, nema pomeranja
        if (!destNode.getType().equals(INodeType.DIRECTORY)) {
            throw new INodeUnsupportedOperationException("Cannot move file into file.");
        }

        // #OGRANIČENJE
        // roditeljski čvor ne može da se pomera u podčvor
        if (destNode.isGrandchild(this)) {
            throw new INodeUnsupportedOperationException("Cannot move parent directory into child directory.");
        }

        // zapamti staru putanju
        String oldPath = getPath();

        // izbriši iz trenutnog čvora
        Directory dest = (Directory) destNode;
        ((Directory) getParent()).unlinkNode(this);

        // dodaj u novi čvor
        dest.linkNode(this);
        this.setParent(dest);

        // pomeri
        IOManager.getIOHandler().moveDirectory(oldPath, getPath());
    }

    /**
     * Registruje novi čvor.
     *
     * @param iNode Novi čvor.
     */
    public void linkNode(INode iNode) {
        if (children.contains(iNode)) return;
        children.add(iNode);
        iNode.setParent(this);
    }

    /**
     * Deregistruje čvor, ukoliko postoji u direktorijumu.
     *
     * @param iNode Čvor za deregistraciju.
     */
    public void unlinkNode(INode iNode) {
        if (!children.contains(iNode)) return;
        children.remove(iNode);
    }

    /**
     * Vraća čvor na datoj putanji.
     *
     * @param path Putanja.
     * @return Čvor ukoliko je nađen.
     * @throws INodeRootNotInitializedException Ukoliko korenski čvor nije inicijalizovan.
     */
    public INode resolvePath(String path) throws INodeRootNotInitializedException {
        if (StorageManager.getInstance().getRoot() == null)
            throw new INodeRootNotInitializedException();

        if (path == null)
            throw new DirectoryInvalidPathException("null");

        // "očisti" path
        path = path.trim();
        if (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);

        // odredi početni direktorijum
        String pathOld = path;
        Directory curr;

        if (path.equals(""))
            return StorageManager.getInstance().getRoot();
        if (path.equals(".") || path.equals("./"))
            return this;

        if (path.substring(0, 1).equals(ROOT_DIRECTORY)) {
            curr = StorageManager.getInstance().getRoot();
            path = path.substring(1);
        } else {
            if (path.length() > 1 && path.substring(0, 2).equals("./"))
                path = path.substring(2);
            curr = this;
        }

        // ukoliko je "očišćeni" string prazan, prekini
        if (path.length() == 0)
            throw new DirectoryInvalidPathException(pathOld);

        // traži se sledeći prelaz u direktorijum
        int i = 0, j;
        String name;
        INode next = curr;
        path = path + "/"; // dodajemo "/" na kraj radi jednostavnosti algoritma

        while ((j = path.indexOf('/', i)) != -1) {
            // proveri da li je prethodni korak bio fajl, ako jeste, loša putanja
            if (next != null && next.getType().equals(INodeType.FILE))
                throw new DirectoryInvalidPathException(pathOld);

            boolean found = false;
            name = path.substring(i, j);
            for (INode child : ((Directory) next).children) {
                if (child.getName().equals(name)) {
                    next = child;
                    found = true;
                    break;
                }
            }

            if (!found) throw new DirectoryInvalidPathException(pathOld);
            i = j + 1;
        }

        return next;
    }
}
