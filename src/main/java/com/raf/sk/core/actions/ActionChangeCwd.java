package com.raf.sk.core.actions;

import com.raf.sk.core.config.IConfig;
import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.IActionBadParameterException;
import com.raf.sk.core.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.core.exceptions.IComponentNotInitializedException;
import com.raf.sk.core.exceptions.INodeRootNotInitializedException;
import com.raf.sk.core.repository.Directory;
import com.raf.sk.core.repository.File;
import com.raf.sk.core.repository.INode;
import com.raf.sk.core.storage.StorageManager;
import com.raf.sk.core.user.PrivilegeType;

/**
 * Korisnička akcija promene trenutno aktivnog direktorijuma.
 *
 * @see com.raf.sk.core.user.User
 */
public class ActionChangeCwd implements IAction {

    /**
     * Stara putanja CWD.
     */
    private String oldPath;

    /**
     * Nova putanja CWD.
     */
    private String path;

    /**
     * Da li je poslednja egzekucija radnje bila run() ili undo().
     */
    private boolean lastRun = false;

    /**
     * Podrazumevani konstruktor.
     *
     * @param path Nova putanja CWD.
     */
    public ActionChangeCwd(String path) {
        this.path = path;
    }

    @Override
    public Object run() {
        if (lastRun) return null;
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        try {
            //noinspection ConstantConditions
            INode targetNode = Core.getInstance().UserManager().getUser().getCwd().resolvePath(path);

            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(targetNode.getPath(), PrivilegeType.INODE_READ) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();

            if (targetNode instanceof File)
                throw new IActionBadParameterException("The path you specified is a file.");
            //noinspection ConstantConditions
            oldPath = Core.getInstance().UserManager().getUser().getCwd().getPath();
            Core.getInstance().UserManager().getUser().setCwd((Directory) targetNode);
            lastRun = true;
            return targetNode;
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        }
    }

    @Override
    public Object undo() {
        if (!lastRun) return null;
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(oldPath, PrivilegeType.INODE_READ) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        try {
            //noinspection ConstantConditions
            INode targetNode = Core.getInstance().UserManager().getUser().getCwd().resolvePath(oldPath);
            if (targetNode instanceof File)
                throw new IActionBadParameterException("The path you specified is a file.");
            //noinspection ConstantConditions
            Core.getInstance().UserManager().getUser().setCwd((Directory) targetNode);
            lastRun = false;
            return targetNode;
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        }
    }
}
