package com.nm.both.login;

import com.nm.base.interfaces.Callback;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by huangming on 2016/10/31.
 */

class LoginManager implements LoginService {

    private Set<LoginListener> loginListeners = new HashSet<>();

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void login(String username, String password, Callback callback) {

    }

    @Override
    public void logout(Callback callback) {
    }

    @Override
    public void registerLoginListener(LoginListener l) {
        loginListeners.add(l);
    }

    @Override
    public void unregisterLoginListener(LoginListener l) {
        loginListeners.remove(l);
    }

    private void notifyLoggedIn() {
        for (LoginListener l : loginListeners) {
            l.onLoggedIn();
        }
    }

    private void notifyLoggedOut() {
        for (LoginListener l : loginListeners) {
            l.onLoggedOut();
        }
    }
}
