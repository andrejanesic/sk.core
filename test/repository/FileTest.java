package repository;

import io.IOHandlerTest;
import exceptions.*;
import io.IOManager;
import loader.Loader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import dummynode.DummyNode;

import static org.junit.jupiter.api.Assertions.*;

class FileTest extends RepositoryTestPrepare {

    @BeforeAll
    public static void setHandler() {
        IOManager.setIOHandler(new IOHandlerTest());
    }

    @Test
    void testMoveFileIntoFile() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        DummyNode rootDummy, targetDummy = null, destDummy = null;
        do {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();
            if (DummyNode.poolFiles.size() == 0) continue;
            targetDummy = DummyNode.poolFiles.get((int) Math.floor(Math.random() * DummyNode.poolFiles.size()));
            destDummy = DummyNode.poolFiles.get((int) Math.floor(Math.random() * DummyNode.poolFiles.size()));
        } while (targetDummy == destDummy);

        Directory root = Loader.getInstance().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);
        File target = (File) root.resolvePath(targetDummy.path());
        File dest = (File) root.resolvePath(destDummy.path());
        assertThrows(INodeUnsupportedOperationException.class, () -> target.move(dest));
    }

    @Test
    void testMoveFileIntoDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        DummyNode rootDummy, targetDummy = null, destDummy = null;
        do {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();
            if (DummyNode.poolFiles.size() < 1 || DummyNode.poolDirs.size() < 1) continue;
            targetDummy = DummyNode.poolFiles.get((int) Math.floor(Math.random() * DummyNode.poolFiles.size()));
            destDummy = DummyNode.poolDirs.get((int) Math.floor(Math.random() * DummyNode.poolDirs.size()));
        } while (targetDummy == null || destDummy == null);
        Directory root = Loader.getInstance().getRoot();
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
            DirectoryMakeNodeInvalidNodeType {
        DummyNode rootDummy, targetDummy = null;
        do {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();
            if (DummyNode.poolFiles.size() == 0) continue;
            int index = (int) Math.floor(Math.random() * DummyNode.poolFiles.size());
            targetDummy = DummyNode.poolFiles.get(index);
        } while (targetDummy == null);

        Directory root = Loader.getInstance().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);
        DummyNode finalTargetDummy = targetDummy;
        assertDoesNotThrow(() -> root.delete(finalTargetDummy.path()));
        targetDummy.traverse((dummyNode) -> {
            assertThrows(DirectoryInvalidPathException.class, () -> root.resolvePath(dummyNode.path()));
        });
    }
}
