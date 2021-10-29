package com.raf.sk.specification.repository.limitations;

/**
 * Predstavlja skup ograničenja {@link com.raf.sk.specification.repository.INode} koje se mogu dodati jednom {@link com.raf.sk.specification.repository.INode}.
 */
public enum INodeLimitationType {
    MAX_FILE_COUNT,
    MAX_SIZE,
    BLACKLIST_EXT
}
