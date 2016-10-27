package com.nm.base.app;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nm.base.R;

/**
 * Created by huangming on 2016/10/1.
 */

public class FragmentHelper {

    private boolean mAttached;

    private int mFragViewId = R.id.fragment_layout_default;

    private FragmentManager mFragMgr;

    FragmentHelper() {
    }

    public void onAttach(Fragment fragment) {
        mFragMgr = fragment.getChildFragmentManager();
        mAttached = true;
    }

    public void onAttach(@NonNull BaseActivity activity) {
        mFragMgr = activity.getSupportFragmentManager();
        mAttached = true;
    }


//    public void switchFragment(Fragment fragment, int fragViewId) {
//        if (!fragment.isAdded()) {
//            addFragment(fragment, fragViewId);
//        }
//        FragmentTransaction ft = mFragMgr.beginTransaction();
//        ft.show(fragment);
//        for (Fragment other : mFragMgr.getFragments()) {
//            if (other != null && other != fragment) {
//                ft.hide(other);
//            }
//        }
//        ft.commitAllowingStateLoss();
//    }
//
//    /**
//     * 切换，隐藏其他Fragment
//     */
//    public void switchFragment(Fragment fragment) {
//        switchFragment(fragment, mFragViewId);
//    }

    /**
     * 替换，删除其他Fragment
     */
    public void replaceFragment(Fragment fragment, int fragViewId) {
        FragmentTransaction ft = mFragMgr.beginTransaction();
        ft.replace(fragViewId, fragment);
        ft.commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, mFragViewId);
    }

    public void setFragViewId(int fragViewId) {
        mFragViewId = fragViewId;
    }

    public void addFragment(Fragment fragment, int fragViewId, boolean toBackStack) {
        FragmentTransaction ft = mFragMgr.beginTransaction();
        ft.add(fragViewId, fragment);
        if (toBackStack) {
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment, boolean toBackStack) {
        addFragment(fragment, mFragViewId, toBackStack);
    }

    public void addFragment(Fragment fragment, int fragViewId) {
        addFragment(fragment, fragViewId, false);
    }

    public void addFragment(Fragment fragment) {
        addFragment(fragment, mFragViewId, false);
    }

    public void onDetach() {
        mAttached = false;
    }

    boolean isAttached() {
        return mAttached;
    }

    boolean onBackPressed() {
        int count = mFragMgr.getBackStackEntryCount();
        if (count <= 0) {
            return false;
        }
        mFragMgr.popBackStack();
        return true;
    }

}
