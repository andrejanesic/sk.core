package user;

import config.ConfigManager;
import config.IConfig;
import core.Core;
import exceptions.IComponentNotInitializedException;
import exceptions.IUserCannotDeleteCurrentUserException;
import exceptions.IUserDuplicateUsernameException;
import user.builder.PrivilegeBuilder;
import user.builder.UserBuilder;

import java.util.ArrayList;
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
    private Collection<IUser> users = new ArrayList<>();

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
    public IUser addUser(String username, String password, Collection<IPrivilege> IPrivileges) {
        if (ConfigManager.getInstance().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        // #OGRANIČENJE proveri da li već postoji korisnik sa datim korisničkim imenom
        for (IUser u : users)
            if (u.getUsername().equals(username))
                throw new IUserDuplicateUsernameException(username);

        // kreiraj novog korisnika
        IUser u = new User(username, password, IPrivileges);

        // dodaj u config
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
        return user;
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
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final UserManager INSTANCE = new UserManager();
    }
}
