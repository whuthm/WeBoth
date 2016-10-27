package com.nm.base.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;

/**
 * Created by huangming on 2016/10/3.
 */

public class AppUtils {

    private AppUtils() {

    }

    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am
                .getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static boolean isMainProcess(Context context, String packageName) {
        boolean result = true;
        String processName = getProcessName(context, android.os.Process.myPid());
        if (processName != null) {
            result = processName.equals(packageName);
        }

        return result;
    }

    public static boolean isMainProcess(Context context) {
        return isMainProcess(context, context.getPackageName());
    }

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * 获取app是否在前台，兼容5.0+
     */
    public static boolean isAppForeground(Context context, String packageName) {
        boolean isForeground = false;
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.RunningAppProcessInfo currentInfo = getRunningAppInfoAfterL(context);
            if (currentInfo != null && packageName.equals(currentInfo.processName)) {
                isForeground = true;
            }
        } else {
            isForeground = getForegroundPackagesPreL(context, packageName);
        }

        return isForeground;
    }

    /**
     * 5.0以下版本获取前台应用
     */
    private static boolean getForegroundPackagesPreL(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(packageName)) {
            return true;
        }
        return false;
    }

    /**
     * 5.0以上版本获取前台应用
     */
    private static ActivityManager.RunningAppProcessInfo getRunningAppInfoAfterL(Context context) {
        final int PROCESS_STATE_TOP = 2;
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        try {
            field = ActivityManager.RunningAppProcessInfo.class
                    .getDeclaredField("processState");
        } catch (Exception ignored) {
        }
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo app : appList) {
            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && app.importanceReasonCode == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
                Integer state = null;
                try {
                    state = field.getInt(app);
                } catch (Exception e) {
                }
                if (state != null && state == PROCESS_STATE_TOP) {
                    currentInfo = app;
                    break;
                }
            }
        }
        return currentInfo;
    }


    /**
     * 获取版本名称
     */
    public static String getVersionName(Context context, String packageName) {
        PackageInfo packInfo = getPackageInfo(context, packageName);
        if (packInfo != null) {
            return packInfo.versionName;
        }
        return "";
    }

    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context, String packageName) {
        PackageInfo packInfo = getPackageInfo(context, packageName);
        if (packInfo != null) {
            return packInfo.versionCode;
        } else
            return 0;
    }

    /**
     * 获取 应用名称
     */
    public static String getLabel(Context context, String packageName) {
        PackageInfo packInfo = getPackageInfo(context, packageName);
        if (packInfo != null) {
            return packInfo.applicationInfo.loadLabel(context.getPackageManager())
                    .toString();
        }
        return "";
    }

    /**
     * 根据包名直接返回PackageInfo
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据apk文件获取包名
     */
    public static String getPackageNameByApkFile(Context context, String apkFileName) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = packageManager.getPackageArchiveInfo(apkFileName,
                PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo;
        if (info != null) {
            appInfo = info.applicationInfo;
            return appInfo != null ? appInfo.packageName : null;
        }
        return null;
    }

    public static File getPackageSourceFile(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        if (packageInfo != null) {
            return new File(packageInfo.applicationInfo.publicSourceDir);
        }

        return null;
    }

    public static long getPackageSourceFileSize(Context context, String packageName) {
        File sourceFile = getPackageSourceFile(context, packageName);
        if (sourceFile != null) {
            return sourceFile.length();
        }

        return 0;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo = getPackageInfo(context, packageName);
        return packageInfo != null;
    }

    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = info.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static ArrayList<ResolveInfo> findLauncherActivities(Context context, String packageName) {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);

        return findActivities(context, mainIntent);
    }

    public static ArrayList<ResolveInfo> findActivities(Context context, Intent searchIntent) {
        return findActivities(context, searchIntent, false);
    }

    public static ArrayList<ResolveInfo> findActivities(Context context, Intent searchIntent,
                                                        boolean isDefault) {
        ArrayList<ResolveInfo> matches = new ArrayList<>();
        final PackageManager packageManager = context.getPackageManager();
        matches.addAll(packageManager.queryIntentActivities(searchIntent,
                isDefault ? PackageManager.MATCH_DEFAULT_ONLY : 0));
        return matches;
    }

    public static boolean hasActivity(Context context, Intent searchIntent, boolean isDefault) {
        ArrayList<ResolveInfo> retList = findActivities(context, searchIntent, isDefault);
        return retList != null && !retList.isEmpty();
    }

    /**
     * 判断是否是debug包
     */
    public static boolean isApkDebuggable(Context context) {
        return isApkDebuggable(context, context.getPackageName());
    }

    /**
     * 判断是否是debug包
     */
    public static boolean isApkDebuggable(Context context, String packageName) {
        try {
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null) {
                ApplicationInfo info = pkgInfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
