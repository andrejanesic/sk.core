package com.raf.sk.core.storage;

import com.raf.sk.core.exceptions.IStorageManagerINodeBuilderTreeInvalidException;
import com.raf.sk.core.repository.Directory;
import org.jetbrains.annotations.Nullable;

/**
 * Interfejs za komponentu skladišta.
 * <p>
 * Ova komponenta je zadužena za inicijalizaciju i deinicijalizaciju skladišta, i mora se pozvati pre interakcij sa
 * skladištem; odnosno, ona poziova povezani {@link com.raf.sk.specification.io.IODriver} koji "gradi" stablo skladišta
 * i potom vraća korenski
 * {@link Directory}, kome pristupamo preko ovog menadžera.
 * <p>
 * Model podataka implementiran je u {@link com.raf.sk.core.repository} paketu.
 */
public interface IStorageManager {

    /**
     * Vraća korenski {@link Directory} skladišta. Može biti null ukoliko još nije izgrađeno.
     *
     * @return Korenski direktorijum {@link Directory} skladišta ili null ukoliko još nije izgrađen.
     */
    @Nullable
    Directory getRoot();

    /**
     * Gradi korenski {@link Directory} na osnovu putanje do skladišta u okruženju. Poziva
     * {@link com.raf.sk.specification.io.IODriver#initStorage()} kako bi dobio podatke o stablu, na osnovu koga gradi
     * {@link com.raf.sk.core.repository.Directory} i {@link com.raf.sk.core.repository.File} čvorove.
     * <p>
     * Za inicijalizaciju, IODriver mora koristiti isti path koji mu je dat prilikom inicijalizacije IConfig
     * komponente, odnosno konfiguracionog fajla.
     *
     * @return Korenski {@link Directory}.
     * @throws IStorageManagerINodeBuilderTreeInvalidException Greška ukoliko u strukturi drveta koju je
     *                                                         {@link com.raf.sk.specification.io.IODriver} predao
     *                                                         postoji logička greška.
     */
    Directory initStorage() throws IStorageManagerINodeBuilderTreeInvalidException;

    /**
     * Deinicijalizuje skladište. Ovime se briše referenca na korenski direktorijum i više nije moguće upravljati
     * skladištem dok se ponovo ne pozove {@link #initStorage()}.
     */
    void deinitStorage();
}
