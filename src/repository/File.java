package repository;

import exceptions.INodeFatalError;
import exceptions.INodeUnsupportedOperationException;
import io.IOManager;
import repository.builder.FileBuilder;

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
    public File(INode parent, String name) {
        super(parent, name, INodeType.FILE);

        if (parent == null)
            throw new INodeFatalError("File cannot have null parent.");
        if (name.contains("/"))
            throw new INodeFatalError("Node cannot contain illegal character '/' in name.");
        IOManager.getIOHandler().makeFile(getPath());
    }

    /**
     * Kreira novi File na osnovu FileBuilder bildera.
     *
     * @param parent      Roditeljski direktorijum.
     * @param fileBuilder FileBuilder bilder.
     */
    public File(Directory parent, FileBuilder fileBuilder) {
        super(parent, fileBuilder.getName(), INodeType.FILE);

        if (parent == null)
            throw new INodeFatalError("File cannot have null parent.");
        if (fileBuilder.getName().contains("/"))
            throw new INodeFatalError("Node cannot contain illegal character '/' in name.");
    }

    @Override
    public void delete() {
        // obriši sebe
        IOManager.getIOHandler().deleteFile(getPath());

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
        IOManager.getIOHandler().moveFile(oldPath, getPath());
    }
}
