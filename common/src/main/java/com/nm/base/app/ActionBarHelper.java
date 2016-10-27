package com.nm.base.app;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by whuthm on 2016/3/18.
 */
public class ActionBarHelper {

    private boolean mAttached;

    private ActionBar mActionBar;

    private Toolbar mToolbar;

    public void onAttach(BaseFragment fragment) {
        onAttach((BaseActivity) fragment.getActivity());
    }

    public void onAttach(@NonNull BaseActivity activity) {
        mAttached = true;
        mActionBar = activity.getSupportActionBar();
        mToolbar = activity.getToolbar();
    }

    public void onDetach() {
        mAttached = false;
    }

    public void setHomeButtonEnabled(boolean enabled) {
        if (allowedOp()) {
            mActionBar.setDisplayHomeAsUpEnabled(enabled);
        }
    }

    public void setNavigationIcon(int resId) {
        if (allowedOp()) {
            mActionBar.setHomeAsUpIndicator(resId);
        }
    }

    public void setNavigationIcon(Drawable icon) {
        if (allowedOp()) {
            mActionBar.setHomeAsUpIndicator(icon);
        }
    }

    public void setActionBarVisible(boolean visible) {
        if (mToolbar != null) {
            mToolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

//    public void setNavigationText(int resId) {
//        if (checkCanOp()) {
//            if (mToolbar instanceof MiddleToolbar) {
//                ((MiddleToolbar) mToolbar).setNavigationText(resId);
//            }
//        }
//    }
//
//    public void setNavigationText(CharSequence text) {
//        if (checkCanOp()) {
//            if (mToolbar instanceof MiddleToolbar) {
//                ((MiddleToolbar) mToolbar).setNavigationText(text);
//            }
//        }
//    }
//
//    public void setCustomView(int resId) {
//        if (checkCanOp()) {
//            if (mToolbar instanceof MiddleToolbar) {
//                ((MiddleToolbar) mToolbar).setCustomView(resId);
//            } else {
//                mActionBar.setCustomView(resId);
//            }
//        }
//    }
//
//    public void setCustomView(View customView) {
//        if (checkCanOp()) {
//            if (mToolbar instanceof MiddleToolbar) {
//                ((MiddleToolbar) mToolbar).setCustomView(customView);
//            } else {
//                mActionBar.setCustomView(customView);
//            }
//        }
//    }
//
//    public View getCustomView() {
//        if (checkCanOp()) {
//            if (mToolbar instanceof MiddleToolbar) {
//                return ((MiddleToolbar) mToolbar).getCustomView();
//            } else {
//                return mActionBar.getCustomView();
//            }
//        }
//        return null;
//    }

    public void setTitle(String title) {
        if (allowedOp()) {
            mActionBar.setTitle(title);
        }
    }

    public void setTitle(int resId) {
        if (allowedOp()) {
            mActionBar.setTitle(resId);
        }
    }

    private boolean allowedOp() {
        return mAttached && mActionBar != null;
    }

}
