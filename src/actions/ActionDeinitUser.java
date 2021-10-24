package actions;

import exceptions.ActionUndoImpossibleException;
import loader.Loader;

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
        if (!Loader.getInstance().getUser().isAuthenticated())
            return true;
        Loader.getInstance().deinitStorage();
        Loader.getInstance().deinitUser();
        return true;
    }

    @Override
    public Object undo() {
        throw new ActionUndoImpossibleException("logging out again");
    }
}
