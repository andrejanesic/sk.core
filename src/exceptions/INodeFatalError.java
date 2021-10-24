package exceptions;

/**
 * Dešava se kada za vreme izvršavanja dođe do postavke pogrešnog parametra koji onemogućava rad čvora.
 */
public class INodeFatalError extends RuntimeException {

    public INodeFatalError(String s) {
        super(s);
    }
}
