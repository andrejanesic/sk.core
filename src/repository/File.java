package repository;

import exceptions.INodeFatalException;
import exceptions.INodeRootNotInitializedException;
import exceptions.INodeUnsupportedOperationException;
import io.IOManager;
import repository.builder.FileBuilder;

/**
 * Klasa fajlova.
 */
public class File extends INode {

    //
    // #TODO: ŠTA AKO JE OVAJ FAJL ZAPRAVO KONFIGURACIONI FAJL? ONEMOGUĆITI SVE!
    //

    /**
     * Podrazumevani konstruktor.
     *
     * @param parent Roditeljski direktorijum.
     * @param name   Naziv fajla.
     */
    public File(INode parent, String name) {
        super(parent, name, INodeType.FILE);

        if (parent == null)
            throw new INodeFatalException("File cannot have null parent.");
        if (name.contains("/"))
            throw new INodeFatalException("Node cannot contain illegal character '/' in name.");
        IOManager.getIOAdapter().makeFile(getPath());
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
            throw new INodeFatalException("File cannot have null parent.");
        if (fileBuilder.getName().contains("/"))
            throw new INodeFatalException("Node cannot contain illegal character '/' in name.");
    }

    @Override
    public void delete() {
        // obriši sebe
        IOManager.getIOAdapter().deleteFile(getPath());

        // obriši iz roditelja
        ((Directory) getParent()).unlinkNode(this);
    }

    @Override
    public void delete(String path) throws INodeRootNotInitializedException {
        INode target = ((Directory) getParent()).resolvePath(path);

        // ako nije pozvan na meti za brisanje, prebaci na tu instancu
        if (target != this) {
            target.delete();
            return;
        }

        delete();
    }

    @Override
    public void move(String path) throws INodeRootNotInitializedException {
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
        IOManager.getIOAdapter().moveFile(oldPath, getPath());
    }
}
