package loader;

import io.IOHandlerTest;
import io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.Directory;
import storage.StorageManager;
import user.UserManager;

import static org.junit.jupiter.api.Assertions.*;

public class LoaderTest {

    @BeforeAll
    static void setHandler() {
        IOManager.setIOHandler(new IOHandlerTest());
    }

    @Test
    void testInitRoot() {
        assertDoesNotThrow(() -> StorageManager.getInstance().initStorage("test"));
        assertDoesNotThrow(() -> StorageManager.getInstance().getRoot().getPath());
        assertEquals(Directory.ROOT_DIRECTORY, StorageManager.getInstance().getRoot().getPath());
    }

    @Test
    void testInitUser() {
        String username = "foo";
        String password = "bar";
        assertDoesNotThrow(() -> UserManager.getInstance().initUser(username, password));
        assertNotEquals(null, UserManager.getInstance().getUser());
        assertEquals(UserManager.getInstance().getUser().getUsername(), IOHandlerTest.TEST_USERNAME);
        assertEquals(UserManager.getInstance().getUser().getPassword(), IOHandlerTest.TEST_PASSWORD);
    }
}
