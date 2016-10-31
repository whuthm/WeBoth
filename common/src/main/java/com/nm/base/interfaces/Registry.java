package com.nm.base.interfaces;

/**
 * Created by huangming on 2016/10/31.
 */

public interface Registry<T> {

    void register(T t);

    void unregister(T t);

    void unregisterAll();

}
