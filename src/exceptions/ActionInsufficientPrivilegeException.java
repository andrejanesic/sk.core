package exceptions;

/**
 * Dešava se kada korisnik nema pravo da izvrši neku komandu, ili nije ulogovan.
 */
public class ActionInsufficientPrivilegeException extends RuntimeException {

    public ActionInsufficientPrivilegeException() {
        super("You lack the permission to execute this command.");
    }
}
