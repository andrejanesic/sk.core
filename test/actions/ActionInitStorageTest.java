package actions;

import exceptions.ActionUndoImpossibleException;
import loader.Loader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.PrivilegeType;
import user.UserManager;

import static org.junit.jupiter.api.Assertions.*;

public class ActionInitStorageTest extends ActionsPrepareTest {

    private Action action;

    @BeforeEach
    void testSetup() {
        UserManager.getInstance().getUser().grantPrivilege("test", PrivilegeType.INIT_STORAGE);
        action = new ActionInitStorage("test");
    }

    @AfterEach
    void testCleanup() {
        UserManager.getInstance().getUser().revokePrivilege(PrivilegeType.INIT_STORAGE);
    }

    @Test
    void testActionInitStorageRun() {
        assertNull(Loader.getInstance().getRoot());
        assertDoesNotThrow(() -> action.run());
        assertNotNull(Loader.getInstance().getRoot());
    }

    @Test
    void testActionInitStorageRunMultiple() {
        int n = (int) Math.floor(Math.random() * 10);
        while (n-- > 0)
            assertDoesNotThrow(() -> action.run());
    }

    @Test
    void testActionInitStorageUndo() {
        try {
            action.run();
        } catch (ActionUndoImpossibleException ignored) {
        }
        Object o = Loader.getInstance().getRoot();
        assertDoesNotThrow(() -> action.undo());
        assertNull(Loader.getInstance().getRoot());
    }
}
