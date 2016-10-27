package com.nm.base.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by huangming on 2016/10/16.
 */

public class ToastWrapper {

    private static Context sContext;

    private ToastWrapper() {

    }

    public static void init(Context context) {
        sContext = context;
    }

    public static void show(String text) {
        Toast.makeText(sContext, text, Toast.LENGTH_SHORT).show();
    }

    public static void show(int resId) {
        Toast.makeText(sContext, resId, Toast.LENGTH_SHORT).show();
    }
}
