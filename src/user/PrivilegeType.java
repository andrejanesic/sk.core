package user;

/**
 * Predstavlja odobrenje korisniku da radi nešto.
 * <p>
 * Privilegije nisu jednake po važnosti. Na primer, privilegija "ALL" daje korisniku sve privilegije, dok postoje
 * "podgrupe" odnosno kategorije privilegija sa svojim "master" privilegijama, koje daju korisniku sve pod-privilegije u
 * toj kategoriji.
 */
public enum PrivilegeType {
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

    // LIMITATION MANAGEMENT
    LIMIT_ADD,
    LIMIT_READ,
    LIMIT_DELETE,

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
