package io;

import org.jetbrains.annotations.NotNull;
import repository.builder.DirectoryBuilder;

/**
 * Vrši interakciju sa okruženjem. Ovaj interfejs se koristi za implementaciju aplikacije na konkretnoj platformi.
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
     * Čita konfiguracioni fajl. Vraća null ukoliko ne postoji.
     *
     * @param path Putanja na kojoj treba pročitati ili inicijalizovati konfiguracioni fajl u OS okruženju.
     * @return Potrebno je vratiti sadržinu konfiguracionog fajla (u JSON formatu) ili null ukoliko konfiguracioni fajl
     * ne postoji.
     */
    String readConfig(String path);

    /**
     * Piše konfiguracioni fajl na zadatoj putanji.
     *
     * @param json Sadržina konfiguracionog fajla u JSON formatu.
     * @param path Putanja na kojoj treba pročitati ili inicijalizovati konfiguracioni fajl u OS okruženju.
     */
    void writeConfig(String json, String path);

    /**
     * Inicijalizuje strukturu skladišta na datoj putanji u korenski {@link DirectoryBuilder}. Ako je skladište prazno
     * (ne sadrži ni jedan fajl/direktorijum), potrebno je vratiti "prazni" {@link DirectoryBuilder}. Povratna vrednost
     * ne sme biti null.
     *
     * @param path Putanja skladišta u okruženju OS-a.
     * @return {@link DirectoryBuilder} koji sadrži podstablo čvorova {@link repository.builder.INodeBuilder}.
     */
    @NotNull
    DirectoryBuilder initStorage(String path);
}
