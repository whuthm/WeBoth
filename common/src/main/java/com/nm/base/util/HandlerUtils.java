package com.nm.base.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by huangming on 2016/8/18.
 */
public class HandlerUtils {
    
    private static Handler sUIHandler = new Handler(Looper.getMainLooper());
    
    private HandlerUtils() {}
    
    public static Handler ui() {
        return sUIHandler;
    }
    
    public static void runOnUiThread(final Runnable r) {
        if (Thread.currentThread() == sUIHandler.getLooper().getThread()) {
            r.run();
        }
        else {
            sUIHandler.post(r);
        }
    }
    
    public static void runOnWorkThread(final Handler workHandler, final Runnable r) {
        if (Thread.currentThread() == workHandler.getLooper().getThread()) {
            r.run();
        }
        else {
            workHandler.post(r);
        }
    }
}
