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
        assertDoesNotThrow(() -> new ActionINodeMove(finalTargetDummy.path(), finalDestDummy.path()).run());

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
        assertDoesNotThrow(() -> new ActionINodeMove(finalTargetDummy.path(), finalDestDummy.path()).run());
        assertDoesNotThrow(() -> new ActionINodeMove(finalTargetDummy.path(), finalDestDummy.path()).undo());

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
