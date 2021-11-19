package com.raf.sk.core.actions;

import com.raf.sk.core.core.Core;
import com.raf.sk.core.exceptions.IActionUndoImpossibleException;
import com.raf.sk.core.user.PrivilegeType;
import com.raf.sk.specification.builders.DirectoryBuilder;
import com.raf.sk.specification.builders.FileBuilder;
import com.raf.sk.specification.io.IODriver;
import com.raf.sk.specification.io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
            public DirectoryBuilder initStorage() {
                return new DirectoryBuilder();
            }
        });
        Core.getInstance().ConfigManager().initConfig("");
        Core.getInstance().UserManager().initUser().grantPrivilege("test", PrivilegeType.STORAGE_INIT);
        action = new ActionInitStorage();
    }

    @AfterEach
    void testCleanup() {
        Core.getInstance().UserManager().initUser().revokePrivilege(PrivilegeType.STORAGE_INIT);
    }

    @Test
    void testActionInitStorageRun() {
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.STORAGE_INIT);
        Assertions.assertDoesNotThrow(() -> action.run());
        assertNotNull(Core.getInstance().StorageManager().getRoot());
    }

    @Test
    void testActionInitStorageRunMultiple() {
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.STORAGE_INIT);
        int n = (int) Math.floor(Math.random() * 10);
        while (n-- > 0)
            Assertions.assertDoesNotThrow(() -> action.run());
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
        Assertions.assertDoesNotThrow(() -> action.undo());
        assertNull(Core.getInstance().StorageManager().getRoot());
    }
}
