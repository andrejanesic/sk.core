package actions;

import core.Core;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;
import user.IPrivilege;
import user.Privilege;
import user.PrivilegeType;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ActionInitUserTest {

    @Test
    void testActionInitUserRun() {
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
        IAction a = new ActionInitUser(u, p);
        assertDoesNotThrow(a::run);
        assertNotNull(Core.getInstance().UserManager().getUser());
        assertEquals(Core.getInstance().UserManager().getUser().getUsername(), u);
        assertEquals(Core.getInstance().UserManager().getUser().getPassword(), p);
    }

    @Test
    void testActionInitUserRunMultiple() {
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
        IAction a = new ActionInitUser(u, p);
        for (int i = (int) Math.floor(Math.random() * 10); i-- > 0; ) {
            assertDoesNotThrow(a::run);
            assertNotNull(Core.getInstance().UserManager().getUser());
            assertEquals(Core.getInstance().UserManager().getUser().getUsername(), u);
            assertEquals(Core.getInstance().UserManager().getUser().getPassword(), p);
        }
    }

    @Test
    void testActionInitUserUndo() {
        String u = UUID.randomUUID().toString();
        String p = UUID.randomUUID().toString();
        Collection<IPrivilege> privileges = new HashSet<>();
        privileges.add(new Privilege(PrivilegeType.ALL));
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
        Core.getInstance().ConfigManager().initConfig("test");
        Core.getInstance().UserManager().addUser(u, p, privileges);
        IAction a = new ActionInitUser(u, p);
        assertDoesNotThrow(a::undo);
        assertNotNull(Core.getInstance().UserManager().getUser());
        assertNotEquals(Core.getInstance().UserManager().getUser().getUsername(), u);
    }

    @Test
    void testActionInitUserUndoAfterRun() {
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
        IAction a = new ActionInitUser(u, p);
        assertDoesNotThrow(a::run);
        assertNotNull(Core.getInstance().UserManager().getUser());
        assertEquals(Core.getInstance().UserManager().getUser().getUsername(), u);
        assertEquals(Core.getInstance().UserManager().getUser().getPassword(), p);
        assertDoesNotThrow(a::undo);
        assertNotNull(Core.getInstance().UserManager().getUser());
        assertNotEquals(Core.getInstance().UserManager().getUser().getUsername(), u);
        assertNotEquals(Core.getInstance().UserManager().getUser().getPassword(), p);
    }

    @Test
    void testActionInitUserUndoMultiple() {
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
        IAction a = new ActionInitUser(u, p);
        for (int i = (int) Math.floor(Math.random() * 10); i-- > 0; ) {
            assertDoesNotThrow(a::undo);
            assertNotNull(Core.getInstance().UserManager().getUser());
            assertNotEquals(Core.getInstance().UserManager().getUser().getUsername(), u);
        }
    }
}
