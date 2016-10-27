package com.nm.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by huangming on 2016/10/19.
 */

public class VideoLayout extends FrameLayout {

    private final static float ASPECT_RATIO = ((float) 16) / 9;

    private float mAspectRatio = ASPECT_RATIO;


    public VideoLayout(Context context) {
        super(context);
    }

    public VideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置视频的宽高比:默认16:9
     */
    public void setAspectRatio(float aspectRatio) {
        if (mAspectRatio != aspectRatio) {
            mAspectRatio = aspectRatio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSpecSize;
        int height = heightSpecSize;
        int widthMode = widthSpecMode;
        int heightMode = heightSpecMode;
        if (mAspectRatio > 0f) {
            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode != MeasureSpec.EXACTLY) {
                height = heightSpecMode == MeasureSpec.UNSPECIFIED ? (int) (widthSpecSize / mAspectRatio) : Math.min((int) (widthSpecSize / mAspectRatio), widthSpecSize);
                heightMode = MeasureSpec.EXACTLY;
            } else if (widthSpecMode != MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecMode == MeasureSpec.UNSPECIFIED ? (int) (heightSpecSize * mAspectRatio) : Math.min((int) (heightSpecSize * mAspectRatio), heightSpecSize);
                widthMode = MeasureSpec.EXACTLY;
            }
        }

        width = Math.max(width, 0);
        height = Math.max(height, 0);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode), MeasureSpec.makeMeasureSpec(height, heightMode));
    }

}
