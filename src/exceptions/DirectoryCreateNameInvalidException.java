package exceptions;

/**
 * Dešava se kada korisnik pokuša da kreira novi poddirektorijum ili fajl sa ne-validnim nazivom.
 */
public class DirectoryCreateNameInvalidException extends Exception {

    public DirectoryCreateNameInvalidException(String fn) {
        super("File name \"" + fn + "\" for new node is not valid.");
    }

    public DirectoryCreateNameInvalidException() {
        super("File name for new node is not valid.");
    }
}
