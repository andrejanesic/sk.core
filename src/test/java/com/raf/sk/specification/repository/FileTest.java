package com.raf.sk.specification.repository;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.dummynode.DummyNode;
import com.raf.sk.specification.exceptions.*;
import com.raf.sk.specification.io.IODriverTest;
import com.raf.sk.specification.io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileTest extends RepositoryPrepareTest {

    @BeforeAll
    public static void setHandler() {
        IOManager.setIODriver(new IODriverTest());
    }

    @Test
    void testMoveFileIntoFile() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy, targetDummy = null, destDummy = null;
        do {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();
            if (DummyNode.poolFiles.size() == 0) continue;
            targetDummy = DummyNode.poolFiles.get((int) Math.floor(Math.random() * DummyNode.poolFiles.size()));
            destDummy = DummyNode.poolFiles.get((int) Math.floor(Math.random() * DummyNode.poolFiles.size()));
        } while (targetDummy == destDummy);

        Directory root = Core.getInstance().StorageManager().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);
        File target = (File) root.resolvePath(targetDummy.path());
        File dest = (File) root.resolvePath(destDummy.path());
        assertThrows(INodeUnsupportedOperationException.class, () -> target.move(dest));
    }

    @Test
    void testMoveFileIntoDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy, targetDummy = null, destDummy = null;
        do {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();
            if (DummyNode.poolFiles.size() < 1 || DummyNode.poolDirs.size() < 1) continue;
            targetDummy = DummyNode.poolFiles.get((int) Math.floor(Math.random() * DummyNode.poolFiles.size()));
            destDummy = DummyNode.poolDirs.get((int) Math.floor(Math.random() * DummyNode.poolDirs.size()));
        } while (targetDummy == null || destDummy == null);
        Directory root = Core.getInstance().StorageManager().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        File target = (File) root.resolvePath(targetDummy.path());
        Directory dest = (Directory) root.resolvePath(destDummy.path());

        destDummy.children.add(targetDummy);
        targetDummy.parent.children.remove(targetDummy);
        targetDummy.parent = destDummy;

        assertDoesNotThrow(() -> target.move(dest));
        assertEquals(targetDummy.path(), target.getPath());
    }

    @Test
    void testDeleteFile() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy, targetDummy = null;
        do {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();
            if (DummyNode.poolFiles.size() == 0) continue;
            int index = (int) Math.floor(Math.random() * DummyNode.poolFiles.size());
            targetDummy = DummyNode.poolFiles.get(index);
        } while (targetDummy == null);

        Directory root = Core.getInstance().StorageManager().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);
        DummyNode finalTargetDummy = targetDummy;
        assertDoesNotThrow(() -> root.delete(finalTargetDummy.path()));
        targetDummy.traverse((dummyNode) -> assertThrows(DirectoryInvalidPathException.class,
                () -> root.resolvePath(dummyNode.path())));
    }

    @Test
    void testGetExtension() {
        int i = (int) Math.floor(Math.random() * 10) + 1;
        for (int j = 0; j < i; j++) {
            StringBuilder sb = new StringBuilder();
            String ext = UUID.randomUUID().toString();
            for (int n = (int) Math.floor(Math.random() * 3) + 1; n-- > 0; ) {
                ext = UUID.randomUUID().toString();
                sb.append('.').append(ext);
            }

            assertEquals(
                    new File(false,
                            new Directory(false, null, Directory.ROOT_DIRECTORY),
                            sb.toString()).getExtension(),
                    ext);
        }
    }
}
