package com.mmga.library;


import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.List;

public abstract class BaseIndicatorController {

    private View mTarget;

    private List<Animator> mAnimators;

    public void setmTarget(View mTarget) {
        this.mTarget = mTarget;
    }

    public View getmTarget() {
        return mTarget;
    }

    public int getWidth() {
        return mTarget.getWidth();
    }

    public int getHeight() {
        return mTarget.getHeight();
    }

    public void postInvalidate() {
        mTarget.postInvalidate();
    }


    public abstract void draw(Canvas canvas, Paint paint);

    public abstract List<Animator> createAnimation();

    public void initAnimation() {
        mAnimators = createAnimation();
    }

    public void setmAnimationStatus(AnimStatus animStatus) {
        if (mAnimators == null) {
            return;
        }
        int count = mAnimators.size();
        for (int i = 0; i < count; i++) {
            Animator animator = mAnimators.get(i);
            boolean isRunning = animator.isRunning();
            switch (animStatus) {
                case START:
                    if (!isRunning) {
                        animator.start();
                    }
                    break;
                case END:
                    if (!isRunning) {
                        animator.end();
                    }
                    break;
                case CANCEL:
                    animator.cancel();
                    break;
            }
        }
    }

    public enum AnimStatus {
        START,END,CANCEL
    }
}
