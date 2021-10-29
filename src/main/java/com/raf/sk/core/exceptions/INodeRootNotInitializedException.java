package com.raf.sk.core.exceptions;

/**
 * Dešava se kada je potrebno pristupiti korenskom čvoru a on nije dostupan, tj. nije inicijalizovan.
 */
public class INodeRootNotInitializedException extends Exception {

    public INodeRootNotInitializedException() {
        super("Root node not initialized. You must init the com.raf.sk.specification.storage first.");
    }
}
