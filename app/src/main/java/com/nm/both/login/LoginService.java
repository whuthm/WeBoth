package com.nm.both.login;

import com.nm.base.interfaces.Callback;

/**
 * Created by huangming on 2016/10/31.
 */

public interface LoginService {
    
    boolean isLoggedIn();
    
    void login(String username, String password, Callback callback);
    
    void logout(Callback callback);
    
    void registerLoginListener(LoginListener l);
    
    void unregisterLoginListener(LoginListener l);
    
    interface LoginListener {
        
        void onLoggedIn();
        
        void onLoggedOut();
        
    }
    
}
