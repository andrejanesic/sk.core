package exceptions;

/**
 * Dešava se ukoliko se na direktorijumu kreira novi čvor sa nazivom čvora koji već postoji.
 */
public class DirectoryCreateNameNotUniqueException extends Exception {

    public DirectoryCreateNameNotUniqueException(String s) {
        super("Node with name \"" + s + "\" already exists.");
    }

    public DirectoryCreateNameNotUniqueException() {
        super("New nodes must have unique names.");
    }
}
