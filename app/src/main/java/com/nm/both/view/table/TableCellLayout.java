package com.nm.both.view.table;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by huangming on 2016/11/2.
 */

class TableCellLayout extends LinearLayout {
    
    private TableCellAdapter mAdapter;
    private DataSetObserver mDataSetObserver;
    
    TableCellLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }
    
    int getCountX() {
        return mAdapter != null ? mAdapter.getCountX() : 0;
    }
    
    int getCountY() {
        return mAdapter != null ? mAdapter.getCountY() : 0;
    }
    
    public void setAdapter(TableCellAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mDataSetObserver = new CellDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        
        resetCellLayout();
    }
    
    private void resetCellLayout() {
        removeAllViewsInLayout();
        if (mAdapter == null) {
            return;
        }
        for (int i = 0; i < getCountX(); i++) {
            LinearLayout cellChildrenLayout = new LinearLayout(getContext());
            for (int j = 0; i < getCountY(); i++) {
                cellChildrenLayout.addView(mAdapter.getView(i, j, cellChildrenLayout));
            }
            addView(cellChildrenLayout, i);
        }
    }
    
    View getChildAt(int cellX, int cellY) {
        View childrenLayout = getChildAt(cellX);
        if (childrenLayout != null && (childrenLayout instanceof LinearLayout)) {
            return ((LinearLayout) childrenLayout).getChildAt(cellY);
        }
        return null;
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new CellDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
    }
    
    private class CellDataSetObserver extends DataSetObserver {
        
        @Override
        public void onChanged() {
            resetCellLayout();
        }
        
        @Override
        public void onInvalidated() {
            resetCellLayout();
        }
        
    }
}
