package actions;

import core.Core;
import exceptions.IActionInsufficientPrivilegeException;
import exceptions.IUserDuplicateUsernameException;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import user.IPrivilege;
import user.IUser;
import user.Privilege;
import user.PrivilegeType;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ActionAddUserTest {

    @Test
    void testActionAddUserRun() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new HashSet<>();
        privileges.add(new Privilege(PrivilegeType.ALL));
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
        Core.getInstance().ConfigManager().initConfig("test");
        Core.getInstance().UserManager().addUser(u, p, privileges);
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionAddUser(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        assertDoesNotThrow(a::run);
    }

    @Test
    void testActionAddUserRunMultiple() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new HashSet<>();
        privileges.add(new Privilege(PrivilegeType.ALL));
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
        Core.getInstance().ConfigManager().initConfig("test");
        Core.getInstance().UserManager().addUser(u, p, privileges);
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionAddUser(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        assertDoesNotThrow(a::run);
        for (int n = (int) Math.floor(Math.random() * 10) + 1; n-- > 0; )
            assertThrows(IUserDuplicateUsernameException.class, a::run);
    }

    @Test
    void testActionAddUserRunInsufficientPrivileges() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new HashSet<>();
        privileges.add(new Privilege(PrivilegeType.ALL));
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
        Core.getInstance().ConfigManager().initConfig("test");
        Core.getInstance().UserManager().addUser(u, p, privileges);
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionAddUser(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        assertDoesNotThrow(a::run);
    }

    @Test
    void testActionAddUserUndo() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new HashSet<>();
        privileges.add(new Privilege(PrivilegeType.ALL));
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
        Core.getInstance().ConfigManager().initConfig("test");
        Core.getInstance().UserManager().addUser(u, p, privileges);
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionAddUser(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        a.run();
        assertDoesNotThrow(a::undo);
    }

    @Test
    void testActionAddUserUndoInsufficientPrivileges() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new HashSet<>();
        privileges.add(new Privilege(PrivilegeType.ALL));
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
        Core.getInstance().ConfigManager().initConfig("test");
        Core.getInstance().UserManager().addUser(u, p, privileges);
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionAddUser(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        a.run();
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().revokePrivilege(PrivilegeType.ALL);
        assertThrows(IActionInsufficientPrivilegeException.class, a::undo);
    }

    @Test
    void testActionAddUserRunUndoMultiple() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new HashSet<>();
        privileges.add(new Privilege(PrivilegeType.ALL));
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
        Core.getInstance().ConfigManager().initConfig("test");
        Core.getInstance().UserManager().addUser(u, p, privileges);
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionAddUser(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        IUser ref = (IUser) a.run();
        for (int n = (int) Math.floor(Math.random() * 10) + 1; n-- > 0; ) {
            assertDoesNotThrow(a::undo);
            assertFalse(Core.getInstance().UserManager().getUsers().contains(ref));
            assertDoesNotThrow(a::run);
            assertTrue(Core.getInstance().UserManager().getUsers().contains(ref));
        }
    }
}
