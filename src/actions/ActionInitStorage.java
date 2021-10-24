package actions;

import exceptions.ActionInsufficientPrivilegeException;
import loader.Loader;

import static user.PrivilegeType.INIT_STORAGE;

/**
 * Radnja inicijalizacije skladišta.
 */
public class ActionInitStorage implements Action {

    /**
     * Putanja do skladišta u OS okruženju.
     */
    private String path;

    /**
     * Podrazumevani konstruktor.
     *
     * @param path Putanja do skladišta u OS okruženju.
     */
    public ActionInitStorage(String path) {
        this.path = path;
    }

    @Override
    public Boolean run() {
        if (!Loader.getInstance().getUser().hasPrivilege(path, INIT_STORAGE))
            throw new ActionInsufficientPrivilegeException();

        Loader.getInstance().initStorage(path);
        return true;
    }

    @Override
    public Boolean undo() {
        Loader.getInstance().deinitStorage();
        return true;
    }
}
