package com.nm.both.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huangming on 2016/10/28.
 */

class MemoryHolder {

    private Map<String, Memory> memories = new ConcurrentHashMap<>();

}
