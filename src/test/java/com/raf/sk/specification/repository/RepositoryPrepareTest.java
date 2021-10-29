package com.raf.sk.specification.repository;

import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.dummynode.DummyNode;
import com.raf.sk.specification.exceptions.IStorageManagerINodeBuilderTreeInvalidException;
import com.raf.sk.specification.io.IODriverTest;
import com.raf.sk.specification.io.IOManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class RepositoryPrepareTest {

    @BeforeAll
    static void setHandler() throws IStorageManagerINodeBuilderTreeInvalidException {
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
