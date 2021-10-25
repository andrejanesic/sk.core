package repository;

import core.Core;
import dummynode.DummyNode;
import io.IODriverTest;
import io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class RepositoryTestPrepare {

    @BeforeAll
    static void setHandler() {
        IOManager.setIODriver(new IODriverTest());
        Core.getInstance().StorageManager().initStorage("test");
    }

    @BeforeEach
    void clearDummies() {
        DummyNode.pool.clear();
        DummyNode.poolDirs.clear();
        DummyNode.poolFiles.clear();
    }
}
