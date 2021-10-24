package actions;

import io.IOHandlerTest;
import io.IOManager;
import loader.Loader;
import org.junit.jupiter.api.BeforeAll;

public class ActionsPrepareTest {

    @BeforeAll
    static void setHandler() {
        IOManager.setIOHandler(new IOHandlerTest());
    }
}
