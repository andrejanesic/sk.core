package com.raf.sk.specification.storage;

import com.raf.sk.specification.config.IConfig;
import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.exceptions.IComponentNotInitializedException;
import com.raf.sk.specification.exceptions.INodeLimitationException;
import com.raf.sk.specification.exceptions.INodeRootNotInitializedException;
import com.raf.sk.specification.exceptions.IStorageManagerINodeBuilderTreeInvalidException;
import com.raf.sk.specification.io.IOManager;
import com.raf.sk.specification.repository.Directory;
import com.raf.sk.specification.repository.File;
import com.raf.sk.specification.repository.builder.DirectoryBuilder;
import com.raf.sk.specification.repository.builder.FileBuilder;
import com.raf.sk.specification.repository.builder.INodeBuilder;
import com.raf.sk.specification.repository.builder.INodeBuilderType;
import com.raf.sk.specification.repository.limitations.INodeLimitation;
import org.jetbrains.annotations.Nullable;

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
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (root != null)
            return root;

        DirectoryBuilder rootBuilder = IOManager.getIODriver().initStorage(path);
        root = traverseDirectoryBuilder(null, rootBuilder);
        systemRoot = path;

        if (Core.getInstance().UserManager().getUser() != null)
            //noinspection ConstantConditions
            Core.getInstance().UserManager().getUser().setCwd(root);

        //noinspection ConstantConditions
        for (INodeLimitation e : Core.getInstance().ConfigManager().getConfig().getLimitations()) {
            try {
                e.setHost(root.resolvePath(e.getPath()));
            } catch (INodeRootNotInitializedException ignored) {
                // neće se desiti jer smo gore inicijalizovali skladište
            }
        }

        if (Core.getInstance().UserManager().getUser() != null)
            //noinspection ConstantConditions
            Core.getInstance().UserManager().getUser().setCwd(root);
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
