package exceptions;

/**
 * Dešava se ukoliko korisnik pokuša da doda novog korisnika sa istim username-om kao neki već postojeći korisnik.
 */
public class IUserDuplicateUsernameException extends RuntimeException {

    public IUserDuplicateUsernameException(String s) {
        super("User with username \"" + s + "\" already exists.");
    }

    public IUserDuplicateUsernameException() {
        super("Cannot add user with username as another existing user.");
    }
}
