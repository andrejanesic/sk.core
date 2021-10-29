package com.raf.sk.specification.exceptions;

/**
 * Dešava se kada korisnik pokuša da izbriše korisnika putem {@link com.raf.sk.specification.user.IUserManager#deleteUser(String)}, ali korisnik
 * sa datim korisničkim imenom ne postoji.
 */
public class IUserDeleteNotExistException extends RuntimeException {

    public IUserDeleteNotExistException() {
        super("Cannot delete com.raf.sk.specification.user, the given username isn't registered to any com.raf.sk.specification.user.");
    }
}
