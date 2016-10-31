package com.nm.both.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nm.both.ui.WrapperFragment;

/**
 * Created by huangming on 2016/10/31.
 */

public class NotLoggedInWrapperFragment extends WrapperFragment {

    private NotLoggedInFragment mNotLoggedInFragment;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNotLoggedInFragment = new NotLoggedInFragment();

    }

}
