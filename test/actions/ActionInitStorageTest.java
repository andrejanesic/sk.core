package actions;

import exceptions.ActionUndoImpossibleException;
import loader.Loader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActionInitStorageTest extends ActionsPrepareTest {

    private Action action;

    @BeforeEach
    void testSetup() {
        action = new ActionInitStorage("test");
    }

    @Test
    void testActionInitStorageRun() {
        assertDoesNotThrow(() -> action.run());
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
        assertThrows(ActionUndoImpossibleException.class, () -> action.undo());
        assertEquals(o, Loader.getInstance().getRoot());
    }
}
