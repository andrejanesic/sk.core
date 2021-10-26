package user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.Core;
import exceptions.IUserCannotDeleteCurrentUserException;
import exceptions.IUserDuplicateUsernameException;
import io.IODriver;
import io.IOManager;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import user.builder.PrivilegeBuilder;
import user.builder.PrivilegeTypeBuilder;
import user.builder.UserBuilder;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {

    @Test
    void testInitUser() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Type listType = new TypeToken<List<UserBuilder>>() {
        }.getType();
        List<UserBuilder> userBuilders = new ArrayList<>();
        List<PrivilegeBuilder> privilegeBuilders = new ArrayList<>();

        int n = (int) Math.floor(Math.random() * 10);
        int p = (int) Math.floor(Math.random() * PrivilegeTypeBuilder.values().length);
        while (n-- > 0) {
            privilegeBuilders.add(new PrivilegeBuilder(
                    Math.random() * 10 > 5 ? "test" : null,
                    PrivilegeTypeBuilder.values()[p]));
        }
        userBuilders.add(new UserBuilder(
                username,
                password,
                privilegeBuilders));

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
                System.out.println(gson.toJson(userBuilders, listType));
                return "{\"users\":" + gson.toJson(userBuilders, listType) + "}";
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

        IUser curr = Core.getInstance().UserManager().getUser();
        assertNotEquals(0, Core.getInstance().ConfigManager().getConfig().getUsers().size());
        assertDoesNotThrow(() -> Core.getInstance().UserManager().initUser(username, password));
        assertNotEquals(curr, Core.getInstance().UserManager().getUser());
        assertEquals(Core.getInstance().UserManager().getUser().getUsername(), username);
        assertEquals(Core.getInstance().UserManager().getUser().getPassword(), password);
    }

    @Test
    void testInitUserBadCredentials() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Type listType = new TypeToken<List<UserBuilder>>() {
        }.getType();
        List<UserBuilder> userBuilders = new ArrayList<>();
        List<PrivilegeBuilder> privilegeBuilders = new ArrayList<>();

        int n = (int) Math.floor(Math.random() * 10);
        int p = (int) Math.floor(Math.random() * PrivilegeTypeBuilder.values().length);
        while (n-- > 0) {
            privilegeBuilders.add(new PrivilegeBuilder(
                    Math.random() * 10 > 5 ? "test" : null,
                    PrivilegeTypeBuilder.values()[p]));
        }
        userBuilders.add(new UserBuilder(
                username,
                password,
                privilegeBuilders));

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

            @Override
            public DirectoryBuilder initStorage(String path) {
                return null;
            }
        });
        Core.getInstance().ConfigManager().initConfig("");

        IUser curr = Core.getInstance().UserManager().getUser();
        assertNotEquals(0, Core.getInstance().ConfigManager().getConfig().getUsers().size());
        assertDoesNotThrow(() -> Core.getInstance().UserManager().initUser(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ));
        assertNotEquals(curr, Core.getInstance().UserManager().getUser());
        assertTrue(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_LOGIN));
        assertTrue(Core.getInstance().UserManager().getUser().hasPrivilege(PrivilegeType.USER_LOGOUT));
    }

    @Test
    void testDeinitUser() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new ArrayList<>();
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
                UserBuilder ub = new UserBuilder(
                        username,
                        password,
                        Collections.singletonList(new PrivilegeBuilder(
                                PrivilegeTypeBuilder.values()[
                                        (int) Math.floor(Math.random() *
                                                PrivilegeTypeBuilder.values().length)]
                        ))
                );
                return gson.toJson(Collections.singletonList(ub));
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

        int count = Core.getInstance().UserManager().getUsers().size();
        assertDoesNotThrow(() -> Core.getInstance().UserManager().addUser(username, password, privileges));
        assertNotEquals(count, Core.getInstance().UserManager().getUsers().size());
        assertDoesNotThrow(() -> Core.getInstance().UserManager().initUser(username, password));
        IUser curr = Core.getInstance().UserManager().getUser();
        assertDoesNotThrow(() -> Core.getInstance().UserManager().deinitUser());
        assertNotEquals(curr, Core.getInstance().UserManager().getUser());
    }

    @Test
    void testAddUser() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new ArrayList<>();
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
                UserBuilder ub = new UserBuilder(
                        username,
                        password,
                        Collections.singletonList(new PrivilegeBuilder(
                                PrivilegeTypeBuilder.values()[
                                        (int) Math.floor(Math.random() *
                                                PrivilegeTypeBuilder.values().length)]
                        ))
                );
                return gson.toJson(Collections.singletonList(ub));
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

        int count = Core.getInstance().UserManager().getUsers().size();
        IUser curr = Core.getInstance().UserManager().getUser();
        IUser u = new User(username, password,
                Collections.singletonList(new Privilege(PrivilegeType.ALL)));
        assertDoesNotThrow(() -> Core.getInstance().UserManager().addUser(username, password, privileges));
        assertTrue(Core.getInstance().UserManager().getUsers().contains(u));
        assertEquals(curr, Core.getInstance().UserManager().getUser());
        assertEquals(count + 1, Core.getInstance().UserManager().getUsers().size());
    }

    @Test
    void testAddUserViaBuilder() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new ArrayList<>();
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
                UserBuilder ub = new UserBuilder(
                        username,
                        password,
                        Collections.singletonList(new PrivilegeBuilder(
                                PrivilegeTypeBuilder.values()[
                                        (int) Math.floor(Math.random() *
                                                PrivilegeTypeBuilder.values().length)]
                        ))
                );
                return gson.toJson(Collections.singletonList(ub));
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

        int count = Core.getInstance().UserManager().getUsers().size();
        IUser curr = Core.getInstance().UserManager().getUser();
        IUser u = new User(username, password,
                Collections.singletonList(new Privilege(PrivilegeType.ALL)));
        assertDoesNotThrow(() -> Core.getInstance().UserManager().addUser(new UserBuilder(
                username, password, Collections.singletonList(new PrivilegeBuilder(PrivilegeTypeBuilder.ALL))
        )));
        assertTrue(Core.getInstance().UserManager().getUsers().contains(u));
        assertEquals(curr, Core.getInstance().UserManager().getUser());
        assertEquals(count + 1, Core.getInstance().UserManager().getUsers().size());
    }

    @Test
    void testAddUserDuplicateUsername() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new ArrayList<>();
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
                UserBuilder ub = new UserBuilder(
                        username,
                        password,
                        Collections.singletonList(new PrivilegeBuilder(
                                PrivilegeTypeBuilder.values()[
                                        (int) Math.floor(Math.random() *
                                                PrivilegeTypeBuilder.values().length)]
                        ))
                );
                return gson.toJson(Collections.singletonList(ub));
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

        int count = Core.getInstance().UserManager().getUsers().size();
        IUser curr = Core.getInstance().UserManager().getUser();
        assertDoesNotThrow(() -> Core.getInstance().UserManager().addUser(username, password, privileges));
        assertThrows(IUserDuplicateUsernameException.class,
                () -> Core.getInstance().UserManager().addUser(username, password, privileges));
        assertEquals(curr, Core.getInstance().UserManager().getUser());
        assertEquals(count + 1, Core.getInstance().UserManager().getUsers().size());
    }

    @Test
    void testDeleteUser() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new ArrayList<>();
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
                UserBuilder ub = new UserBuilder(
                        username,
                        password,
                        Collections.singletonList(new PrivilegeBuilder(
                                PrivilegeTypeBuilder.values()[
                                        (int) Math.floor(Math.random() *
                                                PrivilegeTypeBuilder.values().length)]
                        ))
                );
                return gson.toJson(Collections.singletonList(ub));
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

        int count = Core.getInstance().UserManager().getUsers().size();
        IUser u = new User(username, password,
                Collections.singletonList(new Privilege(PrivilegeType.ALL)));
        assertDoesNotThrow(() -> Core.getInstance().UserManager().addUser(username, password, privileges));
        assertDoesNotThrow(() -> Core.getInstance().UserManager().deleteUser(u));
        assertFalse(Core.getInstance().UserManager().getUsers().contains(u));
        assertEquals(count, Core.getInstance().UserManager().getUsers().size());
    }

    @Test
    void testAddUserDuplicateUsernameAfterDelete() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new ArrayList<>();
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
                UserBuilder ub = new UserBuilder(
                        username,
                        password,
                        Collections.singletonList(new PrivilegeBuilder(
                                PrivilegeTypeBuilder.values()[
                                        (int) Math.floor(Math.random() *
                                                PrivilegeTypeBuilder.values().length)]
                        ))
                );
                return gson.toJson(Collections.singletonList(ub));
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

        int count = Core.getInstance().UserManager().getUsers().size();
        IUser curr = Core.getInstance().UserManager().getUser();
        IUser u = new User(username, password,
                Collections.singletonList(new Privilege(PrivilegeType.ALL)));
        assertDoesNotThrow(() -> Core.getInstance().UserManager().addUser(username, password, privileges));
        assertDoesNotThrow(() -> Core.getInstance().UserManager().deleteUser(u));
        assertDoesNotThrow(() -> Core.getInstance().UserManager().addUser(username, password, privileges));
        assertEquals(curr, Core.getInstance().UserManager().getUser());
        assertEquals(count + 1, Core.getInstance().UserManager().getUsers().size());
    }

    @Test
    void testDeleteCurrentUser() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
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
                UserBuilder ub = new UserBuilder(
                        username,
                        password,
                        Collections.singletonList(new PrivilegeBuilder(
                                PrivilegeTypeBuilder.values()[
                                        (int) Math.floor(Math.random() *
                                                PrivilegeTypeBuilder.values().length)]
                        ))
                );
                return gson.toJson(Collections.singletonList(ub));
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

        int count = Core.getInstance().UserManager().getUsers().size();
        IUser curr = Core.getInstance().UserManager().getUser();
        assertThrows(IUserCannotDeleteCurrentUserException.class,
                () -> Core.getInstance().UserManager().deleteUser(curr));
        assertEquals(curr, Core.getInstance().UserManager().getUser());
        assertEquals(count, Core.getInstance().UserManager().getUsers().size());
    }
}
