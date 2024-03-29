package com.raf.sk.core.repository.limitations;

import com.raf.sk.core.core.Core;
import com.raf.sk.core.dummynode.DummyNode;
import com.raf.sk.core.exceptions.*;
import com.raf.sk.core.repository.Directory;
import com.raf.sk.core.repository.File;
import com.raf.sk.core.repository.RepositoryPrepareTest;
import com.raf.sk.specification.builders.FileBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class INodeBlacklistExtLimitationTest extends RepositoryPrepareTest {

    @Test
    void testMakeFileInDirectoryWithBlacklistExtLimitationEmpty() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        Directory root = Core.getInstance().StorageManager().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        assertNotNull(root);
        root.addLimitation(new INodeBlacklistExtLimitation(root, ""));
        String ext = UUID.randomUUID().toString() + '.' + UUID.randomUUID().toString();
        File f = new File(false, root, new FileBuilder(null, ext, 0));
        assertDoesNotThrow(() -> root.linkNode(f));
    }

    @Test
    void testMakeFileInDirectoryWithBlacklistExtLimitationCaught() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        Directory root = Core.getInstance().StorageManager().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        assertNotNull(root);
        Directory d = (Directory) root.resolvePath(
                DummyNode.poolDirs.get((int) Math.floor(Math.random() * DummyNode.poolDirs.size())).path());

        int i = (int) Math.floor(Math.random() * 10) + 1;
        File[] files = new File[i];
        for (int j = 0; j < i; j++) {
            StringBuilder sb = new StringBuilder();
            String ext = UUID.randomUUID().toString();
            for (int n = (int) Math.floor(Math.random() * 3) + 1; n-- > 0; ) {
                ext = UUID.randomUUID().toString();
                sb.append('.').append(ext);
            }

            files[j] = new File(false, root, sb.toString());
            d.addLimitation(new INodeBlacklistExtLimitation(d, ext));
        }

        for (File f : files) {
            assertThrows(INodeLimitationException.class,
                    () -> d.linkNode(f));
        }
    }

    @Test
    void testMakeFileInDirectoryWithParentBlacklistExtLimitationCaught() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        Directory root = Core.getInstance().StorageManager().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        assertNotNull(root);
        Directory d = (Directory) root.resolvePath(
                DummyNode.poolDirs.get((int) Math.floor(Math.random() * DummyNode.poolDirs.size())).path());

        int i = (int) Math.floor(Math.random() * 10) + 1;
        File[] files = new File[i];
        for (int j = 0; j < i; j++) {
            StringBuilder sb = new StringBuilder();
            String ext = UUID.randomUUID().toString();
            for (int n = (int) Math.floor(Math.random() * 3) + 1; n-- > 0; ) {
                ext = UUID.randomUUID().toString();
                sb.append('.').append(ext);
            }

            files[j] = new File(false, root, sb.toString());
            root.addLimitation(new INodeBlacklistExtLimitation(root, ext));
        }

        for (File f : files) {
            assertThrows(INodeLimitationException.class,
                    () -> d.linkNode(f));
        }
    }
}
