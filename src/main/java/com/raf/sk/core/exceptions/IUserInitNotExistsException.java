package com.raf.sk.core.exceptions;

/**
 * Dešava se kada korisnik hoće da se uloguje u korisnika koji nije definisan, ili je dat loš password.
 */
public class IUserInitNotExistsException extends RuntimeException {

    public IUserInitNotExistsException() {
        super("User with the given username or password doesn't exist.");
    }
}
