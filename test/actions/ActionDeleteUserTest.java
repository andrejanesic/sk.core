package actions;

import core.Core;
import exceptions.IActionUndoImpossibleException;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import user.IPrivilege;
import user.Privilege;
import user.PrivilegeType;
import user.builder.UserBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ActionDeleteUserTest {

    @Test
    void testActionDeleteUserRun() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        String u2 = UUID.randomUUID().toString();
        String p2 = UUID.randomUUID().toString();
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
        Core.getInstance().UserManager().addUser(u2, p2, privileges);
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().ConfigManager().getConfig().getUsers().contains(
                new UserBuilder(u, p, null)));
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().ConfigManager().getConfig().getUsers().contains(
                new UserBuilder(u2, p2, null)));
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionDeleteUser(u2);
        assertDoesNotThrow(a::run);
        assertNotNull(Core.getInstance().UserManager().getUser());
        //noinspection ConstantConditions
        assertFalse(Core.getInstance().ConfigManager().getConfig().getUsers().contains(
                new UserBuilder(u2, p, null)));
    }

    @Test
    void testActionDeleteUserRunMultiple() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        String u2 = UUID.randomUUID().toString();
        String p2 = UUID.randomUUID().toString();
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
        Core.getInstance().UserManager().addUser(u2, p2, privileges);
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().ConfigManager().getConfig().getUsers().contains(
                new UserBuilder(u, p, null)));
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().ConfigManager().getConfig().getUsers().contains(
                new UserBuilder(u2, p2, null)));
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionDeleteUser(u2);
        for (int i = (int) Math.floor(Math.random() * 10) + 1; i-- > 0; ) {
            assertDoesNotThrow(a::run);
            assertNotNull(Core.getInstance().UserManager().getUser());
            //noinspection ConstantConditions
            assertFalse(Core.getInstance().ConfigManager().getConfig().getUsers().contains(
                    new UserBuilder(u2, p, null)));
        }
    }

    @Test
    void testActionDeleteUserUndo() {
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
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().ConfigManager().getConfig().getUsers().contains(
                new UserBuilder(u, p, null)));
        Core.getInstance().UserManager().initUser(u, p);
        IAction a = new ActionDeleteUser(u);
        assertThrows(IActionUndoImpossibleException.class, a::undo);
        assertNotNull(Core.getInstance().UserManager().getUser());
    }
}
