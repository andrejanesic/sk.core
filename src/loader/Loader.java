package loader;

import io.IOManager;
import repository.Directory;
import repository.File;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;
import repository.builder.INodeBuilder;
import repository.builder.INodeBuilderType;

/**
 * Učitava skladište i njegove fajlove.
 */
public class Loader {

    /**
     * Singleton lock za sinhronizaciju.
     */
    private static final Object lock = new Object();

    /**
     * Singleton instanca.
     */
    private static Loader instance;

    /**
     * Korenski direktorijum.
     */
    private Directory root;

    /**
     * Putanja do skladišta u okruženju OS-a.
     */
    private String storagePath;

    /**
     * Podrazumevani konstruktor.
     *
     * @param storagePath Putanja do skladišta u okruženju OS-a.
     */
    private Loader(String storagePath) {
        this.storagePath = storagePath;
    }

    /**
     * Vraća instancu Loader-a ili null ukoliko još nije inicijalizovan.
     *
     * @return Instanca Loader-a.
     */
    public static Loader getInstance() {
        return instance;
    }

    /**
     * Inicijalizuje Loader i vraća instancu.
     *
     * @param storagePath Putanja do skladišta u okruženju OS-a.
     * @return Instanca Loader-a.
     */
    public static Loader getInstance(String storagePath) {
        if (instance != null)
            return instance;

        synchronized (lock) {
            if (instance == null)
                instance = new Loader(storagePath);

            return instance;
        }
    }

    /**
     * Vraća korenski direktorijum skladišta. Može biti null ukoliko još nije izgrađeno.
     *
     * @return Korenski direktorijum skladišta ili null ukoliko još nije izgrađen.
     */
    public Directory getRoot() {
        return this.root;
    }

    /**
     * Gradi korenski Directory na osnovu putanje do skladišta u okruženju.
     *
     * @return Korenski Directory.
     */
    public synchronized Directory buildRoot() {
        if (this.root != null)
            return root;

        DirectoryBuilder rootBuilder = IOManager.getIOHandler().buildStorage(storagePath);
        this.root = traverseDirectoryBuilder(null, rootBuilder);
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
}
