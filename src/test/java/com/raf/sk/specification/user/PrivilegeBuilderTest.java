package com.raf.sk.specification.user;

import com.raf.sk.specification.user.builder.PrivilegeBuilder;
import com.raf.sk.specification.user.builder.PrivilegeTypeBuilder;
import org.junit.jupiter.api.Test;

public class PrivilegeBuilderTest extends PrepareUserTest {

    @Test
    void testConstructor() {
        new PrivilegeBuilder(PrivilegeTypeBuilder.ALL);
        new PrivilegeBuilder(new Object(), PrivilegeTypeBuilder.ALL);
    }
}
