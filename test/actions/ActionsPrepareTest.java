package actions;

import io.IODriverTest;
import io.IOManager;
import org.junit.jupiter.api.BeforeAll;

public class ActionsPrepareTest {

    @BeforeAll
    static void setHandler() {
        IOManager.setIODriver(new IODriverTest());
    }
}
