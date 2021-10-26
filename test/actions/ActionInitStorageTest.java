package actions;

import core.Core;
import exceptions.ActionUndoImpossibleException;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import user.PrivilegeType;

import static org.junit.jupiter.api.Assertions.*;

public class ActionInitStorageTest {

    private Action action;

    @BeforeEach
    void testSetup() {
        IOManager.setIODriver(new IODriver() {
            @Override
            public void makeDirectory(String path) {

            }

            @Override
            public void makeFile(String path) {

            }

            @Override
            public void deleteDirectory(String path) {

            }

            @Override
            public void deleteFile(String path) {

            }

            @Override
            public void moveDirectory(String sourcePath, String destPath) {

            }

            @Override
            public void moveFile(String sourcePath, String destPath) {

            }

            @Override
            public String readConfig(String path) {
                return null;
            }

            @Override
            public void writeConfig(String json, String path) {

            }

            @NotNull
            @Override
            public DirectoryBuilder initStorage(String path) {
                return new DirectoryBuilder();
            }
        });
        Core.getInstance().ConfigManager().initConfig("");
        Core.getInstance().UserManager().initUser().grantPrivilege("test", PrivilegeType.INIT_STORAGE);
        action = new ActionInitStorage("test");
    }

    @AfterEach
    void testCleanup() {
        Core.getInstance().UserManager().initUser().revokePrivilege(PrivilegeType.INIT_STORAGE);
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
