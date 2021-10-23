package exceptions;

/**
 * Dešava se ukoliko se pozove Directory.resolvePath sa lošom putanjom.
 */
public class DirectoryInvalidPathException extends Exception {

    public DirectoryInvalidPathException(String s) {
        super("Path " + s + " doesn't exist.");
    }
}
