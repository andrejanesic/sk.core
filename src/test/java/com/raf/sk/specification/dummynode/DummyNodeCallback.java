package com.raf.sk.specification.dummynode;

import com.raf.sk.specification.exceptions.INodeRootNotInitializedException;

/**
 * Koristi se kao callback za DummyNode funkcije.
 */
public interface DummyNodeCallback {

    /**
     * Callback funkcija.
     *
     * @throws INodeRootNotInitializedException Ukoliko korenski čvor nije inicijalizovan.
     */
    default void execute() throws INodeRootNotInitializedException {
        execute(null);
    }

    /**
     * Callback funkcija.
     *
     * @param dummyNode Čvor koji se obrađuje.
     */
    void execute(DummyNode dummyNode) throws INodeRootNotInitializedException;
}
