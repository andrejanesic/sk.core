package storage;

import repository.Directory;

/**
 * Interfejs za komponentu skladišta.
 */
public interface IStorageManager {

    /**
     * Vraća korenski direktorijum skladišta. Može biti null ukoliko još nije izgrađeno.
     *
     * @return Korenski direktorijum skladišta ili null ukoliko još nije izgrađen.
     */
    Directory getRoot();

    /**
     * Gradi korenski Directory na osnovu putanje do skladišta u okruženju.
     *
     * @param path Putanja do skladišta u OS okruženju.
     * @return Korenski Directory.
     */
    Directory initStorage(String path);

    /**
     * Deinicijalizuje skladište.
     */
    void deinitStorage();
}
