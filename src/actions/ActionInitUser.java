package actions;

import core.Core;
import exceptions.ActionInsufficientPrivilegeException;
import user.IUser;

import static user.PrivilegeType.USER_LOGIN;

/**
 * Radnja inicijalizacije (logovanja i izlogovanja) korisnika.
 */
public class ActionInitUser implements Action {

    /**
     * Prati da li je korisnik već jednom pokušao da se uloguje sa ovom akcijom.
     */
    private boolean tried = false;

    /**
     * Korisničko ime.
     */
    private String username;

    /**
     * Lozinka.
     */
    private String password;

    /**
     * Podrazumevani konstruktor.
     *
     * @param username Korisničko ime.
     * @param password Lozinka.
     */
    public ActionInitUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Boolean run() {
        if (tried)
            return false;

        if (Core.getInstance().UserManager().getUser() == null)
            throw new ActionInsufficientPrivilegeException();
        //noinspection ConstantConditions
        if (!Core.getInstance().UserManager().getUser().hasPrivilege(USER_LOGIN))
            throw new ActionInsufficientPrivilegeException();

        tried = true;
        IUser u = Core.getInstance().UserManager().initUser(username, password);
        return u.isAuthenticated();
    }

    @Override
    public Boolean undo() {
        if (Core.getInstance().UserManager().getUser() == null)
            throw new ActionInsufficientPrivilegeException();
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser().isAuthenticated())
            Core.getInstance().UserManager().deinitUser();
        return true;
    }
}
