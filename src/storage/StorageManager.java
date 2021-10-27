package storage;

import exceptions.INodeLimitationException;
import exceptions.IStorageManagerINodeBuilderTreeInvalidException;
import io.IOManager;
import org.jetbrains.annotations.Nullable;
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

    /**
     * Putanja do korenskog direktorijuma (onog u kome se nalazi inicijalizacijski/konfiguracioni fajl skladišta) u
     * samom OS-u.
     * <p>
     * Na ovu putanju se dalje "lepe" putanje do čvorova u direktorijumu.
     */
    private String systemRoot;

    private StorageManager() {
    }

    /**
     * Vraća instancu komponente {@link StorageManager}.
     *
     * @return Instanca komponente {@link StorageManager}.
     */
    public static StorageManager getInstance() {
        return StorageManager.Holder.INSTANCE;
    }

    @Override
    public Directory getRoot() {
        return root;
    }

    @Override
    public synchronized Directory initStorage(String path) throws IStorageManagerINodeBuilderTreeInvalidException {
        if (root != null)
            return root;

        DirectoryBuilder rootBuilder = IOManager.getIOAdapter().initStorage(path);
        root = traverseDirectoryBuilder(null, rootBuilder);
        systemRoot = path;
        return root;
    }

    @Override
    public synchronized void deinitStorage() {
        // #TODO ovde treba izvršiti promene koje su u privremenoj memoriji i još nisu napisane, ako postoje
        root = null;
    }

    /**
     * Prolazi kroz stablo {@link DirectoryBuilder}-a i pravi nove {@link Directory} i {@link File}.
     *
     * @param parent       Roditeljski {@link Directory}.
     * @param iNodeBuilder Trenutno obrađivan {@link INodeBuilder}.
     * @return Korenski {@link Directory} za celo skladište..
     */
    private Directory traverseDirectoryBuilder(
            @Nullable Directory parent,
            INodeBuilder iNodeBuilder
    ) throws IStorageManagerINodeBuilderTreeInvalidException {
        if (iNodeBuilder.getType().equals(INodeBuilderType.FILE)) {
            if (parent == null) {
                // roditelj fajla uvek mora biti direktorijum, nikada null
                throw new IStorageManagerINodeBuilderTreeInvalidException(
                        "FileBuilder must have a parent, it can never be null.");
            }

            try {
                parent.linkNode(new File(false, parent, (FileBuilder) iNodeBuilder));
            } catch (INodeLimitationException e) {
                e.printStackTrace();
            }
            return null;
        }

        Directory dir = new Directory(false, parent, (DirectoryBuilder) iNodeBuilder);
        if (parent == null)
            parent = dir;
        else {
            try {
                parent.linkNode(dir);
            } catch (INodeLimitationException e) {
                e.printStackTrace();
            }
        }
        for (INodeBuilder nb : ((DirectoryBuilder) iNodeBuilder).getChildren()) {
            traverseDirectoryBuilder(dir, nb);
        }
        return parent;
    }

    /**
     * Holder za thread-safe singleton instancu.
     */
    private static class Holder {
        private static final StorageManager INSTANCE = new StorageManager();
    }
}
