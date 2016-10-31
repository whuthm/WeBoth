package com.nm.both;

import com.nm.base.app.BaseApplication;

/**
 * Created by huangming on 2016/10/26.
 */

public class App extends BaseApplication {
    
    @Override
    protected void onCreateOnMainThread() {
        super.onCreateOnMainThread();
        ServiceFactory.loadServices();
        ServiceFactory.getNetworkService().init(this);
    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        ServiceFactory.getNetworkService().destroy();
    }
}
