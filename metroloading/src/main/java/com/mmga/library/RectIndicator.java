package com.mmga.library;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectIndicator {
    private int width;
    private int height;
    private int left;
    private int centerPositionX;
    private int centerPositionY;

    int shape;

    private int radius;


    private boolean hasShadow = false;
    Paint bodyPaint;
    Paint shadowPaint;

    public RectIndicator(Paint bodyPaint) {
        this.bodyPaint = bodyPaint;
        hasShadow = false;
    }

    public RectIndicator(Paint bodyPaint, Paint shadowPaint) {
        this.bodyPaint = bodyPaint;
        this.shadowPaint = shadowPaint;
        hasShadow = true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getCenterPositionX() {
        return centerPositionX;
    }

    public void setCenterPositionX(int centerPositionX) {
        this.centerPositionX = centerPositionX;
    }

    public void setCenterPositionY(int centerPositionY) {
        this.centerPositionY = centerPositionY;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }

    public void drawItself(Canvas canvas) {
        switch (shape) {
            case MetroLoadingView.rectangle:
                if (hasShadow) {
                    canvas.drawRect((float) (centerPositionX - 0.25 * width),
                            (float) (centerPositionY - 0.25 * height),
                            (float) (centerPositionX + 0.75 * width),
                            (float) (centerPositionY + 0.75 * height), shadowPaint);
                }
                canvas.drawRect((float) (centerPositionX - 0.5 * width),
                        (float) (centerPositionY - 0.5 * height),
                        (float) (centerPositionX + 0.5 * width),
                        (float) (centerPositionY + 0.5 * height), bodyPaint);
                break;
            case MetroLoadingView.circle:
                if (hasShadow) {
                    canvas.drawCircle((float) (centerPositionX+0.5*radius), (float) (centerPositionY+0.5*radius),radius,shadowPaint);
                }
                canvas.drawCircle(centerPositionX, centerPositionY, radius, bodyPaint);
                break;
        }

    }


}