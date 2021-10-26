package exceptions;

/**
 * Dešava se kada nekoj {@link actions.IAction} korisnik preda loš parametar za izvršavanje.
 */
public class IActionBadParameterException extends RuntimeException {

    public IActionBadParameterException(String s) {
        super("Bad parameter given: " + s);
    }

    public IActionBadParameterException() {
        super("Bad parameter given.");
    }
}
