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
 * Korisnička akcija čitanja sadržaja direktorijuma zadatog putanjom.
 *
 * @see com.raf.sk.core.user.User
 * @see com.raf.sk.core.repository.Directory
 */
public class ActionListDirectory implements IAction {

    /**
     * Putanja za izlistavanje.
     */
    private String path;

    /**
     * Podrazumevani konstruktor.
     *
     * @param path Nova putanja CWD.
     */
    public ActionListDirectory(String path) {
        this.path = path;
    }

    @Override
    public Directory run() {
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
            return (Directory) targetNode;
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        }
    }

    @Override
    public Directory undo() {
        return run();
    }
}
