package com.nm.both.view.table;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.nm.both.R;
import com.nm.both.view.HVScrollView;

/**
 * Created by huangming on 2016/10/31.
 */

public class TableView extends LinearLayout {
    
    private TableAdapter mAdapter;
    private LinearLayout mHeaderContainer;
    private TableItemContainer mItemContainer;
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
        
        ensureHeaderAndItemContainer();
        mItemContainer.setHVScrollListener(mHVScrollListener);
    }
    
    private HVScrollView.HVScrollListener mHVScrollListener = new HVScrollView.HVScrollListener() {
        @Override
        public void onScrollChange(HVScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        }
    };
    
    public void setAdapter(TableAdapter adapter) {
        mAdapter = adapter;
        fillViews();
    }
    
    private void fillViews() {
        mItemContainer.removeAllViewsInLayout();
        mHeaderContainer.removeAllViewsInLayout();
        
        if (mAdapter == null) {
            return;
        }
        
        int headerCount = mAdapter.getHeaderCount();
        for (int i = 0; i < headerCount; i++) {
            LinearLayout headerView = new LinearLayout(getContext());
            headerView.setOrientation(
                    getOrientation() == HORIZONTAL ? VERTICAL : HORIZONTAL);
            View headerUnitView = mAdapter.getHeaderUnitView(i, headerView);
            headerView.addView(headerUnitView);
        }
        
        int itemCount = mAdapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            LinearLayout itemView = new LinearLayout(getContext());
            itemView.setOrientation(
                    getOrientation() == HORIZONTAL ? VERTICAL : HORIZONTAL);
            View itemUnitView = mAdapter.getItemUnitView(i, itemView);
            itemView.addView(itemUnitView);
        }
        
        requestLayout();
        invalidate();
    }
    
    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        ensureHeaderAndItemContainer();
        mHeaderContainer.setOrientation(orientation);
        //mItemContainer.setOrientation(orientation);
        
        fillViews();
    }
    
    private void ensureHeaderAndItemContainer() {
        if (mHeaderContainer != null) {
            mHeaderContainer = new LinearLayout(getContext());
            addView(mHeaderContainer);
        }
        if (mItemContainer != null) {
            mItemContainer = new TableItemContainer(getContext());
            addView(mItemContainer);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //
        // final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        
        if (getOrientation() == HORIZONTAL) {
            remeasureHorizontal();
        }
        else {
            remeasureVertical();
        }
    }
    
    private void remeasureVertical() {
        
    }
    
    private void remeasureHorizontal() {
        
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
