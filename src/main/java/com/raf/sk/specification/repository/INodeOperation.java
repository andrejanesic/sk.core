package com.raf.sk.specification.repository;

/**
 * Predstavlja skup operacija koje se mogu izvršiti nad {@link com.raf.sk.specification.repository.INode} klasom.
 */
public enum INodeOperation {
    ADD_CHILD_TO_SELF,
    DOWNLOAD,
    DOWNLOAD_CHILD,
    DELETE_SELF,
    DELETE_CHILD,
    MOVE,
}
