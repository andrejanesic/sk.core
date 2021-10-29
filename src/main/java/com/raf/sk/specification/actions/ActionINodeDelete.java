package com.raf.sk.specification.actions;

import com.raf.sk.specification.config.IConfig;
import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.*;
import com.raf.sk.specification.repository.INode;
import com.raf.sk.specification.storage.StorageManager;
import com.raf.sk.specification.user.PrivilegeType;

/**
 * Korisnička akcija brisanja čvora {@link com.raf.sk.specification.repository.INode}.
 */
public class ActionINodeDelete implements IAction {

    /**
     * Putanja čvora.
     */
    private String target;

    /**
     * Podrazumevani konstruktor.
     *
     * @param target Putanja čvora.
     */
    public ActionINodeDelete(String target) {
        this.target = target;
    }

    @Override
    public Object run() {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        try {
            //noinspection ConstantConditions
            INode targetNode = Core.getInstance().UserManager().getUser().getCwd().resolvePath(target);
            String path = targetNode.getPath();
            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(path, PrivilegeType.INODE_DELETE) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();

            targetNode.delete();
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        } catch (INodeLimitationException e2) {
            throw new IActionInsufficientPrivilegeException(
                    "The target or destination are limited by limitations that disallow this operation.");
        }
        return true;
    }

    @Override
    public Object undo() {
        throw new IActionUndoImpossibleException("deleting a file");
    }
}
