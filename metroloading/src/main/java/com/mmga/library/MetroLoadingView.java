package com.mmga.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MetroLoadingView extends View {

    private int width = 9;
    private int height = 16;
    private int mColor = Color.WHITE;
    public boolean isAnimating = false;
    private int delay = 200;
    private int number = 5;
    AnimatorSet set;

    ArrayList<Paint> paints = new ArrayList<>();
    List<Integer> lefts = new ArrayList<>();
    List<Animator> valueAnimators = new ArrayList<>();

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

        for (int i = 0; i < number; i++) {
            lefts.add(-width);
            Paint paint = new Paint();
            paint.setColor(mColor);
            paint.setAntiAlias(true);
            paints.add(paint);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < number; i++) {
            canvas.drawRect(lefts.get(i), 0, lefts.get(i) + width, height, paints.get(i));
        }

    }


    public void start() {
        this.setVisibility(VISIBLE);

        for (int i = 0; i < number; i++) {
            ValueAnimator animator = new ValueAnimator();
            valueAnimators.add(animator);
            startAnim(animator, i, delay * i);
        }
        set = new AnimatorSet();
        set.playTogether(valueAnimators);
        set.setDuration(2000);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                set.start();
            }
        });
        set.start();

        isAnimating = true;
    }

    private void startAnim(ValueAnimator animator, final int i, int startDelay) {
//        if (i == number - 1) {
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
////                    set.start();
//                }
//            });
//        }
//        animator.setDuration(2000);
        animator.setFloatValues(0, 1f);
        animator.setStartDelay(startDelay);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                lefts.set(i, (int) (fraction * getWindowWidth()));
                invalidate();
            }
        });
        animator.setInterpolator(new CustomInterpolator());

    }


    public void stop() {
//        set.cancel();
//        this.setVisibility(GONE);
        isAnimating = false;
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


}

