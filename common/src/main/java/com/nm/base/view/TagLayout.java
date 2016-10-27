package com.nm.base.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nm.base.R;

/**
 * Created by huangming on 2015/10/29.
 */
public class TagLayout extends ViewGroup implements View.OnClickListener,
        ViewGroup.OnHierarchyChangeListener {

    private static final int MODE_ADAPTABLE = 0;
    private static final int MODE_FIXED = 1;

    private static final String TAG_FOOTER = "footer";
    private final int mMode;
    private final int mNumColumns;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private boolean mSingleChoice;
    private View mFooterView;
    private boolean mRejectSelected = false;
    private SparseArray<List<View>> mSparseViews = new SparseArray<List<View>>();
    private SparseArray<Integer> mSparseHeights = new SparseArray<Integer>();

    private HashMap<Object, Boolean> mTagStates = new HashMap<Object, Boolean>();

    private OnTagSelectedListener mTagListener;
    private boolean mCancelable = false;
    private boolean mIsSingleLine = false;

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        mSingleChoice = a.getBoolean(R.styleable.TagLayout_singleChoice, true);
        mHorizontalSpacing = a.getDimensionPixelOffset(R.styleable.TagLayout_android_horizontalSpacing, 0);
        mHorizontalSpacing = Math.max(0, mHorizontalSpacing);
        mVerticalSpacing = a.getDimensionPixelOffset(R.styleable.TagLayout_android_verticalSpacing, 0);
        mVerticalSpacing = Math.max(0, mVerticalSpacing);
        mMode = a.getInt(R.styleable.TagLayout_tagMode, MODE_ADAPTABLE);
        mNumColumns = Math.max(1, a.getInt(R.styleable.TagLayout_android_numColumns, 1));
        mCancelable = a.getBoolean(R.styleable.TagLayout_cancelable, false);
        mIsSingleLine = a.getBoolean(R.styleable.TagLayout_singleLine, false);
        a.recycle();

        setOnHierarchyChangeListener(this);
    }

    public void setTagRejectSelected(boolean rejectSelected) {
        mRejectSelected = rejectSelected;
    }

    public void setOnTagSelectedListener(OnTagSelectedListener l) {
        mTagListener = l;
    }

    private void notifyTagSelectedChange(Object tag, boolean selected) {
        if (mTagListener != null) {
            mTagListener.onTagSelectedChange(tag, selected);
        }
    }

    public void addTag(Object tag, View child) {
        addTag(tag, child, false);
    }

    public void addTag(Object tag, View child, boolean selected) {
        child.setTag(tag);
        addView(child);

        setChildrenSelected(child, selected);
    }

    public void addFooterView(View view) {
        super.addView(view, getChildCount() - 1, generateDefaultLayoutParams());
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        Object tag = child.getTag();
        if (tag != null) {
            if (TAG_FOOTER.equals(tag)) {
                if (mFooterView != null) {
                    removeView(mFooterView);
                }
                mFooterView = child;
                super.addView(child, getChildCount() - 1, generateDefaultLayoutParams());
            } else if (!mTagStates.containsKey(tag)) {
                int realIndex = index;
                if (mFooterView != null) {
                    realIndex = getChildCount() - 1;
                }
                super.addView(child, realIndex, params);
                child.setOnClickListener(this);
                mTagStates.put(tag, child.isSelected());
            } else {
                Log.e("TagLayout", "tag duplicate : " + tag);
            }
        } else {
            Log.e("TagLayout", "tag = null");
        }
    }

    public void setSelectedIndex(int index) {
        setChildrenSelected(getChildAt(index), true);
    }

    /**
     * 手动设定某个 item 的显示状态，不触发选择回调
     *
     * @param index    item 的序号
     * @param isSelect 是否选中
     */
    public void setItemSelectInView(int index, boolean isSelect) {
        int count = getChildCount();
        if (index >= 0 && index < count) {
            View v = getChildAt(index);
            v.setSelected(isSelect);
        }
    }

    private void setChildrenSelected(View curChild, boolean curSelected) {
        int curIndex = indexOfChild(curChild);
        if (curIndex >= 0) {
            if (mSingleChoice) {
                int count = getChildCount();
                Object tag = null;
                boolean isSelected = false;
                for (int i = 0; i < count; i++) {
                    View v = getChildAt(i);
                    // 取消选中
                    if (v.isSelected() && mCancelable && curIndex == i) {
                        v.setSelected(false);
                        setChildStatus(v, false);
                        tag = v.getTag();
                        isSelected = v.isSelected();
                        break;
                    }

                    //选择其他
                    if (curSelected) {
                        v.setSelected(i == curIndex);
                        if (isSelected != v.isSelected()) {
                            if (tag == null) {
                                tag = v.getTag();
                                isSelected = v.isSelected();
                                deselectAll();
                                v.setSelected(true);
                                setChildStatus(v, true);
                            }
                        }
                    }
                }

                // 只回调点击的 item
                if (tag != null) {
                    notifyTagSelectedChange(tag, isSelected);
                }
            } else {
                boolean isSelected = curChild.isSelected();
                if (isSelected != curSelected) {
                    curChild.setSelected(curSelected);
                    setChildStatus(curChild, curChild.isSelected());
                    notifyTagSelectedChange(curChild.getTag(), curChild.isSelected());
                }
            }
        }

    }

    public void selectAll() {
        resetAllChildStatus(true);
    }

    public void deselectAll() {
        resetAllChildStatus(false);
    }

    private void resetAllChildStatus(boolean status) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setSelected(status);
            setChildStatus(v, status);
        }
    }

    private void setChildStatus(View v, boolean status) {
        if (v.getTag() != null) {
            mTagStates.remove(v.getTag());
            mTagStates.put(v.getTag(), status);
        }
    }

    public List<Object> getSelectedTags() {
        List<Object> tags = new ArrayList<Object>();
        for (Object tag : mTagStates.keySet()) {
            if (mTagStates.get(tag)) {
                tags.add(tag);
            }
        }
        return tags;
    }

    public Object getSelectedTag() {
        List<Object> tags = getSelectedTags();
        if (tags != null && tags.size() > 0) {
            return tags.get(0);
        }
        return null;
    }

    public void setSingleLine(boolean isSingleLine) {
        mIsSingleLine = isSingleLine;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mSparseViews.clear();
        mSparseHeights.clear();

        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int maxWidth = widthSpecSize - paddingLeft - paddingRight;
        int newWidth = 0;
        int maxHeight = heightSpecSize - paddingTop - paddingBottom;
        int newHeight = 0;

        final int size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = child.getLayoutParams();

                int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                        paddingLeft + paddingRight, lp.width);
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                        paddingTop + paddingBottom, lp.height);

                if (mMode == MODE_FIXED) {
                    int childFixedWidth = (maxWidth - (mNumColumns - 1) * mHorizontalSpacing) / mNumColumns;
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childFixedWidth, MeasureSpec.EXACTLY);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

        // 未使用，计算minWidth minHeight,以及background的值
        // int suggestedWidth = getSuggestedMinimumWidth();
        // int suggestedHeight = getSuggestedMinimumHeight();

        int count = getChildCount();
        int rowIndex = 0;
        int rowMaxWidth = 0;
        if ((maxWidth > 0 && widthSpecMode != MeasureSpec.UNSPECIFIED)
                || (widthSpecMode == MeasureSpec.UNSPECIFIED)) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    List<View> views = mSparseViews.get(rowIndex);
                    Integer childMaxHeight = mSparseHeights.get(rowIndex);
                    if (views == null) {
                        views = new ArrayList<View>();
                        mSparseViews.put(rowIndex, views);
                    }
                    if (childMaxHeight == null) {
                        childMaxHeight = new Integer(0);
                        mSparseHeights.put(rowIndex, childMaxHeight);
                    }

                    int childWidth = child.getMeasuredWidth();
                    int childHeight = child.getMeasuredHeight();

                    if (childHeight > childMaxHeight) {
                        mSparseHeights.put(rowIndex, childHeight);
                    }

                    int rowChildCount = views.size();
                    if (widthSpecMode != MeasureSpec.UNSPECIFIED) {
                        int tempRowWidth = rowMaxWidth + childWidth
                                + (rowChildCount > 0 ? mHorizontalSpacing : 0);
                        if (rowChildCount <= 0 || tempRowWidth <= maxWidth) {
                            rowMaxWidth = tempRowWidth;
                            views.add(child);
                        } else {
                            if (!mIsSingleLine) {
                                rowIndex++;
                                views = new ArrayList<View>();
                                mSparseViews.put(rowIndex, views);
                                mSparseHeights.put(rowIndex, childHeight);
                                rowMaxWidth = childWidth;
                                views.add(child);
                            }
                        }
                    } else {
                        rowMaxWidth += childWidth
                                + (rowChildCount > 0 ? mHorizontalSpacing : 0);
                        views.add(child);
                    }
                    newWidth = rowMaxWidth;
                }

            }

            int rowCount = mSparseViews.size();
            if (widthSpecMode == MeasureSpec.AT_MOST) {
                if (rowCount <= 1) {
                    newWidth = Math.min(newWidth, maxWidth);
                } else {
                    newWidth = maxWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                newWidth = maxWidth;
            }
        }

        int rowMaxHeight = 0;
        int rowCount = mSparseHeights.size();
        for (int i = 0; i < rowCount; i++) {
            rowMaxHeight += mSparseHeights.get(i) + (i > 0 ? mVerticalSpacing : 0);
        }
        if (heightSpecMode == MeasureSpec.AT_MOST) {
            newHeight = Math.min(rowMaxHeight, maxHeight);
        } else if (heightSpecMode == MeasureSpec.EXACTLY) {
            newHeight = maxHeight;
        } else {
            newHeight = rowMaxHeight;
        }
        setMeasuredDimension(newWidth + paddingLeft + paddingLeft, newHeight + paddingTop
                + paddingBottom);

    }

    @Override
    protected void onLayout(boolean change, int left, int top, int right, int bottom) {
        int rowCount = mSparseHeights.size();
        int rowLeft = getPaddingLeft();
        int rowTop = getPaddingTop();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            List<View> views = mSparseViews.get(rowIndex);
            int childMaxHeight = mSparseHeights.get(rowIndex);
            int rowChildCount = views.size();
            int l = rowLeft;
            for (int i = 0; i < rowChildCount; i++) {
                View child = views.get(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                int t = rowTop + (childMaxHeight - childHeight) / 2;
                child.layout(l, t, l + childWidth, t + childHeight);
                l += childWidth + mHorizontalSpacing;
            }
            rowTop += childMaxHeight + mVerticalSpacing;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mRejectSelected || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (mRejectSelected && action == MotionEvent.ACTION_UP) {
            if (mTagListener != null) {
                mTagListener.onTagRejectSelected();
            }
        }
        return mRejectSelected || super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mSparseViews.clear();
        mSparseHeights.clear();
        mTagStates.clear();
    }

    @Override
    public void onClick(View v) {
        if (indexOfChild(v) >= 0) {
            setChildrenSelected(v, !v.isSelected());
        }
    }

    @Override
    public void onChildViewAdded(View view, View child) {

    }

    @Override
    public void onChildViewRemoved(View view, View child) {
        if (view == this) {
            mTagStates.remove(child.getTag());
        }
    }

    public interface OnTagSelectedListener {
        public void onTagSelectedChange(Object tag, boolean selected);

        public void onTagRejectSelected();
    }
}
