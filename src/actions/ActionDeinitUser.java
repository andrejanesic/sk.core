package actions;

import core.Core;
import exceptions.ActionUndoImpossibleException;

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
        if (!Core.getInstance().UserManager().getUser().isAuthenticated())
            return true;
        Core.getInstance().StorageManager().deinitStorage();
        Core.getInstance().UserManager().deinitUser();
        return true;
    }

    @Override
    public Object undo() {
        throw new ActionUndoImpossibleException("logging out again");
    }
}
