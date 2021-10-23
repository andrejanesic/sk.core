package repository;

import exceptions.DirectoryMakeNodeInvalidNodeType;
import exceptions.DirectoryMakeNodeNameInvalidException;
import exceptions.DirectoryMakeNodeNameNotUniqueException;
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

        assertTrue(f0_0_2.getPath().contains("d0_0/"));
        assertTrue(d0_1_0_0.getPath().contains("d0_1_0/"));
        assertTrue(d0_1_0_0.getPath().contains("d0_1_0/"));
    }
}