package io;

import exceptions.IOManagerNoDriverException;

/**
 * Upravlja trenutnom {@link IODriver} implementacijom. Na ovu komponentu se "kače" konkretne implementacije.
 */
public class IOManager {

    /**
     * Trenutna {@link IODriver} implementacija.
     */
    private static IODriver instance;

    /**
     * Vraća registrovan {@link IODriver}.
     *
     * @return IODriver instanca.
     * @throws IOManagerNoDriverException Ukoliko nije registrovan ni jedan {@link IODriver} a neko pokuša da pristupi
     *                                    instanci.
     */
    public static IODriver getIODriver() throws IOManagerNoDriverException {
        if (instance == null)
            throw new IOManagerNoDriverException();
        return instance;
    }

    /**
     * Registruje instancu {@link IODriver}.
     *
     * @param instance Potklasa {@link IODriver} interfejsa.
     */
    public static void setIODriver(IODriver instance) {
        IOManager.instance = instance;
    }
}
