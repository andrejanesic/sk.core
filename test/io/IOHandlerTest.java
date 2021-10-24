package io;

import dummynode.DummyNode;
import repository.builder.DirectoryBuilder;
import user.builder.PrivilegeBuilder;
import user.builder.UserBuilder;

import java.util.Collection;
import java.util.HashSet;

import static user.builder.PrivilegeTypeBuilder.ALL;

/**
 * Testna implementacija IOHandlerTest-a.
 */
public class IOHandlerTest implements IOHandler {

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
    public DirectoryBuilder initStorage(String path) {
        DummyNode rootDummy = DummyNode.generateDummyNodes();
        DirectoryBuilder rootBuilder = new DirectoryBuilder();
        DummyNode.dummyNodeTreeToBuilderNodeTree(rootBuilder, rootDummy);
        return rootBuilder;
    }

    @Override
    public UserBuilder initUser(String username, String password) {
        int r = (int) Math.floor(Math.random() * 10);

        Collection<PrivilegeBuilder> privilegeBuilders;
        if (r > 5) {
            privilegeBuilders = null;
        } else {
            privilegeBuilders = new HashSet<>();
            privilegeBuilders.add(new PrivilegeBuilder(ALL));
        }
        return new UserBuilder(TEST_USERNAME, TEST_PASSWORD, privilegeBuilders, true);
    }

    @Override
    public void deinitUser(String username) {
    }
}
