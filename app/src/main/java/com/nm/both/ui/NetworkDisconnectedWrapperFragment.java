package com.nm.both.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.nm.both.ServiceFactory;
import com.nm.both.network.NetworkService;

/**
 * Created by huangming on 2016/10/31.
 */

public class NetworkDisconnectedWrapperFragment extends WrapperFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ServiceFactory.getNetworkService().registerConnectionListener(mConnectionListener);
    }

    private NetworkService.ConnectionListener mConnectionListener = new NetworkService.ConnectionListener() {
        @Override
        public void onConnected() {

        }

        @Override
        public void onDisconnected() {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ServiceFactory.getNetworkService().unregisterConnectionListener(mConnectionListener);
    }
}
