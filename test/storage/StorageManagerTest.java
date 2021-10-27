package storage;

import core.Core;
import dummynode.DummyNode;
import exceptions.INodeRootNotInitializedException;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;

import static org.junit.jupiter.api.Assertions.*;

public class StorageManagerTest {

    @Test
    void testInitStorage() throws INodeRootNotInitializedException {
        assertDoesNotThrow(() -> Core.getInstance().StorageManager().deinitStorage());
        assertNull(Core.getInstance().StorageManager().getRoot());

        DummyNode rootDummy = DummyNode.generateDummyNodes();
        DirectoryBuilder rootBuilder = new DirectoryBuilder();
        DummyNode.dummyNodeTreeToBuilderNodeTree(rootBuilder, rootDummy);
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
                return rootBuilder;
            }
        });

        Core.getInstance().ConfigManager().initConfig("");

        assertDoesNotThrow(() -> Core.getInstance().StorageManager().initStorage(null));
        assertNotNull(Core.getInstance().StorageManager().getRoot());
        rootDummy.traverse(dummyNode -> assertDoesNotThrow(() -> {
            //noinspection ConstantConditions
            Core.getInstance().StorageManager().getRoot().resolvePath(dummyNode.path());
        }));
    }
}
