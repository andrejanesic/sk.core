package com.raf.sk.core.storage;

import com.raf.sk.core.core.Core;
import com.raf.sk.core.dummynode.DummyNode;
import com.raf.sk.core.exceptions.INodeRootNotInitializedException;
import com.raf.sk.core.io.IODriver;
import com.raf.sk.core.io.IOManager;
import com.raf.sk.core.repository.builder.DirectoryBuilder;
import com.raf.sk.core.repository.builder.FileBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

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
