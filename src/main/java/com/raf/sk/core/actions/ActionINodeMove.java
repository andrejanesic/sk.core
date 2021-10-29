package com.raf.sk.core.actions;

import com.raf.sk.core.config.IConfig;
import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.*;
import com.raf.sk.core.repository.INode;
import com.raf.sk.core.storage.StorageManager;
import com.raf.sk.core.user.PrivilegeType;

/**
 * Korisnička akcija pomeranja {@link com.raf.sk.core.repository.INode}.
 */
public class ActionINodeMove implements IAction {

    /**
     * Stara putanja čvora.
     */
    private String target;

    /**
     * Putanja novog roditeljskog direktorijuma.
     */
    private String newParent;

    /**
     * Podrazumevani konstruktor.
     *
     * @param target    Stara putanja čvora.
     * @param newParent Nova putanja čvora.
     */
    public ActionINodeMove(String target, String newParent) {
        this.target = target;
        this.newParent = newParent;
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
            String targetPath = targetNode.getPath();

            //noinspection ConstantConditions
            INode destNode = Core.getInstance().UserManager().getUser().getCwd().resolvePath(newParent);
            String destPath = destNode.getPath();

            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(targetPath, PrivilegeType.INODE_DELETE) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();
            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(destPath, PrivilegeType.INODE_ADD) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();

            targetNode.move(destNode);
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
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        String target = this.newParent + '/' + this.target.substring(this.target.lastIndexOf('/') + 1);
        String newParent = this.target.substring(0, this.target.lastIndexOf('/'));

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(target, PrivilegeType.INODE_DELETE) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();
        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(newParent, PrivilegeType.INODE_ADD) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        try {
            //noinspection ConstantConditions
            INode targetNode = Core.getInstance().UserManager().getUser().getCwd().resolvePath(target);
            //noinspection ConstantConditions
            INode destNode = Core.getInstance().UserManager().getUser().getCwd().resolvePath(newParent);
            targetNode.move(destNode);
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        } catch (INodeLimitationException e2) {
            throw new IActionInsufficientPrivilegeException(
                    "The target or destination are limited by limitations that disallow this operation.");
        }
        return true;
    }
}
