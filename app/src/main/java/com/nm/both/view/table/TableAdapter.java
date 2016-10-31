package com.nm.both.view.table;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangming on 2016/10/31.
 */

public abstract class TableAdapter {

    public abstract int getItemCount();

    public abstract int getHeaderCount();

    public abstract View getHeaderUnitView(int headerPosition, ViewGroup parent);

    public abstract View getItemUnitView(int itemPosition, ViewGroup parent);

}
