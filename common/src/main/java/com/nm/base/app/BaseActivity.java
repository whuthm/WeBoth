package com.nm.base.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nm.base.R;
import com.nm.base.log.Logger;

import butterknife.ButterKnife;

/**
 * Created by huangming on 2016/9/25. 必须使用主题BaseTheme
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private Toolbar mToolbar;

    private ViewGroup mScreenContainer;

    protected FragmentHelper mFragHelper;

    protected ActionBarHelper mActionBarHelper;

    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
        ActivityStack.push(this);
        // layout with tool bar
        setContentViewInternal(R.layout.activity_base);
        initToolbar();
        // layout with real content layout
        setContentView(getLayoutResId());
        mFragHelper = new FragmentHelper();
        mFragHelper.onAttach(this);

        mActionBarHelper = new ActionBarHelper();
        mActionBarHelper.onAttach(this);

        ButterKnife.bind(this);

        initView();
    }

    protected int getLayoutResId() {
        return R.layout.activity_layout_default;
    }

    @CallSuper
    protected void initView() {
    }

    protected void showLoadingDialog(String title, String message) {
        mLoadingDialog = ProgressDialog.show(this, title, message);
    }

    protected void showLoadingDialog(int titleResId, int msgResId) {
        mLoadingDialog = ProgressDialog.show(this, getString(titleResId),
                getString(msgResId));
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 调用在setContentViewInternal之后
     */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.base_tool_bar);
        mScreenContainer = (ViewGroup) findViewById(R.id.base_screen_container);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            setTitle(getTitle());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if (mToolbar != null) {
            mToolbar.setTitle(titleId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setContentViewInternal(int layoutResId) {
        super.setContentView(layoutResId);
    }

    @Override
    public void setContentView(View view) {
        ensureOnlyToolbar();
        mScreenContainer.addView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        ensureOnlyToolbar();
        mScreenContainer.addView(
                LayoutInflater.from(this).inflate(layoutResID, mScreenContainer, false));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        ensureOnlyToolbar();
        mScreenContainer.addView(view, params);
    }

    private void ensureOnlyToolbar() {
        int count = mScreenContainer.getChildCount();
        int index = 0;
        while (index < count) {
            View child = mScreenContainer.getChildAt(index);
            if (child == mToolbar) {
                index++;
            } else {
                mScreenContainer.removeView(child);
                count--;
            }

        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.i(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Logger.i(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.i(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.i(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy");
        ActivityStack.pop(this);
        mFragHelper.onDetach();
        mActionBarHelper.onDetach();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        if (!mFragHelper.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
