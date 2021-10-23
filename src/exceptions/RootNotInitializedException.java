package exceptions;

/**
 * Dešava se kada program pristupi korenskom direktorijumu a isti još uvek nije inicijalizovan.
 */
public class RootNotInitializedException extends Exception {

    /**
     * Podrazumevani konstruktor.
     */
    public RootNotInitializedException() {
        super("Attempting to access root Directory but directory not yet initialized.");
    }
}
