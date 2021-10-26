package user;


import config.ConfigManager;
import config.IConfig;
import core.Core;
import exceptions.IComponentNotInitializedException;
import org.jetbrains.annotations.Nullable;
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
     * Podrazumevani konstruktor za ne-anonimne korisnike.
     *
     * @param username   Korisničko ime.
     * @param password   Lozinka.
     * @param privileges Privilegije korisnika.
     */
    User(String username, String password, @Nullable Collection<IPrivilege> privileges) {
        this.username = username;
        this.password = password;
        this.privileges = new HashSet<>();
        if (privileges != null)
            this.privileges.addAll(privileges); // mora biti ovako zbog potencijalne imutabilnosti privileges kolekcije

        initAnonymousPrivileges();
        initAuthenticatedPrivileges();
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
     * Podrazumevani konstruktor za ne-anonimne korisnike na osnovu UserBuilder-a.
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
        initAuthenticatedPrivileges();
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

    /**
     * Postavlja privilegije koje imaju svi autorizovani, ne-anonimni korisnici.
     */
    private void initAuthenticatedPrivileges() {
        if (privileges == null)
            privileges = new HashSet<>();

        privileges.add(new Privilege(PrivilegeType.STORAGE_INIT));
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
        // dodaj zavisne privilegije
        if (p.getType().equals(PrivilegeType.INODE_UPLOAD) ||
                p.getType().equals(PrivilegeType.INODE_DOWNLOAD) ||
                p.getType().equals(PrivilegeType.USER_DELETE) ||
                p.getType().equals(PrivilegeType.INODE_MOVE)) {
            privileges.add(new Privilege(p.getReferencedObject(), PrivilegeType.INODE_READ));
        }
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
        // proveri da li je osnovna funkcija
        if (p.getType().equals(PrivilegeType.USER_LOGIN) ||
                p.getType().equals(PrivilegeType.USER_LOGOUT))
            return;

        // obriši zavisne privilegije
        if (p.getType().equals(PrivilegeType.INODE_READ)) {
            privileges.remove(new Privilege(p.getReferencedObject(), PrivilegeType.INODE_UPLOAD));
            privileges.remove(new Privilege(p.getReferencedObject(), PrivilegeType.INODE_DOWNLOAD));
            privileges.remove(new Privilege(p.getReferencedObject(), PrivilegeType.INODE_DELETE));
            privileges.remove(new Privilege(p.getReferencedObject(), PrivilegeType.INODE_MOVE));
        }

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
