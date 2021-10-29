package com.raf.sk.core.repository;

/**
 * Predstavlja skup operacija koje se mogu izvršiti nad {@link com.raf.sk.core.repository.INode} klasom.
 */
public enum INodeOperation {
    ADD_CHILD_TO_SELF,
    DOWNLOAD,
    DOWNLOAD_CHILD,
    DELETE_SELF,
    DELETE_CHILD,
    MOVE,
}
