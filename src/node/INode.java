package node;

/**
 * INode predstavlja jedan čvor u skladištu, koji može biti fajl ili direktorijum.
 */
abstract class INode {

    /**
     * Roditeljski čvor.
     */
    private INode parent;

    /**
     * Naziv čvora.
     */
    private String name;

    /**
     * Podrazumevani konstruktor.
     *
     * @param parent Roditeljski čvor.
     * @param name   Naziv čvora.
     */
    public INode(INode parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    /**
     * Vraća roditeljski čvor.
     *
     * @return Roditeljski čvor.
     */
    public INode getParent() {
        return parent;
    }

    /**
     * Vraća putanju čvora.
     *
     * @return Putanja čvora.
     */
    public String getPath() {
        return (parent == null ? "" : parent.getPath()) + name;
    }
}
