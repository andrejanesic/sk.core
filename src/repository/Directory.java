package repository;

import exceptions.DirectoryMakeNodeInvalidNodeType;
import exceptions.DirectoryMakeNodeNameInvalidException;
import exceptions.DirectoryMakeNodeNameNotUniqueException;
import io.IOManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Klasa direktorijuma. Čuva korenski direktorijum.
 */
public class Directory extends INode {

    public static final String ROOT_DIRECTORY = "root";

    /**
     * Za sinhronizaciju.
     */
    private static final Object lock = new Object();
    /**
     * Korenski direktorijum.
     */
    private static Directory root = null;

    /**
     * Podčvorovi.
     */
    private Collection<INode> children;

    /**
     * Podrazumevani konstruktor.
     *
     * @param parent Roditeljski direktorijum.
     * @param name   Naziv direktorijuma.
     */
    private Directory(Directory parent, String name) {
        super(parent, name, INodeType.DIRECTORY);

        children = new HashSet<>();
        if (parent != null)
            IOManager.getInstance().makeDirectory(getPath());
    }

    /**
     * Inicijalizuje novi korenski direktorijum.
     *
     * @return Korenski direktorijum.
     */
    public static Directory makeRoot() {
        synchronized (lock) {
            // korenski čvor nema ime, tj. ime je ""
            root = new Directory(null, ROOT_DIRECTORY);
            return root;
        }
    }

    /**
     * Vraća korenski direktorijum.
     *
     * @return Korenski direktorijum.
     */
    public static Directory getRoot() {
        return root;
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
     * @throws DirectoryMakeNodeNameInvalidException   Greška ukoliko nije ispravan naziv čvora.
     * @throws DirectoryMakeNodeNameNotUniqueException Greška ukoliko već postoji čvor sa datim nazivom istog tipa.
     * @throws DirectoryMakeNodeInvalidNodeType        Greška ukoliko je dati tip čvora null.
     */
    private INode makeNode(String name, INodeType type) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
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
            throw new DirectoryMakeNodeInvalidNodeType();
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
     * @throws DirectoryMakeNodeNameInvalidException   Greška ukoliko nije ispravan naziv direktorijuma.
     * @throws DirectoryMakeNodeNameNotUniqueException Greška ukoliko već postoji direktorijum sa datim nazivom.
     * @throws DirectoryMakeNodeInvalidNodeType        Greška ukoliko je dati tip čvora null.
     */
    public Directory makeDirectory(String name) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        return (Directory) makeNode(name, INodeType.DIRECTORY);
    }

    /**
     * Kreira novi fajl.
     *
     * @param name Naziv fajla.
     * @return Novonapravljeni fajl.
     * @throws DirectoryMakeNodeNameInvalidException   Greška ukoliko nije ispravan naziv fajla.
     * @throws DirectoryMakeNodeNameNotUniqueException Greška ukoliko već postoji fajl sa datim nazivom.
     * @throws DirectoryMakeNodeInvalidNodeType        Greška ukoliko je dati tip čvora null.
     */
    public File makeFile(String name) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        return (File) makeNode(name, INodeType.FILE);
    }

    @Override
    public void delete() {
        // ako je korenski čvor, nema brisanja
        if (getParent() == null) return;

        // obriši sve podčvorove
        Iterator<INode> it = children.iterator();
        while (it.hasNext()) {
            INode i = it.next();
            if (i.getType().equals(INodeType.DIRECTORY))
                i.delete();
            else
                i.delete();
            it.remove();
        }

        // obriši sebe
        IOManager.getInstance().deleteDirectory(getPath());

        // obriši iz roditelja
        ((Directory) getParent()).unlinkNode(this);
    }

    @Override
    public void move(INode iNode) {
        // ako je korenski čvor, nema pomeranja
        if (getParent() == null) return;

        // ako je destinacija fajl, nema pomeranja
        if (!iNode.getType().equals(INodeType.DIRECTORY)) {
            throw new RuntimeException("Cannot move file into file.");
        }

        // zapamti staru putanju
        String oldPath = getPath();

        // izbriši iz trenutnog čvora
        Directory dest = (Directory) iNode;
        ((Directory) getParent()).unlinkNode(this);

        // dodaj u novi čvor
        dest.linkNode(this);
        this.setParent(dest);

        // pomeri
        IOManager.getInstance().moveFile(oldPath, getPath());
    }

    /**
     * Registruje novi čvor.
     *
     * @param iNode Novi čvor.
     */
    protected void linkNode(INode iNode) {
        if (children.contains(iNode)) return;
        children.add(iNode);
        iNode.setParent(this);
    }

    /**
     * Deregistruje čvor, ukoliko postoji u direktorijumu.
     *
     * @param iNode Čvor za deregistraciju.
     */
    protected void unlinkNode(INode iNode) {
        if (!children.contains(iNode)) return;
        children.remove(iNode);
    }
}
