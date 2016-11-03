package com.nm.both.view.table;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangming on 2016/11/3.
 */

public class TableCellChildrenLayout extends ViewGroup {
    
    private int[] mMaxWidths;
    private int[] mMaxHeights;
    private int mCountX;
    private int mCountY;
    
    private int mWidthGap;
    private int mHeightGap;
    
    public TableCellChildrenLayout(Context context) {
        super(context);
    }
    
    public void setGridSize(int countX, int countY) {
        if (countX < 0 || countY < 0) {
            throw new RuntimeException("countX < 0 || countY < 0");
        }
        mMaxWidths = new int[countX];
        mMaxHeights = new int[countY];
        if (countX != mCountX || countY != mCountY) {
            mCountX = countX;
            mCountY = countY;
            requestLayout();
        }
    }
    
    void setCellGap(int widthGap, int heightGap) {
        mWidthGap = widthGap;
        mHeightGap = heightGap;
        requestLayout();
    }
    
    View getChildAt(int cellX, int cellY) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            TableCellLayout.LayoutParams lp = (TableCellLayout.LayoutParams) child
                    .getLayoutParams();
            if (lp.cellX == cellX && lp.cellY == cellY) {
                return child;
            }
        }
        return null;
    }
    
    int getMaxWidthAt(int cellX) {
        return mMaxWidths[cellX];
    }
    
    int getMaxHeightAt(int cellY) {
        return mMaxHeights[cellY];
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("TableCellChildrenLayout", "onMeasure");
        int count = getChildCount();
        boolean remeasure = false;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            remeasure |= measureChild(child);
        }
        
        if (remeasure) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                remeasureChild(child);
            }
            postInvalidate();
        }
        
        setMeasuredDimension(resolveWidthDimension(), resolveHeightDimension());
    }
    
    private int resolveWidthDimension() {
        int measuredWidth = 0;
        if (mMaxWidths != null) {
            for (int i = 0; i < mCountX; i++) {
                measuredWidth += mMaxWidths[i];
            }
        }
        measuredWidth += (mCountX - 1) * mWidthGap;
        return measuredWidth;
    }
    
    private int resolveHeightDimension() {
        int measuredHeight = 0;
        if (mMaxHeights != null) {
            for (int i = 0; i < mCountY; i++) {
                measuredHeight += mMaxHeights[i];
            }
        }
        measuredHeight += (mCountY - 1) * mHeightGap;
        return measuredHeight;
    }
    
    private void remeasureChild(View child) {
        TableCellLayout.LayoutParams lp = (TableCellLayout.LayoutParams) child
                .getLayoutParams();
        lp.setup(mMaxWidths, mMaxHeights, mWidthGap, mHeightGap);
        Log.e("TableCellChildrenLayout", "remeasureChild : " + lp.toString());
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width,
                MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height,
                MeasureSpec.EXACTLY);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
    
    private boolean measureChild(View child) {
        TableCellLayout.LayoutParams lp = (TableCellLayout.LayoutParams) child
                .getLayoutParams();
        
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width,
                MeasureSpec.UNSPECIFIED);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height,
                MeasureSpec.UNSPECIFIED);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        int childMeasureWidth = child.getMeasuredWidth();
        int childMeasureHeight = child.getMeasuredHeight();
        int oldMaxWidth = mMaxWidths[lp.cellX];
        int oldMaxHeight = mMaxHeights[lp.cellY];
        boolean remeasure = (childMeasureWidth > oldMaxWidth
                || childMeasureHeight > oldMaxHeight);
        if (remeasure) {
            mMaxWidths[lp.cellX] = childMeasureWidth;
            mMaxHeights[lp.cellY] = childMeasureHeight;
        }
        return remeasure;
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                TableCellLayout.LayoutParams lp = (TableCellLayout.LayoutParams) child
                        .getLayoutParams();
                int childLeft = lp.x;
                int childTop = lp.y;
                child.layout(childLeft, childTop, childLeft + lp.width,
                        childTop + lp.height);
            }
        }
    }
}
