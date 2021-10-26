package user;

/**
 * Predstavlja odobrenje korisniku da radi nešto.
 * <p>
 * Privilegije nisu jednake po važnosti. Na primer, privilegija "ALL" daje korisniku sve privilegije, dok postoje
 * "podgrupe" odnosno kategorije privilegija sa svojim "master" privilegijama, koje daju korisniku sve pod-privilegije u
 * toj kategoriji.
 */
public enum PrivilegeType {
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
}
