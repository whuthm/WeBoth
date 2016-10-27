package com.nm.base.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

/**
 * Created by huangming on 2016/10/1.
 */

public abstract class PresenterActivity<PRESENTER extends Presenter, VIEW extends Presenter.View>
        extends BaseActivity
        implements PresenterContext<PRESENTER, VIEW> {

    private PRESENTER presenter;
    private VIEW presenterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

}
