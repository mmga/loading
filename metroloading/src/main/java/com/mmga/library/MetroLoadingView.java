package com.mmga.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import com.mmga.library.Indicator.MetroIndicator;

public class MetroLoadingView extends View {

    public static final int rectangle = 0;
    public static final int circle = 1;
    public static final int metro = 2;

    public static final int DEFAULT_SIZE = 40;

    @IntDef(flag = true,
            value = {rectangle,circle,metro})
    public @interface Indicator {
    }

    int mIndicatorId;
    int mIndicatorColor;

    Paint mPaint;

    private BaseIndicatorController mIndicatorController;
    private boolean mHasAnimation;


    public MetroLoadingView(Context context) {
        super(context);
        init(null, 0);
    }

    public MetroLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MetroLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public MetroLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(attrs, defStyleAttr);
//    }


    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MetroLoadingView);
        mIndicatorId = a.getInt(R.styleable.MetroLoadingView_indicator, metro);
        mIndicatorColor = a.getColor(R.styleable.MetroLoadingView_indicator_color, Color.rgb(31, 174, 255));
        a.recycle();

        mPaint=new Paint();
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        applyIndicator();
    }

    private void applyIndicator() {
        mIndicatorController = new MetroIndicator();
        mIndicatorController.setmTarget(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(widthMeasureSpec, dp2px(DEFAULT_SIZE));
        int height = measureDimension(heightMeasureSpec, dp2px(DEFAULT_SIZE));
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int measureSpec, int defaultSize) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mIndicatorController.draw(canvas, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation) {
            mHasAnimation = true;
            applyIndicator();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIndicatorController.setmAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
    }

    @Override
    public void setVisibility(int visibility) {
        if (getVisibility() != visibility) {
            super.setVisibility(visibility);
            if (visibility == GONE || visibility == INVISIBLE) {
                mIndicatorController.setmAnimationStatus(BaseIndicatorController.AnimStatus.END);
            } else {
                mIndicatorController.setmAnimationStatus(BaseIndicatorController.AnimStatus.START);
            }
        }

    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }
}
