package com.nm.both.view.table;

import android.database.DataSetObserver;

/**
 * Created by huangming on 2016/10/31.
 */

public class TableAdapter extends TableBaseAdapter {
    
    private final TableCellAdapter mHorizontalHeaderAdapter;
    private final TableCellAdapter mVerticalHeaderAdapter;
    private final TableCellAdapter mDataAdapter;
    
    public TableAdapter(TableCellAdapter horizontalHeaderAdapter,
            TableCellAdapter verticalHeaderAdapter, TableCellAdapter dataAdapter) {
        this.mHorizontalHeaderAdapter = horizontalHeaderAdapter;
        this.mVerticalHeaderAdapter = verticalHeaderAdapter;
        this.mDataAdapter = dataAdapter;
    }
    
    public TableAdapter(TableCellAdapter dataAdapter) {
        this(null, null, dataAdapter);
    }
    
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        if (mHorizontalHeaderAdapter != null) {
            mHorizontalHeaderAdapter.registerDataSetObserver(observer);
        }
        
        if (mVerticalHeaderAdapter != null) {
            mVerticalHeaderAdapter.registerDataSetObserver(observer);
        }
        
        if (mDataAdapter != null) {
            mDataAdapter.registerDataSetObserver(observer);
        }
        
    }
    
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        
        if (mHorizontalHeaderAdapter != null) {
            mHorizontalHeaderAdapter.unregisterDataSetObserver(observer);
        }
        
        if (mVerticalHeaderAdapter != null) {
            mVerticalHeaderAdapter.unregisterDataSetObserver(observer);
        }
        
        if (mDataAdapter != null) {
            mDataAdapter.unregisterDataSetObserver(observer);
        }
    }
    
    @Override
    public void notifyDataSetChanged() {
        if (mHorizontalHeaderAdapter != null) {
            mHorizontalHeaderAdapter.notifyDataSetChanged();
        }
        
        if (mVerticalHeaderAdapter != null) {
            mVerticalHeaderAdapter.notifyDataSetChanged();
        }
        
        if (mDataAdapter != null) {
            mDataAdapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public void notifyDataSetInvalidated() {
        if (mHorizontalHeaderAdapter != null) {
            mHorizontalHeaderAdapter.notifyDataSetInvalidated();
        }
        
        if (mVerticalHeaderAdapter != null) {
            mVerticalHeaderAdapter.notifyDataSetInvalidated();
        }
        
        if (mDataAdapter != null) {
            mDataAdapter.notifyDataSetInvalidated();
        }
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
    
}
