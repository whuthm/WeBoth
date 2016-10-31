package com.nm.both.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.nm.base.util.NetworkUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by huangming on 2016/10/31.
 */

class NetworkManger implements NetworkService {
    private Context mContext;
    private boolean mConnected;
    
    private Set<ConnectionListener> mConnectionListeners = new HashSet<>();
    
    @Override
    public boolean isConnected() {
        return mConnected;
    }
    
    private void setConnected(boolean connected) {
        this.mConnected = connected;
    }
    
    private void connectionChanged(boolean connected) {
        boolean preConnected = isConnected();
        if (preConnected != connected) {
            setConnected(connected);
            if (connected) {
                notifyConnected();
            }
            else {
                notifyDisconnected();
            }
        }
    }
    
    @Override
    public void registerConnectionListener(ConnectionListener l) {
        mConnectionListeners.add(l);
    }
    
    @Override
    public void unregisterConnectionListener(ConnectionListener l) {
        mConnectionListeners.remove(l);
    }
    
    private void notifyConnected() {
        for (ConnectionListener l : mConnectionListeners) {
            l.onConnected();
        }
    }
    
    private void notifyDisconnected() {
        for (ConnectionListener l : mConnectionListeners) {
            l.onDisconnected();
        }
    }
    
    @Override
    public void init(Context context) {
        mContext = context;
        setConnected(NetworkUtils.isNetworkConnected(context));
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mNetworkReceiver, filter);
    }
    
    @Override
    public void destroy() {
        mContext.unregisterReceiver(mNetworkReceiver);
    }
    
    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectionChanged(NetworkUtils.isNetworkConnected(context));
        }
    };
}
