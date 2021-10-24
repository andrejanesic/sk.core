package exceptions;

/**
 * Dešava se kada za vreme izvršavanja dođe do postavke pogrešnog parametra koji onemogućava rad čvora.
 */
public class INodeFatalException extends RuntimeException {

    public INodeFatalException(String s) {
        super(s);
    }
}
