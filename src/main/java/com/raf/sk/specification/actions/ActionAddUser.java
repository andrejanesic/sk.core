package com.raf.sk.specification.actions;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.specification.user.IPrivilege;
import com.raf.sk.specification.user.PrivilegeType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Akcija dodavanja novog korisnika. Novi korisnik ima anonimna prava {@link com.raf.sk.specification.user.Privilege}, odnosno prava se dodaju
 * odvojenom akcijom.
 * <p>
 * Trenutni korisnik mora imati adekvatna prava za dodavanje ({@link com.raf.sk.specification.user.PrivilegeType#ALL} ili
 * {@link com.raf.sk.specification.user.PrivilegeType#USER_ADD}.
 * <p>
 *
 * @see com.raf.sk.specification.exceptions.IUserDuplicateUsernameException Greška ukoliko korisnik pokuša da napravi korisnika
 */
public class ActionAddUser implements IAction {

    /**
     * Korisničko ime novog korisnika.
     */
    private String username;

    /**
     * Lozinka novog korisnika.
     */
    private String password;

    /**
     * Podrazumevani konstruktor.
     *
     * @param username Korisničko ime novog korisnika.
     * @param password Lozinka novog korisnika.
     */
    public ActionAddUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Object run() {
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser() == null ||
                !(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_ADD) ||
                        Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_ALL) ||
                        Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();
        Collection<IPrivilege> privileges = new ArrayList<>();

        return Core.getInstance().UserManager().addUser(username, password, privileges);
    }

    @Override
    public Object undo() {
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser() == null ||
                !(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_DELETE) ||
                        Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_ALL) ||
                        Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();
        Core.getInstance().UserManager().deleteUser(username);
        return null;
    }
}
