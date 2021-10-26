package user;


import config.ConfigManager;
import config.IConfig;
import core.Core;
import exceptions.IComponentNotInitializedException;
import user.builder.PrivilegeBuilder;
import user.builder.UserBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Implementacija IUser interfejsa.
 */
public class User implements IUser {

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
    private Collection<IPrivilege> privileges;

    /**
     * Kreira novog anonimnog, neulogovanog korisnika.
     */
    User() {
        initAnonymousPrivileges();
    }

    /**
     * Podrazumevani konstruktor.
     *
     * @param username   Korisničko ime.
     * @param password   Lozinka.
     * @param privileges Privilegije korisnika.
     */
    User(String username, String password, Collection<IPrivilege> privileges) {
        this.username = username;
        this.password = password;
        this.privileges = new HashSet<>();
        this.privileges.addAll(privileges); // mora biti ovako zbog potencijalne imutabilnosti privileges kolekcije

        initAnonymousPrivileges();
    }

    /**
     * Podrazumevani konstruktor.
     *
     * @param username Korisničko ime.
     * @param password Lozinka.
     */
    User(String username, String password) {
        this(username, password, new HashSet<>());
    }

    /**
     * Konstruktor na osnovu UserBuilder-a.
     *
     * @param userBuilder UserBuilder instanca.
     */
    User(UserBuilder userBuilder) {
        username = userBuilder.getUsername();
        password = userBuilder.getPassword();

        privileges = new HashSet<>();
        if (userBuilder.getPrivileges() != null)
            for (PrivilegeBuilder pb : userBuilder.getPrivileges())
                privileges.add(new Privilege(pb));

        initAnonymousPrivileges();
    }

    /**
     * Postavlja privilegije koje imaju svi korisnici, čak i anonimni.
     */
    private void initAnonymousPrivileges() {
        if (privileges == null)
            privileges = new HashSet<>();

        privileges.add(new Privilege(PrivilegeType.USER_LOGIN));
        privileges.add(new Privilege(PrivilegeType.USER_LOGOUT));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<IPrivilege> getPrivileges() {
        return privileges;
    }

    @Override
    public boolean isAuthenticated() {
        if (Core.getInstance().UserManager().getUser() == null)
            return false;
        //noinspection ConstantConditions
        return Core.getInstance().UserManager().getUser().equals(this);
    }

    @Override
    public void grantPrivilege(IPrivilege p) {
        privileges.add(p);
        update();
    }

    @Override
    public void grantPrivilege(Object o, PrivilegeType type) {
        grantPrivilege(new Privilege(o, type));
    }

    @Override
    public void grantPrivilege(PrivilegeType type) {
        grantPrivilege(null, type);
    }

    @Override
    public void revokePrivilege(IPrivilege p) {
        privileges.remove(p);
        update();
    }

    @Override
    public void revokePrivilege(Object o, PrivilegeType type) {
        revokePrivilege(new Privilege(o, type));
    }

    @Override
    public void revokePrivilege(PrivilegeType type) {
        revokePrivilege(null, type);
    }

    @Override
    public boolean hasPrivilege(IPrivilege p) {
        return privileges.contains(p);
    }

    @Override
    public boolean hasPrivilege(Object o, PrivilegeType type) {
        return hasPrivilege(new Privilege(o, type));
    }

    @Override
    public void update() {
        if (ConfigManager.getInstance().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);
        //noinspection ConstantConditions
        Core.getInstance().ConfigManager().getConfig().updateUser(toBuilder());
    }

    @Override
    public UserBuilder toBuilder() {
        return new UserBuilder(username, password, privilegesToBuilder());
    }

    /**
     * Pretvara privilegije u {@link PrivilegeBuilder} tip.
     *
     * @return {@link PrivilegeBuilder} bilderi.
     */
    private Collection<PrivilegeBuilder> privilegesToBuilder() {
        Collection<PrivilegeBuilder> pbs = new ArrayList<>();
        for (IPrivilege p : privileges) {
            pbs.add(p.toBuilder());
        }
        return pbs;
    }

    @Override
    public boolean hasPrivilege(PrivilegeType type) {
        return hasPrivilege(null, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
