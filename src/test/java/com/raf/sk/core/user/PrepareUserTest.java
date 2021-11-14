package com.raf.sk.core.user;

import com.raf.sk.core.io.IODriverTest;
import com.raf.sk.specification.io.IOManager;
import org.junit.jupiter.api.BeforeAll;

public class PrepareUserTest {

    @BeforeAll
    static void setHandler() {
        IOManager.setIODriver(new IODriverTest());
    }
}
