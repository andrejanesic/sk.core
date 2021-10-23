package node;

import exceptions.DirectoryCreateNameInvalidException;
import exceptions.DirectoryCreateNameNotUniqueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryTest {

    @Test
    void testMakeRoot() {
        Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().getPath());
    }

    @Test
    void testGetRootPath() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        try {
            assertEquals("/", Directory.getRoot().getPath());
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
        assertThrows(DirectoryCreateNameInvalidException.class, () -> Directory.getRoot().makeDirectory("abc/"));
    }

    @Test
    void testMakeFileInvalidName() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        assertThrows(DirectoryCreateNameInvalidException.class, () -> Directory.getRoot().makeFile("def/"));
    }

    @Test
    void testMakeDirectoryDuplicateName() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().makeFile("foo123"));
        assertThrows(DirectoryCreateNameNotUniqueException.class, () -> Directory.getRoot().makeFile("foo123"));
    }

    @Test
    void testMakeFileDuplicateName() {
        assertDoesNotThrow(() -> Directory.getRoot().makeFile("bar"));
        assertThrows(DirectoryCreateNameNotUniqueException.class, () -> Directory.getRoot().makeFile("bar"));
    }
}