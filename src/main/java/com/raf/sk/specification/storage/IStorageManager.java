package com.raf.sk.specification.storage;

import com.raf.sk.specification.exceptions.IStorageManagerINodeBuilderTreeInvalidException;
import com.raf.sk.specification.repository.Directory;
import org.jetbrains.annotations.Nullable;

/**
 * Interfejs za komponentu skladišta.
 * <p>
 * Ova komponenta je zadužena za inicijalizaciju i deinicijalizaciju skladišta, i mora se pozvati pre interakcij sa
 * skladištem; odnosno, ona poziova povezani {@link com.raf.sk.specification.io.IODriver} koji "gradi" stablo skladišta i potom vraća korenski
 * {@link Directory}, kome pristupamo preko ovog menadžera.
 * <p>
 * Model podataka implementiran je u {@link com.raf.sk.specification.repository} paketu.
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
     * {@link com.raf.sk.specification.io.IODriver#initStorage(String)} kako bi dobio podatke o stablu, na osnovu koga gradi
     * {@link com.raf.sk.specification.repository.Directory} i {@link com.raf.sk.specification.repository.File} čvorove.
     *
     * @param path Putanja do skladišta u OS okruženju.
     * @return Korenski {@link Directory}.
     * @throws IStorageManagerINodeBuilderTreeInvalidException Greška ukoliko u strukturi drveta koju je
     *                                                         {@link com.raf.sk.specification.io.IODriver} predao postoji logička greška.
     */
    Directory initStorage(String path) throws IStorageManagerINodeBuilderTreeInvalidException;

    /**
     * Deinicijalizuje skladište. Ovime se briše referenca na korenski direktorijum i više nije moguće upravljati
     * skladištem dok se ponovo ne pozove {@link #initStorage(String)}.
     */
    void deinitStorage();
}
