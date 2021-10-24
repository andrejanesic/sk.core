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
        // obriši sebe
        IOManager.getInstance().deleteFile(getPath());

        // obriši iz roditelja
        ((Directory) getParent()).unlinkNode(this);
    }

    @Override
    public void delete(String path) {
        INode target = ((Directory) getParent()).resolvePath(path);

        // ako nije pozvan na meti za brisanje, prebaci na tu instancu
        if (target != this) {
            target.delete();
            return;
        }

        delete();
    }

    @Override
    public void move(String path) {
        INode iNode = ((Directory) getParent()).resolvePath(path);

        // ako se pomera u isti čvor, samo vrati
        if (iNode == this) return;

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
