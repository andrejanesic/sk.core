package io;

import repository.builder.DirectoryBuilder;
import user.builder.UserBuilder;

/**
 * Vrši interakciju sa okruženjem.
 */
public interface IODriver {

    /**
     * Kreira novi direktorijum.
     *
     * @param path Putanja do čvora.
     */
    void makeDirectory(String path);

    /**
     * Kreira novi fajl.
     *
     * @param path Putanja do čvora.
     */
    void makeFile(String path);

    /**
     * Briše direktorijum.
     *
     * @param path Putanja do čvora.
     */
    void deleteDirectory(String path);

    /**
     * Briše fajl.
     *
     * @param path Putanja do čvora.
     */
    void deleteFile(String path);

    /**
     * Pomera direktorijum sa sourcePath na destPath.
     *
     * @param sourcePath Izvorna putanja.
     * @param destPath   Nova putanja.
     */
    void moveDirectory(String sourcePath, String destPath);

    /**
     * Pomera fajl sa sourcePath na destPath.
     *
     * @param sourcePath Izvorna putanja.
     * @param destPath   Nova putanja.
     */
    void moveFile(String sourcePath, String destPath);

    /**
     * Inicijalizuje strukturu skladišta na datoj putanji u korenski direktorijum, to jest Directory.getRoot().
     *
     * @param path Putanja skladišta.
     * @return Vraća DirectoryBuilder instancu koja sadrži podstablo čvorova ili null ukoliko je neuspešno.
     */
    DirectoryBuilder initStorage(String path);

    /**
     * Autorizuje korisnika iz konfiguracije korisnika i vraća novi bilder ukoliko korisnik postoji.
     *
     * @param username Korisničko ime.
     * @param password Lozinka.
     * @return Bilder za korisnika ili null ukoliko je neuspešno.
     */
    UserBuilder initUser(String username, String password);

    /**
     * Deautorizuje korisnika (trenutno koristi sistem).
     *
     * @param username Korisničko ime.
     */
    void deinitUser(String username);
}
