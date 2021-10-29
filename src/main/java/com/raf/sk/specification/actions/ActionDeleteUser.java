package com.raf.sk.specification.actions;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.specification.exceptions.IActionUndoImpossibleException;
import com.raf.sk.specification.user.PrivilegeType;

/**
 * Akcija za brisanje korisnika.
 * <p>
 * Trenutni korisnik mora imati adekvatna prava za dodavanje ({@link com.raf.sk.specification.user.PrivilegeType#ALL} ili
 * {@link com.raf.sk.specification.user.PrivilegeType#USER_DELETE}.
 */
public class ActionDeleteUser implements IAction {

    /**
     * Da li je već izvršen run() ili ne.
     */
    private boolean execRun = false;

    /**
     * Korisničko ime korisnika za brisanje.
     */
    private String username;

    /**
     * Podrazumevani konstruktor.
     *
     * @param username Korisničko ime korisnika za brisanje.
     */
    public ActionDeleteUser(String username) {
        this.username = username;
    }

    @Override
    public Object run() {
        if (execRun)
            return null;

        if (Core.getInstance().UserManager().getUser() == null)
            throw new IActionInsufficientPrivilegeException();

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_DELETE) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();
        Core.getInstance().UserManager().deleteUser(username);
        execRun = true;
        return null;
    }

    @Override
    public Object undo() {
        throw new IActionUndoImpossibleException("removing a com.raf.sk.specification.user");
    }
}
