package user;

import io.IOManager;
import user.builder.UserBuilder;

/**
 * Implementacija komponente za korisnički menadžment.
 */
public class UserManager implements IUserManager {

    /**
     * Trenutni korisnik.
     */
    private User user;

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

    public User getUser() {
        return user;
    }

    public synchronized User initUser(String username, String password) {
        deinitUser();
        UserBuilder userBuilder = IOManager.getIOAdapter().initUser(username, password);
        user = new User(userBuilder);
        return user;
    }

    public synchronized User deinitUser() {
        user = new User();
        IOManager.getIOAdapter().deinitUser(getUser().getUsername());
        return user;
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final UserManager INSTANCE = new UserManager();
    }
}
