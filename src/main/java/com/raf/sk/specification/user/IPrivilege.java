package com.raf.sk.specification.user;

import com.raf.sk.specification.user.builder.PrivilegeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Čuva informacije o privilegijama korisnika.
 * <p>
 * Jedna privilegija predstavlja jedno pravo korisnika (videti {@link IUser} da nešto uradi, npr: čita skladište,
 * upisuje u određeni direktorijum, dodaje nove korisnike, i slično.
 * <p>
 * Postoje generalizovane privilegije i specifične privilegije.
 * <p>
 * Generalizovane su vezane za tip ali ne i za specifičan objekat. Kod generalizovane privilegije,
 * {@link #getReferencedObject()} metoda uvek vraća null.
 * <p>
 * Na primer: pravo da se čitaju korisnici. Specifične privilegije su precizno vezane za neki objekat. Na primer:
 * korisnik nema pravo da briše direktorijum na određenoj String putanji.
 * <p>
 * Sve privilegije moraju imati definisan tip {@link PrivilegeType}.
 * <p>
 * Privilegije se dodaju korisniku preko {@link IUser} interfejsa korisnika.
 */
public interface IPrivilege {

    /**
     * Vraća objekat za koji je privilegija vezana, ili null ukoliko nije vezana ni za jedan objekat.
     *
     * @return Objekat za koji je privilegija vezana, ili null ukoliko nije vezana ni za jedan objekat.
     */
    @Nullable
    Object getReferencedObject();

    /**
     * Vraća tip privilegije {@link PrivilegeType} za koji je vezana privilegija.
     *
     * @return Tip privilegije {@link PrivilegeType} za koji je vezana privilegija.
     */
    @NotNull
    PrivilegeType getType();

    /**
     * Pretvara privilegiju u tip {@link PrivilegeBuilder} za dalju upotrebu.
     *
     * @return {@link PrivilegeBuilder} bilder klasa.
     */
    @NotNull
    PrivilegeBuilder toBuilder();
}
