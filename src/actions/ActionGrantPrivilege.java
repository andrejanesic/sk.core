package actions;

import core.Core;
import exceptions.IActionBadParameterException;
import exceptions.IActionInsufficientPrivilegeException;
import user.IUser;
import user.PrivilegeType;

/**
 * Ovom komandom korisink {@link user.IUser} daje drugom korisniku određenu {@link user.Privilege} generalnog ili
 * preciznog (referncira objekat) tipa.
 * <p>
 * Ulogovani korisnik mora imati bar privilegiju {@link user.PrivilegeType} PRIVILEGE_GRANT kako bi davao privilegije,
 * odnosno {@link user.PrivilegeType} PRIVILEGE_REVOKE kako bi ih oduzimao. Obe su potrebne da bi komanda radila kako
 * treba usled {@link #undo()} mogućnosti.
 * <p>
 * <em>VAŽNO:</em> ukoliko korisnik daje privilegiju vezanu za neki direktorijum, on MORA napisati punu putanju tog
 * direktorijuma.
 */
public class ActionGrantPrivilege implements IAction {

    /**
     * Naziv korisnika kome treba dati privilegiju.
     */
    private String username;

    /**
     * Objekat koji je referenciran.
     */
    private String referencedObject;

    /**
     * Tip privilegije kao String.
     *
     * @see user.PrivilegeType
     */
    private String type;

    /**
     * Podrazumevani konstruktor.
     *
     * @param username         Korisnik kome treba dati privilegiju.
     * @param type             Tip privilegije kao String.
     * @param referencedObject Objekat koji je referenciran.
     */
    public ActionGrantPrivilege(String username, String type, String referencedObject) {
        this.username = username;
        this.referencedObject = referencedObject;
        this.type = type;
    }

    /**
     * Podrazumevani konstruktor.
     *
     * @param username Korisnik kome treba dati privilegiju.
     * @param type     Tip privilegije kao String.
     */
    public ActionGrantPrivilege(String username, String type) {
        this(username, type, null);
    }

    @Override
    public Object run() {
        if (Core.getInstance().UserManager().getUser() == null)
            throw new IActionInsufficientPrivilegeException();

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.PRIVILEGE_GRANT) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.PRIVILEGE_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        PrivilegeType privilegeType = null;
        for (PrivilegeType pt : PrivilegeType.values()) {
            if (pt.toString().equalsIgnoreCase(type)) {
                privilegeType = pt;
                break;
            }
        }

        // nije prepoznat tip privilegije
        if (privilegeType == null)
            throw new IActionBadParameterException(type);

        // #OGRANIČENJE korisnik ne može sam sebi dati privilegije
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser().getUsername() != null
                && Core.getInstance().UserManager().getUser().getUsername().equals(username))
            throw new IActionBadParameterException("you cannot give privileges to yourself.");

        // #OGRANIČENJE korisnik ne može nikome dati neku privilegiju koju sam nema
        //noinspection ConstantConditions
        if (!Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL) &&
                !Core.getInstance().UserManager().getUser().hasPrivilege(referencedObject, privilegeType))
            throw new IActionInsufficientPrivilegeException();

        // proveriti da li referencirani korisnik postoji
        IUser u = Core.getInstance().UserManager().getUser(username);
        if (u == null)
            throw new IActionBadParameterException("no user with the username \"" + username + "\" exists.");

        // ako korisnik već ima privilegiju, abortiraj
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser(username).hasPrivilege(referencedObject, privilegeType))
            throw new IActionBadParameterException("user already has that permission.");

        u.grantPrivilege(referencedObject, privilegeType);
        return true;
    }

    @Override
    public Object undo() {
        if (Core.getInstance().UserManager().getUser() == null)
            throw new IActionInsufficientPrivilegeException();

        //noinspection ConstantConditions
        if (!(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.PRIVILEGE_REVOKE) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.PRIVILEGE_ALL) ||
                Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL)))
            throw new IActionInsufficientPrivilegeException();

        PrivilegeType privilegeType = null;
        for (PrivilegeType pt : PrivilegeType.values()) {
            if (pt.toString().equalsIgnoreCase(type)) {
                privilegeType = pt;
                break;
            }
        }

        // nije prepoznat tip privilegije
        if (privilegeType == null)
            throw new IActionBadParameterException(type);

        // #OGRANIČENJE korisnik ne može sam sebi dati privilegije
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser().getUsername() != null
                && Core.getInstance().UserManager().getUser().getUsername().equals(username))
            throw new IActionBadParameterException("you cannot give privileges to yourself.");

        // #OGRANIČENJE korisnik ne može nikome dati neku privilegiju koju sam nema
        //noinspection ConstantConditions
        if (!Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.ALL) &&
                !Core.getInstance().UserManager().getUser().hasPrivilege(referencedObject, privilegeType))
            throw new IActionInsufficientPrivilegeException();

        // #OGRANIČENJE korisnik ne može brisati osnovne dozvole
        if (privilegeType.equals(PrivilegeType.USER_LOGIN) || privilegeType.equals(PrivilegeType.USER_LOGOUT))
            throw new IActionBadParameterException("you cannot delete essential user privileges.");

        // proveriti da li referencirani korisnik postoji
        IUser u = Core.getInstance().UserManager().getUser(username);
        if (u == null)
            throw new IActionBadParameterException("no user with the username \"" + username + "\" exists.");

        // ako korisnik već nema privilegiju, abortiraj
        //noinspection ConstantConditions
        if (!Core.getInstance().UserManager().getUser(username).hasPrivilege(referencedObject, privilegeType))
            return true;

        u.revokePrivilege(referencedObject, privilegeType);
        return true;
    }
}
