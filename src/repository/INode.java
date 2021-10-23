package repository;

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
     * Tip čvora.
     */
    private INodeType type;

    /**
     * Podrazumevani konstruktor.
     *
     * @param parent Roditeljski čvor.
     * @param name   Naziv čvora.
     */
    public INode(INode parent, String name, INodeType type) {
        this.parent = parent;
        this.name = name;
        this.type = type;
    }

    /**
     * Vraća tip čvora.
     *
     * @return Tip čvora.
     */
    public INodeType getType() {
        return type;
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
     * Omogućava promenu roditeljskog čvora. Dozvoljeno samo na nivou paketa radi premeštanja čvorova.
     *
     * @param iNode Novi roditeljski čvor.
     */
    protected void setParent(INode iNode) {
        parent = iNode;
    }

    /**
     * Vraća putanju čvora.
     *
     * @return Putanja čvora.
     */
    public String getPath() {
        // ako je korenski čvor, vratiće samo ime
        if (parent == null) return name;
        return parent.getPath() + "/" + name;
    }

    /**
     * Vraća naziv čvora, bez putanje.
     *
     * @return Naziv čvora.
     */
    public String getName() {
        return name;
    }

    /**
     * Briše sebe iz IO i svoje podčvorove, ukoliko postoje.
     */
    public abstract void delete();

    /**
     * Pomeranje čvora u novi čvor.
     *
     * @param dest Destinacioni čvor.
     */
    public abstract void move(INode dest);
}
