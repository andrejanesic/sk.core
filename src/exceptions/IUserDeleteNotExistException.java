package exceptions;

/**
 * Dešava se kada korisnik pokuša da izbriše korisnika putem {@link user.IUserManager#deleteUser(String)}, ali korisnik
 * sa datim korisničkim imenom ne postoji.
 */
public class IUserDeleteNotExistException extends RuntimeException {

    public IUserDeleteNotExistException() {
        super("Cannot delete user, the given username isn't registered to any user.");
    }
}
