package actions;

import core.Core;
import exceptions.IActionUndoImpossibleException;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;
import user.PrivilegeType;

import static org.junit.jupiter.api.Assertions.*;

public class ActionInitStorageTest {

    private IAction action;

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
            public void downloadDirectory(String sourcePath, String downloadPath) {

            }

            @Override
            public void downloadFile(String sourcePath, String downloadPath) {

            }

            @Override
            public FileBuilder uploadFile(String destRelPath, String filePath) {
                return null;
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
        Core.getInstance().UserManager().initUser().grantPrivilege("test", PrivilegeType.STORAGE_INIT);
        action = new ActionInitStorage("test");
    }

    @AfterEach
    void testCleanup() {
        Core.getInstance().UserManager().initUser().revokePrivilege(PrivilegeType.STORAGE_INIT);
    }

    @Test
    void testActionInitStorageRun() {
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.STORAGE_INIT);
        assertNull(Core.getInstance().StorageManager().getRoot());
        assertDoesNotThrow(() -> action.run());
        assertNotNull(Core.getInstance().StorageManager().getRoot());
    }

    @Test
    void testActionInitStorageRunMultiple() {
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.STORAGE_INIT);
        int n = (int) Math.floor(Math.random() * 10);
        while (n-- > 0)
            assertDoesNotThrow(() -> action.run());
    }

    @Test
    void testActionInitStorageUndo() {
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.STORAGE_INIT);
        try {
            action.run();
        } catch (IActionUndoImpossibleException ignored) {
        }
        Object o = Core.getInstance().StorageManager().getRoot();
        assertDoesNotThrow(() -> action.undo());
        assertNull(Core.getInstance().StorageManager().getRoot());
    }
}
