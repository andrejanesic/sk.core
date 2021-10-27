package exceptions;

/**
 * Dešava se kada korisnik nema pravo da izvrši neku komandu, ili nije ulogovan.
 */
public class IActionInsufficientPrivilegeException extends RuntimeException {

    public IActionInsufficientPrivilegeException(String s) {
        super(s);
    }

    public IActionInsufficientPrivilegeException() {
        super("You lack the permission to execute this command.");
    }
}
