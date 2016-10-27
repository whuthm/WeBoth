package com.nm.base.spf;

import android.content.Context;

/**
 * Created by huangming on 2016/10/13.
 */

public interface ContextService extends Service {
    
    void init(Context context);
    
    void destroy();
    
}
