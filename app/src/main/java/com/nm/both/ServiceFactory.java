package com.nm.both;

import com.nm.base.log.Logger;
import com.nm.base.spf.Service;
import com.nm.base.spf.Services;
import com.nm.both.network.NetworkService;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by huangming on 2016/10/31.
 */

public class ServiceFactory {
    
    private static final Set<String> PROVIDER_CLASS_NAMES = new LinkedHashSet<>();
    
    static {
        PROVIDER_CLASS_NAMES
                .add("com.yoisee.ins.core.wifi.direct.udp.UDPDirectClientProvider");
    }
    
    private ServiceFactory() {}
    
    public static void loadServices() {
        Iterator<String> iterator = PROVIDER_CLASS_NAMES.iterator();
        while (iterator.hasNext()) {
            String providerClassName = iterator.next();
            try {
                Class.forName(providerClassName);
            } catch (ClassNotFoundException e) {
                Logger.e("ServicesLoadConfig",
                        "class(" + providerClassName + ") not found");
            }
        }
    }
    
    private static Service getService(String name) {
        return Services.getService(name);
    }
    
    public static NetworkService getNetworkService() {
        return null;
    }
    
}
