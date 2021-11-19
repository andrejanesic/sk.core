package com.raf.sk.core.repository;

import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.*;
import com.raf.sk.core.user.IPrivilege;
import com.raf.sk.core.user.IUser;
import com.raf.sk.specification.builders.DirectoryBuilder;
import com.raf.sk.specification.builders.FileBuilder;
import com.raf.sk.specification.io.IOManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

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
     * @param write  Da li upisati čvor preko {@link com.raf.sk.core.io.IODriver}.
     * @param parent Roditeljski direktorijum.
     * @param name   Naziv direktorijuma.
     */
    public Directory(boolean write, Directory parent, String name) {
        super(parent, name, INodeType.DIRECTORY);

        children = new HashSet<>();
        if (!name.equals(ROOT_DIRECTORY)) {
            if (parent == null)
                throw new INodeFatalException("Non-root node cannot have null parent.");
            if (name.contains("/"))
                throw new INodeFatalException("Node cannot contain illegal character '/' in name.");
        } else {
            if (parent != null)
                throw new INodeFatalException("Root node cannot have a parent.");
        }
        if (parent != null && write)
            IOManager.getIODriver().makeDirectory(getPath());
    }

    /**
     * Konstruktor na osnovu bilder klase.
     *
     * @param write            Da li upisati čvor preko {@link com.raf.sk.core.io.IODriver}.
     * @param parent           Roditeljski direktorijum.
     * @param directoryBuilder Bilder.
     */
    public Directory(boolean write, Directory parent, DirectoryBuilder directoryBuilder) {
        this(write, parent, directoryBuilder.getName());
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
     * @throws INodeLimitationException                  Ukoliko postoji ograničenje koje onemogućava operaciju na čvoru.
     */
    private INode makeNode(String name, INodeType type) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeLimitationException {

        // proveri da li je legalna operacija
        checkLimitations(INodeOperation.ADD_CHILD_TO_SELF, name, type);

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
            i = new Directory(true, this, name);
        } else {
            i = new File(true, this, name);
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
     * @throws INodeLimitationException                  Ukoliko postoji ograničenje koje onemogućava operaciju na čvoru.
     */
    public Directory makeDirectory(String name) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeLimitationException {
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
     * @throws INodeLimitationException                  Ukoliko postoji ograničenje koje onemogućava operaciju na čvoru.
     */
    public File makeFile(String name) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeLimitationException {
        return (File) makeNode(name, INodeType.FILE);
    }

    @Override
    public void delete() throws INodeLimitationException {
        // ako je korenski čvor, nema brisanja
        if (getParent() == null)
            throw new INodeUnsupportedOperationException("Cannot delete root directory.");

        // proveri da li je legalna operacija
        checkLimitations(INodeOperation.DELETE_SELF);
        getParent().checkLimitations(INodeOperation.DELETE_CHILD);

        // obriši sebe
        IOManager.getIODriver().deleteDirectory(getPath());

        String oldPath = getPath();

        // obriši iz roditelja
        ((Directory) getParent()).unlinkNode(this);

        // apdejtuj privilegije
        for (IUser u : Core.getInstance().UserManager().getUsers()) {
            //noinspection ForLoopReplaceableByForEach
            for (Iterator<IPrivilege> it = u.getPrivileges().iterator(); it.hasNext(); ) {
                IPrivilege p = it.next();
                if (p.getReferencedObject() == null) continue;
                if (!p.getReferencedObject().equals(oldPath)) continue;
                u.revokePrivilege(p);
            }
        }
    }

    @Override
    public void delete(String path) throws INodeRootNotInitializedException, INodeLimitationException {
        INode target = resolvePath(path);

        // ako nije pozvan na meti za brisanje, prebaci na tu instancu
        if (target != this) {
            target.delete();
            return;
        }

        delete();
    }

    @Override
    public void move(String path) throws INodeRootNotInitializedException, INodeLimitationException {
        INode destNode = resolvePath(path);

        // ako je korenski čvor, nema pomeranja
        if (getParent() == null)
            throw new INodeUnsupportedOperationException("Cannot move root directory.");

        // proveri da li je legalna operacija
        checkLimitations(INodeOperation.MOVE, destNode);
        getParent().checkLimitations(INodeOperation.DELETE_CHILD, this);
        destNode.checkLimitations(INodeOperation.ADD_CHILD_TO_SELF, this);

        // ako se pomera u isti čvor, samo vrati
        if (destNode == this) return;

        // ako je destinacija fajl, nema pomeranja
        if (!destNode.getType().equals(INodeType.DIRECTORY)) {
            throw new INodeUnsupportedOperationException("Cannot move directory into file.");
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
        IOManager.getIODriver().moveDirectory(oldPath, getPath());

        // apdejtuj privilegije
        for (IUser u : Core.getInstance().UserManager().getUsers()) {
            //noinspection ForLoopReplaceableByForEach
            for (Iterator<IPrivilege> it = u.getPrivileges().iterator(); it.hasNext(); ) {
                IPrivilege p = it.next();
                if (p.getReferencedObject() == null) continue;
                if (!p.getReferencedObject().equals(oldPath)) continue;
                u.revokePrivilege(p);
                u.grantPrivilege(getPath(), p.getType());
            }
        }
    }

    @Override
    public INode upload(String path) throws INodeLimitationException {
        // proveri da li je legalna operacija
        checkLimitations(INodeOperation.ADD_CHILD_TO_SELF);

        // preuzmi
        FileBuilder fb = IOManager.getIODriver().uploadFile(getPath(), path);
        File f = new File(false, this, fb);
        this.linkNode(f);
        return f;
    }

    @Override
    public void download(String path) throws INodeLimitationException {
        // proveri da li je legalna operacija
        checkLimitations(INodeOperation.DOWNLOAD);

        // preuzmi
        IOManager.getIODriver().downloadDirectory(getPath(), path);
    }

    /**
     * Registruje novi čvor.
     *
     * @param iNode Novi čvor.
     * @throws INodeLimitationException Ukoliko postoji ograničenje koje onemogućava operaciju na čvoru.
     */
    public void linkNode(INode iNode) throws INodeLimitationException {
        if (children.contains(iNode)) return;
        checkLimitations(INodeOperation.ADD_CHILD_TO_SELF, iNode);
        children.add(iNode);
        iNode.setParent(this);
    }

    /**
     * Deregistruje čvor, ukoliko postoji u direktorijumu.
     *
     * @param iNode Čvor za deregistraciju.
     * @throws INodeLimitationException Ukoliko postoji ograničenje koje onemogućava operaciju na čvoru.
     */
    public void unlinkNode(INode iNode) throws INodeLimitationException {
        if (!children.contains(iNode)) return;
        checkLimitations(INodeOperation.DELETE_CHILD, iNode);
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
        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new INodeRootNotInitializedException();

        if (path == null)
            throw new DirectoryInvalidPathException("Invalid path: null");

        // "očisti" path
        path = path.trim();
        if (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);

        // odredi početni direktorijum
        String pathOld = path;
        Directory curr;

        if (path.equals(""))
            return Core.getInstance().StorageManager().getRoot();
        if (path.equals(".") || path.equals("./"))
            return this;

        if (path.substring(0, 1).equals(ROOT_DIRECTORY)) {
            curr = Core.getInstance().StorageManager().getRoot();
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
            if (name.equals("..")) {
                if (getParent() == null) {
                    throw new DirectoryInvalidPathException("/../");
                }
                if (next != null) {
                    if (path.substring(j + 1).length() == 0)
                        return getParent();
                    return ((Directory) getParent()).resolvePath(path.substring(j + 1));
                } else
                    throw new DirectoryInvalidPathException("Current cannot be null.");
            }
            //noinspection ConstantConditions
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

    /**
     * Vraća broj {@link File} u direktorijumu.
     *
     * @return Broj {@link File} u direktorijumu.
     */
    public int getFileCount() {
        int count = 0;
        for (INode c : children) {
            if (c.getType().equals(INodeType.FILE)) {
                count++;
            } else {
                count += ((Directory) c).getFileCount();
            }
        }
        return count;
    }

    /**
     * Vraća veličinu direktorijuma u [B].
     *
     * @return Veličina direktorijuma u [B].
     */
    public long getSize() {
        int size = 0;
        for (INode c : children) {
            if (c.getType().equals(INodeType.FILE)) {
                size += ((File) c).getSize();
            } else {
                size += ((Directory) c).getSize();
            }
        }
        return size;
    }
}
