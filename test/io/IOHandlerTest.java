package io;

import dummynode.DummyNode;
import loader.Loader;
import org.junit.jupiter.api.Test;
import repository.Directory;
import repository.builder.DirectoryBuilder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testna implementacija IOHandlerTest-a.
 */
public class IOHandlerTest implements IOHandler {

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
    public DirectoryBuilder buildStorage(String path) {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        DirectoryBuilder rootBuilder = new DirectoryBuilder();
        DummyNode.dummyNodeTreeToBuilderNodeTree(rootBuilder, rootDummy);
        return rootBuilder;
    }
}
