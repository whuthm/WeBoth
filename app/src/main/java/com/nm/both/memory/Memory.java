package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/26.
 *
 * 实体
 */

public class Memory {

    private final String id;

    private long createdTime;
    private long updatedTime;

    private MemoryContent content;

    public Memory(String id) {
        this.id = id;
    }

    public MemoryContent getContent() {
        return content;
    }

    public void setContent(MemoryContent content) {
        this.content = content;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

}
