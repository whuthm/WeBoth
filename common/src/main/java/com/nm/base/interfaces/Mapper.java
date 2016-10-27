package com.nm.base.interfaces;

import com.nm.common.exception.MapperError;

/**
 * Created by huangming on 2016/10/8.
 */

public interface Mapper<SRC, DST> {
    DST map(SRC src) throws MapperError;
}
