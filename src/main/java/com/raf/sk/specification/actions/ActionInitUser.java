package com.raf.sk.specification.actions;

import com.raf.sk.specification.config.IConfig;
import com.raf.sk.specification.config.IConfigManager;
import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.IActionInsufficientPrivilegeException;
import com.raf.sk.specification.exceptions.IComponentNotInitializedException;
import com.raf.sk.specification.user.IUser;
import com.raf.sk.specification.user.IUserManager;
import com.raf.sk.specification.user.Privilege;

import java.util.Collections;

import static com.raf.sk.specification.user.PrivilegeType.ALL;
import static com.raf.sk.specification.user.PrivilegeType.USER_LOGIN;

/**
 * Radnja inicijalizacije (logovanja i izlogovanja) korisnika.
 * <p>
 * Da bi korisnik pristupio skladištu, mora izvršiti ovaj korak pre inicijalizacije skladišta.
 * <p>
 * Ova akcija zahteva da je izvršena inicijalizacija konfiguracije {@link ActionInitConfig}. Ukoliko ni jedan korisnik
 * nije učitan iz konfiguracionog fajla, ova akcija će napraviti novog "master" korisnika.
 */
public class ActionInitUser implements IAction {

    /**
     * Prati da li je korisnik već jednom pokušao da se uloguje sa ovom akcijom.
     */
    private boolean tried = false;

    /**
     * Korisničko ime.
     */
    private String username;

    /**
     * Lozinka.
     */
    private String password;

    /**
     * Podrazumevani konstruktor.
     *
     * @param username Korisničko ime.
     * @param password Lozinka.
     */
    public ActionInitUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Boolean run() {
        if (tried)
            return false;

        IConfigManager config = Core.getInstance().ConfigManager();
        IUserManager users = Core.getInstance().UserManager();

        // proveri da li postoji konfiguracija
        if (config.getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        // proveri da li postoji korisnik (bar anonimni, koji se automatski inicijalizuje pozivom instance UserManager)
        if (users.getUser() == null)
            throw new IActionInsufficientPrivilegeException();

        // ako ne postoje registrovani korisnici, napravi novog koji će imati sve privilegije
        if (config.getConfig().getUsers().size() == 0) {
            IUser u = users.addUser(username, password, Collections.singletonList(new Privilege(ALL)));
            users.initUser(username, password);
            return u.isAuthenticated();
        }

        // postoje korisnici, proveri da li korisnik ima pravo logovanja (predefinisano da)
        //noinspection ConstantConditions
        if (!Core.getInstance().UserManager().getUser().hasPrivilege(USER_LOGIN))
            throw new IActionInsufficientPrivilegeException();

        tried = true;
        IUser u = Core.getInstance().UserManager().initUser(username, password);
        return u.isAuthenticated();
    }

    @Override
    public Boolean undo() {
        if (Core.getInstance().UserManager().getUser() == null)
            throw new IActionInsufficientPrivilegeException();

        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser().getUsername().equals(username))
            Core.getInstance().UserManager().deinitUser();
        return true;
    }
}
