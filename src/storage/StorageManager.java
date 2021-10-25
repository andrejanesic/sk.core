package storage;

import io.IOManager;
import repository.Directory;
import repository.File;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;
import repository.builder.INodeBuilder;
import repository.builder.INodeBuilderType;

/**
 * Implementacija komponente za menadžment skladišta.
 */
public class StorageManager implements IStorageManager {

    /**
     * Korenski direktorijum.
     */
    private Directory root;

    private StorageManager() {
    }

    /**
     * Vraća instancu komponente.
     *
     * @return Instanca komponente.
     */
    public static StorageManager getInstance() {
        return StorageManager.Holder.INSTANCE;
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

        DirectoryBuilder rootBuilder = IOManager.getIOAdapter().initStorage(path);
        root = traverseDirectoryBuilder(null, rootBuilder);
        return root;
    }

    /**
     * Deinicijalizuje skladište.
     */
    public synchronized void deinitStorage() {
        // #TODO ovde treba izvršiti promene koje su u privremenoj memoriji i još nisu napisane, ako postoje
        root = null;
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
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final StorageManager INSTANCE = new StorageManager();
    }
}
