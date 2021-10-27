package actions;

import core.Core;
import exceptions.IActionUndoImpossibleException;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ActionInitConfigTest {

    @Test
    void testActionInitConfigRun() {
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
                return new DirectoryBuilder(null, "/");
            }
        });
        final IAction[] a = new IAction[1];
        assertDoesNotThrow(() -> a[0] = new ActionInitConfig(UUID.randomUUID().toString()));
        assertDoesNotThrow(() -> a[0].run());
        assertNotNull(Core.getInstance().ConfigManager().getConfig());
    }

    @Test
    void testActionInitConfigRunMultipleTimes() {
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
                return new DirectoryBuilder(null, "/");
            }
        });
        final IAction[] a = new IAction[1];
        assertDoesNotThrow(() -> a[0] = new ActionInitConfig(UUID.randomUUID().toString()));
        for (int n = (int) Math.floor(Math.random() * 10) + 1; n-- > 0; )
            assertDoesNotThrow(() -> a[0].run());
        assertNotNull(Core.getInstance().ConfigManager().getConfig());
    }

    @Test
    void testActionInitConfigUndo() {
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
                return new DirectoryBuilder(null, "/");
            }
        });

        assertThrows(IActionUndoImpossibleException.class, () -> new ActionInitConfig(
                UUID.randomUUID().toString()).undo());
        final IAction[] a = new IAction[1];
        assertDoesNotThrow(() -> a[0] = new ActionInitConfig(UUID.randomUUID().toString()));
        assertDoesNotThrow(() -> a[0].run());
        assertThrows(IActionUndoImpossibleException.class, () -> a[0].undo());
    }
}
