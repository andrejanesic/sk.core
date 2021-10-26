import core.Core;
import io.IODriver;
import io.IOManager;
import org.junit.jupiter.api.Test;
import repository.Directory;
import repository.builder.DirectoryBuilder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoreTest {

    @Test
    void testInitRoot() {
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

            @Override
            public DirectoryBuilder initStorage(String path) {
                return null;
            }
        });

        Core.getInstance().ConfigManager().initConfig("");
        assertDoesNotThrow(() -> Core.getInstance().StorageManager().initStorage("test"));
        assertDoesNotThrow(() -> Core.getInstance().StorageManager().getRoot().getPath());
        assertEquals(Directory.ROOT_DIRECTORY, Core.getInstance().StorageManager().getRoot().getPath());
    }
}
