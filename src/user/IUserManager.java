package user;

import org.jetbrains.annotations.Nullable;
import user.builder.UserBuilder;

import java.util.Collection;

/**
 * Interfejs za komponentu za menadžment korisnika.
 * <p>
 * Ova komponenta se koristi za svu "globalnu" interakciju sa korisnikom: (de)inicijalizacija, dobijanje svih korisnika,
 * kreiranje/brisanje novih korisnika, itd.
 * <p>
 * Za konkretnu instancu korisnika, videti {@link IUser} klasu.
 */
public interface IUserManager {

    /**
     * Inicijalizuje anonimnog korisnika {@link IUser} sa osnovnim anonimnim kredencijalima. Diskonektuje trenutnog
     * korisnika.
     *
     * @return Korisnik {@link IUser}.
     */
    IUser initUser();

    /**
     * Inicijalizuje korisnika {@link IUser} na osnovu datih kredencijala. Trenutno ulogovan korisnik se diskonektuje.
     * Ukoliko kredencijali nisu tačni, korisnik će se svakako diskonektovati i ponovo konektovati kao anonimni
     * korisnik.
     *
     * @param username Korisničko ime.
     * @param password Lozinka.
     * @return Korisnik {@link IUser} ili null.
     */
    IUser initUser(String username, String password);

    /**
     * Deinicijalizuje korisnika {@link IUser}. Na ovaj način se korisnici "izloguju", tj. diskonektuju, sa aplikacije.
     * Ovaj metod NE deinicijalizuje skladište, to se mora uraditi preko {@link storage.IStorageManager} komponente.
     *
     * @return Novi anonimni korisnik {@link IUser}.
     */
    IUser deinitUser();

    /**
     * Dodaje novog korisnika u {@link config.IConfigManager} i vraća instancu {@link IUser}. Ovaj korisnik NEĆE biti
     * instanciran kao trenutni. To se mora naknadno uraditi putem {@link #initUser(String, String)} metode.
     *
     * @param username   Korisničko ime.
     * @param password   Lozinka.
     * @param privileges Kolekcija privilegija {@link IPrivilege}.
     * @return Novi {@link IUser}.
     * @throws exceptions.IUserDuplicateUsernameException Ukoliko je dato isto korisničko ime za novog korisnika kao ime
     *                                                    koje koristi neki već postojeći {@link IUser}.
     */
    IUser addUser(String username, String password, Collection<IPrivilege> privileges);

    /**
     * Dodaje novog {@link IUser} u {@link config.IConfigManager} i vraća instancu {@link IUser}.
     *
     * @param userBuilder {@link UserBuilder} bilder na kome će korisnik biti napravljen.
     * @return Novi {@link IUser}.
     * @throws exceptions.IUserDuplicateUsernameException Ukoliko je dato isto korisničko ime za novog korisnika kao ime
     *                                                    koje koristi neki već postojeći {@link IUser}.
     */
    IUser addUser(UserBuilder userBuilder);

    /**
     * Vraća trenutno povezanog {@link IUser} ili null ukoliko niko nije povezan.
     *
     * @return {@link IUser} ili null.
     */
    @Nullable
    IUser getUser();

    /**
     * Vraća {@link Collection} svih učitanih {@link IUser} preko {@link config.IConfigManager}-a.
     *
     * @return {@link Collection} svih učitanih {@link IUser}-a.
     */
    Collection<IUser> getUsers();

    /**
     * Briše {@link IUser} iz instance kao i iz konfiguracije putem {@link config.IConfigManager}.
     * <p>
     * Nije moguće obrisati trenutno instanciranog korisnika. To izbacuje
     * {@link exceptions.IUserCannotDeleteCurrentUserException}.
     *
     * @param username Korisničko ime {@link IUser} koga treba obrisati.
     * @throws exceptions.IUserCannotDeleteCurrentUserException Ukoliko korisnik pokuša da obriše {@link IUser} koji je
     *                                                          trenutno instanciran.
     */
    void deleteUser(String username);

    /**
     * Briše {@link IUser} iz instance kao i iz konfiguracije putem {@link config.IConfigManager}.
     * <p>
     * Nije moguće obrisati trenutno instanciranog korisnika. To izbacuje
     * {@link exceptions.IUserCannotDeleteCurrentUserException}.
     *
     * @param u {@link IUser} koga treba obrisati.
     * @throws exceptions.IUserCannotDeleteCurrentUserException Ukoliko korisnik pokuša da obriše {@link IUser} koji je
     *                                                          trenutno instanciran.
     */
    void deleteUser(IUser u);
}
