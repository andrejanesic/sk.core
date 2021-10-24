package repository;

import implementation.IOHandler;
import io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import repository.dummynode.DummyNode;

public class TestPrepare {

    @BeforeAll
    static void setHandler() {
        IOManager.setInstance(new IOHandler());
    }

    @BeforeEach
    void clearDummies() {
        if (Directory.getRoot() == null) Directory.makeRoot();
        DummyNode.pool.clear();
        DummyNode.poolDirs.clear();
        DummyNode.poolFiles.clear();
    }
}
