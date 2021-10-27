package user;

import config.ConfigManager;
import config.IConfig;
import core.Core;
import exceptions.*;
import user.builder.PrivilegeBuilder;
import user.builder.UserBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementacija komponente za korisnički menadžment.
 */
public class UserManager implements IUserManager {

    /**
     * Trenutni korisnik.
     */
    private IUser user;

    /**
     * Kolekcija svih korisnika.
     */
    private Collection<IUser> users = new HashSet<>();

    /**
     * Za inicijalno lazy-učitavanje korisnika.
     */
    private boolean usersLoaded = false;

    private UserManager() {
        user = new User();
    }

    /**
     * Vraća instancu komponente.
     *
     * @return Instanca komponente.
     */
    public static UserManager getInstance() {
        return UserManager.Holder.INSTANCE;
    }

    @Override
    public IUser initUser() {
        return deinitUser();
    }

    @Override
    public synchronized IUser initUser(String username, String password) {
        if (ConfigManager.getInstance().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        deinitUser();
        //noinspection ConstantConditions
        Collection<UserBuilder> userBuilders = Core.getInstance().ConfigManager().getConfig().getUsers();

        for (UserBuilder builder : userBuilders) {
            if (builder.getUsername().equals(username) &&
                    builder.getPassword().equals(password)
            ) {
                // uloguj korisnika
                return authUser(builder);
            }
        }

        // netačna lozinka ili korisnik ne postoji, izloguj se
        user = new User();
        if (Core.getInstance().StorageManager().getRoot() != null)
            user.setCwd(Core.getInstance().StorageManager().getRoot());
        return user;
    }

    /**
     * Autorizuje korisnika i postavlja ga kao onog u trenutnoj upotrebi.
     *
     * @param builder {@link UserBuilder} bilder za korisnika.
     * @return Nova instanca korisnika {@link IUser}.
     */
    private IUser authUser(UserBuilder builder) {
        user = new User(builder);
        return user;
    }

    @Override
    public synchronized IUser deinitUser() {
        user = new User();
        // #TODO kako se ovo odnosi na skladište? Da li sada deinicijalizovati skladište?
        return user;
    }

    @Override
    public Collection<IUser> getUsers() {
        return users;
    }

    @Override
    public IUser addUser(String username, String password, Collection<IPrivilege> privileges) {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        // učitaj sve korisnike iz konfiguracije
        loadUsers();

        // #OGRANIČENJE proveri da li već postoji korisnik sa datim korisničkim imenom
        for (IUser u : users)
            if (u.getUsername().equals(username))
                throw new IUserDuplicateUsernameException(username);

        // parametri ne smeju biti kao za anonimnog korisnika
        if (username == null || username.length() == 0 || password == null || password.length() == 0)
            throw new IUserInvalidDataException();

        // kreiraj novog korisnika
        IUser u = new User(username, password, privileges);

        // dodaj u config
        //noinspection ConstantConditions
        ConfigManager.getInstance().getConfig().addUser(u.toBuilder());

        // dodaj u lokalni niz
        users.add(u);
        return u;
    }

    @Override
    public IUser addUser(UserBuilder userBuilder) {
        Set<IPrivilege> privileges = new HashSet<>();
        for (PrivilegeBuilder pb : userBuilder.getPrivileges())
            privileges.add(new Privilege(pb));
        return addUser(userBuilder.getUsername(), userBuilder.getPassword(), privileges);
    }

    @Override
    public IUser getUser() {
        loadUsers();
        return user;
    }

    @Override
    public IUser getUser(String username) {
        loadUsers();
        for (IUser u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String username) {
        if (ConfigManager.getInstance().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        // #OGRANIČENJE zabranjeno brisanje trenutnog korisnika
        if (user.getUsername().equals(username))
            throw new IUserCannotDeleteCurrentUserException();

        // učitaj sve korisnike i pretraži
        IUser target = null;
        loadUsers();
        for (IUser u : users) {
            if (u.getUsername().equals(username)) {
                target = u;
                break;
            }
        }

        // ako ne postoji dati korisnik
        if (target == null)
            throw new IUserDeleteNotExistException();

        // izbriši iz config-a
        ConfigManager.getInstance().getConfig().deleteUser(target.toBuilder());

        // izbriši iz niza
        users.remove(target);
    }

    @Override
    public void deleteUser(IUser u) {
        if (ConfigManager.getInstance().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        // #OGRANIČENJE zabranjeno brisanje trenutnog korisnika
        if (u.equals(user))
            throw new IUserCannotDeleteCurrentUserException();

        // izbriši iz config-a
        ConfigManager.getInstance().getConfig().deleteUser(u.toBuilder());

        // izbriši iz niza
        users.remove(u);
    }

    /**
     * Učitava sve korisnike iz konfiguracije {@link IConfig}.
     */
    private void loadUsers() {
        if (usersLoaded) return;

        if (ConfigManager.getInstance().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        //noinspection ConstantConditions
        for (UserBuilder ub : Core.getInstance().ConfigManager().getConfig().getUsers()) {
            IUser t = new User(ub);
            if (!users.contains(t))
                users.add(t);
        }

        usersLoaded = true;
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final UserManager INSTANCE = new UserManager();
    }
}
