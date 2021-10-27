package actions;

import core.Core;
import dummynode.DummyNode;
import exceptions.*;
import io.IODriverTest;
import io.IOManager;
import org.junit.jupiter.api.Test;
import repository.Directory;
import user.PrivilegeType;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ActionINodeDeleteTest {

    @Test
    void testActionINodeDeleteRun() throws
            IStorageManagerINodeBuilderTreeInvalidException,
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        IOManager.setIODriver(new IODriverTest());
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        Core.getInstance().ConfigManager().initConfig(null);
        Directory root = Core.getInstance().StorageManager().initStorage("test");
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.ALL);
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        int indexDir = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());
        DummyNode targetDummy = DummyNode.poolDirs.get(indexDir);
        if (targetDummy == rootDummy) {
            assertThrows(INodeUnsupportedOperationException.class,
                    () -> new ActionINodeDelete(targetDummy.path()).run());
        } else {
            assertDoesNotThrow(() -> new ActionINodeDelete(targetDummy.path()).run());
            targetDummy.traverse((dummyNode) -> assertThrows(DirectoryInvalidPathException.class,
                    () -> root.resolvePath(dummyNode.path()))
            );
        }
    }
}
