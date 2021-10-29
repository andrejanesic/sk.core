package com.raf.sk.core.actions;

import com.raf.sk.core.config.IConfig;
import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.*;
import com.raf.sk.core.repository.INode;
import com.raf.sk.core.storage.StorageManager;
import com.raf.sk.core.user.PrivilegeType;

/**
 * Korisnička akcija preuzimanja direktorijuma ili fajla.
 *
 * @see INode
 * @see com.raf.sk.core.io.IODriver
 */
public class ActionINodeDownload implements IAction {

    /**
     * Putanja čvora za preuzimanje.
     */
    private String srcPath;

    /**
     * Putanja direktorijuma gde će čvor biti smešten na OS-u.
     */
    private String sysPath;

    /**
     * Podrazumevani konstruktor.
     *
     * @param srcPath Putanja čvora za preuzimanje.
     * @param sysPath Putanja direktorijuma gde će čvor biti smešten na OS-u.
     */
    public ActionINodeDownload(String srcPath, String sysPath) {
        this.srcPath = srcPath;
        this.sysPath = sysPath;
    }

    @Override
    public Object run() {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(srcPath, PrivilegeType.INODE_DOWNLOAD) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        try {
            //noinspection ConstantConditions
            INode target = Core.getInstance().UserManager().getUser().getCwd().resolvePath(srcPath);
            target.download(sysPath);
            return null;
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        } catch (INodeLimitationException e2) {
            throw new IActionInsufficientPrivilegeException(
                    "The target or destination are limited by limitations that disallow this operation.");
        }
    }

    @Override
    public Object undo() {
        return null;
    }
}
