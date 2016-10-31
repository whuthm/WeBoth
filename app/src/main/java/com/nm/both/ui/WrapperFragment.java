package com.nm.both.ui;

import com.nm.base.app.BaseFragment;

/**
 * Created by huangming on 2016/10/31.
 */

public class WrapperFragment extends BaseFragment {

    private BaseFragment mWrappedFragment;

    protected BaseFragment getWrappedFragment() {
        return mWrappedFragment;
    }
}
