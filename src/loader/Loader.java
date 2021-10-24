package loader;

import io.IOManager;
import repository.Directory;
import repository.File;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;
import repository.builder.INodeBuilder;
import repository.builder.INodeBuilderType;
import user.User;
import user.builder.UserBuilder;

/**
 * Učitava skladište i njegove fajlove, konfiguraciju i korisnike.
 */
public class Loader {

    /**
     * Korenski direktorijum.
     */
    private Directory root;

    /**
     * Trenutni korisnik.
     */
    private User user;

    /**
     * Podrazumevani konstruktor.
     */
    private Loader() {
    }

    /**
     * Vraća instancu Loader-a.
     *
     * @return Loader instanca.
     */
    public static Loader getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Vraća korenski direktorijum skladišta. Može biti null ukoliko još nije izgrađeno.
     *
     * @return Korenski direktorijum skladišta ili null ukoliko još nije izgrađen.
     */
    public Directory getRoot() {
        return root;
    }

    /**
     * Gradi korenski Directory na osnovu putanje do skladišta u okruženju.
     *
     * @param path Putanja do skladišta u OS okruženju.
     * @return Korenski Directory.
     */
    public synchronized Directory initStorage(String path) {
        if (root != null)
            return root;

        DirectoryBuilder rootBuilder = IOManager.getIOHandler().initStorage(path);
        root = traverseDirectoryBuilder(null, rootBuilder);
        return root;
    }

    /**
     * Prolazi kroz stablo DirectoryBuilder-a i pravi nove Directory-je i File-ove.
     *
     * @param parent       Roditeljski Directory.
     * @param iNodeBuilder Trenutno obrađivan INodeBuilder.
     * @return Korenski direktorijum.
     */
    private Directory traverseDirectoryBuilder(Directory parent, INodeBuilder iNodeBuilder) {
        if (iNodeBuilder.getType().equals(INodeBuilderType.FILE)) {
            new File(parent, (FileBuilder) iNodeBuilder);
            return null;
        }

        Directory dir = new Directory(parent, (DirectoryBuilder) iNodeBuilder);
        for (INodeBuilder nb : ((DirectoryBuilder) iNodeBuilder).getChildren()) {
            traverseDirectoryBuilder(dir, nb);
        }

        if (parent != null) return parent;
        return dir;
    }

    /**
     * Vraća trenutno povezanog korisnika ili null ukoliko niko nije povezan.
     *
     * @return Korisnik ili null.
     */
    public User getUser() {
        return user;
    }

    /**
     * Inicijalizuje korisnika na osnovu datih kredencijala. Trenutno ulogovan korisnik se diskonektuje.
     *
     * @param username Korisničko ime.
     * @param password Lozinka.
     * @return Korisnik ili null.
     */
    public synchronized User initUser(String username, String password) {
        user = null;
        UserBuilder userBuilder = IOManager.getIOHandler().initUser(username, password);
        user = new User(userBuilder);
        return user;
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final Loader INSTANCE = new Loader();
    }
}
