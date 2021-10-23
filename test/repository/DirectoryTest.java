package repository;

import exceptions.*;
import implementation.IOHandler;
import io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryTest {

    @BeforeAll
    public static void setHandler() {
        IOManager.setInstance(new IOHandler());
    }

    @Test
    void testMakeRoot() {
        Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().getPath());
    }

    @Test
    void testGetRootPath() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        try {
            assertEquals(Directory.ROOT_DIRECTORY, Directory.getRoot().getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMakeRootAgain() {
        Directory root1 = Directory.makeRoot();
        Directory root2 = Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().getPath());
        assertEquals(root2, Directory.getRoot());
        assertNotEquals(root1, root2);
    }

    @Test
    void testMakeDirectory() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().makeDirectory("test"));
    }

    @Test
    void testMakeDirectoryInvalidName() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        assertThrows(DirectoryMakeNodeNameInvalidException.class, () -> Directory.getRoot().makeDirectory("abc/"));
    }

    @Test
    void testMakeFileInvalidName() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        assertThrows(DirectoryMakeNodeNameInvalidException.class, () -> Directory.getRoot().makeFile("def/"));
    }

    @Test
    void testMakeDirectoryDuplicateName() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().makeFile("foo123"));
        assertThrows(DirectoryMakeNodeNameNotUniqueException.class, () -> Directory.getRoot().makeFile("foo123"));
    }

    @Test
    void testMakeFileDuplicateName() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().makeFile("bar"));
        assertThrows(DirectoryMakeNodeNameNotUniqueException.class, () -> Directory.getRoot().makeFile("bar"));
    }

    @Test
    void testNestedDirectoryMakeNode() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot()
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
            DirectoryMakeNodeInvalidNodeType {
        if (Directory.getRoot() == null) Directory.makeRoot();
        Directory d0_0 = Directory.getRoot().makeDirectory("d0_0");
        Directory d0_1 = Directory.getRoot().makeDirectory("d0_1");
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
    void testMoveFileIntoFile() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        if (Directory.getRoot() == null) Directory.makeRoot();
        File f0 = Directory.getRoot().makeFile("2021-10-23-19-53-0");
        File f1 = Directory.getRoot().makeFile("2021-10-23-19-53-1");
        assertThrows(INodeUnsupportedOperationException.class, () -> f0.move(f1));
    }

    @Test
    void testMoveDirectoryIntoFile() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        if (Directory.getRoot() == null) Directory.makeRoot();
        File f = Directory.getRoot().makeFile("2021-10-23-19-54-0");
        Directory d = Directory.getRoot().makeDirectory("2021-10-23-19-54-1");
        assertThrows(INodeUnsupportedOperationException.class, () -> d.move(f));
    }

    @Test
    void testMoveFileIntoDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        if (Directory.getRoot() == null) Directory.makeRoot();
        File f = Directory.getRoot().makeFile("2021-10-23-19-57-0");
        Directory d = Directory.getRoot().makeDirectory("2021-10-23-19-57-1");
        assertDoesNotThrow(() -> f.move(d));
    }

    @Test
    void testMoveDirectorIntoDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        if (Directory.getRoot() == null) Directory.makeRoot();
        Directory d0 = Directory.getRoot().makeDirectory("2021-10-23-20-00-0");
        Directory d1 = Directory.getRoot().makeDirectory("2021-10-23-20-00-1");
        assertDoesNotThrow(() -> d0.move(d1));
    }

    @Test
    void testMoveDirectoryWithContentsIntoDirectory() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType {
        if (Directory.getRoot() == null) Directory.makeRoot();
        Directory d0 = Directory.getRoot().makeDirectory("2021-10-23-19-58-0");
        Directory d1 = Directory.getRoot().makeDirectory("2021-10-23-19-58-1");
        Directory d2 = d1.makeDirectory("2021-10-23-19-58-2");
        Directory d3 = d2.makeDirectory("2021-10-23-19-58-3");
        File f1 = d2.makeFile("2021-10-23-19-59-0");

        assertDoesNotThrow(() -> d1.move(d0));
        assertTrue(d2.getPath().contains("2021-10-23-19-58-0"));
        assertTrue(f1.getPath().contains("2021-10-23-19-58-0"));
        assertTrue(d3.getPath().contains("2021-10-23-19-58-0"));
    }

    @Test
    void testDirectoryPathResolution() throws
            DirectoryMakeNodeNameInvalidException,
            DirectoryMakeNodeNameNotUniqueException,
            DirectoryMakeNodeInvalidNodeType,
            DirectoryInvalidPathException {
        if (Directory.getRoot() == null) Directory.makeRoot();
        Directory d0 = Directory.getRoot().makeDirectory("2021-10-23-20-46-0");
        Directory d1 = Directory.getRoot().makeDirectory("2021-10-23-20-46-1");
        Directory d2 = d1.makeDirectory("2021-10-23-20-46-2");
        Directory d3 = d2.makeDirectory("2021-10-23-20-46-3");
        File f1 = d2.makeFile("2021-10-23-20-46-4");

        // assert no errors
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("2021-10-23-20-46-0"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("2021-10-23-20-46-1"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-3"));

        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("/2021-10-23-20-46-0"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("/2021-10-23-20-46-1"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-3"));

        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("./2021-10-23-20-46-0"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("./2021-10-23-20-46-1"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("./2021-10-23-20-46-1/2021-10-23-20-46-2"));
        assertDoesNotThrow(() -> Directory.getRoot().resolvePath("./2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-3"));

        assertDoesNotThrow(() -> d1.resolvePath("2021-10-23-20-46-2"));
        assertDoesNotThrow(() -> d2.resolvePath("2021-10-23-20-46-3"));
        assertDoesNotThrow(() -> d1.resolvePath("2021-10-23-20-46-2/2021-10-23-20-46-3"));

        assertDoesNotThrow(() -> d1.resolvePath("/2021-10-23-20-46-0"));
        assertDoesNotThrow(() -> d2.resolvePath("/2021-10-23-20-46-1"));
        assertDoesNotThrow(() -> d3.resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2"));
        assertDoesNotThrow(() -> d2.resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-3"));

        assertDoesNotThrow(() -> d1.resolvePath("./2021-10-23-20-46-2"));
        assertDoesNotThrow(() -> d2.resolvePath("./2021-10-23-20-46-3"));
        assertDoesNotThrow(() -> d1.resolvePath("./2021-10-23-20-46-2/2021-10-23-20-46-3"));

        // assert errors
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-21-01-0"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-21-05-0"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-21-06-0/2021-10-23-20-46-3"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-4/2021-10-23-21-07-0"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-21-01-0/"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-21-01-0//"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath(" 2021-10-23-21-01-0 "));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-20-46-1//2021-10-23-20-46-2"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-20-46-1/ /2021-10-23-20-46-2"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2///"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-20-46-1//2021-10-23-20-46-2///"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("2021-10-23-20-46-1////2021-10-23-20-46-2"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("/./2021-10-23-21-01-0"));
        assertThrows(DirectoryInvalidPathException.class, () -> Directory.getRoot().resolvePath("./2021-10-23-21-01-0"));

        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath("2021-10-23-21-01-0"));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath("2021-10-23-20-46-2/2021-10-23-21-05-0"));
        assertThrows(DirectoryInvalidPathException.class, () -> d3.resolvePath("2021-10-23-20-46-1/2021-10-23-21-06-0/2021-10-23-20-46-3"));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-4/2021-10-23-21-07-0"));
        assertThrows(DirectoryInvalidPathException.class, () -> d2.resolvePath("2021-10-23-21-01-0/"));
        assertThrows(DirectoryInvalidPathException.class, () -> d3.resolvePath("2021-10-23-21-01-0//"));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath(" 2021-10-23-21-01-0 "));
        assertThrows(DirectoryInvalidPathException.class, () -> d2.resolvePath("2021-10-23-20-46-1//2021-10-23-20-46-2"));
        assertThrows(DirectoryInvalidPathException.class, () -> d3.resolvePath("2021-10-23-20-46-1/ /2021-10-23-20-46-2"));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2///"));
        assertThrows(DirectoryInvalidPathException.class, () -> d2.resolvePath("2021-10-23-20-46-1//2021-10-23-20-46-2///"));
        assertThrows(DirectoryInvalidPathException.class, () -> d3.resolvePath("2021-10-23-20-46-1////2021-10-23-20-46-2"));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath("/./2021-10-23-21-01-0"));
        assertThrows(DirectoryInvalidPathException.class, () -> d1.resolvePath("./2021-10-23-21-05-0"));

        // assert well resolved
        assertEquals(d0, Directory.getRoot().resolvePath("2021-10-23-20-46-0"));
        assertEquals(d1, Directory.getRoot().resolvePath("2021-10-23-20-46-1"));
        assertEquals(d2, Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2"));
        assertEquals(d3, Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-3"));
        assertEquals(f1, Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-4"));
        assertEquals(f1, Directory.getRoot().resolvePath("2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-4/"));

        assertEquals(d0, Directory.getRoot().resolvePath("/2021-10-23-20-46-0"));
        assertEquals(d1, Directory.getRoot().resolvePath("/2021-10-23-20-46-1"));
        assertEquals(d2, Directory.getRoot().resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2"));
        assertEquals(d3, Directory.getRoot().resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-3"));
        assertEquals(f1, Directory.getRoot().resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-4"));
        assertEquals(f1, Directory.getRoot().resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-4/"));

        assertEquals(d0, Directory.getRoot().resolvePath("./2021-10-23-20-46-0"));
        assertEquals(d1, Directory.getRoot().resolvePath("./2021-10-23-20-46-1"));
        assertEquals(d2, Directory.getRoot().resolvePath("./2021-10-23-20-46-1/2021-10-23-20-46-2"));
        assertEquals(d3, Directory.getRoot().resolvePath("./2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-3"));
        assertEquals(f1, Directory.getRoot().resolvePath("./2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-4"));
        assertEquals(f1, Directory.getRoot().resolvePath("./2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-4/"));

        assertEquals(d2, d1.resolvePath("2021-10-23-20-46-2"));
        assertEquals(d3, d1.resolvePath("2021-10-23-20-46-2/2021-10-23-20-46-3"));
        assertEquals(d3, d1.resolvePath("2021-10-23-20-46-2/2021-10-23-20-46-3/"));
        assertEquals(d3, d2.resolvePath("2021-10-23-20-46-3"));
        assertEquals(d3, d2.resolvePath("2021-10-23-20-46-3/"));
        assertEquals(f1, d2.resolvePath("2021-10-23-20-46-4"));
        assertEquals(f1, d2.resolvePath("2021-10-23-20-46-4/"));

        assertEquals(d0, d1.resolvePath("/2021-10-23-20-46-0"));
        assertEquals(d1, d1.resolvePath("/2021-10-23-20-46-1"));
        assertEquals(d2, d1.resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2"));
        assertEquals(d3, d1.resolvePath("/2021-10-23-20-46-1/2021-10-23-20-46-2/2021-10-23-20-46-3"));

        assertEquals(d2, d1.resolvePath("./2021-10-23-20-46-2"));
        assertEquals(d3, d1.resolvePath("./2021-10-23-20-46-2/2021-10-23-20-46-3"));
        assertEquals(d3, d1.resolvePath("./2021-10-23-20-46-2/2021-10-23-20-46-3/"));
        assertEquals(d3, d2.resolvePath("./2021-10-23-20-46-3"));
        assertEquals(d3, d2.resolvePath("./2021-10-23-20-46-3/"));
        assertEquals(f1, d2.resolvePath("./2021-10-23-20-46-4"));
        assertEquals(f1, d2.resolvePath("./2021-10-23-20-46-4/"));
    }
}