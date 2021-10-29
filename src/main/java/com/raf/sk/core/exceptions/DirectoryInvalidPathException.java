package com.raf.sk.core.exceptions;

/**
 * Dešava se ukoliko se pozove Directory.resolvePath sa lošom putanjom.
 */
public class DirectoryInvalidPathException extends RuntimeException {

    public DirectoryInvalidPathException(String s) {
        super("Path " + s + " doesn't exist.");
    }
}
