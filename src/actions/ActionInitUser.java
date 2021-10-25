package actions;

import exceptions.ActionInsufficientPrivilegeException;
import loader.Loader;
import user.User;
import user.UserManager;

import static user.PrivilegeType.LOGIN;

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

        if (!UserManager.getInstance().getUser().hasPrivilege(LOGIN))
            throw new ActionInsufficientPrivilegeException();

        tried = true;
        User u = UserManager.getInstance().initUser(username, password);
        return u.isAuthenticated();
    }

    @Override
    public Boolean undo() {
        if (UserManager.getInstance().getUser().isAuthenticated())
            UserManager.getInstance().deinitUser();
        return true;
    }
}