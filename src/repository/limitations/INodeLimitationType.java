package repository.limitations;

/**
 * Predstavlja skup ograničenja {@link repository.INode} koje se mogu dodati jednom {@link repository.INode}.
 */
public enum INodeLimitationType {
    MAX_FILE_COUNT,
    MAX_SIZE,
    BLACKLIST_EXT
}
