package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/27.
 */

public class NormalFileBody extends FileBody {

    NormalFileBody(String url) {
        super(url);
    }

    @Override
    String getNodeTag() {
        return "file";
    }

}
