package node;

import exceptions.RootNotInitializedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryTest {

    @Test
    void testGetRootWhenNotInitialized() {
        assertThrows(RootNotInitializedException.class, Directory::getRoot);
    }

    @Test
    void testMakeRoot() {
        Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().getPath());
    }

    @Test
    void testGetRootPath() {
        Directory.makeRoot();
        testMakeRoot();
        try {
            assertEquals("/", Directory.getRoot().getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMakeRootAgain() throws RootNotInitializedException {
        Directory root1 = Directory.makeRoot();
        Directory root2 = Directory.makeRoot();
        assertDoesNotThrow(() -> Directory.getRoot().getPath());
        assertEquals(root2, Directory.getRoot());
        assertNotEquals(root1, root2);
    }
}