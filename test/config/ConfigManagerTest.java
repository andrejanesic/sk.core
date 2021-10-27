package config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.Core;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.Directory;
import repository.INode;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;
import repository.limitations.INodeLimitation;
import repository.limitations.INodeLimitationType;
import user.builder.PrivilegeBuilder;
import user.builder.PrivilegeTypeBuilder;
import user.builder.UserBuilder;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ALL")
public class ConfigManagerTest {

    private static final String BASE_CONFIG = "{\"users\":[],\"limitations\":[]}";

    @Test
    void testLoadEmptyConfig() {
        IOManager.setIODriver(new IODriver() {
            @Override
            public void makeDirectory(String path) {

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
                return "{}";
            }

            @Override
            public void writeConfig(String json, String path) {

            }

            @NotNull
            @Override
            public DirectoryBuilder initStorage(String path) {
                return null;
            }
        });
        assertDoesNotThrow(() -> Core.getInstance().ConfigManager().initConfig(""));
        assertNotNull(Core.getInstance().ConfigManager().getConfig());
        assertEquals(BASE_CONFIG, Core.getInstance().ConfigManager().getConfig().toJson());
    }

    @Test
    void testLoadInvalidJson() {
        IOManager.setIODriver(new IODriver() {
            @Override
            public void makeDirectory(String path) {

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
                return "xx{}";
            }

            @Override
            public void writeConfig(String json, String path) {

            }

            @NotNull
            @Override
            public DirectoryBuilder initStorage(String path) {
                return null;
            }
        });
        assertDoesNotThrow(() -> Core.getInstance().ConfigManager().initConfig(""));
        assertNotNull(Core.getInstance().ConfigManager().getConfig());
        assertEquals(BASE_CONFIG, Core.getInstance().ConfigManager().getConfig().toJson());
    }

    @Test
    void testLoadJsonNull() {
        IOManager.setIODriver(new IODriver() {
            @Override
            public void makeDirectory(String path) {

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
                return null;
            }
        });
        assertDoesNotThrow(() -> Core.getInstance().ConfigManager().initConfig(""));
        assertNotNull(Core.getInstance().ConfigManager().getConfig());
        assertEquals(BASE_CONFIG, Core.getInstance().ConfigManager().getConfig().toJson());
    }

    @Test
    void testReadUsers() {
        Type listType = new TypeToken<List<UserBuilder>>() {
        }.getType();
        List<UserBuilder> userBuilders = new ArrayList<>();
        List<PrivilegeBuilder> privilegeBuilders = new ArrayList<>();
        int n = (int) Math.floor(Math.random() * 20) + 1;

        while (n-- > 0) {
            privilegeBuilders.clear();
            int p = (int) Math.floor(Math.random() * 10);
            int q = (int) Math.floor(Math.random() * PrivilegeTypeBuilder.values().length);

            // generiši privilegije
            while (p-- > 0) {
                privilegeBuilders.add(new PrivilegeBuilder(
                        Math.random() * 10 > 5 ? "test" : null,
                        PrivilegeTypeBuilder.values()[q]));
            }

            userBuilders.add(new UserBuilder(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    privilegeBuilders));
        }

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
            public void moveDirectory(String sourcePath, String destPath) {

            }

            @Override
            public void moveFile(String sourcePath, String destPath) {

            }

            @Override
            public String readConfig(String path) {
                Gson gson = new Gson();
                return "{\"users\":" + gson.toJson(userBuilders, listType) + "}";
            }

            @Override
            public void writeConfig(String json, String path) {

            }

            @NotNull
            @Override
            public DirectoryBuilder initStorage(String path) {
                return null;
            }
        });

        Gson gson = new Gson();
        assertDoesNotThrow(() -> Core.getInstance().ConfigManager().initConfig(""));
        assertNotNull(Core.getInstance().ConfigManager().getConfig());
        assertEquals(new HashSet<>(userBuilders), Core.getInstance().ConfigManager().getConfig().getUsers());
        assertEquals("{\"users\":" + gson.toJson(
                Core.getInstance().ConfigManager().getConfig().getUsers(), listType) + "}",
                Core.getInstance().IODriver().readConfig(""));
    }

    @Test
    void testReadLimitations() {
        Type listType = new TypeToken<List<INodeLimitation>>() {
        }.getType();
        Collection<INodeLimitation> limitations = new HashSet<>();
        INode d = new Directory(false, null, Directory.ROOT_DIRECTORY);
        int n = (int) Math.floor(Math.random() * 20) + 1;

        while (n-- > 0) {
            int j = (int) Math.floor(Math.random() * INodeLimitationType.values().length);
            switch (INodeLimitationType.values()[j]) {
                case MAX_FILE_COUNT:
                    limitations.add(new INodeLimitation(
                            d,
                            INodeLimitationType.MAX_FILE_COUNT,
                            // changing from double to any other type will cause the test to break because it's being
                            // read from IODriver as (double)!
                            (double) Math.floor(Math.random() * 1_000_000_000L)
                    ));
                    break;
                case BLACKLIST_EXT:
                    limitations.add(new INodeLimitation(
                            d,
                            INodeLimitationType.BLACKLIST_EXT,
                            UUID.randomUUID().toString()
                    ));
                    break;
                case MAX_SIZE:
                    limitations.add(new INodeLimitation(
                            d,
                            INodeLimitationType.MAX_SIZE,
                            (double) Math.floor(Math.random() * 1_000_000_000L)
                    ));
                default:
                    break;
            }
        }

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
                Gson gson = new Gson();
                return "{\"limitations\":" + gson.toJson(limitations, listType) + "}";
            }

            @Override
            public void writeConfig(String json, String path) {

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

            @NotNull
            @Override
            public DirectoryBuilder initStorage(String path) {
                return null;
            }
        });

        Gson gson = new Gson();
        assertDoesNotThrow(() -> Core.getInstance().ConfigManager().initConfig(""));
        assertNotNull(Core.getInstance().ConfigManager().getConfig());
        // have to manually invoke path resolution here for test sake
        Core.getInstance().ConfigManager().getConfig().getLimitations().forEach(e -> e.setHost(d));
        assertEquals(limitations, Core.getInstance().ConfigManager().getConfig().getLimitations());
        assertEquals("{\"limitations\":" + gson.toJson(
                Core.getInstance().ConfigManager().getConfig().getLimitations(), listType) + "}",
                Core.getInstance().IODriver().readConfig(""));
    }

    @Test
    void testAddUser() {
        IOManager.setIODriver(new IODriver() {
            @Override
            public void makeDirectory(String path) {

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
                return null;
            }
        });

        Core.getInstance().ConfigManager().initConfig("");

        Set<UserBuilder> userBuilders = new HashSet<>();
        Set<PrivilegeBuilder> privilegeBuilders = new HashSet<>();
        int n = (int) Math.floor(Math.random() * 20);

        while (n-- > 0) {
            privilegeBuilders.clear();
            int p = (int) Math.floor(Math.random() * 10);
            int q = (int) Math.floor(Math.random() * PrivilegeTypeBuilder.values().length);

            // generiši privilegije
            while (p-- > 0) {
                privilegeBuilders.add(new PrivilegeBuilder(
                        Math.random() * 10 > 5 ? "test" : null,
                        PrivilegeTypeBuilder.values()[q]));
            }

            userBuilders.add(new UserBuilder(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    privilegeBuilders));
        }

        for (UserBuilder u : userBuilders)
            assertDoesNotThrow(() -> Core.getInstance().ConfigManager().getConfig().addUser(u));
        assertEquals(userBuilders, Core.getInstance().ConfigManager().getConfig().getUsers());
    }

    @Test
    void testGetUsers() {
        IOManager.setIODriver(new IODriver() {
            @Override
            public void makeDirectory(String path) {

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
                return null;
            }
        });

        Core.getInstance().ConfigManager().initConfig("");

        List<UserBuilder> userBuilders = new ArrayList<>();
        List<PrivilegeBuilder> privilegeBuilders = new ArrayList<>();
        int n = (int) Math.floor(Math.random() * 20);

        while (n-- > 0) {
            privilegeBuilders.clear();
            int p = (int) Math.floor(Math.random() * 10);
            int q = (int) Math.floor(Math.random() * PrivilegeTypeBuilder.values().length);

            // generiši privilegije
            while (p-- > 0) {
                privilegeBuilders.add(new PrivilegeBuilder(
                        Math.random() * 10 > 5 ? "test" : null,
                        PrivilegeTypeBuilder.values()[q]));
            }

            userBuilders.add(new UserBuilder(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    privilegeBuilders));
        }

        for (UserBuilder u : userBuilders)
            Core.getInstance().ConfigManager().getConfig().addUser(u);

        assertEquals(new HashSet<>(userBuilders), Core.getInstance().ConfigManager().getConfig().getUsers());
    }

    @Test
    void testUpdateUser() {
        String username = UUID.randomUUID().toString();
        String oldPassword = UUID.randomUUID().toString();
        String newPassword = UUID.randomUUID().toString();
        UserBuilder existing = new UserBuilder(
                username,
                oldPassword,
                null
        );
        UserBuilder nonExist = new UserBuilder(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                null
        );

        IOManager.setIODriver(new IODriver() {
            @Override
            public void makeDirectory(String path) {

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
                return null;
            }
        });
        Core.getInstance().ConfigManager().initConfig("");
        IConfig c = Core.getInstance().ConfigManager().getConfig();
        c.addUser(existing);

        existing.setPassword(newPassword);
        assertDoesNotThrow(() -> c.updateUser(existing));

        Collection<UserBuilder> before = new HashSet<>();
        before.addAll(c.getUsers());
        c.updateUser(nonExist);
        Collection<UserBuilder> after = c.getUsers();
        assertEquals(before, after);
        boolean found = false;
        for (UserBuilder u : c.getUsers())
            if (u.equals(existing)) {
                assertEquals(u.getPassword(), existing.getPassword());
                found = true;
            }
        if (!found)
            fail("Did not find existing user in getUsers() method");
    }

    @Test
    void testDeleteUser() {

        IOManager.setIODriver(new IODriver() {
            @Override
            public void makeDirectory(String path) {

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
                return null;
            }
        });

        Core.getInstance().ConfigManager().initConfig("");

        List<UserBuilder> userBuilders = new ArrayList<>();
        List<PrivilegeBuilder> privilegeBuilders = new ArrayList<>();
        int n = (int) Math.floor(Math.random() * 20);

        while (n-- > 0) {
            privilegeBuilders.clear();
            int p = (int) Math.floor(Math.random() * 10);
            int q = (int) Math.floor(Math.random() * PrivilegeTypeBuilder.values().length);

            // generiši privilegije
            while (p-- > 0) {
                privilegeBuilders.add(new PrivilegeBuilder(
                        Math.random() * 10 > 5 ? "test" : null,
                        PrivilegeTypeBuilder.values()[q]));
            }

            userBuilders.add(new UserBuilder(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    privilegeBuilders));
        }

        for (UserBuilder u : userBuilders)
            Core.getInstance().ConfigManager().getConfig().addUser(u);

        for (int i = (int) Math.floor(Math.random() * userBuilders.size()), j = 0; j < i; j++) {
            int k = (int) Math.floor(Math.random() * userBuilders.size());
            Core.getInstance().ConfigManager().getConfig().deleteUser(userBuilders.get(k));
            userBuilders.remove(k);
        }

        assertEquals(new HashSet<>(userBuilders), Core.getInstance().ConfigManager().getConfig().getUsers());
    }
}
