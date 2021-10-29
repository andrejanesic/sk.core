package com.raf.sk.core.user.builder;

/**
 * Tipovi bildera korisničkih privilegija.
 */
public enum PrivilegeTypeBuilder {
    // MASTER
    ALL,

    // STORAGE MANAGEMENT
    STORAGE_INIT,

    // REPOSITORY MANAGEMENT
    INODE_ALL,
    INODE_ADD,
    INODE_DOWNLOAD,
    INODE_DELETE,
    INODE_READ,

    // LIMITATION MANAGEMENT
    LIMIT_ALL,
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
    PRIVILEGE_ALL,
    PRIVILEGE_GRANT,
    PRIVILEGE_REVOKE,
}
