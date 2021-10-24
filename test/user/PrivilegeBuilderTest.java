package user;

import org.junit.jupiter.api.Test;
import user.builder.PrivilegeBuilder;
import user.builder.PrivilegeTypeBuilder;

public class PrivilegeBuilderTest extends PrepareUserTest {

    @Test
    void testConstructor() {
        new PrivilegeBuilder(PrivilegeTypeBuilder.ALL);
        new PrivilegeBuilder(new Object(), PrivilegeTypeBuilder.ALL);
    }
}
