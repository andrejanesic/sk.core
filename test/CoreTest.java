import core.Core;
import io.IODriverTest;
import io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.Directory;
import storage.StorageManager;

import static org.junit.jupiter.api.Assertions.*;

public class CoreTest {

    @BeforeAll
    static void setHandler() {
        IOManager.setIODriver(new IODriverTest());
    }

    @Test
    void testInitRoot() {
        assertDoesNotThrow(() -> Core.getInstance().StorageManager().initStorage("test"));
        assertDoesNotThrow(() -> Core.getInstance().StorageManager().getRoot().getPath());
        assertEquals(Directory.ROOT_DIRECTORY, Core.getInstance().StorageManager().getRoot().getPath());
    }

    @Test
    void testInitUser() {
        String username = "foo";
        String password = "bar";
        assertDoesNotThrow(() -> Core.getInstance().UserManager().initUser(username, password));
        assertNotEquals(null, Core.getInstance().UserManager().getUser());
        assertEquals(Core.getInstance().UserManager().getUser().getUsername(), IODriverTest.TEST_USERNAME);
        assertEquals(Core.getInstance().UserManager().getUser().getPassword(), IODriverTest.TEST_PASSWORD);
    }
}
