package com.raf.sk.core.exceptions;

/**
 * Ukoliko prosleđeni tip čvora za kreiranje u direktorijumu nije validan.
 */
public class DirectoryMakeNodeInvalidNodeTypeException extends Exception {

    public DirectoryMakeNodeInvalidNodeTypeException() {
        super("Node type not valid.");
    }
}
