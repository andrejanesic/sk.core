package exceptions;

/**
 * Dešava se kada je potrebno pristupiti korenskom čvoru a on nije dostupan, tj. nije inicijalizovan.
 */
public class INodeRootNotInitializedException extends Exception {

    public INodeRootNotInitializedException() {
        super("Root node not initialized. You must init the storage first.");
    }
}
