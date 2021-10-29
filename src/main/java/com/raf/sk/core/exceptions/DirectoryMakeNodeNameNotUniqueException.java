package com.raf.sk.core.exceptions;

/**
 * Dešava se ukoliko se na direktorijumu kreira novi čvor sa nazivom čvora koji već postoji.
 */
public class DirectoryMakeNodeNameNotUniqueException extends Exception {

    public DirectoryMakeNodeNameNotUniqueException(String s) {
        super("Node with name \"" + s + "\" already exists.");
    }

    public DirectoryMakeNodeNameNotUniqueException() {
        super("New nodes must have unique names.");
    }
}
