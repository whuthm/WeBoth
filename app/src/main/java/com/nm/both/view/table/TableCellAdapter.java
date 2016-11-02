package com.nm.both.view.table;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangming on 2016/11/2.
 */

public abstract class TableCellAdapter  extends TableBaseAdapter {

    public abstract int getCountX();

    public abstract int getCountY();

    public abstract View getView(int cellX, int cellY, ViewGroup parent);

}
