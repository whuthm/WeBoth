package com.nm.both.view.table;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by huangming on 2016/10/31.
 */

public class TableAdapter extends TableCellAdapter {

    public static final int ORIENTATION_HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int ORIENTATION_VERTICAL = LinearLayout.VERTICAL;

    private final TableCellAdapter mHorizontalHeaderAdapter;
    private final TableCellAdapter mVerticalHeaderAdapter;
    private final TableCellAdapter mDataAdapter;
    
    public TableAdapter(TableCellAdapter horizontalHeaderAdapter,
            TableCellAdapter verticalHeaderAdapter, TableCellAdapter dataAdapter) {
        this.mHorizontalHeaderAdapter = horizontalHeaderAdapter;
        this.mVerticalHeaderAdapter = verticalHeaderAdapter;
        this.mDataAdapter = dataAdapter;
    }
    
    TableCellAdapter getHorizontalHeaderAdapter() {
        return mHorizontalHeaderAdapter;
    }
    
    TableCellAdapter getVerticalHeaderAdapter() {
        return mVerticalHeaderAdapter;
    }
    
    TableCellAdapter getDataAdapter() {
        return mDataAdapter;
    }

    public int getOrientation() {
        return ORIENTATION_HORIZONTAL;
    }

    @Override
    public int getCountX() {
        return 0;
    }

    @Override
    public int getCountY() {
        return 0;
    }

    @Override
    public View getView(int cellX, int cellY, ViewGroup parent) {
        return null;
    }
}
