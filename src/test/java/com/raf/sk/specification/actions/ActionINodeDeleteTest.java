package com.raf.sk.specification.actions;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.dummynode.DummyNode;
import com.raf.sk.specification.exceptions.*;
import com.raf.sk.specification.io.IODriverTest;
import com.raf.sk.specification.io.IOManager;
import com.raf.sk.specification.repository.Directory;
import com.raf.sk.specification.user.PrivilegeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.ALL);
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        rootDummy.traverse((dummyNode) -> assertDoesNotThrow(() -> root.resolvePath(dummyNode.path())));

        int indexDir = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());
        DummyNode targetDummy = DummyNode.poolDirs.get(indexDir);
        if (targetDummy == rootDummy) {
            assertThrows(INodeUnsupportedOperationException.class,
                    () -> new ActionINodeDelete(targetDummy.path()).run());
        } else {
            Assertions.assertDoesNotThrow(() -> new ActionINodeDelete(targetDummy.path()).run());
            targetDummy.traverse((dummyNode) -> assertThrows(DirectoryInvalidPathException.class,
                    () -> root.resolvePath(dummyNode.path()))
            );
        }
    }
}
