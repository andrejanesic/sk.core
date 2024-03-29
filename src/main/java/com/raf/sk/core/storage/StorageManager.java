package com.raf.sk.core.storage;

import com.raf.sk.core.config.IConfig;
import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.IComponentNotInitializedException;
import com.raf.sk.core.exceptions.INodeLimitationException;
import com.raf.sk.core.exceptions.INodeRootNotInitializedException;
import com.raf.sk.core.exceptions.IStorageManagerINodeBuilderTreeInvalidException;
import com.raf.sk.core.repository.Directory;
import com.raf.sk.core.repository.File;
import com.raf.sk.core.repository.limitations.INodeLimitation;
import com.raf.sk.specification.builders.DirectoryBuilder;
import com.raf.sk.specification.builders.FileBuilder;
import com.raf.sk.specification.builders.INodeBuilder;
import com.raf.sk.specification.builders.INodeBuilderType;
import com.raf.sk.specification.io.IOManager;
import org.jetbrains.annotations.Nullable;

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
    public synchronized Directory initStorage() throws IStorageManagerINodeBuilderTreeInvalidException {
        if (Core.getInstance().ConfigManager().getConfig() == null)
            throw new IComponentNotInitializedException(IConfig.class);

        if (root != null)
            return root;

        DirectoryBuilder rootBuilder = IOManager.getIODriver().initStorage();
        root = traverseDirectoryBuilder(null, rootBuilder);

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
     * @throws IStorageManagerINodeBuilderTreeInvalidException Ukoliko je došlo do greške prilikom parsiranja ulaza iz
     *                                                         datog {@link com.raf.sk.specification.io.IODriver}.
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
