package dummynode;

import exceptions.*;
import repository.Directory;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Testni model klase na kome se testira rad repository komponenti.
 */
public class DummyNode {

    public static List<DummyNode> pool = new ArrayList<>();
    public static List<DummyNode> poolDirs = new ArrayList<>();
    public static List<DummyNode> poolFiles = new ArrayList<>();
    public List<DummyNode> children = new ArrayList<>();
    public DummyNode parent;
    public DummyNodeType type;

    public DummyNode(DummyNode parent, DummyNodeType type) {
        this.parent = parent;
        this.type = type;

        pool.add(this);
        if (type == DummyNodeType.DIR) poolDirs.add(this);
        else poolFiles.add(this);
    }

    /**
     * Generiše testne klase za potrebu testova.
     *
     * @return Kolekcija testnih klasa.
     */
    public static DummyNode generateDummyNodes() {
        DummyNode root = new DummyNode(null, DummyNodeType.DIR);
        return generateDummyNodes(
                root,
                1,
                (int) Math.round(Math.random() * 4) + 3
        );
    }

    /**
     * Generiše testne klase za potrebu testova.
     *
     * @param dummy    Trenutno stablo.
     * @param depth    Dubina stabla.
     * @param maxDepth Maksimalna dubina stabla.
     * @return Kolekcija testnih klasa.
     */
    private static DummyNode generateDummyNodes(DummyNode dummy, int depth, int maxDepth) {
        if (depth == maxDepth)
            return dummy;

        if (dummy.type == DummyNodeType.FILE)
            return dummy;

        // generiši stablo klasa
        int subNodes = (int) Math.round(Math.random() * 20);
        for (int i = 0; i < subNodes; i++) {
            dummy.children.add(new DummyNode(
                    dummy, Math.random() * 10 > 6 ? DummyNodeType.DIR : DummyNodeType.FILE
            ));

        }

        for (DummyNode c : dummy.children)
            generateDummyNodes(c, depth + 1, maxDepth);

        return dummy;
    }

    /**
     * Pretvara dummy stablo u pravo stablo za testiranje.
     *
     * @param ref      Referenca na trenutni pravi čvor.
     * @param dummyRef Referenca na treuntni testni čvor.
     * @return Referenca na pravi čvor sa kojim je funkcija pozvana.
     * @throws DirectoryMakeNodeNameInvalidException     Ukoliko ime čvora nije validno.
     * @throws DirectoryMakeNodeNameNotUniqueException   Ukoliko se ponovi ime čvora.
     * @throws DirectoryMakeNodeInvalidNodeTypeException Ukoliko tip čvora nije validan.
     */
    public static Directory dummyNodeTreeToNodeTree(Directory ref, DummyNode dummyRef) throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException {
        for (DummyNode c : dummyRef.children) {
            if (c.type.equals(DummyNodeType.FILE)) {
                try {
                    ref.makeFile(c.id());
                } catch (INodeLimitationException e) {
                    e.printStackTrace();
                }
            } else {
                Directory t = null;
                try {
                    t = ref.makeDirectory(c.id());
                } catch (INodeLimitationException e) {
                    e.printStackTrace();
                }
                dummyNodeTreeToNodeTree(t, c);
            }
        }
        return ref;
    }

    /**
     * Pretvara dummy stablo u bilder stablo za testiranje.
     *
     * @param ref      Referenca na trenutni pravi čvor.
     * @param dummyRef Referenca na treuntni bilder čvor.
     * @return Referenca na bilder čvor sa kojim je funkcija pozvana.
     */
    public static DirectoryBuilder dummyNodeTreeToBuilderNodeTree(DirectoryBuilder ref, DummyNode dummyRef) {
        for (DummyNode c : dummyRef.children) {
            if (c.type.equals(DummyNodeType.FILE)) {
                ref.addChild(new FileBuilder(ref, c.id(), 0));
            } else {
                DirectoryBuilder t = new DirectoryBuilder(ref, c.id());
                ref.addChild(t);
                dummyNodeTreeToBuilderNodeTree(t, c);
            }
        }
        return ref;
    }

    /**
     * Ispisuje stablo DummyNode-a.
     */
    public void print() {
        print(0);
    }

    /**
     * Ispisuje stablo.
     *
     * @param depth Trenutna dubina.
     */
    private void print(int depth) {
        int t = depth;
        while (t-- > 0)
            System.out.print(" ");
        if (depth > 0) System.out.print("⎿");
        System.out.print(hashCode() + " sub:" + children.size() + " dep:" + depth + " type:" + type);
        for (DummyNode c : children) {
            System.out.print("\n");
            c.print(depth + 1);
        }
    }

    /**
     * Ispisuje apsolutnu putanju do testnog čvora. Početni i razdelni karakter je '/'.
     *
     * @return Putanja do testnog čvora, razdvojena '/' karakterima.
     */
    public String path() {
        if (parent == null) return Directory.ROOT_DIRECTORY;
        return parent.path() + (parent.parent == null ? "" : "/") + hashCode();
    }

    /**
     * Vraća tačno ukoliko je dati iNode direktni roditelj instance čvora ili se nalazi u lancu roditelja.
     *
     * @param node Potencijalni roditelj koga treba proveriti.
     * @return Tačno ukoliko je node (direktni ili indirektni) roditelj čvora, netačno ukoliko nije.
     */
    public boolean isGrandchild(DummyNode node) {
        if (parent == node) return true;
        if (parent == null) return false;
        return parent.isGrandchild(node);
    }

    /**
     * Vraća ID testnog čvora.
     *
     * @return ID testnog čvora.
     */
    public String id() {
        return String.valueOf(hashCode());
    }

    /**
     * Prolazi kroz svu decu i poziva callback funkciju na njima.
     *
     * @param cb Callback funkcija.
     * @throws INodeRootNotInitializedException Ukoliko korenski čvor nije inicijalizovan.
     */
    public void traverse(DummyNodeCallback cb) throws INodeRootNotInitializedException {
        cb.execute(this);
        for (DummyNode c : children)
            c.traverse(cb);
    }
}