package com.nm.base.crash;

import com.nm.base.log.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框
 * 实现该接口并注册为程序中的默认未捕获异常处理 这样当未捕获异常发生时，就可以做些异常处理操作 例如：收集异常信息，发送错误报告 等。
 *
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 */
public enum GlobalCrashHandler implements UncaughtExceptionHandler {

    INSTANCE;

    /** 系统默认的UncaughtException处理类 */
    private UncaughtExceptionHandler mDefaultHandler;

    private GlobalCrashListener mCrashLister;

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
        else {
            // Sleep一会后结束程序
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            ex.printStackTrace();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public interface GlobalCrashListener {
        void onCrashHappened();
    }

    public void setCrashListener(GlobalCrashListener listener) {
        mCrashLister = listener;
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        Logger.e("Crash", "", ex);
        if (mCrashLister != null) {
            mCrashLister.onCrashHappened();
        }
        return false;
    }
}
