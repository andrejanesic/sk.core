package repository;

import dummynode.DummyNode;
import dummynode.DummyNodeType;
import exceptions.*;
import loader.Loader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryTest extends RepositoryTestPrepare {

    @Test
    void testGetRootPath() {
        try {
            assertEquals(Directory.ROOT_DIRECTORY, Loader.getInstance().getRoot().getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMakeDirectory() {
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().makeDirectory("test"));
    }

    @Test
    void testMakeDirectoryInvalidName() {
        assertThrows(DirectoryMakeNodeNameInvalidException.class, () -> Loader.getInstance().getRoot().makeDirectory("abc/"));
    }

    @Test
    void testMakeFileInvalidName() {
        assertThrows(DirectoryMakeNodeNameInvalidException.class, () -> Loader.getInstance().getRoot().makeFile("def/"));
    }

    @Test
    void testMakeDirectoryDuplicateName() {
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().makeFile("foo123"));
        assertThrows(DirectoryMakeNodeNameNotUniqueException.class, () -> Loader.getInstance().getRoot().makeFile("foo123"));
    }

    @Test
    void testMakeFileDuplicateName() {
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().makeFile("bar"));
        assertThrows(DirectoryMakeNodeNameNotUniqueException.class, () -> Loader.getInstance().getRoot().makeFile("bar"));
    }

    @Test
    void testNestedDirectoryMakeNode() {
        assertDoesNotThrow(() -> Loader.getInstance().getRoot()
                .makeDirectory("abcdefghi0")
                .makeDirectory("abcdefghi1")
                .makeDirectory("abcdefghi2")
                .makeFile("abcdefghiFile")
        );
    }

    @Test
    void testNodePaths() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException {
        Directory d0_0 = Loader.getInstance().getRoot().makeDirectory("d0_0");
        Directory d0_1 = Loader.getInstance().getRoot().makeDirectory("d0_1");
        Directory d0_0_0 = d0_0.makeDirectory("d0_0_0");
        Directory d0_0_1 = d0_0.makeDirectory("d0_0_1");
        Directory d0_1_0 = d0_1.makeDirectory("d0_1_0");
        Directory d0_1_0_0 = d0_1_0.makeDirectory("d0_1_0_0");
        File f0_0_2 = d0_0.makeFile("f0_0_2");

        // assert well nested
        assertTrue(f0_0_2.getPath().contains("d0_0/"));
        assertTrue(d0_1_0_0.getPath().contains("d0_1_0/"));
        assertTrue(d0_1_0_0.getPath().contains("d0_1_0/"));

        // assert paths don't end with "/"
        assertFalse(d0_0_0.getPath().endsWith("/"));
        assertFalse(f0_0_2.getPath().endsWith("/"));
    }

    @Test
    void testMoveDirectoryIntoFile() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        Directory root = Loader.getInstance().getRoot();
        while (DummyNode.poolDirs.size() < 1 || DummyNode.poolFiles.size() < 1) {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();
        }
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        int indexDir = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());
        int indexFile = (int) Math.floor(Math.random() * DummyNode.poolFiles.size());
        Directory target = (Directory) root.resolvePath(DummyNode.poolDirs.get(indexDir).path());
        File dest = (File) root.resolvePath(DummyNode.poolFiles.get(indexFile).path());
        assertThrows(INodeUnsupportedOperationException.class, () -> target.move(dest));
    }

    @Test
    void testMoveDirectorIntoDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy, targetDummy, destDummy;
        do {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();

            int indexDir1 = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());
            int indexDir2 = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());

            targetDummy = DummyNode.poolDirs.get(indexDir1);
            destDummy = DummyNode.poolDirs.get(indexDir2);

        } while (destDummy.isGrandchild(targetDummy) || targetDummy == rootDummy);

        Directory root = Loader.getInstance().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        Directory target = (Directory) root.resolvePath(targetDummy.path());
        Directory dest = (Directory) root.resolvePath(destDummy.path());

        assertDoesNotThrow(() -> target.move(dest));

        if (targetDummy != destDummy) {
            if (targetDummy.parent != null)
                targetDummy.parent.children.remove(targetDummy);
            destDummy.children.add(targetDummy);
            targetDummy.parent = destDummy;
        }

        DummyNode finalRootDummy = rootDummy;
        assertDoesNotThrow(() -> finalRootDummy.traverse(dummyNode -> {
            try {
                Loader.getInstance()
                        .getRoot().resolvePath(dummyNode.path());
            } catch (INodeRootNotInitializedException e) {
                e.printStackTrace();
            }
        }));
    }

    @Test
    void testMoveRootDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException, INodeRootNotInitializedException {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        rootDummy.children.removeIf((d) -> d.type == DummyNodeType.FILE);
        if (rootDummy.children.size() == 0) return;

        Directory root = Loader.getInstance().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        DummyNode destDummy = rootDummy.children.get((int) Math.floor(Math.random() * rootDummy.children.size()));
        Directory target = (Directory) root.resolvePath(rootDummy.path());
        Directory dest = (Directory) root.resolvePath(destDummy.path());

        assertThrows(INodeUnsupportedOperationException.class, () -> target.move(dest));

        destDummy.children.add(rootDummy);
        destDummy.parent = null;
        rootDummy.parent = destDummy;
        rootDummy.children.remove(destDummy);

        assertThrows(DirectoryInvalidPathException.class, () -> destDummy.traverse(
                dummyNode -> Loader.getInstance().getRoot().resolvePath(dummyNode.path()))
        );
    }

    @Test
    void testMoveParentDirectorIntoDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy, targetDummy, destDummy;
        do {
            clearDummies();
            rootDummy = DummyNode.generateDummyNodes();

            int indexDir1 = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());
            int indexDir2 = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());

            targetDummy = DummyNode.poolDirs.get(indexDir1);
            destDummy = DummyNode.poolDirs.get(indexDir2);

        } while (!destDummy.isGrandchild(targetDummy) || targetDummy == rootDummy);

        assertTrue(destDummy.isGrandchild(targetDummy));

        Directory root = Loader.getInstance().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        Directory target = (Directory) root.resolvePath(targetDummy.path());
        Directory dest = (Directory) root.resolvePath(destDummy.path());

        assertThrows(INodeUnsupportedOperationException.class, () -> target.move(dest));
    }

    @Test
    void testDeleteDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            INodeRootNotInitializedException {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        Directory root = Loader.getInstance().getRoot();
        DummyNode.dummyNodeTreeToNodeTree(root, rootDummy);

        int indexDir = (int) Math.floor(Math.random() * DummyNode.poolDirs.size());
        DummyNode targetDummy = DummyNode.poolDirs.get(indexDir);
        if (targetDummy == rootDummy) {
            assertThrows(INodeUnsupportedOperationException.class, () -> root.delete(targetDummy.path()));
        } else {
            assertDoesNotThrow(() -> root.delete(targetDummy.path()));
            targetDummy.traverse((dummyNode) -> assertThrows(DirectoryInvalidPathException.class,
                    () -> root.resolvePath(dummyNode.path()))
            );
        }
    }

    @Test
    void testPathResolution() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeTypeException,
            DirectoryInvalidPathException,
            INodeRootNotInitializedException {
        String d0Name = "2021-10-23-20-46-0";
        String d1Name = "2021-10-23-20-46-1";
        String d2Name = "2021-10-23-20-46-2";
        String d3Name = "2021-10-23-20-46-3";
        String f1Name = "2021-10-23-20-46-4";
        String i1Name = "2021-10-23-21-01-0";
        String i2Name = "2021-10-23-21-05-0";
        String i3Name = "2021-10-23-21-06-0";
        String i4Name = "2021-10-23-21-07-0";
        Directory d0 = Loader.getInstance().getRoot().makeDirectory(d0Name);
        Directory d1 = Loader.getInstance().getRoot().makeDirectory(d1Name);
        Directory d2 = d1.makeDirectory(d2Name);
        Directory d3 = d2.makeDirectory(d3Name);
        File f1 = d2.makeFile(f1Name);

        // assert no errors
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath(d0Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath(d1Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath(d1Name + "/" + d2Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath(d1Name + "/" + d2Name + "/" + d3Name));

        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath("/" + d0Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath("/" + d1Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath("/" + d1Name + "/" + d2Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath("/" + d1Name + "/" + d2Name + "/" + d3Name));

        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath("./" + d0Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath("./" + d1Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath("./" + d1Name + "/" + d2Name));
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().resolvePath("./" + d1Name + "/" + d2Name + "/" + d3Name));

        assertDoesNotThrow(() -> d1.resolvePath(d2Name));
        assertDoesNotThrow(() -> d2.resolvePath(d3Name));
        assertDoesNotThrow(() -> d1.resolvePath(d2Name + "/" + d3Name));

        assertDoesNotThrow(() -> d1.resolvePath("/" + d0Name));
        assertDoesNotThrow(() -> d2.resolvePath("/" + d1Name));
        assertDoesNotThrow(() -> d3.resolvePath("/" + d1Name + "/" + d2Name));
        assertDoesNotThrow(() -> d2.resolvePath("/" + d1Name + "/" + d2Name + "/" + d3Name));

        assertDoesNotThrow(() -> d1.resolvePath("./" + d2Name));
        assertDoesNotThrow(() -> d2.resolvePath("./" + d3Name));
        assertDoesNotThrow(() -> d1.resolvePath("./" + d2Name + "/" + d3Name));

        // assert errors
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(i1Name));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(d1Name + "/" + i2Name));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(d1Name + "/" + i3Name + "/" + d3Name));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(d1Name + "/" + d2Name + "/" + f1Name + "/" + i4Name));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(i1Name + "/"));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(i1Name + "//"));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(" " + i1Name + " "));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(d1Name + "//" + d2Name));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(d1Name + "/ /" + d2Name));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(d1Name + "/" + d2Name + "///"));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(d1Name + "//" + d2Name + "///"));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath(d1Name + "////" + d2Name));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath("/./" + i1Name));
        assertThrows(DirectoryInvalidPathException.class, () -> Loader.getInstance().getRoot().resolvePath("./" + i1Name));

        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath(i1Name));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath(d2Name + "/" + i2Name));
        assertThrows(DirectoryInvalidPathException.class, () -> d3.resolvePath(d1Name + "/" + i3Name + "/" + d3Name));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath(d1Name + "/" + d2Name + "/" + f1Name + "/" + i4Name));
        assertThrows(DirectoryInvalidPathException.class, () -> d2.resolvePath(i1Name + "/"));
        assertThrows(DirectoryInvalidPathException.class, () -> d3.resolvePath(i1Name + "//"));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath(" " + i1Name + " "));
        assertThrows(DirectoryInvalidPathException.class, () -> d2.resolvePath(d1Name + "//" + d2Name));
        assertThrows(DirectoryInvalidPathException.class, () -> d3.resolvePath(d1Name + "/ /" + d2Name));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath(d1Name + "/" + d2Name + "///"));
        assertThrows(DirectoryInvalidPathException.class, () -> d2.resolvePath(d1Name + "//" + d2Name + "///"));
        assertThrows(DirectoryInvalidPathException.class, () -> d3.resolvePath(d1Name + "////" + d2Name));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath("/./" + i1Name));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath("./" + i2Name));

        // assert well resolved
        assertEquals(d0, Loader.getInstance().getRoot().resolvePath(d0Name));
        assertEquals(d1, Loader.getInstance().getRoot().resolvePath(d1Name));
        assertEquals(d2, Loader.getInstance().getRoot().resolvePath(d1Name + "/" + d2Name));
        assertEquals(d3, Loader.getInstance().getRoot().resolvePath(d1Name + "/" + d2Name + "/" + d3Name));
        assertEquals(f1, Loader.getInstance().getRoot().resolvePath(d1Name + "/" + d2Name + "/" + f1Name));
        assertEquals(f1, Loader.getInstance().getRoot().resolvePath(d1Name + "/" + d2Name + "/" + f1Name + "/"));

        assertEquals(d0, Loader.getInstance().getRoot().resolvePath("/" + d0Name));
        assertEquals(d1, Loader.getInstance().getRoot().resolvePath("/" + d1Name));
        assertEquals(d2, Loader.getInstance().getRoot().resolvePath("/" + d1Name + "/" + d2Name));
        assertEquals(d3, Loader.getInstance().getRoot().resolvePath("/" + d1Name + "/" + d2Name + "/" + d3Name));
        assertEquals(f1, Loader.getInstance().getRoot().resolvePath("/" + d1Name + "/" + d2Name + "/" + f1Name));
        assertEquals(f1, Loader.getInstance().getRoot().resolvePath("/" + d1Name + "/" + d2Name + "/" + f1Name + "/"));

        assertEquals(d0, Loader.getInstance().getRoot().resolvePath("./" + d0Name));
        assertEquals(d1, Loader.getInstance().getRoot().resolvePath("./" + d1Name));
        assertEquals(d2, Loader.getInstance().getRoot().resolvePath("./" + d1Name + "/" + d2Name));
        assertEquals(d3, Loader.getInstance().getRoot().resolvePath("./" + d1Name + "/" + d2Name + "/" + d3Name));
        assertEquals(f1, Loader.getInstance().getRoot().resolvePath("./" + d1Name + "/" + d2Name + "/" + f1Name));
        assertEquals(f1, Loader.getInstance().getRoot().resolvePath("./" + d1Name + "/" + d2Name + "/" + f1Name + "/"));

        assertEquals(d2, d1.resolvePath(d2Name));
        assertEquals(d3, d1.resolvePath(d2Name + "/" + d3Name));
        assertEquals(d3, d1.resolvePath(d2Name + "/" + d3Name + "/"));
        assertEquals(d3, d2.resolvePath(d3Name));
        assertEquals(d3, d2.resolvePath(d3Name + "/"));
        assertEquals(f1, d2.resolvePath(f1Name));
        assertEquals(f1, d2.resolvePath(f1Name + "/"));

        assertEquals(d0, d1.resolvePath("/" + d0Name));
        assertEquals(d1, d1.resolvePath("/" + d1Name));
        assertEquals(d2, d1.resolvePath("/" + d1Name + "/" + d2Name));
        assertEquals(d3, d1.resolvePath("/" + d1Name + "/" + d2Name + "/" + d3Name));

        assertEquals(d2, d1.resolvePath("./" + d2Name));
        assertEquals(d3, d1.resolvePath("./" + d2Name + "/" + d3Name));
        assertEquals(d3, d1.resolvePath("./" + d2Name + "/" + d3Name + "/"));
        assertEquals(d3, d2.resolvePath("./" + d3Name));
        assertEquals(d3, d2.resolvePath("./" + d3Name + "/"));
        assertEquals(f1, d2.resolvePath("./" + f1Name));
        assertEquals(f1, d2.resolvePath("./" + f1Name + "/"));
    }
}