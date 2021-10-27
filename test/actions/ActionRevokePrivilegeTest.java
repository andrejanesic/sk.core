package actions;

import core.Core;
import exceptions.IActionBadParameterException;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import user.PrivilegeType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ActionRevokePrivilegeTest {

    @Test
    void testActionGrantPrivilegeRun() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
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
        Core.getInstance().UserManager().addUser(u, p, null);
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.ALL);

        String privilege = PrivilegeType.values()[
                (int) Math.floor(Math.random() * PrivilegeType.values().length)].toString();
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser(u).grantPrivilege(PrivilegeType.valueOf(privilege));
        IAction a = new ActionGrantPrivilege(u, privilege);
        if (PrivilegeType.valueOf(privilege) == PrivilegeType.USER_LOGIN ||
                PrivilegeType.valueOf(privilege) == PrivilegeType.USER_LOGOUT)
            assertThrows(IActionBadParameterException.class, a::undo);
        else
            assertDoesNotThrow(a::undo);
        assertNotNull(Core.getInstance().UserManager().getUser(u));
        //noinspection ConstantConditions
        if (PrivilegeType.valueOf(privilege) == PrivilegeType.USER_LOGIN ||
                PrivilegeType.valueOf(privilege) == PrivilegeType.USER_LOGOUT)
            assertTrue(Core.getInstance().UserManager().getUser(u).hasPrivilege(PrivilegeType.valueOf(privilege)));
        else
            assertFalse(Core.getInstance().UserManager().getUser(u).hasPrivilege(PrivilegeType.valueOf(privilege)));
    }

    @Test
    void testActionGrantPrivilegeUndo() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
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
        Core.getInstance().UserManager().addUser(u, p, null);
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.ALL);

        String privilege = PrivilegeType.values()[
                (int) Math.floor(Math.random() * PrivilegeType.values().length)].toString();
        IAction a = new ActionGrantPrivilege(u, privilege);
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser(u).hasPrivilege(PrivilegeType.valueOf(privilege)))
            assertThrows(IActionBadParameterException.class, a::run);
        else
            assertDoesNotThrow(a::run);
        assertNotNull(Core.getInstance().UserManager().getUser(u));
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().UserManager().getUser(u).hasPrivilege(PrivilegeType.valueOf(privilege)));
    }

    @Test
    void testActionGrantPrivilegeUndoMultiple() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
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
        Core.getInstance().UserManager().addUser(u, p, null);
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.ALL);

        String privilege = PrivilegeType.values()[
                (int) Math.floor(Math.random() * PrivilegeType.values().length)].toString();
        IAction a = new ActionGrantPrivilege(u, privilege);
        //noinspection ConstantConditions
        if (Core.getInstance().UserManager().getUser(u).hasPrivilege(PrivilegeType.valueOf(privilege)))
            assertThrows(IActionBadParameterException.class, a::run);
        else
            assertDoesNotThrow(a::run);
        assertNotNull(Core.getInstance().UserManager().getUser(u));
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().UserManager().getUser(u).hasPrivilege(PrivilegeType.valueOf(privilege)));
        assertThrows(IActionBadParameterException.class, a::run);
    }
}
