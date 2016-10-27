package com.nm.base.util;

import java.util.Arrays;

/**
 * Created by huangming on 2016/10/19.
 */
public class Objects {

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static int hashCode(Object o) {
        return (o == null) ? 0 : o.hashCode();
    }

    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

}
