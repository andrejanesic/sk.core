package com.raf.sk.specification.user;

import com.raf.sk.specification.repository.Directory;
import com.raf.sk.specification.user.builder.UserBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Čuva informacije o korisniku. Ova klasa se koristi samo za interakciju sa korisnikom (npr. daj/proveri/oduzmi
 * privilegiju {@link IPrivilege}).
 * <p>
 * Ova klasa se ne koristi za "globalni" menadžment korisnika (npr. dodaj/obriši korisnika). Za to se koristi
 * {@link IUserManager} klasa.
 */
public interface IUser {

    String getUsername();

    String getPassword();

    Collection<IPrivilege> getPrivileges();

    boolean isAuthenticated();

    /**
     * Dodaje novu {@link IPrivilege} korisniku.
     *
     * @param p Nova {@link IPrivilege}.
     */
    void grantPrivilege(IPrivilege p);

    /**
     * Dodaje novu {@link IPrivilege} korisniku.
     *
     * @param o    Objekat vezan za {@link IPrivilege}.
     * @param type Tip privilegije {@link PrivilegeType}.
     */
    void grantPrivilege(Object o, PrivilegeType type);

    /**
     * Dodaje novi tip {@link IPrivilege} (generalizovan) korisniku.
     *
     * @param type Tip privilegije {@link PrivilegeType}.
     */
    void grantPrivilege(PrivilegeType type);

    /**
     * Oduzima {@link IPrivilege} od korisnika. Postoje određene privilegije koje svaki korisnik mora da ima, i koje se
     * ne mogu obrisati, a čak i ukoliko program pokuša, vratiće se vrednost kao da je obrisana; ali nije. Te
     * privilegije su važne za osnovno funkcionisanje aplikacije.
     *
     * @param p {@link IPrivilege} koju treba oduzeti.
     */
    void revokePrivilege(IPrivilege p);

    /**
     * Oduzima {@link IPrivilege} od korisnika. Postoje određene privilegije koje svaki korisnik mora da ima, i koje se
     * ne mogu obrisati, a čak i ukoliko program pokuša, vratiće se vrednost kao da je obrisana; ali nije. Te
     * privilegije su važne za osnovno funkcionisanje aplikacije.
     *
     * @param o    Objekat vezan za {@link IPrivilege}.
     * @param type Tip privilegije {@link PrivilegeType}.
     */
    void revokePrivilege(Object o, PrivilegeType type);

    /**
     * Oduzima {@link IPrivilege} od korisnika. Postoje određene privilegije koje svaki korisnik mora da ima, i koje se
     * ne mogu obrisati, a čak i ukoliko program pokuša, vratiće se vrednost kao da je obrisana; ali nije. Te
     * privilegije su važne za osnovno funkcionisanje aplikacije.
     *
     * @param type Tip privilegije {@link PrivilegeType}.
     */
    void revokePrivilege(PrivilegeType type);

    /**
     * Proverava da li korisnik ima datu privilegiju {@link IPrivilege}.
     *
     * @param p {@link IPrivilege}.
     * @return True ako ima, false ako nema.
     */
    boolean hasPrivilege(IPrivilege p);

    /**
     * Proverava da li korisnik ima datu privilegiju {@link IPrivilege}.
     *
     * @param o    Objekat vezan za privilegiju.
     * @param type Tip privilegije {@link PrivilegeType}.
     * @return True ako ima, false ako nema.
     */
    boolean hasPrivilege(Object o, PrivilegeType type);

    /**
     * Proverava da li korisnik ima dati tip privilegije (generalizovano {@link IPrivilege}).
     *
     * @param type Tip privilegije {@link PrivilegeType}.
     * @return True ako ima, false ako nema.
     */
    boolean hasPrivilege(PrivilegeType type);

    /**
     * Vraća trenutni {@link Directory} u kome se korisnik nalazi. Može biti null, ukoliko još nije inicijalizovano
     * skladište.
     *
     * @return Trenutni {@link Directory} u kome se korisnik nalazi.
     */
    @Nullable
    Directory getCwd();

    /**
     * Postavlja direktorijum {@link Directory} u kome se korisnik nalazi.
     *
     * @param d Novi radni direktorijum.
     */
    void setCwd(Directory d);

    /**
     * Ažurira korisnika u konfiguraciji. Mora biti pozvano nakon svakog metoda koji ažurira podatke korisnika.
     */
    void update();

    /**
     * Vraća bilder ekvivalent {@link UserBuilder} korisnika za upotrebu u konfiguraciji.
     *
     * @return Bilder ekvivalent {@link UserBuilder} korisnika za upotrebu u konfiguraciji.
     */
    UserBuilder toBuilder();
}
