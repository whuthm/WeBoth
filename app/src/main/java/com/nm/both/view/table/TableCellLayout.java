package com.nm.both.view.table;

import android.content.Context;
import android.database.DataSetObserver;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by huangming on 2016/11/2.
 */

public class TableCellLayout extends ViewGroup {
    
    private TableCellAdapter mAdapter;
    private DataSetObserver mDataSetObserver;
    
    private TableCellChildrenLayout mChildrenLayout;
    
    public TableCellLayout(Context context) {
        this(context, null);
    }
    
    public TableCellLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        
        mChildrenLayout = new TableCellChildrenLayout(context);
        addViewInternal(mChildrenLayout);
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
    
    void setCellGap(int widthGap, int heightGap) {
        mChildrenLayout.setCellGap(widthGap, heightGap);
    }
    
    int getMaxWidthAt(int cellX) {
        return mChildrenLayout.getMaxWidthAt(cellX);
    }
    
    int getMaxHeightAt(int cellY) {
        return mChildrenLayout.getMaxHeightAt(cellY);
    }
    
    private void resetCellLayout() {
        mChildrenLayout.removeAllViewsInLayout();
        if (mAdapter == null) {
            return;
        }
        Log.e("TableCellLayout", "resetCellLayout before");
        mChildrenLayout.setGridSize(getCountX(), getCountY());
        
        for (int i = 0; i < getCountX(); i++) {
            for (int j = 0; j < getCountY(); j++) {
                LayoutParams lp = new LayoutParams();
                lp.cellX = i;
                lp.cellY = j;
                Log.i("TableCellLayout", "before : i = " + i + ", j = " + j);
                mChildrenLayout.addView(mAdapter.getView(i, j, mChildrenLayout), lp);
                Log.i("TableCellLayout", "after : i = " + i + ", j = " + j);
            }
        }
        Log.e("TableCellLayout", "resetCellLayout after");
    }
    
    View getChildAt(int cellX, int cellY) {
        return mChildrenLayout.getChildAt(cellX, cellY);
    }
    
    private void addViewInternal(View child) {
        super.addView(child, -1, generateDefaultLayoutParams());
    }
    
    @Override
    public void addView(View child) {
        throw new IllegalStateException(
                "TableCellLayout can host only TableCellChildrenLayout child");
    }
    
    @Override
    public void addView(View child, int index) {
        throw new IllegalStateException(
                "TableCellLayout can host only TableCellChildrenLayout child");
    }
    
    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        throw new IllegalStateException(
                "TableCellLayout can host only TableCellChildrenLayout child");
    }
    
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        throw new IllegalStateException(
                "TableCellLayout can host only TableCellChildrenLayout child");
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
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        
        int newWidth;
        int newHeight;
        
        measureChild(mChildrenLayout, widthMeasureSpec, heightMeasureSpec);
        
        if (widthSpecMode == MeasureSpec.UNSPECIFIED) {
            newWidth = mChildrenLayout.getMeasuredWidth() + getPaddingLeft()
                    + getPaddingRight();
        }
        else {
            newWidth = Math.min(widthSpecSize, mChildrenLayout.getMeasuredWidth()
                    + getPaddingLeft() + getPaddingRight());
        }
        
        if (heightSpecMode == MeasureSpec.UNSPECIFIED) {
            newHeight = mChildrenLayout.getMeasuredHeight() + getPaddingTop()
                    + getPaddingBottom();
        }
        else {
            newHeight = Math.min(heightSpecSize, mChildrenLayout.getMeasuredHeight()
                    + getPaddingTop() + getPaddingBottom());
        }
        setMeasuredDimension(newWidth, newHeight);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mChildrenLayout.layout(getPaddingLeft(), getPaddingTop(),
                r - l - getPaddingRight(), b - t - getPaddingBottom());
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
    
    static class LayoutParams extends ViewGroup.LayoutParams {
        
        int cellX;
        int cellY;
        
        int x;
        int y;
        
        LayoutParams() {
            super(WRAP_CONTENT, WRAP_CONTENT);
        }
        
        void setup(int[] maxWidths, int[] maxHeights, int widthGap, int heightGap) {
            x = y = 0;
            for (int i = 0; i < cellX; i++) {
                x += maxWidths[i] + widthGap;
            }
            
            for (int j = 0; j < cellY; j++) {
                y += maxHeights[j] + heightGap;
            }
            width = maxWidths[cellX];
            height = maxHeights[cellY];
        }
        
        @Override
        public String toString() {
            return "LayoutParams(cellX=" + cellX + ", cellY=" + cellY + ", width=" + width
                    + ", height=" + height + ", x=" + x + ", y=" + y + ")";
        }
    }
}
