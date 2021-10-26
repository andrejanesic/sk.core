package user.builder;

/**
 * Tipovi bildera korisničkih privilegija.
 */
public enum PrivilegeTypeBuilder {
    ALL,
    INIT_STORAGE,

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
