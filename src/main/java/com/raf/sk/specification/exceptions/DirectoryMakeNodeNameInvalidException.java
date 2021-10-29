package com.raf.sk.specification.exceptions;

/**
 * Dešava se kada korisnik pokuša da kreira novi poddirektorijum ili fajl sa ne-validnim nazivom.
 */
public class DirectoryMakeNodeNameInvalidException extends Exception {

    public DirectoryMakeNodeNameInvalidException(String fn) {
        super("File name \"" + fn + "\" for new node is not valid.");
    }

    public DirectoryMakeNodeNameInvalidException() {
        super("File name for new node is not valid.");
    }
}
