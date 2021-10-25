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
     * Da li je korisnik autentifikovan, tj. ulogovan, ili ne.
     */
    private boolean authenticated = false;

    /**
     * Kreira novog anonimnog, neulogovanog korisnika.
     */
    public User() {
        initAnonymousPrivileges();
    }

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

        initAnonymousPrivileges();
        authenticated = true;
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

        initAnonymousPrivileges();
        authenticated = true;
    }

    /**
     * Konstruktor na osnovu UserBuilder-a.
     *
     * @param userBuilder UserBuilder instanca.
     */
    public User(UserBuilder userBuilder) {
        if (userBuilder.isAuthenticated()) {
            username = userBuilder.getUsername();
            password = userBuilder.getPassword();

            privileges = new HashSet<>();
            if (userBuilder.getPrivileges() != null)
                for (PrivilegeBuilder pb : userBuilder.getPrivileges())
                    privileges.add(new Privilege(pb));

            authenticated = true;
        }

        initAnonymousPrivileges();
    }

    /**
     * Postavlja privilegije koje imaju svi korisnici, čak i anonimni.
     */
    private void initAnonymousPrivileges() {
        if (privileges == null)
            privileges = new HashSet<>();

        privileges.add(new Privilege(PrivilegeType.LOGIN));
        privileges.add(new Privilege(PrivilegeType.LOGOUT));
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

    public boolean isAuthenticated() {
        return authenticated;
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
     * Dodaje novu privilegiju korisniku.
     *
     * @param o    Objekat vezan za privilegiju.
     * @param type Tip privilegije.
     */
    public void grantPrivilege(Object o, PrivilegeType type) {
        privileges.add(new Privilege(o, type));
    }

    /**
     * Dodaje novi tip privilegije (generalizovan) korisniku.
     *
     * @param type Tip privilegije.
     */
    public void grantPrivilege(PrivilegeType type) {
        grantPrivilege(null, type);
    }

    /**
     * Oduzima privilegiju od korisnika.
     *
     * @param p Nova privilegija.
     */
    public void revokePrivilege(Privilege p) {
        privileges.remove(p);
    }

    /**
     * Oduzima privilegiju od korisnika.
     *
     * @param o    Objekat vezan za privilegiju.
     * @param type Tip privilegije.
     */
    public void revokePrivilege(Object o, PrivilegeType type) {
        privileges.remove(new Privilege(o, type));
    }

    /**
     * Oduzima privilegiju od korisnika.
     *
     * @param type Tip privilegije.
     */
    public void revokePrivilege(PrivilegeType type) {
        revokePrivilege(null, type);
    }

    /**
     * Proverava da li korisnik ima datu privilegiju.
     *
     * @param p Privilegija.
     * @return True ako ima, false ako nema.
     */
    public boolean hasPrivilege(Privilege p) {
        return privileges.contains(p);
    }

    /**
     * Proverava da li korisnik ima datu privilegiju.
     *
     * @param o    Objekat vezan za privilegiju.
     * @param type Tip privilegije.
     * @return True ako ima, false ako nema.
     */
    public boolean hasPrivilege(Object o, PrivilegeType type) {
        return privileges.contains(new Privilege(o, type));
    }

    /**
     * Proverava da li korisnik ima dati tip privilegije (generalizovano).
     *
     * @param type Tip privilegije.
     * @return True ako ima, false ako nema.
     */
    public boolean hasPrivilege(PrivilegeType type) {
        return hasPrivilege(null, type);
    }
}
