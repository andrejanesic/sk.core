package com.raf.sk.core.actions;

import com.raf.sk.core.config.IConfig;
import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.IActionBadParameterException;
import com.raf.sk.core.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.core.exceptions.IComponentNotInitializedException;
import com.raf.sk.core.exceptions.INodeRootNotInitializedException;
import com.raf.sk.core.repository.INode;
import com.raf.sk.core.repository.limitations.INodeLimitation;
import com.raf.sk.core.repository.limitations.INodeLimitationType;
import com.raf.sk.core.storage.StorageManager;
import com.raf.sk.core.user.PrivilegeType;

/**
 * Korisnička radnja dodavanja novog ograničenja za zadati čvor {@link com.raf.sk.core.repository.INode} (ili za celo skladište ako je
 * putanja jednaka {@link com.raf.sk.core.repository.Directory#ROOT_DIRECTORY}.
 *
 * @see com.raf.sk.core.repository.limitations.INodeLimitation
 */
public class ActionAddLimit implements IAction {

    /**
     * Putanja do {@link com.raf.sk.core.repository.INode} na kome je ograničenje sprovedeno.
     */
    private String path;

    /**
     * Tip ograničenja, vrednost mora biti jedna od vrednosti {@link com.raf.sk.core.repository.limitations.INodeLimitationType}.
     */
    private INodeLimitationType type;

    /**
     * Argument za ograničenje, odnosno vrednost ograničenja. Na primer, maksimalna veličina, zabranjena fajl
     * ekstenzija, i slično.
     */
    private Object arg;

    /**
     * Podrazumevani konstruktor.
     *
     * @param path Putanja do direktorijuma koji treba ograničiti.
     * @param type Tip ograničenja. Stringovna vrednost {@link com.raf.sk.core.repository.limitations.INodeLimitationType}.
     * @param arg  Argument za konstruktor ograničenja.
     */
    public ActionAddLimit(String path, String type, Object arg) {
        this.path = path;
        try {
            this.type = INodeLimitationType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new IActionBadParameterException("The limitation type you specified is invalid.");
        }
        this.arg = arg;
    }

    @Override
    public Object run() {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        try {
            //noinspection ConstantConditions
            INode target = Core.getInstance().UserManager().getUser().getCwd().resolvePath(path);

            //noinspection ConstantConditions
            if (!(
                    Core.getInstance().UserManager().getUser().hasPrivilege(target.getPath(), PrivilegeType.LIMIT_ADD) ||
                            Core.getInstance().UserManager().getUser().hasPrivilege(target.getPath(), PrivilegeType.LIMIT_ALL) ||
                            Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.LIMIT_ADD) ||
                            Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.LIMIT_ALL) ||
                            Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();

            target.addLimitation(new INodeLimitation(target, type, arg));
        } catch (INodeRootNotInitializedException e) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        }
        return null;
    }

    @Override
    public Object undo() {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (Core.getInstance().StorageManager().getRoot() == null)
            throw new IComponentNotInitializedException(StorageManager.class);

        try {
            //noinspection ConstantConditions
            INode target = Core.getInstance().UserManager().getUser().getCwd().resolvePath(path);

            //noinspection ConstantConditions
            if (!(Core.getInstance().UserManager().getUser().hasPrivilege(target.getPath(), PrivilegeType.LIMIT_DELETE) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(target.getPath(), PrivilegeType.LIMIT_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.LIMIT_DELETE) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.LIMIT_ALL) ||
                    Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
                throw new IActionInsufficientPrivilegeException();
            target.deleteLimitation(new INodeLimitation(target, type, arg));
        } catch (INodeRootNotInitializedException e) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        }
        return null;
    }
}
