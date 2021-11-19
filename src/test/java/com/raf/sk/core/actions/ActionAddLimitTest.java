package com.raf.sk.core.actions;

import com.raf.sk.core.core.Core;
import com.raf.sk.core.io.IODriverTest;
import com.raf.sk.core.user.PrivilegeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ActionAddLimitTest {

    @Test
    void testActionAddLimitRun() {
        try {
            Class.forName("com.raf.sk.core.io.IODriverTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new ActionInitConfig(null).run();
        new ActionInitUser(IODriverTest.MASTER_USERNAME, IODriverTest.MASTER_PASSWORD).run();
        //noinspection ConstantConditions
        Core.getInstance().UserManager().getUser().grantPrivilege(PrivilegeType.ALL);
        new ActionInitStorage().run();
        IAction a = new ActionAddLimit("/", "MAX_SIZE", 21214214L);
        assertDoesNotThrow(a::run);
    }
}
