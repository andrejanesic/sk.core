package repository;

import exceptions.INodeFatalError;

/**
 * INode predstavlja jedan čvor u skladištu, koji može biti fajl ili direktorijum.
 */
abstract class INode {

    public static final String INODE_ROOT = "/";

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

        if (this == parent)
            throw new INodeFatalError("Fatal error: INode cannot be its own parent.");
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

        if (this == parent)
            throw new RuntimeException("Fatal error: INode cannot be its own parent.");
    }

    /**
     * Vraća tačno ukoliko je dati iNode direktni roditelj instance čvora ili se nalazi u lancu roditelja.
     *
     * @param iNode Potencijalni roditelj koga treba proveriti.
     * @return Tačno ukoliko je iNode (direktni ili indirektni) roditelj čvora, netačno ukoliko nije.
     */
    public boolean isGrandchild(INode iNode) {
        if (parent == iNode) return true;
        if (parent == null) return false;
        return parent.isGrandchild(iNode);
    }

    /**
     * Vraća putanju čvora.
     *
     * @return Putanja čvora.
     */
    public String getPath() {
        // ako je korenski čvor, vratiće samo ime
        if (parent == null) return INODE_ROOT;
        return parent.getPath() + (parent.getParent() == null ? "" : "/") + name;
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
     * Briše metu iz IO i svoje podčvorove, ukoliko postoje.
     *
     * @param path Putanja do čvora za brisanje.
     */
    public abstract void delete(String path);

    /**
     * Briše sebe iz IO i svoje podčvorove, ukoliko postoje.
     */
    public abstract void delete();

    /**
     * Pomeranje čvora u novi čvor.
     *
     * @param dest Destinacioni čvor.
     */
    public void move(INode dest) {
        move(dest.getPath());
    }

    /**
     * Pomeranje čvora u novi čvor.
     *
     * @param path Putanja destinacionog čvora.
     */
    public abstract void move(String path);

}
