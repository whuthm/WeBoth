package com.nm.both.view.table;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    private int mBorderWidth;
    private int mBorderHeight;
    private Drawable mBorderDrawable;

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableView);
        mBorderDrawable = a.getDrawable(R.styleable.TableView_border);
        final int borderSize = a.getDimensionPixelSize(R.styleable.TableView_borderSize,
                0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.TableView_borderWidth,
                borderSize);
        mBorderHeight = a.getDimensionPixelSize(R.styleable.TableView_borderHeight,
                borderSize);
        a.recycle();

        ensureHeaderAndDataLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private HVScrollView.HVScrollListener mHVScrollListener = new HVScrollView.HVScrollListener() {
        @Override
        public void onScrollChange(HVScrollView v, int scrollX, int scrollY,
                int oldScrollX, int oldScrollY) {}
    };

    public void setAdapter(TableAdapter adapter) {
        mAdapter = adapter;
        fillViews();
    }

    private void fillViews() {
        mHorizontalHeaderLayout.removeAllViewsInLayout();
        mVerticalHeaderLayout.removeAllViewsInLayout();
        mDataLayout.removeAllViewsInLayout();

        if (mAdapter == null) {
            return;
        }

        requestLayout();
        invalidate();
    }

    private void ensureHeaderAndDataLayout() {

        if (mHorizontalHeaderScrollView == null) {
            mHorizontalHeaderScrollView = new HVScrollView(getContext());
        }

        if (mHorizontalHeaderLayout != null) {
            mHorizontalHeaderLayout = new TableCellLayout(getContext());
            mHorizontalHeaderScrollView.addView(mHorizontalHeaderLayout);
        }
        if (mVerticalHeaderScrollView == null) {
            mVerticalHeaderScrollView = new HVScrollView(getContext());
            addView(mVerticalHeaderScrollView);
        }

        if (mVerticalHeaderLayout != null) {
            mVerticalHeaderLayout = new TableCellLayout(getContext());
            mVerticalHeaderScrollView.addView(mVerticalHeaderLayout);
        }

        if (mDataScrollView == null) {
            mDataScrollView = new HVScrollView(getContext());
            addView(mDataScrollView);
        }

        if (mDataLayout != null) {
            mDataLayout = new TableCellLayout(getContext());
            mDataScrollView.addView(mDataLayout);
        }
    }
}
