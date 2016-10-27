package com.nm.base.util;

import android.content.Context;

/**
 * Created by huangming on 2016/10/4.
 */

public class DeviceUtils {

    /**
     * 获取device id
     */
    public static String getDeviceId(Context context) {
        return "11-22-33-44-55";
//        String value = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//        return value != null ? value : "";
    }

    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    public static String getDeviceName(Context context) {
        return getDeviceModel();
    }

}
