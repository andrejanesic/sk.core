package actions;

import config.IConfig;
import core.Core;
import exceptions.IActionBadParameterException;
import exceptions.IActionInsufficientPrivilegeException;
import exceptions.IComponentNotInitializedException;
import exceptions.INodeRootNotInitializedException;
import repository.INode;
import repository.limitations.INodeLimitation;
import repository.limitations.INodeLimitationType;
import storage.StorageManager;
import user.PrivilegeType;

/**
 * Korisnička radnja oduzimanja ograničenja za zadati čvor {@link repository.INode} (ili za celo skladište ako je
 * putanja jednaka {@link repository.Directory#ROOT_DIRECTORY}.
 *
 * @see repository.limitations.INodeLimitation
 */
public class ActionDeleteLimit implements IAction {

    /**
     * Putanja do {@link repository.INode} na kome je ograničenje sprovedeno.
     */
    private String path;

    /**
     * Tip ograničenja, vrednost mora biti jedna od vrednosti {@link repository.limitations.INodeLimitationType}.
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
     * @param type Tip ograničenja. Stringovna vrednost {@link repository.limitations.INodeLimitationType}.
     * @param arg  Argument za konstruktor ograničenja.
     */
    public ActionDeleteLimit(String path, String type, Object arg) {
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

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(path, PrivilegeType.LIMIT_DELETE) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        try {
            INode target = Core.getInstance().UserManager().getUser().getCwd().resolvePath(path);
            target.deleteLimitation(new INodeLimitation(target, type, arg));
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

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(path, PrivilegeType.LIMIT_ADD) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        try {
            INode target = Core.getInstance().UserManager().getUser().getCwd().resolvePath(path);
            target.addLimitation(new INodeLimitation(target, type, arg));
        } catch (INodeRootNotInitializedException e) {
            throw new IActionBadParameterException("The path you specified is not valid.");
        }
        return null;
    }
}
