package config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.Core;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import user.builder.PrivilegeBuilder;
import user.builder.PrivilegeTypeBuilder;
import user.builder.UserBuilder;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ALL")
public class ConfigManagerTest {

    private static final String BASE_CONFIG = "{\"users\":[]}";

    @Test
    void testLoadEmptyConfig() {
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

        assertFalse(userBuilders.isEmpty());

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
        assertEquals(gson.toJson(Core.getInstance().ConfigManager().getConfig(), Config.class),
                Core.getInstance().IODriver().readConfig(""));
    }

    @Test
    void testAddUser() {
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

        Collection<UserBuilder> before = c.getUsers();
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
