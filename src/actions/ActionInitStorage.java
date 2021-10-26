package actions;

import core.Core;
import exceptions.ActionInsufficientPrivilegeException;
import exceptions.IStorageManagerINodeBuilderTreeInvalidException;

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
        if (!Core.getInstance().UserManager().getUser().hasPrivilege(path, INIT_STORAGE))
            throw new ActionInsufficientPrivilegeException();

        if (Core.getInstance().StorageManager().getRoot() == null) {
            try {
                Core.getInstance().StorageManager().initStorage(path);
            } catch (IStorageManagerINodeBuilderTreeInvalidException e) {
                // #TODO dodaj komponentu za logging
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean undo() {
        Core.getInstance().StorageManager().deinitStorage();
        return true;
    }
}
