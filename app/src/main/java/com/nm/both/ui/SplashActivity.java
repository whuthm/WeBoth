package com.nm.both.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nm.base.app.BaseActivity;
import com.nm.both.R;

/**
 * Created by huangming on 2016/10/26.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBarHelper.setHomeButtonEnabled(false);
    }
}
