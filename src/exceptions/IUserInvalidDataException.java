package exceptions;

import java.util.Collection;

/**
 * Dešava se kada korisnik pokuša da napravi novog korisnika putem
 * {@link user.IUserManager#addUser(String, String, Collection)} metode, ali da loše parametre.
 */
public class IUserInvalidDataException extends RuntimeException {

    public IUserInvalidDataException() {
        super("Cannot create new user, invalid parameters.");
    }
}
