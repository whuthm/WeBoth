package com.nm.base.view;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.nm.base.R;
import com.nm.base.log.Logger;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class AsyncImageView extends com.facebook.drawee.view.SimpleDraweeView {

    private static final String TAG = "AsyncImageView";

    private static final int SHAPE_SQUARE = 1;
    private static final int SHAPE_ROUND = 2;

    private Uri mUri;
    private int mFailedImage;
    private int mDefaultImage;

    private GenericDraweeHierarchy mHierarchy;
    private GenericDraweeHierarchyBuilder mDraweeBuilder;

    private int mShape;

    public AsyncImageView(Context context) {
        this(context, null);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final Resources res = getResources();
        mDraweeBuilder = new GenericDraweeHierarchyBuilder(res);

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.AsyncImageView);
        mShape = a.getInt(R.styleable.AsyncImageView_shape, -1);
        Logger.i(TAG, "SHAPE = " + mShape);
        a.recycle();
    }

    public void setImageUrl(String url) {
        setImageUrl(url, 0);
    }

    public void setImageUrl(String url, int defaultImage) {
        setImageUrl(url, defaultImage, 0);
    }

    public void setImageUrl(String url, int defaultImage, int failedImage) {
        if (url == null) {
            url = "";
        }
        setImageUri(Uri.parse(url), defaultImage, failedImage);
    }

    public void setImageUri(Uri uri, int defaultImage, int failedImage) {
        mUri = uri;
        Logger.i(TAG, "set image url = " + mUri);
        mFailedImage = failedImage;
        mDefaultImage = defaultImage;

        ScalingUtils.ScaleType scaleType = convertToScaleType(getScaleType());

        mDraweeBuilder.setFadeDuration(300).setActualImageScaleType(scaleType);
        if (mFailedImage > 0) {
            mDraweeBuilder.setFailureImage(getResources().getDrawable(mFailedImage));
        }
        if (mDefaultImage > 0) {
            mDraweeBuilder.setPlaceholderImage(getResources().getDrawable(mDefaultImage),
                    scaleType);
        }
        mHierarchy = mDraweeBuilder.build();
        setHierarchy(mHierarchy);

        if (isRound()) {
            RoundingParams roundingParams = RoundingParams.asCircle();
            mHierarchy.setRoundingParams(roundingParams);
        } else if (isSquare()) {
            setAspectRatio(1.0f);
        }

        Log.e(TAG, "before");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                // .setTapToRetryEnabled(true)
                .setOldController(getController())
                .setControllerListener(mControllerListener).setUri(mUri).build();
        setController(controller);
        Log.e(TAG, "after");
    }

    public void setImageUri(Uri uri) {
        setImageUri(uri, 0, 0);
    }

    private boolean isRound() {
        return mShape == SHAPE_ROUND;
    }

    private boolean isSquare() {
        return mShape == SHAPE_SQUARE;
    }

    private ScalingUtils.ScaleType convertToScaleType(ImageView.ScaleType type) {

        ScalingUtils.ScaleType scaleType = ScalingUtils.ScaleType.CENTER_CROP;

        switch (type) {

            case CENTER:
                return ScalingUtils.ScaleType.CENTER;
            case CENTER_CROP:
                return ScalingUtils.ScaleType.CENTER_CROP;
            case CENTER_INSIDE:
                return ScalingUtils.ScaleType.CENTER_INSIDE;
            case FIT_CENTER:
                return ScalingUtils.ScaleType.FIT_CENTER;
            case FIT_END:
                return ScalingUtils.ScaleType.FIT_END;
            case FIT_START:
                return ScalingUtils.ScaleType.FIT_START;
            case FIT_XY:
                return ScalingUtils.ScaleType.FIT_XY;
            case MATRIX:
                break;
        }

        return scaleType;
    }

    private BaseControllerListener<ImageInfo> mControllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onRelease(String id) {
            super.onRelease(id);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            final String s = throwable != null ? throwable.getMessage() : "unknown";
            Logger.i(TAG, mUri + "---onFailure:" + s);
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
        }

        @Override
        public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
            super.onIntermediateImageSet(id, imageInfo);
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo,
                                    Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            Logger.i(TAG, mUri + "---onFinalImageSet" + imageInfo.getWidth() + "  "
                    + imageInfo.getHeight());
        }

        @Override
        public void onSubmit(String id, Object callerContext) {
            super.onSubmit(id, callerContext);
        }
    };

}
