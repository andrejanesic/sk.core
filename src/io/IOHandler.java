package io;

/**
 * Vrši interakciju sa okruženjem.
 */
public abstract class IOHandler {

    /**
     * Kreira novi direktorijum.
     *
     * @param parentPath Putanja roditeljskog direktorijuma.
     * @param name       Naziv direktorijuma.
     */
    public abstract void makeDirectory(String parentPath, String name);

    /**
     * Kreira novi fajl.
     *
     * @param parentPath Putanja roditeljskog direktorijuma.
     * @param name       Naziv fajla.
     */
    public abstract void makeFile(String parentPath, String name);
}
