package com.raf.sk.specification.actions;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.specification.exceptions.IActionUndoImpossibleException;
import com.raf.sk.specification.user.PrivilegeType;

/**
 * Akcija za izlogovanje korisnika. Deinicijalizuje skladište.
 */
public class ActionDeinitUser implements IAction {

    /**
     * Podrazumevani konstruktor. Klasa koristi već ulogovanog korisnika.
     */
    public ActionDeinitUser() {
    }

    @Override
    public Object run() {
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser() == null ||
                !(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_LOGOUT) ||
                        Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_ALL) ||
                        Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        //noinspection ConstantConditions
        if (!Core.getInstance().UserManager().getUser().isAuthenticated())
            return true;
        Core.getInstance().StorageManager().deinitStorage();
        Core.getInstance().UserManager().deinitUser();
        return true;
    }

    @Override
    public Object undo() {
        throw new IActionUndoImpossibleException("logging out again");
    }
}
