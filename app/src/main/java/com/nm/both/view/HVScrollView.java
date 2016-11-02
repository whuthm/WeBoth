package com.nm.both.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.nm.both.R;

/**
 * Created by huangming on 2016/10/31.
 */

public class HVScrollView extends FrameLayout {
    
    private static final String TAG = "HVScrollView";
    
    /**
     * Sentinel value for no current active pointer. Used by
     * {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;
    
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    
    private HVScrollListener mHVScrollListener;
    
    private OverScroller mScroller;
    
    private boolean mIsBeingDragged = false;
    
    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId = INVALID_POINTER;
    
    /**
     * Determines speed during touch scrolling
     */
    private VelocityTracker mVelocityTracker;
    
    /**
     * X of the last motion event.
     */
    private int mLastMotionX;
    
    /**
     * Y of the last motion event.
     */
    private int mLastMotionY;
    
    private ScrollMode mScrollMode;
    private ScrollMode mCurrentMode;
    
    public HVScrollView(Context context) {
        this(context, null);
    }
    
    public HVScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initScrollView();
    }
    
    public HVScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HVScrollView, 0,
                0);
        mScrollMode = ScrollMode
                .mapIntToValue(a.getInteger(R.styleable.HVScrollView_scrollMode, 0));
        a.recycle();
        
        initScrollView();
    }
    
    private void initScrollView() {
        mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }


    public void setScrollMode(ScrollMode mode) {
        this.mScrollMode = mode;
    }

    public void setHVScrollListener(HVScrollListener hvScrollListener) {
        this.mHVScrollListener = hvScrollListener;
    }
    
    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "HVScrollView can host only one direct child");
        }
        
        super.addView(child);
    }
    
    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "HVScrollView can host only one direct child");
        }
        
        super.addView(child, index);
    }
    
    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "HVScrollView can host only one direct child");
        }
        
        super.addView(child, params);
    }
    
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "HVScrollView can host only one direct child");
        }
        
        super.addView(child, index, params);
    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mHVScrollListener != null) {
            mHVScrollListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }
    
    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        else {
            mVelocityTracker.clear();
        }
    }
    
    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }
    
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
    
    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollX = getScrollX();
            final int scrollY = getScrollY();
            final View child = getChildAt(0);
            return !(y < child.getTop() - scrollY || y >= child.getBottom() - scrollY
                    || x < child.getLeft() - scrollX || x >= child.getRight() - scrollX);
        }
        return false;
    }
    
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction()
                & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = (int) ev.getY(newPointerIndex);
            mLastMotionY = (int) ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }
    
    private void endDrag() {
        mCurrentMode = mScrollMode;
        mIsBeingDragged = false;
        recycleVelocityTracker();
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        
        /*
         * Don't try to intercept touch if we can't scroll anyway.
         */
        if ((getScrollY() == 0 && !canScrollVertically(1))
                || (getScrollX() == 0 && !canScrollHorizontally(1))) {
            return false;
        }
        
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on
                    // content.
                    break;
                }
                
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent");
                    break;
                }
                
                final int x = (int) ev.getX(pointerIndex);
                final int xDiff = Math.abs(x - mLastMotionX);
                final int y = (int) ev.getY(pointerIndex);
                final int yDiff = Math.abs(y - mLastMotionY);
                if ((xDiff > mTouchSlop && canHScroll())
                        || (yDiff > mTouchSlop && canVScroll())) {
                    mIsBeingDragged = true;
                    mCurrentMode = xDiff > yDiff ? ScrollMode.H : ScrollMode.V;
                    mLastMotionX = x;
                    mLastMotionY = y;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            }
            
            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                if (!inChild(x, y)) {
                    mCurrentMode = mScrollMode;
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                    break;
                }
                
                /*
                 * Remember location of down touch. ACTION_DOWN always refers to
                 * pointer index 0.
                 */
                mLastMotionX = x;
                mLastMotionY = y;
                mActivePointerId = ev.getPointerId(0);
                
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mIsBeingDragged = false;
                mCurrentMode = mScrollMode;
                break;
            }
            
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                /* Release the drag */
                Log.i(TAG, "onInterceptTouchEvent:" + (action & MotionEvent.ACTION_MASK));
                mIsBeingDragged = false;
                mCurrentMode = mScrollMode;
                mActivePointerId = INVALID_POINTER;
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        
        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return mIsBeingDragged;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        
        MotionEvent vtev = MotionEvent.obtain(ev);
        
        final int actionMasked = ev.getActionMasked();
        
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                Log.e(TAG, "onTouchEvent ACTION_DOWN : " + getScrollX() + ", "
                        + getScrollY());
                if (getChildCount() == 0) {
                    return false;
                }

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                
                // Remember where the motion event started
                mLastMotionX = (int) ev.getX();
                mLastMotionY = (int) ev.getY();
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG,
                            "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }
                
                final int x = (int) ev.getX(activePointerIndex);
                final int y = (int) ev.getY(activePointerIndex);
                int deltaX = mLastMotionX - x;
                int deltaY = mLastMotionY - y;
                if (!mIsBeingDragged && ((Math.abs(deltaY) > mTouchSlop && canVScroll())
                        || (Math.abs(deltaX) > mTouchSlop && canHScroll()))) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    mCurrentMode = Math.abs(deltaX) > Math.abs(deltaY) ? ScrollMode.H
                            : ScrollMode.V;
                }
                Log.e(TAG,
                        "onTouchEvent : " + deltaX + ", " + deltaY + ", x=" + x + ", y="
                                + y + ", mLastMotionX=" + mLastMotionX + ", mLastMotionY="
                                + mLastMotionY+ ", currentMode=" + mCurrentMode);
                if (mIsBeingDragged) {
                    mLastMotionX = x;
                    mLastMotionY = y;
                    if (overScrollBy(canHScroll() ? deltaX : 0, canVScroll() ? deltaY : 0,
                            getScrollX(), getScrollY(), getHScrollRange(),
                            getVScrollRange(), 0, 0, true)) {
                        // Break our velocity if we hit a scroll barrier.
                        mVelocityTracker.clear();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocityX = (int) velocityTracker
                            .getXVelocity(mActivePointerId);
                    int initialVelocityY = (int) velocityTracker
                            .getYVelocity(mActivePointerId);
                    
                    Log.e(TAG, "Velocity : " + initialVelocityX + ", " + initialVelocityY
                            + ", mMinimumVelocity=" + mMinimumVelocity
                            + ", mMaximumVelocity=" + mMaximumVelocity + ", canHScroll="
                            + canHScroll() + ", canVScroll=" + canVScroll()
                            + ", currentMode=" + mCurrentMode.name());
                    
                    if ((Math.abs(initialVelocityX) > mMinimumVelocity)
                            || (Math.abs(initialVelocityY) > mMinimumVelocity)) {
                        fling(canHScroll() ? -initialVelocityX : 0,
                                canVScroll() ? -initialVelocityY : 0);
                    }
                    
                    mActivePointerId = INVALID_POINTER;
                    endDrag();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {
                    mActivePointerId = INVALID_POINTER;
                    endDrag();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionX = (int) ev.getX(index);
                mLastMotionY = (int) ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionX = (int) ev.getX(ev.findPointerIndex(mActivePointerId));
                mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
                break;
        }
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }
    
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
            boolean clampedY) {
        scrollTo(scrollX, scrollY);
        awakenScrollBars();
    }
    
    private int getVScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getHeight()
                    - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }
    
    private int getHScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getWidth()
                    - (getWidth() - getPaddingLeft() - getPaddingRight()));
        }
        return scrollRange;
    }
    
    private boolean canVScroll() {
        View child = getChildAt(0);
        return child != null && (mScrollMode == ScrollMode.V
                || mScrollMode == ScrollMode.BOTH
                || (mScrollMode == ScrollMode.SELECTIVE && mCurrentMode != ScrollMode.H));
    }
    
    private boolean canHScroll() {
        View child = getChildAt(0);
        return child != null && (mScrollMode == ScrollMode.H
                || mScrollMode == ScrollMode.BOTH
                || (mScrollMode == ScrollMode.SELECTIVE && mCurrentMode != ScrollMode.V));
    }
    
    private void fling(int velocityX, int velocityY) {
        if (getChildCount() > 0) {
            Log.i(TAG, "fling");
            int width = getWidth() - getPaddingLeft() - getPaddingRight();
            int height = getHeight() - getPaddingTop() - getPaddingBottom();
            int right = getChildAt(0).getWidth();
            int bottom = getChildAt(0).getHeight();
            
            mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY, 0,
                    Math.max(0, right - width), 0, Math.max(0, bottom - height), 0, 0);
            postInvalidate();
        }
    }
    
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.i(TAG, "computeScroll");
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            
            if (oldX != x || oldY != y) {
                overScrollBy(x - oldX, y - oldY, oldX, oldY, getHScrollRange(),
                        getVScrollRange(), 0, 0, false);
            }
        }
    }
    
    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
            int parentHeightMeasureSpec) {
        
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        
        final int childWidthMeasureSpec = mScrollMode != ScrollMode.V
                ? MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(parentWidthMeasureSpec),
                        MeasureSpec.UNSPECIFIED)
                : getChildMeasureSpec(parentWidthMeasureSpec,
                        getPaddingLeft() + getPaddingRight(), lp.width);
        
        final int childHeightMeasureSpec = mScrollMode != ScrollMode.H
                ? MeasureSpec.makeMeasureSpec(
                        MeasureSpec.getSize(parentHeightMeasureSpec),
                        MeasureSpec.UNSPECIFIED)
                : getChildMeasureSpec(parentHeightMeasureSpec,
                        getPaddingTop() + getPaddingBottom(), lp.height);
        
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
    
    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec,
            int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "parent : " + getMeasuredWidth() + ", " + getMeasuredHeight());
        if (getChildCount() > 0) {
            Log.e(TAG, "child : " + getChildAt(0).getMeasuredWidth() + ", "
                    + getChildAt(0).getMeasuredHeight());
        }
    }
    
    public interface HVScrollListener {
        void onScrollChange(HVScrollView v, int scrollX, int scrollY, int oldScrollX,
                int oldScrollY);
    }
    
    public enum ScrollMode {
        
        BOTH(0x0), H(0x1), V(0x2), SELECTIVE(0X3);
        
        int value;
        
        ScrollMode(int value) {
            this.value = value;
        }
        
        public static ScrollMode getDefault() {
            return BOTH;
        }
        
        private int getValue() {
            return value;
        }
        
        public static ScrollMode mapIntToValue(final int modeInt) {
            for (ScrollMode value : ScrollMode.values()) {
                if (modeInt == value.getValue()) {
                    return value;
                }
            }
            return getDefault();
        }
    }
    
}
