package repository;

import exceptions.INodeUnsupportedOperationException;
import io.IOManager;

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

        IOManager.getInstance().makeFile(getPath());
    }

    @Override
    public void delete() {
        IOManager.getInstance().deleteFile(getPath());
        ((Directory) getParent()).unlinkNode(this);
    }

    @Override
    public void move(INode iNode) {
        // ako je destinacija fajl, nema pomeranja
        if (!iNode.getType().equals(INodeType.DIRECTORY)) {
            throw new INodeUnsupportedOperationException("Cannot move file into file.");
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
}
