package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/28.
 */

public final class MemoryGroup {
    
    private final int id;
    private final String name;
    
    public MemoryGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}
