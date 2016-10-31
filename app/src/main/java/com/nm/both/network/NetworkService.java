package com.nm.both.network;

import com.nm.base.spf.ContextService;

/**
 * Created by huangming on 2016/10/31.
 */

public interface NetworkService extends ContextService {

    boolean isConnected();
    
    void registerConnectionListener(ConnectionListener l);
    
    void unregisterConnectionListener(ConnectionListener l);
    
    interface ConnectionListener {
        void onConnected();
        
        void onDisconnected();
    }
    
}
