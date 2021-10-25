package repository;

import dummynode.DummyNode;
import dummynode.DummyNodeType;
import exceptions.*;
import org.junit.jupiter.api.Test;
import storage.StorageManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyNodeTest extends RepositoryTestPrepare {

    @Test
    void testDummyNodeTreeToNodeTree() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            DirectoryInvalidPathException,
            INodeRootNotInitializedException {

        DummyNode rootDummy = DummyNode.generateDummyNodes();
        Directory root = StorageManager.getInstance().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        for (DummyNode d : DummyNode.pool) {
            assertDoesNotThrow(() -> root.resolvePath(d.path()));
            assertEquals(
                    d.type.equals(DummyNodeType.FILE) ? INodeType.FILE : INodeType.DIRECTORY,
                    root.resolvePath(d.path()).getType());
        }
    }
}
