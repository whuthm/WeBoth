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
    
    public enum NetType {
        NONE, UNKNOWN, ETHERNET, WIFI, G_UNKNOWN, G2, G3, G4
    }
    
    /**
     * Returns whether the network is available
     */
    public static boolean isNetworkAvailable(Context context) {
        return getConnectedNetworkInfo(context) != null;
    }
    
    /**
     * 获取网络类型
     */
    public static int getNetworkType(Context context) {
        NetworkInfo networkInfo = getConnectedNetworkInfo(context);
        if (networkInfo != null) {
            return networkInfo.getType();
        }
        
        return -1;
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
    
    public static boolean isMobileConnected(Context context) {
        return (ConnectivityManager.TYPE_MOBILE == getNetworkType(context));
    }
    
    public static boolean isWifiConnected(Context context) {
        return (ConnectivityManager.TYPE_WIFI == getNetworkType(context));
    }
    
    public static NetType getNetType(Context context) {
        
        if (!isNetworkAvailable(context))
            return NetType.NONE;
        
        if (isWifiConnected(context)) {
            return NetType.WIFI;
        }
        else {
            NetworkInfo networkInfo = getConnectedNetworkInfo(context);
            if (networkInfo == null) {
                return NetType.UNKNOWN;
            }
            int nType = networkInfo.getType();
            
            switch (nType) {
                case ConnectivityManager.TYPE_WIFI:
                    return NetType.WIFI;
                case ConnectivityManager.TYPE_ETHERNET:
                    return NetType.ETHERNET;
                case ConnectivityManager.TYPE_MOBILE:
                    int subType = networkInfo.getSubtype();
                    switch (subType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NetType.G2;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NetType.G3;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NetType.G4;
                        default:
                            // 三种3G制式
                            String subTypeName = networkInfo.getSubtypeName();
                            if (subTypeName.equalsIgnoreCase("TD-SCDMA")
                                    || subTypeName.equalsIgnoreCase("WCDMA")
                                    || subTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NetType.G3;
                            }
                            else {
                                return NetType.UNKNOWN;
                            }
                    }
                default:
                    return NetType.UNKNOWN;
            }
        }
    }
    
    /**
     * 获得本机的wifi MAC地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifiMan = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMan != null ? wifiMan.getConnectionInfo() : null;
        return info != null ? info.getMacAddress() : null;
    }
    
    public static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                int ipAddressInt = wifiInfo.getIpAddress();
                return intToIpAddress(ipAddressInt);
            }
        }
        return "";
    }
    
    private static String intToIpAddress(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
                + (i >> 24 & 0xFF);
    }
    
}
