package com.raf.sk.specification.repository;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.INodeFatalException;
import com.raf.sk.specification.exceptions.INodeLimitationException;
import com.raf.sk.specification.exceptions.INodeRootNotInitializedException;
import com.raf.sk.specification.exceptions.INodeUnsupportedOperationException;
import com.raf.sk.specification.io.IOManager;
import com.raf.sk.specification.repository.builder.FileBuilder;
import com.raf.sk.specification.user.IPrivilege;
import com.raf.sk.specification.user.IUser;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * Klasa fajlova.
 */
public class File extends INode {

    //
    // #TODO: ŠTA AKO JE OVAJ FAJL ZAPRAVO KONFIGURACIONI FAJL? ONEMOGUĆITI SVE!
    //

    /**
     * Veličina fajla u [B].
     */
    private long size;

    /**
     * Podrazumevani konstruktor. Dodaje fajl u sistem preko {@link com.raf.sk.specification.io.IODriver}.
     *
     * @param write  Da li upisati čvor preko {@link com.raf.sk.specification.io.IODriver}.
     * @param parent Roditeljski direktorijum.
     * @param name   Naziv fajla.
     * @param size   Veličina fajla.
     */
    public File(boolean write, INode parent, String name, long size) {
        super(parent, name, INodeType.FILE);
        this.size = size;

        if (parent == null)
            throw new INodeFatalException("File cannot have null parent.");
        if (name.contains("/"))
            throw new INodeFatalException("Node cannot contain illegal character '/' in name.");

        if (write)
            IOManager.getIODriver().makeFile(getPath());
    }

    /**
     * Podrazumevani konstruktor. Dodaje fajl u sistem preko {@link com.raf.sk.specification.io.IODriver}.
     *
     * @param write  Da li upisati čvor preko {@link com.raf.sk.specification.io.IODriver}.
     * @param parent Roditeljski direktorijum.
     * @param name   Naziv fajla.
     */
    public File(boolean write, INode parent, String name) {
        this(write, parent, name, 0);
    }

    /**
     * Kreira novi File na osnovu FileBuilder bildera. UPOZORENJE: ovaj metod <em>NE DODAJE</em> fajl u sistem preko
     * {@link com.raf.sk.specification.io.IODriver}!
     *
     * @param write       Da li upisati čvor preko {@link com.raf.sk.specification.io.IODriver}.
     * @param parent      Roditeljski direktorijum.
     * @param fileBuilder FileBuilder bilder.
     */
    public File(boolean write, Directory parent, FileBuilder fileBuilder) {
        this(write, parent, fileBuilder.getName(), fileBuilder.getSize());
    }

    @Override
    public void delete() throws INodeLimitationException {
        // proveri da li je legalna operacija
        checkLimitations(INodeOperation.DELETE_SELF);
        getParent().checkLimitations(INodeOperation.DELETE_CHILD);

        // obriši sebe
        IOManager.getIODriver().deleteFile(getPath());

        String oldPath = getPath();

        // obriši iz roditelja
        ((Directory) getParent()).unlinkNode(this);

        // apdejtuj privilegije
        for (IUser u : Core.getInstance().UserManager().getUsers()) {
            //noinspection ForLoopReplaceableByForEach
            for (Iterator<IPrivilege> it = u.getPrivileges().iterator(); it.hasNext(); ) {
                IPrivilege p = it.next();
                if (p.getReferencedObject() == null) continue;
                if (!p.getReferencedObject().equals(oldPath)) continue;
                u.revokePrivilege(p);
            }
        }
    }

    @Override
    public void delete(String path) throws INodeRootNotInitializedException, INodeLimitationException {
        INode target = ((Directory) getParent()).resolvePath(path);

        // ako nije pozvan na meti za brisanje, prebaci na tu instancu
        if (target != this) {
            target.delete();
            return;
        }

        delete();
    }

    @Override
    public void move(String path) throws INodeRootNotInitializedException, INodeLimitationException {
        INode iNode = ((Directory) getParent()).resolvePath(path);

        // ako se pomera u isti čvor, samo vrati
        if (iNode == this) return;

        // ako je destinacija fajl, nema pomeranja
        if (!iNode.getType().equals(INodeType.DIRECTORY)) {
            throw new INodeUnsupportedOperationException("Cannot move file into file.");
        }

        // proveri da li je legalna operacija
        checkLimitations(INodeOperation.MOVE, iNode);
        getParent().checkLimitations(INodeOperation.DELETE_CHILD, this);
        iNode.checkLimitations(INodeOperation.ADD_CHILD_TO_SELF, this);

        // zapamti staru putanju
        String oldPath = getPath();

        // izbriši iz trenutnog čvora
        Directory dest = (Directory) iNode;
        ((Directory) getParent()).unlinkNode(this);

        // dodaj u novi čvor
        dest.linkNode(this);
        this.setParent(dest);

        // pomeri
        IOManager.getIODriver().moveFile(oldPath, getPath());

        // apdejtuj privilegije
        for (IUser u : Core.getInstance().UserManager().getUsers()) {
            //noinspection ForLoopReplaceableByForEach
            for (Iterator<IPrivilege> it = u.getPrivileges().iterator(); it.hasNext(); ) {
                IPrivilege p = it.next();
                if (p.getReferencedObject() == null) continue;
                if (!p.getReferencedObject().equals(oldPath)) continue;
                u.revokePrivilege(p);
                u.grantPrivilege(getPath(), p.getType());
            }
        }
    }

    @Override
    public INode upload(String path) {
        throw new INodeUnsupportedOperationException("Cannot upload file into file.");
    }

    @Override
    public void download(String path) throws INodeLimitationException {
        // proveri da li je legalna operacija
        checkLimitations(INodeOperation.DOWNLOAD);

        // preuzmi
        IOManager.getIODriver().downloadFile(getPath(), path);
    }

    /**
     * Vraća fajl ekstenziju na osnovu naziva, ili null ako fajl nema ekstenziju.
     *
     * @return Fajl ekstenzija na osnovu naziva, ili null ako fajl nema ekstenziju.
     */
    @Nullable
    public String getExtension() {
        if (getName() == null) return null;
        if (getName().lastIndexOf('.') == -1) return null;
        return getName().substring(getName().lastIndexOf('.') + 1);
    }

    /**
     * Vraća veličinu fajla u [B].
     *
     * @return Veličina fajla u [B].
     */
    public long getSize() {
        return size;
    }
}
