package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/28.
 */

public class MemoryManager {

    private MemoryGroupManager groupManager;
    private MemoryHolder memoryHolder;

    private MemoryManager() {
        groupManager = new MemoryGroupManager();
    }



}
