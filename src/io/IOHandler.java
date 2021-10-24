package io;

import repository.builder.DirectoryBuilder;

/**
 * Vrši interakciju sa okruženjem.
 */
public interface IOHandler {

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
     * @return Vraća DirectoryBuilder instancu koja sadrži podstablo čvorova.
     */
    DirectoryBuilder buildStorage(String path);
}
