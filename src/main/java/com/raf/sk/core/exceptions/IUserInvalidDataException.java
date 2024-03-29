package com.raf.sk.core.exceptions;

import java.util.Collection;

/**
 * Dešava se kada korisnik pokuša da napravi novog korisnika putem
 * {@link com.raf.sk.core.user.IUserManager#addUser(String, String, Collection)} metode, ali da loše parametre.
 */
public class IUserInvalidDataException extends RuntimeException {

    public IUserInvalidDataException() {
        super("Cannot create new com.raf.sk.specification.user, invalid parameters.");
    }
}
