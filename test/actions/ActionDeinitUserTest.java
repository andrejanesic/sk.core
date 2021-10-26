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

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ActionDeinitUserTest {

    @Test
    void testActionDeinitUserRun() {
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
        assertNotNull(Core.getInstance().UserManager().getUser());
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().UserManager().getUser().isAuthenticated());

        IAction a = new ActionDeinitUser();
        assertDoesNotThrow(a::run);
        assertNull(Core.getInstance().StorageManager().getRoot());
        assertNotNull(Core.getInstance().UserManager().getUser());
        //noinspection ConstantConditions
        assertNull(Core.getInstance().UserManager().getUser().getUsername());
    }

    @Test
    void testActionDeinitUserRunMultiple() {
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
        assertNotNull(Core.getInstance().UserManager().getUser());
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().UserManager().getUser().isAuthenticated());

        IAction a = new ActionDeinitUser();
        for (int i = (int) Math.floor(Math.random() * 10); i-- > 0; ) {
            assertDoesNotThrow(a::run);
            assertNull(Core.getInstance().StorageManager().getRoot());
            assertNotNull(Core.getInstance().UserManager().getUser());
            //noinspection ConstantConditions
            assertNull(Core.getInstance().UserManager().getUser().getUsername());
        }
    }

    @Test
    void testActionDeinitUserUndo() {
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
        assertNotNull(Core.getInstance().UserManager().getUser());
        //noinspection ConstantConditions
        assertTrue(Core.getInstance().UserManager().getUser().isAuthenticated());

        IAction a = new ActionDeinitUser();
        assertThrows(IActionUndoImpossibleException.class, a::undo);
    }
}
