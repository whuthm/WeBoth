package com.nm.base.app;

/**
 * Created by huangming on 2016/10/1.
 */

public interface PresenterContext<PRESENTER extends Presenter, VIEW extends Presenter.View> {

    PRESENTER getPresenter();

    VIEW getPresenterView();

    PRESENTER createPresenter();

    VIEW createPresenterView();

    void initPresenter();

}
