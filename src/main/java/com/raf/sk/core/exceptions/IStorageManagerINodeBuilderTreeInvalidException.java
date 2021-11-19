package com.raf.sk.core.exceptions;

/**
 * Dešava se kada postoji problem u stablu koje je {@link com.raf.sk.specification.io.IODriver} predao {@link com.raf.sk.core.storage.IStorageManager} komponenti
 * za izgradnju stabla u {@link com.raf.sk.core.storage.IStorageManager#initStorage(String)} metodi.
 */
public class IStorageManagerINodeBuilderTreeInvalidException extends Exception {

    public IStorageManagerINodeBuilderTreeInvalidException(String s) {
        super(s);
    }

    public IStorageManagerINodeBuilderTreeInvalidException() {
        super("The given DirectoryBuilder tree is invalid.");
    }
}
