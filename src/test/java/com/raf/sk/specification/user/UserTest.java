package com.raf.sk.specification.user;

import com.raf.sk.specification.user.builder.PrivilegeBuilder;
import com.raf.sk.specification.user.builder.PrivilegeTypeBuilder;
import com.raf.sk.specification.user.builder.UserBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest extends PrepareUserTest {

    @Test
    void testConstructor() {
        assertDoesNotThrow((ThrowingSupplier<User>) User::new);
        assertDoesNotThrow(() -> new User("foo", "bar"));

        IUser t1 = new User();
        assertNull(t1.getUsername());
        assertNull(t1.getPassword());
        assertTrue(t1.getPrivileges().contains(new Privilege(PrivilegeType.USER_LOGIN)) &&
                t1.getPrivileges().contains(new Privilege(PrivilegeType.USER_LOGOUT)));

        String t2username = UUID.randomUUID().toString();
        String t2password = UUID.randomUUID().toString();
        IUser t2 = new User(t2username, t2password);
        assertEquals(t2username, t2.getUsername());
        assertEquals(t2password, t2.getPassword());
        assertTrue(t2.getPrivileges().contains(new Privilege(PrivilegeType.USER_LOGIN)) &&
                t2.getPrivileges().contains(new Privilege(PrivilegeType.USER_LOGOUT)));
    }

    @Test
    void testConstructorBuilder() {
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
        for (UserBuilder userBuilder : userBuilders)
            assertDoesNotThrow(() -> new User(userBuilder));
    }
}
