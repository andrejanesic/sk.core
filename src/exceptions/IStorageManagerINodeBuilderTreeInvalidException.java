package exceptions;

/**
 * Dešava se kada postoji problem u stablu koje je {@link io.IODriver} predao {@link storage.IStorageManager} komponenti
 * za izgradnju stabla u {@link storage.IStorageManager#initStorage(String)} metodi.
 */
public class IStorageManagerINodeBuilderTreeInvalidException extends Exception {

    public IStorageManagerINodeBuilderTreeInvalidException(String s) {
        super(s);
    }

    public IStorageManagerINodeBuilderTreeInvalidException() {
        super("The given DirectoryBuilder tree is invalid.");
    }
}
