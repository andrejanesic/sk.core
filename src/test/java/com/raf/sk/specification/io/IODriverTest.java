package com.raf.sk.specification.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raf.sk.specification.dummynode.DummyNode;
import com.raf.sk.specification.repository.Directory;
import com.raf.sk.specification.repository.INode;
import com.raf.sk.specification.repository.builder.DirectoryBuilder;
import com.raf.sk.specification.repository.builder.FileBuilder;
import com.raf.sk.specification.repository.limitations.INodeLimitation;
import com.raf.sk.specification.repository.limitations.INodeLimitationType;
import com.raf.sk.specification.user.builder.PrivilegeBuilder;
import com.raf.sk.specification.user.builder.PrivilegeTypeBuilder;
import com.raf.sk.specification.user.builder.UserBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Testna implementacija IODriverTest-a.
 */
public class IODriverTest implements IODriver {

    public static final String MASTER_USERNAME = UUID.randomUUID().toString();
    public static final String MASTER_PASSWORD = UUID.randomUUID().toString();

    static {
        IODriver inst = new IODriverTest();
        IOManager.setIODriver(inst);
    }

    private Collection<UserBuilder> userBuilders;
    private Collection<INodeLimitation> limitations;
    private DirectoryBuilder root;

    public IODriverTest() {
        DummyNode.pool.clear();
        DummyNode.poolDirs.clear();
        DummyNode.poolFiles.clear();
        DummyNode rootDummy = DummyNode.generateDummyNodes();

        root = new DirectoryBuilder();
        DummyNode.dummyNodeTreeToBuilderNodeTree(root, rootDummy);

        userBuilders = new ArrayList<>();
        userBuilders.add(new UserBuilder(MASTER_USERNAME, MASTER_PASSWORD,
                Collections.singletonList(new PrivilegeBuilder(PrivilegeTypeBuilder.ALL))));
        List<PrivilegeBuilder> privilegeBuilders = new ArrayList<>();
        int n = (int) Math.floor(Math.random() * 20) + 1;

        while (n-- > 0) {
            privilegeBuilders.clear();
            int p = (int) Math.floor(Math.random() * 10);
            int q = (int) Math.floor(Math.random() * PrivilegeTypeBuilder.values().length);

            // generiši privilegije
            while (p-- > 0) {
                privilegeBuilders.add(new PrivilegeBuilder(
                        Math.random() > 0.5 ? DummyNode.poolDirs.get(
                                (int) Math.floor(Math.random() * DummyNode.poolDirs.size())
                        ).path() : null,
                        PrivilegeTypeBuilder.values()[q]));
            }

            userBuilders.add(new UserBuilder(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    privilegeBuilders));
        }

        limitations = new HashSet<>();
        INode d = new Directory(false, null, Directory.ROOT_DIRECTORY);
        n = (int) Math.floor(Math.random() * 20) + 1;

        while (n-- > 0) {
            int j = (int) Math.floor(Math.random() * INodeLimitationType.values().length);
            switch (INodeLimitationType.values()[j]) {
                case MAX_FILE_COUNT:
                    limitations.add(new INodeLimitation(
                            DummyNode.poolDirs.get(
                                    (int) Math.floor(Math.random() * DummyNode.poolDirs.size())
                            ).path(),
                            INodeLimitationType.MAX_FILE_COUNT,
                            Math.floor(Math.random() * 1_000_000_000L)
                    ));
                    break;
                case BLACKLIST_EXT:
                    limitations.add(new INodeLimitation(
                            DummyNode.poolDirs.get(
                                    (int) Math.floor(Math.random() * DummyNode.poolDirs.size())
                            ).path(),
                            INodeLimitationType.BLACKLIST_EXT,
                            UUID.randomUUID().toString()
                    ));
                    break;
                case MAX_SIZE:
                    limitations.add(new INodeLimitation(
                            DummyNode.poolDirs.get(
                                    (int) Math.floor(Math.random() * DummyNode.poolDirs.size())
                            ).path(),
                            INodeLimitationType.MAX_SIZE,
                            Math.floor(Math.random() * 1_000_000_000L)
                    ));
                default:
                    break;
            }
        }
    }

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
        Gson gson = new Gson();
        return "{\"users\":" +
                gson.toJson(userBuilders, new TypeToken<List<UserBuilder>>() {
                }.getType()) +
                ",\"limitations\":" +
                gson.toJson(limitations, new TypeToken<List<INodeLimitation>>() {
                }.getType()) +
                "}";
    }

    @Override
    public void writeConfig(String json, String path) {
        //System.out.println(json);
    }

    @NotNull
    @Override
    public DirectoryBuilder initStorage(String path) {
        return root;
    }

}
