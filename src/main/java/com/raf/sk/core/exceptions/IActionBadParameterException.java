package com.raf.sk.core.exceptions;

/**
 * Dešava se kada nekoj {@link com.raf.sk.core.actions.IAction} korisnik preda loš parametar za izvršavanje.
 */
public class IActionBadParameterException extends RuntimeException {

    public IActionBadParameterException(String s) {
        super("Bad parameter given: " + s);
    }

    public IActionBadParameterException() {
        super("Bad parameter given.");
    }
}
