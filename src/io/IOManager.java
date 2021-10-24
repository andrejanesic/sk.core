package io;

/**
 * Upravlja trenutnom IOHandler implementacijom.
 */
public class IOManager {

    /**
     * Trenutna IOHandler implementacija.
     */
    private static IOHandler instance;

    /**
     * Vraća IOHandler klasu.
     *
     * @return IOHandler instanca.
     */
    public static IOHandler getIOHandler() {
        return instance;
    }

    /**
     * Postavlja instancu IOHandler-a.
     *
     * @param instance Potklasa IOHandler-a.
     */
    public static void setIOHandler(IOHandler instance) {
        IOManager.instance = instance;
    }
}
