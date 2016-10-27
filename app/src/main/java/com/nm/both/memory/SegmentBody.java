package com.nm.both.memory;

/**
 * Created by huangming on 2016/10/26.
 */

public abstract class SegmentBody {

    public abstract int getContentLength();


    public abstract void writeTo(StringBuilder xml);

}
