package com.nm.both.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by huangming on 2016/10/31.
 */

public class HVScrollView extends LinearLayout {
    
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    
    private int mOverscrollDistance;
    private int mOverflingDistance;
    private ScrollChangeListener mScrollChangeListener;
    
    public HVScrollView(Context context) {
        super(context);
        initScrollView();
    }
    
    public HVScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScrollView();
    }
    
    public HVScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollView();
    }
    
    private void initScrollView() {
        //mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
        mOverflingDistance = configuration.getScaledOverflingDistance();
    }
    
    public void setScrollChangeListener(ScrollChangeListener l) {
        this.mScrollChangeListener = l;
    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollChangeListener != null) {
            mScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    
    public interface ScrollChangeListener {
        void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX,
                int oldScrollY);
    }
    
}
