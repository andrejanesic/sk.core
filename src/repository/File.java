package repository;

/**
 * Klasa fajlova.
 */
public class File extends INode {

    /**
     * Podrazumevani konstruktor.
     *
     * @param parent Roditeljski direktorijum.
     * @param name   Naziv fajla.
     */
    File(INode parent, String name) {
        super(parent, name, INodeType.FILE);
    }
}
