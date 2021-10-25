package io;

/**
 * Upravlja trenutnom IODriver implementacijom.
 */
public class IOManager {

    /**
     * Trenutna IODriver implementacija.
     */
    private static IODriver instance;

    /**
     * Vraća IODriver klasu.
     *
     * @return IODriver instanca.
     */
    public static IODriver getIOAdapter() {
        return instance;
    }

    /**
     * Postavlja instancu IODriver-a.
     *
     * @param instance Potklasa IODriver-a.
     */
    public static void setIODriver(IODriver instance) {
        IOManager.instance = instance;
    }
}
