package com.nm.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.nm.base.R;

/**
 * Created by huangming on 2015/10/26.
 * 分隔线线性布局
 */
public class DividerLineLinearLayout extends LinearLayout {
    
    private int mDividerTopMargin;
    private int mDividerBottomMargin;
    private int mDividerLeftMargin;
    private int mDividerRightMargin;
    
    private Drawable mDividerDrawable;
    private int mDividerWidth;
    private int mDividerHeight;
    
    private boolean mDrawStart;
    private boolean mDrawEnd;

    private boolean mShowDividerLine = true;
    
    public DividerLineLinearLayout(Context context) {
        this(context, null);
    }
    
    public DividerLineLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setWillNotDraw(false);
        
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DividerLinearLayout);
        mDividerDrawable = a.getDrawable(R.styleable.DividerLinearLayout_android_divider);
        mDividerWidth = a.getDimensionPixelSize(
                R.styleable.DividerLinearLayout_dividerWidth, 0);
        mDividerHeight = a.getDimensionPixelSize(
                R.styleable.DividerLinearLayout_android_dividerHeight, 0);
        
        final int horMargin = a.getDimensionPixelSize(
                R.styleable.DividerLinearLayout_dividerHorMargin, 0);
        final int verMargin = a.getDimensionPixelSize(
                R.styleable.DividerLinearLayout_dividerVerMargin, 0);
        mDividerTopMargin = a.getDimensionPixelSize(
                R.styleable.DividerLinearLayout_dividerTopMargin, verMargin);
        mDividerBottomMargin = a.getDimensionPixelSize(
                R.styleable.DividerLinearLayout_dividerBottomMargin, verMargin);
        mDividerLeftMargin = a.getDimensionPixelSize(
                R.styleable.DividerLinearLayout_dividerLeftMargin, horMargin);
        mDividerRightMargin = a.getDimensionPixelSize(
                R.styleable.DividerLinearLayout_dividerRightMargin, horMargin);
        
        mDrawStart = a.getBoolean(R.styleable.DividerLinearLayout_drawStart, true);
        mDrawEnd = a.getBoolean(R.styleable.DividerLinearLayout_drawEnd, true);
        a.recycle();
    }

    public void showStartDivider(boolean show){
        mDrawStart = show;
    }

    public void showEndDivider(boolean show){
        mDrawEnd = show;
    }

    public void showDividerLine(boolean show) {
        mShowDividerLine = show;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        final int orientation = getOrientation();
        final Drawable d = mDividerDrawable;
        final int width = getWidth();
        final int height = getHeight();
        if (d != null && width > 0 && height > 0 && mShowDividerLine) {
            if (orientation == HORIZONTAL && mDividerWidth > 0) {
                drawHorizontalDividerLine(canvas);
            }
            else if (orientation == VERTICAL && mDividerHeight > 0) {
                drawVerticalDividerLine(canvas);
            }
        }
    }

    private void drawHorizontalDividerLine(Canvas canvas) {
        final Drawable d = mDividerDrawable;
        final int width = getWidth();
        final int height = getHeight();
        int left;
        int right;
        int top = mDividerTopMargin;
        int bottom = height - mDividerBottomMargin;
        if (bottom <= top) {
            return;
        }
        
        int count = getChildCount();
        if (count > 0 && mDrawStart) {
            left = 0;
            right = left + mDividerWidth;
            d.setBounds(left, top, right, bottom);
            d.draw(canvas);
        }
        for (int i = 0; i < count - 1; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                left = child.getRight();
                right = left + mDividerWidth;
                d.setBounds(left, top, right, bottom);
                d.draw(canvas);
            }
        }
        
        if (count > 0 && mDrawEnd) {
            left = width - mDividerWidth;
            right = left + mDividerWidth;
            d.setBounds(left, top, right, bottom);
            d.draw(canvas);
        }
    }
    
    private void drawVerticalDividerLine(Canvas canvas) {
        final Drawable d = mDividerDrawable;
        final int width = getWidth();
        final int height = getHeight();
        int left = mDividerLeftMargin;
        int right = width - mDividerRightMargin;
        int top;
        int bottom;
        if (right <= left) {
            return;
        }
        
        int count = getChildCount();
        if (count > 0 && mDrawStart) {
            top = 0;
            bottom = top + mDividerHeight;
            d.setBounds(left, top, right, bottom);
            d.draw(canvas);
        }
        for (int i = 0; i < count - 1; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                top = child.getBottom();
                bottom = top + mDividerHeight;
                d.setBounds(left, top, right, bottom);
                d.draw(canvas);
            }
        }
        
        if (count > 0 && mDrawEnd) {
            top = height - mDividerHeight;
            bottom = top + mDividerHeight;
            d.setBounds(left, top, right, bottom);
            d.draw(canvas);
        }
    }
}
