package io;

/**
 * Vrši interakciju sa okruženjem.
 */
public abstract class IOHandler {

    /**
     * Kreira novi direktorijum.
     *
     * @param path Putanja do čvora.
     */
    public abstract void makeDirectory(String path);

    /**
     * Kreira novi fajl.
     *
     * @param path Putanja do čvora.
     */
    public abstract void makeFile(String path);

    /**
     * Briše direktorijum.
     *
     * @param path Putanja do čvora.
     */
    public abstract void deleteDirectory(String path);

    /**
     * Briše fajl.
     *
     * @param path Putanja do čvora.
     */
    public abstract void deleteFile(String path);

    /**
     * Pomera direktorijum sa sourcePath na destPath.
     *
     * @param sourcePath Izvorna putanja.
     * @param destPath   Nova putanja.
     */
    public abstract void moveDirectory(String sourcePath, String destPath);

    /**
     * Pomera fajl sa sourcePath na destPath.
     *
     * @param sourcePath Izvorna putanja.
     * @param destPath   Nova putanja.
     */
    public abstract void moveFile(String sourcePath, String destPath);
}
