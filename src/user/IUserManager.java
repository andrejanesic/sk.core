package user;

/**
 * Interfejs za komponentu za menadžment korisnika.
 */
public interface IUserManager {

    /**
     * Vraća trenutno povezanog korisnika ili null ukoliko niko nije povezan.
     *
     * @return Korisnik ili null.
     */
    User getUser();

    /**
     * Inicijalizuje korisnika na osnovu datih kredencijala. Trenutno ulogovan korisnik se diskonektuje.
     *
     * @param username Korisničko ime.
     * @param password Lozinka.
     * @return Korisnik ili null.
     */
    User initUser(String username, String password);

    /**
     * Deinicijalizuje korisnika.
     *
     * @return Novi anonimni korisnik.
     */
    User deinitUser();
}
