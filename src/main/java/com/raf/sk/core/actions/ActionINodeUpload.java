package com.raf.sk.core.actions;

import com.raf.sk.core.config.IConfig;
import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.*;
import com.raf.sk.core.repository.INode;
import com.raf.sk.core.repository.INodeType;
import com.raf.sk.core.storage.StorageManager;
import com.raf.sk.core.user.PrivilegeType;

/**
 * Korisnička akcija otpremanja fajla u direktorijum.
 *
 * @see com.raf.sk.core.repository.Directory
 * @see com.raf.sk.specification.io.IODriver
 */
public class ActionINodeUpload implements IAction {

    /**
     * Putanja destinacionog direktorijuma.
     */
    private String destPath;

    /**
     * Putanja fajla za otpremanje u okviru OS-a.
     */
    private String filePath;

    /**
     * Putanja otpremljenog fajla, ako je uspešno.
     */
    private String uploadedPath;

    /**
     * Podrazumevani konstruktor.
     *
     * @param destPath Putanja destinacionog direktorijuma.
     * @param filePath Putanja fajla za otpremanje u okviru OS-a.
     */
    public ActionINodeUpload(String destPath, String filePath) {
        this.destPath = destPath;
        this.filePath = filePath;
    }

    @Override
    public Object run() {
        if (uploadedPath != null) return null;
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        try {
            //noinspection ConstantConditions
            INode destNode = Core.getInstance().UserManager().getUser().getCwd().resolvePath(destPath);

            if (destNode.getType().equals(INodeType.FILE))
                throw new IActionBadParameterException("Destination cannot be a file.");

            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(destNode.getPath(), PrivilegeType.INODE_ADD) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();

            INode f = destNode.upload(filePath);
            uploadedPath = f.getPath();
            return f;
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        } catch (INodeLimitationException e2) {
            throw new IActionInsufficientPrivilegeException(
                    "The target or destination are limited by limitations that disallow this operation.");
        }
    }

    @Override
    public Object undo() {
        if (uploadedPath == null) return null;
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        try {
            //noinspection ConstantConditions
            INode uploadedNode = Core.getInstance().StorageManager().getRoot().resolvePath(uploadedPath);

            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(uploadedNode, PrivilegeType.INODE_DELETE) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();

            //noinspection ConstantConditions
            uploadedNode.delete();
            uploadedPath = null;
            return null;
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The file is no longer located on its original path.");
        } catch (INodeLimitationException e2) {
            throw new IActionInsufficientPrivilegeException(
                    "The target or destination are limited by limitations that disallow this operation.");
        }
    }
}
