package user;

import io.IODriverTest;
import io.IOManager;
import org.junit.jupiter.api.BeforeAll;

public class PrepareUserTest {

    @BeforeAll
    static void setHandler() {
        IOManager.setIODriver(new IODriverTest());
    }
}
