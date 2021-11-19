package com.raf.sk.core.repository;

import com.raf.sk.core.core.Core;
import com.raf.sk.core.dummynode.DummyNode;
import com.raf.sk.core.exceptions.IStorageManagerINodeBuilderTreeInvalidException;
import com.raf.sk.core.io.IODriverTest;
import com.raf.sk.specification.io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class RepositoryPrepareTest {

    @BeforeAll
    static void setHandler() throws IStorageManagerINodeBuilderTreeInvalidException {
        IOManager.setIODriver(new IODriverTest());
        Core.getInstance().StorageManager().initStorage();
    }

    @BeforeEach
    void clearDummies() {
        DummyNode.pool.clear();
        DummyNode.poolDirs.clear();
        DummyNode.poolFiles.clear();
    }
}
