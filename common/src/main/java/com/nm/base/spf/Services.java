package com.nm.base.spf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nm.common.log.Logger;

/**
 * Created by huangming on 2016/10/3.
 */

public class Services {

    private static final String TAG = "Services";

    private Services() {
    }

    private static final Map<String, Provider> PROVIDERS = new ConcurrentHashMap<>();

    public static void registerProvider(String name, Provider p) {
        PROVIDERS.put(name, p);
        Logger.i(TAG, "register provider with name: " + name);
    }

    public static Service getService(String name) {
        Provider provider = PROVIDERS.get(name);
        if (provider == null) {
            throw new IllegalArgumentException("No provider registered with name: " + name);
        }
        return provider.getService();
    }


}
