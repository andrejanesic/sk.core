package loader;

import io.IOHandlerTest;
import io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.Directory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoaderTest {

    @BeforeAll
    static void setHandler() {
        IOManager.setIOHandler(new IOHandlerTest());
    }

    @Test
    void testBuildRoot() {
        Loader.getInstance("test").buildRoot();
        assertDoesNotThrow(() -> Loader.getInstance().getRoot().getPath());
        assertEquals(Directory.ROOT_DIRECTORY, Loader.getInstance().getRoot().getPath());
    }
}
