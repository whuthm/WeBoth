package com.nm.both.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nm.both.R;

/**
 * Created by whuthm on 2016/3/18.
 */
public class TabView extends RelativeLayout {

    private TextView mTextView;
    private ImageView mIconView;

    private Drawable mIcon;
    private CharSequence mText;
    private boolean mRemain = false;
    private View mRemainView;

    public TabView(Context context) {
        super(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextView = (TextView) findViewById(R.id.tab_text);
        mIconView = (ImageView) findViewById(R.id.tab_icon);
        mRemainView = findViewById(R.id.tab_remain);
        remain(mRemain);
        updateTextAndIcon();
    }

    public void remain(boolean remain) {
        mRemain = remain;
        if (mRemainView != null) {
            mRemainView.setVisibility(remain ? VISIBLE : GONE);
        }
    }

    public TabView setTab(TabLayout.Tab tab) {
        mIcon = tab.getIcon();
        mText = tab.getText();
        updateTextAndIcon();
        return this;
    }

    private void updateTextAndIcon() {
        if (mText != null && mTextView != null) {
            mTextView.setText(mText);
        }
        if (mIcon != null && mIconView != null) {
            mIconView.setImageDrawable(mIcon);
        }
    }
}
