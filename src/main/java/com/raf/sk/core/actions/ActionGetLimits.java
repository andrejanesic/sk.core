package com.raf.sk.core.actions;

import com.raf.sk.core.config.IConfig;
import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.IActionBadParameterException;
import com.raf.sk.core.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.core.exceptions.IComponentNotInitializedException;
import com.raf.sk.core.exceptions.INodeRootNotInitializedException;
import com.raf.sk.core.repository.INode;
import com.raf.sk.core.repository.limitations.INodeLimitation;
import com.raf.sk.core.storage.StorageManager;
import com.raf.sk.core.user.PrivilegeType;

/**
 * Korisnička akcija čitanja ograničenja {@link INodeLimitation}. Vraća sva ograničenja na čvoru {@link INode}.
 */
public class ActionGetLimits implements IAction {

    /**
     * Putanja do {@link com.raf.sk.core.repository.INode} na kome su ograničenja sprovedena.
     */
    private String path;

    /**
     * Podrazumevani konstruktor.
     *
     * @param path Putanja do direktorijuma sa ograničenjima.
     * @param type Tip ograničenja. Stringovna vrednost {@link com.raf.sk.core.repository.limitations.INodeLimitationType}.
     */
    public ActionGetLimits(String path, String type) {
        this.path = path;
    }

    @Override
    public Object run() {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(path, PrivilegeType.LIMIT_READ) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.LIMIT_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        try {
            //noinspection ConstantConditions
            INode target = Core.getInstance().UserManager().getUser().getCwd().resolvePath(path);
            return target.getLimitations();
        } catch (INodeRootNotInitializedException e) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        }
    }

    @Override
    public Object undo() {
        return run();
    }
}
