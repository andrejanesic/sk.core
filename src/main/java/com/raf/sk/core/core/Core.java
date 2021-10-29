package com.raf.sk.core.core;

import com.raf.sk.core.config.ConfigManager;
import com.raf.sk.core.config.IConfigManager;
import com.raf.sk.core.exceptions.IOManagerNoDriverException;
import com.raf.sk.core.io.IODriver;
import com.raf.sk.core.io.IOManager;
import com.raf.sk.core.storage.IStorageManager;
import com.raf.sk.core.storage.StorageManager;
import com.raf.sk.core.user.IUserManager;
import com.raf.sk.core.user.UserManager;

/**
 * Učitava skladište i njegove fajlove, konfiguraciju i korisnike.
 */
public class Core {

    /**
     * Podrazumevani konstruktor.
     */
    private Core() {
    }

    /**
     * Vraća instancu {@link Core}
     *
     * @return {@link Core} instanca.
     */
    public static Core getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Vraća instancu komponente IStorageManager.
     *
     * @return Instanca komponente.
     */
    public IStorageManager StorageManager() {
        return StorageManager.getInstance();
    }

    /**
     * Vraća instancu komponente IUserManager.
     *
     * @return Instanca komponente.
     */
    public IUserManager UserManager() {
        return UserManager.getInstance();
    }

    /**
     * Vraća instancu komponente {@link IConfigManager}.
     *
     * @return Instanca komponente.
     */
    public IConfigManager ConfigManager() {
        return ConfigManager.getInstance();
    }

    /**
     * Vraća registrovan {@link IODriver}.
     *
     * @return Instanca drajvera.
     * @throws IOManagerNoDriverException Ukoliko nema registrovane {@link IODriver} instance na koju je moguće da se
     *                                    komponenta priključi.
     */
    public IODriver IODriver() throws IOManagerNoDriverException {
        return IOManager.getIODriver();
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final Core INSTANCE = new Core();
    }
}
