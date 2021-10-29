package com.raf.sk.core.actions;

import com.raf.sk.core.config.IConfig;
import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.*;
import com.raf.sk.core.repository.Directory;
import com.raf.sk.core.repository.INode;
import com.raf.sk.core.storage.StorageManager;
import com.raf.sk.core.user.PrivilegeType;

/**
 * Korisnička akcija dodavanja novog direktorijuma ili fajla.
 *
 * @see com.raf.sk.core.repository.INode
 * @see com.raf.sk.core.io.IODriver
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

        try {
            //noinspection ConstantConditions
            Directory targetNode = Core.getInstance().UserManager().getUser().getCwd();

            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(
                    Core.getInstance().UserManager().getUser().getCwd().getPath(), PrivilegeType.INODE_ADD) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.INODE_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();

            if (type == null)
                throw new IActionBadParameterException("The type you specified is not valid. Must be FILE or DIR.");
            if (type.equals("FILE")) {
                //noinspection ConstantConditions
                targetNode.makeFile(name);
                return true;
            }
            if (type.equals("DIR")) {
                //noinspection ConstantConditions
                targetNode.makeDirectory(name);
                return true;
            }
            throw new IActionBadParameterException("The type you specified is not valid. Must be FILE or DIR.");
        } catch (INodeLimitationException e2) {
            throw new IActionInsufficientPrivilegeException(
                    "The target or destination are limited by limitations that disallow this operation.");
        } catch (DirectoryMakeNodeNameInvalidException |
                DirectoryMakeNodeInvalidNodeTypeException |
                DirectoryMakeNodeNameNotUniqueException e) {
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
