package com.nm.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 网络相关工具类
 */
public final class NetworkUtils {
    
    /**
     * Returns whether the network is available
     */
    public static boolean isNetworkConnected(Context context) {
        return getConnectedNetworkInfo(context) != null;
    }
    
    public static NetworkInfo getConnectedNetworkInfo(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                Log.w("network", "couldn't get connectivity manager");
            }
            else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return anInfo;
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            Log.w("network", e.toString(), e);
        }
        return null;
    }
    
}
