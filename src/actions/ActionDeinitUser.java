package actions;

import exceptions.ActionUndoImpossibleException;
import loader.Loader;
import user.UserManager;

/**
 * Akcija za izlogovanje korisnika.
 */
public class ActionDeinitUser implements Action {

    /**
     * Podrazumevani konstruktor. Klasa koristi već ulogovanog korisnika.
     */
    public ActionDeinitUser() {
    }

    @Override
    public Object run() {
        if (!UserManager.getInstance().getUser().isAuthenticated())
            return true;
        Loader.getInstance().deinitStorage();
        UserManager.getInstance().deinitUser();
        return true;
    }

    @Override
    public Object undo() {
        throw new ActionUndoImpossibleException("logging out again");
    }
}
