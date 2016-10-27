package com.nm.base.app;

import java.lang.ref.WeakReference;
import java.util.Stack;

import android.app.Activity;

public final class ActivityStack {
    
    private static Stack<WeakReference<Activity>> activityStack = new Stack<>();
    
    /**
     * 指定项出栈
     */
    public static void pop(Activity activity) {
        if (activity == null) {
            return;
        }
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            WeakReference<Activity> reference = activityStack.get(i);
            Activity other = reference.get();
            if (other == null) {
                activityStack.remove(reference);
                size--;
            }
            else {
                if (other == activity) {
                    activityStack.remove(reference);
                    break;
                }
            }
        }
    }
    
    /**
     * 新项入栈
     */
    public static void push(Activity activity) {
        activityStack.add(new WeakReference<>(activity));
    }
    
    /**
     * 所有项出栈
     */
    public static void popAll() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (activityStack.get(i) != null) {
                Activity activity = activityStack.get(i).get();
                if (activity != null) {
                    activity.finish();
                }
            }
        }
        activityStack.clear();
    }
}
