package com.raf.sk.specification.actions;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.specification.exceptions.IStorageManagerINodeBuilderTreeInvalidException;

import static com.raf.sk.specification.user.PrivilegeType.ALL;
import static com.raf.sk.specification.user.PrivilegeType.STORAGE_INIT;

/**
 * Radnja inicijalizacije skladišta.
 */
public class ActionInitStorage implements IAction {

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
    public Object run() {
        // proveri da li korisnik uopšte postoji, makar anonimni
        if (Core.getInstance().UserManager().getUser() == null)
            throw new IActionInsufficientPrivilegeException();

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(STORAGE_INIT) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(ALL)))
            throw new IActionInsufficientPrivilegeException();

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
    public Object undo() {
        Core.getInstance().StorageManager().deinitStorage();
        return true;
    }
}
