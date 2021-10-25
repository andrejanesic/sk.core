package actions;

import core.Core;
import exceptions.ActionUndoImpossibleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.PrivilegeType;

import static org.junit.jupiter.api.Assertions.*;

public class ActionInitStorageTest extends ActionsPrepareTest {

    private Action action;

    @BeforeEach
    void testSetup() {
        Core.getInstance().UserManager().getUser().grantPrivilege("test", PrivilegeType.INIT_STORAGE);
        action = new ActionInitStorage("test");
    }

    @AfterEach
    void testCleanup() {
        Core.getInstance().UserManager().getUser().revokePrivilege(PrivilegeType.INIT_STORAGE);
    }

    @Test
    void testActionInitStorageRun() {
        assertNull(Core.getInstance().StorageManager().getRoot());
        assertDoesNotThrow(() -> action.run());
        assertNotNull(Core.getInstance().StorageManager().getRoot());
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
        Object o = Core.getInstance().StorageManager().getRoot();
        assertDoesNotThrow(() -> action.undo());
        assertNull(Core.getInstance().StorageManager().getRoot());
    }
}
