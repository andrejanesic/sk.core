package user;

import org.junit.jupiter.api.Test;
import user.builder.PrivilegeBuilder;
import user.builder.PrivilegeTypeBuilder;
import user.builder.UserBuilder;

import java.util.Collection;
import java.util.HashSet;

public class UserTest extends PrepareUserTest {

    @Test
    void testConstructor() {
        new User("foo", "bar");
    }

    @Test
    void testConstructorBuilder() {
        Collection<PrivilegeBuilder> privilegeBuilders = new HashSet<>();
        privilegeBuilders.add(new PrivilegeBuilder(new Object(), PrivilegeTypeBuilder.ALL));
        privilegeBuilders.add(new PrivilegeBuilder(PrivilegeTypeBuilder.ALL));
        new User(new UserBuilder("foo", "bar", privilegeBuilders, true));
    }
}
