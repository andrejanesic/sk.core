package user.builder;

/**
 * Tipovi bildera korisničkih privilegija.
 */
public enum PrivilegeTypeBuilder {
    // MASTER
    ALL,

    // STORAGE MANAGEMENT
    STORAGE_INIT,
    STORAGE_CONFIGURE,

    // REPOSITORY MANAGEMENT
    INODE_UPLOAD,
    INODE_DOWNLOAD,
    INODE_DELETE,
    INODE_MOVE,
    INODE_READ,

    // USER MANAGEMENT
    USER_ALL,
    USER_ADD,
    USER_GET,
    USER_UPDATE,
    USER_DELETE,
    USER_LOGIN,
    USER_LOGOUT,

    // PRIVILEGE MANAGEMENT
    PRIVILEGE_GRANT,
    PRIVILEGE_REVOKE,
}
