package repository;

import exceptions.DirectoryInvalidPathException;
import exceptions.DirectoryMakeNodeInvalidNodeType;
import exceptions.DirectoryMakeNodeNameInvalidException;
import exceptions.DirectoryMakeNodeNameNotUniqueException;
import org.junit.jupiter.api.Test;
import repository.dummynode.DummyNode;
import repository.dummynode.DummyNodeType;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyNodeTest extends TestPrepare {

    @Test
    void testDummyNodeTreeToNodeTree() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType,
            DirectoryInvalidPathException {

        DummyNode rootDummy = DummyNode.generateDummyNodes();
        Directory root = Directory.getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        for (DummyNode d : DummyNode.pool) {
            assertDoesNotThrow(() -> root.resolvePath(d.path()));
            assertEquals(
                    d.type.equals(DummyNodeType.FILE) ? INodeType.FILE : INodeType.DIRECTORY,
                    root.resolvePath(d.path()).getType());
        }
    }
}
