package core;

import config.ConfigManager;
import config.IConfigManager;
import io.IODriver;
import io.IOManager;
import storage.IStorageManager;
import storage.StorageManager;
import user.IUserManager;
import user.UserManager;

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
     * Vraća instancu core.Core-a.
     *
     * @return core.Core instanca.
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
     * Vraća instancu komponente IConfigManager.
     *
     * @return Instanca komponente.
     */
    public IConfigManager ConfigManager() {
        return ConfigManager.getInstance();
    }

    /**
     * Vraća instancu IODriver-a.
     *
     * @return Instanca drajvera.
     */
    public IODriver IODriver() {
        return IOManager.getIOAdapter();
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final Core INSTANCE = new Core();
    }
}
