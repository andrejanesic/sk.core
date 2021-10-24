package user;

import io.IOHandlerTest;
import io.IOManager;
import loader.Loader;
import org.junit.jupiter.api.BeforeAll;

public class PrepareUserTest {

    @BeforeAll
    static void setHandler() {
        IOManager.setIOHandler(new IOHandlerTest());
    }
}
