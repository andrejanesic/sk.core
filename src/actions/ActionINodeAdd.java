package actions;

import config.IConfig;
import core.Core;
import exceptions.*;
import repository.Directory;
import repository.INode;
import storage.StorageManager;
import user.PrivilegeType;

/**
 * Korisnička akcija dodavanja novog direktorijuma ili fajla.
 *
 * @see repository.INode
 * @see io.IODriver
 */
public class ActionINodeAdd implements IAction {

    /**
     * Naziv novog čvora.
     */
    private String name;

    /**
     * Tip čvora.
     */
    private String type;

    /**
     * Podrazumevani konstruktor.
     *
     * @param name Naziv novog čvora.
     * @param type Tip čvora.
     */
    public ActionINodeAdd(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Object run() {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(
                Core.getInstance().UserManager().getUser().getCwd().getPath(), PrivilegeType.INODE_ADD) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        try {
            //noinspection ConstantConditions
            Directory targetNode = Core.getInstance().UserManager().getUser().getCwd();
            if (type == null)
                throw new IActionBadParameterException("The type you specified is not valid. Must be FILE or DIR.");
            if (type.equals("FILE")) {
                targetNode.makeFile(name);
                return true;
            }
            if (type.equals("DIR")) {
                targetNode.makeDirectory(name);
                return true;
            }
            throw new IActionBadParameterException("The type you specified is not valid. Must be FILE or DIR.");
        } catch (INodeLimitationException e2) {
            throw new IActionInsufficientPrivilegeException(
                    "The target or destination are limited by limitations that disallow this operation.");
        } catch (DirectoryMakeNodeNameInvalidException e) {
            e.printStackTrace();
        } catch (DirectoryMakeNodeInvalidNodeTypeException e) {
            e.printStackTrace();
        } catch (DirectoryMakeNodeNameNotUniqueException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Object undo() {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        try {
            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(
                    Core.getInstance().UserManager().getUser().getCwd().resolvePath(name), PrivilegeType.INODE_DELETE) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();

            //noinspection ConstantConditions
            INode targetNode = Core.getInstance().UserManager().getUser().getCwd().resolvePath(name);
            targetNode.delete();
        } catch (INodeRootNotInitializedException e1) {
            throw new IActionBadParameterException("The newly created file/folder no longer exists.");
        } catch (INodeLimitationException e2) {
            throw new IActionInsufficientPrivilegeException(
                    "The target or destination are limited by limitations that disallow this operation.");
        }
        return null;
    }
}
