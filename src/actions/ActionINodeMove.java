package actions;

import config.IConfig;
import core.Core;
import exceptions.*;
import repository.INode;
import storage.StorageManager;
import user.PrivilegeType;

/**
 * Korisnička akcija pomeranja {@link repository.INode}.
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
