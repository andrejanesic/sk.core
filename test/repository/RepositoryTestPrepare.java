package repository;

import dummynode.DummyNode;
import io.IOHandlerTest;
import io.IOManager;
import loader.Loader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class RepositoryTestPrepare {

    @BeforeAll
    static void setHandler() {
        IOManager.setIOHandler(new IOHandlerTest());
        Loader.getInstance("test").initRoot();
    }

    @BeforeEach
    void clearDummies() {
        DummyNode.pool.clear();
        DummyNode.poolDirs.clear();
        DummyNode.poolFiles.clear();
    }
}
