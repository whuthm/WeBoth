package com.nm.base.app;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by huangming on 2016/10/1.
 */

public abstract class PresenterFragment<PRESENTER extends Presenter, VIEW extends Presenter.View>
        extends BaseFragment
        implements PresenterContext<PRESENTER, VIEW> {

    private PRESENTER presenter;
    private VIEW presenterView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        getPresenter().onAttach();
    }

    public PRESENTER getPresenter() {
        return presenter;
    }

    public VIEW getPresenterView() {
        return presenterView;
    }

    @Override
    @CallSuper
    public void initPresenter() {
        presenter = createPresenter();
        presenterView = createPresenterView();
        presenter.setView(presenterView);
        getPresenter().onAttach();
    }
}
