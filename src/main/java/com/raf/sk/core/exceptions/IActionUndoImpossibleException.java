package com.raf.sk.core.exceptions;

/**
 * Dešava se kada nije moguće izvršiti undo() neke akcije, npr. akcije inicijalizacije skladišta.
 */
public class IActionUndoImpossibleException extends UnsupportedOperationException {

    public IActionUndoImpossibleException(String action) {
        super("It is impossible to undo the action of " + action + ".");
    }
}
