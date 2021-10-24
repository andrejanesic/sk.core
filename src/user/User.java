package user;


import user.builder.PrivilegeBuilder;
import user.builder.UserBuilder;

import java.util.Collection;
import java.util.HashSet;

/**
 * Čuva informacije o korisniku.
 */
public class User {

    /**
     * Korisničko ime.
     */
    private String username;

    /**
     * Lozinka.
     */
    private String password;

    /**
     * Privilegije korisnika.
     */
    private Collection<Privilege> privileges;

    /**
     * Podrazumevani konstruktor.
     *
     * @param username   Korisničko ime.
     * @param password   Lozinka.
     * @param privileges Privilegije korisnika.
     */
    public User(String username, String password, Collection<Privilege> privileges) {
        this.username = username;
        this.password = password;
        this.privileges = privileges;
    }

    /**
     * Podrazumevani konstruktor.
     *
     * @param username Korisničko ime.
     * @param password Lozinka.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.privileges = new HashSet<>();
    }

    /**
     * Konstruktor na osnovu UserBuilder-a.
     *
     * @param userBuilder UserBuilder instanca.
     */
    public User(UserBuilder userBuilder) {
        username = userBuilder.getUsername();
        password = userBuilder.getPassword();

        privileges = new HashSet<>();
        if (userBuilder.getPrivileges() != null)
            for (PrivilegeBuilder pb : userBuilder.getPrivileges())
                privileges.add(new Privilege(pb));
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    /**
     * Dodaje novu privilegiju korisniku.
     *
     * @param p Nova privilegija.
     */
    public void grantPrivilege(Privilege p) {
        privileges.add(p);
    }

    /**
     * Oduzima privilegiju od korisnika.
     *
     * @param p Nova privilegija.
     */
    public void revokePrivilege(Privilege p) {
        privileges.remove(p);
    }
}
