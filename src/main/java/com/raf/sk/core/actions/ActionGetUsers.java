package com.raf.sk.core.actions;

import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.core.user.IUser;
import com.raf.sk.core.user.PrivilegeType;

import java.util.Collection;

/**
 * Akcija za listanje postojećih korisnika {@link com.raf.sk.core.user.IUser}.
 * <p>
 * Trenutni korisnik mora imati adekvatna prava za dodavanje ({@link com.raf.sk.core.user.PrivilegeType#USER_GET}.
 */
public class ActionGetUsers implements IAction {

    /**
     * Podrazumevani konstruktor.
     */
    public ActionGetUsers() {
    }

    @Override
    public Collection<IUser> run() {
        if (Core.getInstance().UserManager().getUser() == null)
            throw new IActionInsufficientPrivilegeException();

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_GET)))
            throw new IActionInsufficientPrivilegeException();
        return Core.getInstance().UserManager().getUsers();
    }

    @Override
    public Object undo() {
        return run();
    }
}
