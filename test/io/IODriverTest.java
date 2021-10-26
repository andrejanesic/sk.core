package io;

import dummynode.DummyNode;
import repository.builder.DirectoryBuilder;

/**
 * Testna implementacija IODriverTest-a.
 */
public class IODriverTest implements IODriver {

    public static final String TEST_USERNAME = "testFoo";
    public static final String TEST_PASSWORD = "testBar";

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
        return "{\n" +
                "\t\"users\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"username\": \"test\",\n" +
                "\t\t\t\"password\": \"test\",\n" +
                "\t\t\t\"privileges\": [\n" +
                "\t\t\t\t\"type\": \"ALL\"\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
    }

    @Override
    public void writeConfig(String json, String path) {

    }

    @Override
    public DirectoryBuilder initStorage(String path) {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        DirectoryBuilder rootBuilder = new DirectoryBuilder();
        DummyNode.dummyNodeTreeToBuilderNodeTree(rootBuilder, rootDummy);
        return rootBuilder;
    }

}
