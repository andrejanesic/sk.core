package node;

import exceptions.RootNotInitializedException;

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
     * Podrazumevani konstruktor.
     *
     * @param parent Roditeljski direktorijum.
     * @param name   Naziv direktorijuma.
     */
    private Directory(Directory parent, String name) {
        // proverava da li se path završava sa "/", ako ne, dodaje
        super(parent, parent == null ? "/" : (
                parent.getPath() + (
                        name.substring(name.length() - 1).equals("/") ?
                                name : name + "/"
                ))
        );
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
     * @throws RootNotInitializedException Greška ukoliko se pristupa direktorijumu a još uvek nije inicijalizovan.
     */
    public static Directory getRoot() throws RootNotInitializedException {
        if (root == null) {
            throw new RootNotInitializedException();
        }
        return root;
    }
}
