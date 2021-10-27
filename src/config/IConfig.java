package config;

import org.jetbrains.annotations.Nullable;
import repository.limitations.INodeLimitation;
import user.builder.UserBuilder;

import java.util.Collection;

/**
 * IConfig predstavlja konfiguraciju skladišta i funkcioniše kao mapa.
 * <p>
 * Ova klasa se dalje serializuje/deserializuje kao JSON objekat pomoću konstruktora odnosno pomoćne biblioteke i metode
 * {@link #toJson()}.
 */
public interface IConfig {

    /**
     * Dodaje novog {@link UserBuilder}-a u konfiguraciju. {@link UserBuilder} će biti sačuvan, ali ne i inicijalizovan
     * u korisnika. Za to je potrebno upotrebiti {@link user.IUserManager} komponentu.
     *
     * @param userBuilder {@link UserBuilder}.
     */
    void addUser(UserBuilder userBuilder);

    /**
     * Vraća učitane {@link UserBuilder}-e.
     *
     * @return {@link Collection} učitanih {@link UserBuilder}-a.
     */
    Collection<UserBuilder> getUsers();

    /**
     * Vraća bildera korisnika {@link UserBuilder} po korisničkom imenu, ili null ukoliko ne postoji.
     *
     * @param username Korisničko ime.
     * @return {@link UserBuilder} instanca.
     */
    @Nullable
    UserBuilder getUser(String username);

    /**
     * Ažurira učitanog {@link UserBuilder}-a.
     *
     * @param userBuilder {@link UserBuilder} koga treba ažurirati.
     */
    void updateUser(UserBuilder userBuilder);

    /**
     * Briše datog {@link UserBuilder}-a. Ako je neki korisnik učitan iz ovog {@link UserBuilder}-a, on neće biti
     * deinicijalizovan, to se mora izvršiti putem {@link user.IUserManager}-a.
     *
     * @param userBuilder {@link UserBuilder} koga treba obrisati.
     */
    void deleteUser(UserBuilder userBuilder);

    /**
     * Dodaje novo {@link INodeLimitation} ograničenje.
     *
     * @param nodeLimitation Novo ograničenje.
     */
    void addLimitation(INodeLimitation nodeLimitation);

    /**
     * Vraća sva {@link INodeLimitation} ograničenja.
     *
     * @return Sva {@link INodeLimitation} ograničenja.
     */
    Collection<INodeLimitation> getLimitations();

    /**
     * Dodaje novo {@link INodeLimitation} ograničenje.
     *
     * @param nodeLimitation Novo ograničenje.
     */
    void deleteLimitation(INodeLimitation nodeLimitation);

    /**
     * Vraća konfiguraciju u obliku validnog JSON String-a.
     *
     * @return Konfiguracija u obliku validnog JSON String-a.
     */
    String toJson();

    /**
     * "Convenience" metod. Poziva {@link IConfigManager#saveConfig()} ukoliko je pozvano na trenutno učitanoj
     * {@link IConfig} konfiguraciji. Ovaj metod treba da pozovu sve druge metode.
     */
    void save();
}
