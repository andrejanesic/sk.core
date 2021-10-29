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

public class ActionINodeMoveTest {

    @Test
    void testActionINodeMoveRunDirIntoDir() throws
            INodeRootNotInitializedException,
            IStorageManagerINodeBuilderTreeInvalidException,
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException {
        IOManager.setIODriver(new IODriverTest());
        Core.getInstance().ConfigManager().initConfig(null);
        Core.getInstance().StorageManager().initStorage("test");
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.ALL);
        DummyNode rootDummy, targetDummy, destDummy;
        do {
            DummyNode.pool.clear();
            DummyNode.poolDirs.clear();
            DummyNode.poolFiles.clear();
            rootDummy = DummyNode.generateDummyNodes();

            int indexDir1 = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());
            int indexDir2 = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());

            targetDummy = DummyNode.poolDirs.get(indexDir1);
            destDummy = DummyNode.poolDirs.get(indexDir2);

        } while (destDummy.isGrandchild(targetDummy) || targetDummy == rootDummy);

        Directory root = Core.getInstance().StorageManager().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        DummyNode finalTargetDummy = targetDummy;
        DummyNode finalDestDummy = destDummy;
        Assertions.assertDoesNotThrow(() -> new ActionINodeMove(finalTargetDummy.path(), finalDestDummy.path()).run());

        if (targetDummy != destDummy) {
            if (targetDummy.parent != null)
                targetDummy.parent.children.remove(targetDummy);
            destDummy.children.add(targetDummy);
            targetDummy.parent = destDummy;
        }

        DummyNode finalRootDummy = rootDummy;
        assertDoesNotThrow(() -> finalRootDummy.traverse(dummyNode -> {
            try {
                Core.getInstance().StorageManager().getRoot().resolvePath(dummyNode.path());
            } catch (INodeRootNotInitializedException e) {
                e.printStackTrace();
            }
        }));
    }

    @Test
    void testActionINodeMoveUndoDirIntoDir() throws
            INodeRootNotInitializedException,
            IStorageManagerINodeBuilderTreeInvalidException,
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException {
        IOManager.setIODriver(new IODriverTest());
        Core.getInstance().ConfigManager().initConfig(null);
        Core.getInstance().StorageManager().initStorage("test");
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.ALL);
        DummyNode rootDummy, targetDummy, destDummy;
        do {
            DummyNode.pool.clear();
            DummyNode.poolDirs.clear();
            DummyNode.poolFiles.clear();
            rootDummy = DummyNode.generateDummyNodes();

            int indexDir1 = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());
            int indexDir2 = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());

            targetDummy = DummyNode.poolDirs.get(indexDir1);
            destDummy = DummyNode.poolDirs.get(indexDir2);

        } while (destDummy.isGrandchild(targetDummy) || targetDummy == rootDummy);

        Directory root = Core.getInstance().StorageManager().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        DummyNode finalTargetDummy = targetDummy;
        DummyNode finalDestDummy = destDummy;
        Assertions.assertDoesNotThrow(() -> new ActionINodeMove(finalTargetDummy.path(), finalDestDummy.path()).run());
        Assertions.assertDoesNotThrow(() -> new ActionINodeMove(finalTargetDummy.path(), finalDestDummy.path()).undo());

        DummyNode finalRootDummy = rootDummy;
        assertDoesNotThrow(() -> finalRootDummy.traverse(dummyNode -> {
            try {
                Core.getInstance().StorageManager().getRoot().resolvePath(dummyNode.path());
            } catch (INodeRootNotInitializedException e) {
                e.printStackTrace();
            }
        }));
    }
}