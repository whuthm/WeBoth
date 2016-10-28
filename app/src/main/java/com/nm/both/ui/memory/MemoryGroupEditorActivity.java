package com.nm.both.ui.memory;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nm.base.app.BaseActivity;

/**
 * Created by huangming on 2016/10/28.
 */

public class MemoryGroupEditorActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragHelper.addFragment(new MemoryGroupEditorFragment());
    }
}
