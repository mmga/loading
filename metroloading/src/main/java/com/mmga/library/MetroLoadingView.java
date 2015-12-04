package com.mmga.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MetroLoadingView extends View {

    private int mShape;
    private int mWidth;
    private int mHeight;
    private int mRadius;
    private int mColor;
    private int mNumber;
    private boolean mHasShadow;
    private int mShadowColor;
    private int mDuration;
    private int mDelay;
    Paint shadowPaint, bodyPaint;
    public boolean isAnimating = false;
    AnimatorSet set;

    List<Integer> lefts = new ArrayList<>();
    List<Animator> valueAnimators = new ArrayList<>();

    public static final int rectangle = 0;
    public static final int circle = 1;
//    private boolean mTransform;
//    private int mTransformHeight;
//    private int mTransformWidth;
//    private float mTransformHeightScale,mTransformwidthScale;

    public MetroLoadingView(Context context) {
        super(context);
        initView(context, null);
    }

    public MetroLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MetroLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        bodyPaint = new Paint();
        bodyPaint.setColor(mColor);
        bodyPaint.setAntiAlias(true);
        shadowPaint = new Paint();
        shadowPaint.setColor(mShadowColor);
        shadowPaint.setAntiAlias(true);

        for (int i = 0; i < mNumber; i++) {
            lefts.add(-mWidth);
        }
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MetroLoadingView);
            mColor = a.getColor(R.styleable.MetroLoadingView_indicator_color, Color.WHITE);
            mWidth = a.getDimensionPixelSize(R.styleable.MetroLoadingView_indicator_width, dp2px(4));
            mHeight = a.getDimensionPixelSize(R.styleable.MetroLoadingView_indicator_height, dp2px(9));
            mRadius = a.getDimensionPixelSize(R.styleable.MetroLoadingView_indicator_radius, dp2px(4));
            mNumber = a.getInt(R.styleable.MetroLoadingView_number, 5);
            mHasShadow = a.getBoolean(R.styleable.MetroLoadingView_has_shadow, false);
            mShadowColor = a.getColor(R.styleable.MetroLoadingView_shadow_color, Color.DKGRAY);
            mDuration = a.getInt(R.styleable.MetroLoadingView_duration_in_mills, 2000);
            mDelay = a.getInt(R.styleable.MetroLoadingView_interval_in_mills, 200);
            mShape = a.getInt(R.styleable.MetroLoadingView_indicator, rectangle);
//            mTransform =a.getBoolean(R.styleable.MetroLoadingView_transform, false);
//            mTransformHeight = a.getDimensionPixelSize(R.styleable.MetroLoadingView_indicator_width, dp2px(6));
//            mTransformWidth = a.getDimensionPixelSize(R.styleable.MetroLoadingView_indicator_width, dp2px(2));
//
//            mTransformHeightScale = mTransformHeight / mHeight;
//            mTransformwidthScale = mTransformWidth / mWidth;
            a.recycle();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mShape) {
            case (rectangle):
                for (int i = 0; i < mNumber; i++) {
                    if (mHasShadow) {
                        canvas.drawRect((float) (lefts.get(i) + 0.25 * mWidth), (float) (0.25 * mHeight), (float) (lefts.get(i) + 1.25 * mWidth), (float) (1.25 * mHeight), shadowPaint);
                    }
                    canvas.drawRect(lefts.get(i), 0, lefts.get(i) + mWidth, mHeight, bodyPaint);
                }
                break;
            case (circle):
                for (int i = 0; i < mNumber; i++) {
                    if (mHasShadow) {
                        canvas.drawCircle((float) (lefts.get(i) - 0.5 * mRadius), (float) (mRadius * 1.5), mRadius, shadowPaint);
                    }
                    canvas.drawCircle((float) (lefts.get(i) - mRadius), (float) (mRadius), mRadius, bodyPaint);
                }
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = Math.max(mHeight, 2* mRadius);
        setMeasuredDimension(getWindowWidth(),  (2 * height));
    }

    public void start() {
        Log.d("mmga", "start");
        this.setVisibility(VISIBLE);

        for (int i = 0; i < mNumber; i++) {
            lefts.set(i, -mWidth);
            ValueAnimator animator = new ValueAnimator();
            valueAnimators.add(animator);
            initAnim(animator, i, mDelay * i);
        }
        set = new AnimatorSet();
        set.playTogether(valueAnimators);
        set.setDuration(mDuration);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                set.start();
                Log.d("mmga", "end");
            }
        });
        set.start();

        isAnimating = true;
    }

    private void initAnim(ValueAnimator animator, final int i, int startDelay) {
        animator.setFloatValues(0, 1f);
        animator.setStartDelay(startDelay);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                if (mShape == rectangle) {
                    lefts.set(i, (int) (fraction * getWindowWidth()));
                } else if (mShape == circle) {
                    lefts.set(i, (int) (fraction * (getWindowWidth() + 2 * mRadius)));
                }

                invalidate();
            }
        });
        animator.setInterpolator(new CustomInterpolator());

    }


    public void stop() {
        set.cancel();
        this.setVisibility(GONE);
        isAnimating = false;
        valueAnimators.clear();

//        lefts.clear();
        Log.d("mmga", "canceled");
    }


    private int getWindowWidth() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }


    private class CustomInterpolator implements TimeInterpolator {
        @Override
        public float getInterpolation(float input) {
            return (float) (Math.asin(2 * input - 1) / Math.PI + 0.5);
        }
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }





}

