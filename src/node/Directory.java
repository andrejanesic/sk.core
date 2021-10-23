package node;

import exceptions.DirectoryCreateNameInvalidException;
import exceptions.DirectoryCreateNameNotUniqueException;

import java.util.Collection;
import java.util.HashSet;

/**
 * Klasa direktorijuma. Čuva korenski direktorijum.
 */
public class Directory extends INode {

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
        // proverava da li se path završava sa "/", ako ne, dodaje
        super(parent,
                parent == null ? "/" : (
                        parent.getPath() + (
                                name.substring(name.length() - 1).equals("/") ?
                                        name : name + "/"
                        )),
                INodeType.DIRECTORY
        );

        children = new HashSet<>();
    }

    /**
     * Inicijalizuje novi korenski direktorijum.
     *
     * @return Korenski direktorijum.
     */
    public static Directory makeRoot() {
        synchronized (lock) {
            root = new Directory(null, "/");
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
     * @throws DirectoryCreateNameInvalidException   Greška ukoliko nije ispravan naziv čvora.
     * @throws DirectoryCreateNameNotUniqueException Greška ukoliko već postoji čvor sa datim nazivom istog tipa.
     */
    private INode makeNode(String name, INodeType type) throws
            DirectoryCreateNameInvalidException,
            DirectoryCreateNameNotUniqueException {
        // #OGRANIČENJE
        // proveri da li je naziv čvora ispravan, tj. ne završava se sa "/"
        if (name.substring(name.length() - 1).equals("/"))
            throw new DirectoryCreateNameInvalidException(name);

        // #OGRANIČENJE
        // proveri da li već postoji čvor istog tipa
        for (INode i : children) {
            if (i.getName().equals(name) && i.getType().equals(type))
                throw new DirectoryCreateNameNotUniqueException(name);
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
     * @throws DirectoryCreateNameInvalidException   Greška ukoliko nije ispravan naziv direktorijuma.
     * @throws DirectoryCreateNameNotUniqueException Greška ukoliko već postoji direktorijum sa datim nazivom.
     */
    public Directory makeDirectory(String name) throws
            DirectoryCreateNameInvalidException,
            DirectoryCreateNameNotUniqueException {
        return (Directory) makeNode(name, INodeType.DIRECTORY);
    }

    /**
     * Kreira novi fajl.
     *
     * @param name Naziv fajla.
     * @return Novonapravljeni fajl.
     * @throws DirectoryCreateNameInvalidException   Greška ukoliko nije ispravan naziv fajla.
     * @throws DirectoryCreateNameNotUniqueException Greška ukoliko već postoji fajl sa datim nazivom.
     */
    public File makeFile(String name) throws
            DirectoryCreateNameInvalidException,
            DirectoryCreateNameNotUniqueException {
        return (File) makeNode(name, INodeType.FILE);
    }
}
