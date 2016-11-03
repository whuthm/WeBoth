package com.nm.both.view.table;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.nm.both.R;
import com.nm.both.view.HVScrollView;

/**
 * Created by huangming on 2016/10/31.
 */

public class TableView extends ViewGroup {
    
    private TableAdapter mAdapter;
    private TableCellLayout mHorizontalHeaderLayout;
    private HVScrollView mHorizontalHeaderScrollView;
    private TableCellLayout mVerticalHeaderLayout;
    private HVScrollView mVerticalHeaderScrollView;
    private TableCellLayout mDataLayout;
    private HVScrollView mDataScrollView;
    
    /** Header重叠区域, 待定义 */
    private View mHeaderOverlapView;
    
    private int mWidthGap;
    private int mHeightGap;
    private Drawable mGapDrawable;
    
    private int mSizeBorder;
    private Drawable mBorderDrawable;
    
    private DataSetObserver mDataSetObserver;
    
    public TableView(Context context) {
        this(context, null);
    }
    
    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private HVScrollView.HVScrollListener mHVScrollListener = new HVScrollView.HVScrollListener() {
        @Override
        public void onScrollChange(HVScrollView v, int scrollX, int scrollY,
                                   int oldScrollX, int oldScrollY) {}
    };
    
    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        setWillNotDraw(false);
        
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableView);
        
        mBorderDrawable = a.getDrawable(R.styleable.TableView_border);
        final int sizeBorder = a.getDimensionPixelSize(R.styleable.TableView_sizeBorder,
                0);
        setPadding(sizeBorder, sizeBorder, sizeBorder, sizeBorder);
        
        mGapDrawable = a.getDrawable(R.styleable.TableView_gap);
        final int sizeGap = a.getDimensionPixelSize(R.styleable.TableView_sizeGap, 0);
        mWidthGap = a.getDimensionPixelSize(R.styleable.TableView_widthGap, sizeGap);
        mHeightGap = a.getDimensionPixelSize(R.styleable.TableView_heightGap, sizeGap);
        a.recycle();
        
        ensureHeaderAndDataLayout();
    }

    private void ensureHeaderAndDataLayout() {
        if (mHorizontalHeaderScrollView == null) {
            mHorizontalHeaderScrollView = new HVScrollView(getContext());
            addView(mHorizontalHeaderScrollView);
        }

        if (mHorizontalHeaderLayout == null) {
            mHorizontalHeaderLayout = new TableCellLayout(getContext());
            mHorizontalHeaderLayout.setCellGap(mWidthGap, mHeightGap);
            mHorizontalHeaderScrollView.addView(mHorizontalHeaderLayout);
        }
        if (mVerticalHeaderScrollView == null) {
            mVerticalHeaderScrollView = new HVScrollView(getContext());
            addView(mVerticalHeaderScrollView);
        }

        if (mVerticalHeaderLayout == null) {
            mVerticalHeaderLayout = new TableCellLayout(getContext());
            mVerticalHeaderLayout.setCellGap(mWidthGap, mHeightGap);
            mVerticalHeaderScrollView.addView(mVerticalHeaderLayout);
        }

        if (mDataScrollView == null) {
            mDataScrollView = new HVScrollView(getContext());
            addView(mDataScrollView);
        }

        if (mDataLayout == null) {
            mDataLayout = new TableCellLayout(getContext());
            mDataLayout.setCellGap(mWidthGap, mHeightGap);
            mDataScrollView.addView(mDataLayout);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new TableDataSetObserver();
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
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        
        int parentWidthMeasureSpec;
        int parentHeightMeasureSpec;
        
        parentWidthMeasureSpec = widthMeasureSpec;
        parentHeightMeasureSpec = heightMeasureSpec;
        
        // -----测量竖向header宽度
        measureChild(mVerticalHeaderScrollView, parentWidthMeasureSpec,
                parentHeightMeasureSpec);
        int verticalHeaderMeasuredWidth = mVerticalHeaderScrollView.getMeasuredWidth();
        
        parentWidthMeasureSpec = MeasureSpec
                .makeMeasureSpec(Math.max(widthSize - verticalHeaderMeasuredWidth
                        - paddingLeft - paddingRight - mWidthGap, 0), widthMode);
        parentHeightMeasureSpec = heightMeasureSpec;
        
        measureChild(mHorizontalHeaderScrollView, parentWidthMeasureSpec,
                parentHeightMeasureSpec);
        
        parentWidthMeasureSpec = widthMeasureSpec;
        parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(heightSize - mHorizontalHeaderScrollView.getMeasuredHeight()
                        - paddingTop - paddingBottom - mHeightGap, 0),
                heightMode);
        
        measureChild(mVerticalHeaderScrollView, parentWidthMeasureSpec,
                parentHeightMeasureSpec);
        
        parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(widthSize - mVerticalHeaderScrollView.getMeasuredWidth()
                        - paddingLeft - paddingRight - mWidthGap, 0),
                widthMode);
        parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(heightSize - mHorizontalHeaderScrollView.getMeasuredHeight()
                        - paddingTop - paddingBottom - mHeightGap, 0),
                heightMode);
        
        measureChild(mDataScrollView, parentWidthMeasureSpec, parentHeightMeasureSpec);
        
        int newWidth;
        int newHeight;
        int contentWidth = mVerticalHeaderScrollView.getMeasuredWidth()
                + Math.max(mHorizontalHeaderScrollView.getMeasuredWidth(),
                        mDataScrollView.getMeasuredWidth());
        int contentHeight = mHorizontalHeaderScrollView.getMeasuredHeight()
                + Math.max(mVerticalHeaderScrollView.getMeasuredHeight(),
                        mDataScrollView.getMeasuredHeight());
        
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            newWidth = paddingLeft + paddingRight + contentWidth + mWidthGap;
        }
        else {
            newWidth = Math.min(paddingLeft + paddingRight + contentWidth + mWidthGap,
                    widthSize);
        }
        
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            newHeight = paddingTop + paddingBottom + contentHeight + mHeightGap;
        }
        else {
            newHeight = Math.min(paddingTop + paddingBottom + contentHeight + mHeightGap,
                    heightSize);
        }

        Log.e("TableView", newWidth + " " + newHeight + " " + widthSize + "  " + heightSize);
        
        // int newWidth = widthSize;
        // int newHeight = heightSize;
        // if (widthMode == MeasureSpec.AT_MOST) {
        // newWidth = Math.min(paddingLeft + paddingRight + contentWidth +
        // mWidthGap,
        // widthSize);
        // }
        // else if (widthMode == MeasureSpec.UNSPECIFIED) {
        // newWidth = paddingLeft + paddingRight + contentWidth + mWidthGap;
        // }
        //
        // if (heightMode == MeasureSpec.AT_MOST) {
        // newHeight = Math.min(paddingTop + paddingBottom + contentHeight +
        // mHeightGap,
        // heightSize);
        // }
        // else if (heightMode == MeasureSpec.UNSPECIFIED) {
        // newHeight = paddingTop + paddingBottom + contentHeight + mHeightGap;
        // }
        
        setMeasuredDimension(newWidth, newHeight);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        
        int childLeft;
        int childTop;
        
        childLeft = paddingLeft + mVerticalHeaderScrollView.getMeasuredWidth()
                + mWidthGap;
        childTop = paddingTop;
        mHorizontalHeaderScrollView.layout(childLeft, childTop,
                childLeft + mHorizontalHeaderScrollView.getMeasuredWidth(),
                childTop + mHorizontalHeaderScrollView.getMeasuredHeight());
        
        childLeft = paddingLeft;
        childTop = paddingTop + mHorizontalHeaderScrollView.getMeasuredHeight()
                + mHeightGap;
        
        mVerticalHeaderScrollView.layout(childLeft, childTop,
                childLeft + mVerticalHeaderScrollView.getMeasuredWidth(),
                childTop + mVerticalHeaderScrollView.getMeasuredHeight());
        
        childLeft = paddingLeft + mVerticalHeaderScrollView.getMeasuredWidth()
                + mWidthGap;
        childTop = paddingTop + mHorizontalHeaderScrollView.getMeasuredHeight()
                + mHeightGap;
        
        mDataScrollView.layout(childLeft, childTop,
                childLeft + mDataScrollView.getMeasuredWidth(),
                childTop + mDataScrollView.getMeasuredHeight());

        Log.e("TableView", "childLeft = " + childLeft + ", childTop = " + childTop + "  " + getScrollY() + "  " + getScrollX());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        if (paddingLeft > 0 && width > paddingLeft) {
            mBorderDrawable.setBounds(0, 0, paddingLeft, height);
            mBorderDrawable.draw(canvas);
        }

        if (paddingRight > 0 && width > paddingRight) {
            mBorderDrawable.setBounds(width - paddingRight, 0, width, height);
            mBorderDrawable.draw(canvas);
        }

        if (paddingTop > 0 && height > paddingTop) {
            mBorderDrawable.setBounds(0, 0, width, paddingTop);
            mBorderDrawable.draw(canvas);
        }

        if (paddingBottom > 0 && height > paddingBottom) {
            mBorderDrawable.setBounds(0, height - paddingBottom, width, height);
            mBorderDrawable.draw(canvas);
        }
    }
    

    public void setAdapter(TableAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            ensureHeaderAndDataLayout();
            
            mDataSetObserver = new TableDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
            
            final TableCellAdapter dataAdapter = mAdapter.getDataAdapter();
            final TableCellAdapter horizontalHeaderAdapter = mAdapter
                    .getHorizontalHeaderAdapter();
            final TableCellAdapter verticalHeaderAdapter = mAdapter
                    .getVerticalHeaderAdapter();
            
            checkAdapter();
            
            if (dataAdapter != null) {
                mDataLayout.setAdapter(dataAdapter);
            }
            
            if (horizontalHeaderAdapter != null) {
                mHorizontalHeaderLayout.setAdapter(horizontalHeaderAdapter);
            }
            
            if (verticalHeaderAdapter != null) {
                mVerticalHeaderLayout.setAdapter(verticalHeaderAdapter);
            }
            requestLayout();
            invalidate();
        }
    }
    
    private void checkAdapter() {
        
        if (mAdapter == null) {
            return;
        }
        
        final TableCellAdapter dataAdapter = mAdapter.getDataAdapter();
        final TableCellAdapter horizontalHeaderAdapter = mAdapter
                .getHorizontalHeaderAdapter();
        final TableCellAdapter verticalHeaderAdapter = mAdapter
                .getVerticalHeaderAdapter();
        
        if (dataAdapter != null && verticalHeaderAdapter != null
                && dataAdapter.getCountY() != verticalHeaderAdapter.getCountY()) {
            throw new IllegalStateException(
                    "the countY of dataAdapter and verticalHeaderAdapter must be same");
        }
        
        if (dataAdapter != null && horizontalHeaderAdapter != null
                && dataAdapter.getCountX() != horizontalHeaderAdapter.getCountX()) {
            throw new IllegalStateException(
                    "the countY of dataAdapter and horizontalHeaderAdapter must be same");
        }
    }
    
    private class TableDataSetObserver extends DataSetObserver {
        
        @Override
        public void onChanged() {
            checkAdapter();
        }
        
        @Override
        public void onInvalidated() {
            checkAdapter();
        }
    }
    
}
