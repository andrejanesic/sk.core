package com.raf.sk.specification.exceptions;

/**
 * Dešava se kada korisnik pokuša da obriše sam sebe (trenutnog korisnika.)
 */
public class IUserCannotDeleteCurrentUserException extends RuntimeException {

    public IUserCannotDeleteCurrentUserException() {
        super("You cannot delete a currently logged in com.raf.sk.specification.user.");
    }
}
