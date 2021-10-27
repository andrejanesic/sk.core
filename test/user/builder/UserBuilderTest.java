package user.builder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import core.Core;
import io.IODriver;
import io.IOManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import repository.builder.DirectoryBuilder;
import repository.builder.FileBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserBuilderTest {

    @Test
    void testUserToJson() {
        Collection<PrivilegeBuilder> privilegeBuilders = new ArrayList<>();
        int p = (int) Math.floor(Math.random() * 5);
        int q = (int) Math.floor(Math.random() * PrivilegeTypeBuilder.values().length);

        // generiši privilegije
        while (p-- > 0) {
            privilegeBuilders.add(new PrivilegeBuilder(
                    Math.random() * 10 > 5 ? new Object() : null,
                    PrivilegeTypeBuilder.values()[q]));
        }
        UserBuilder ub = new UserBuilder(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                privilegeBuilders
        );

        Gson gson = new Gson();
        assertDoesNotThrow(() -> gson.toJson(ub));
        assertDoesNotThrow(() -> gson.fromJson(gson.toJson(ub), UserBuilder.class));
        assertEquals(ub, gson.fromJson(gson.toJson(ub), UserBuilder.class));
    }

    @Test
    void testUserCollectionToJson() {
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
                Gson gson = new Gson();
                return gson.toJson(userBuilders, listType);
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
        assertEquals(userBuilders, gson.fromJson(
                Core.getInstance().IODriver().readConfig(""),
                listType));
        List<UserBuilder> loaded = gson.fromJson(Core.getInstance().IODriver().readConfig(""), listType);

        // proveri svaki get jer UserBuilder ima custom equals metod
        for (int i = 0; i < userBuilders.size(); i++) {
            UserBuilder a = userBuilders.get(i), b = loaded.get(i);
            assertEquals(a.getUsername(), b.getUsername());
            assertEquals(a.getPassword(), b.getPassword());
            assertEquals(a.getPrivileges(), b.getPrivileges());
        }
    }
}
