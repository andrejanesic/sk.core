package exceptions;

/**
 * Dešava se kada nije moguće izvršiti undo() neke akcije, npr. akcije inicijalizacije skladišta.
 */
public class ActionUndoImpossibleException extends UnsupportedOperationException {

    public ActionUndoImpossibleException(String action) {
        super("It is impossible to undo the action of " + action + ".");
    }
}
