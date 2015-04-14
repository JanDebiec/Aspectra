package com.jandrotek.android.aspectra.lib;

/**
 * Created by jan on 09.12.14.
 */
public class Peak {
    private int mPosition;
    private int mValue;
    private int mLeftBorderPos;
    private int mLeftBorderValue;
    private int mRightBorderPos;
    private int mRightBorderValue;

    public Peak(){

    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }

    public int getLeftBorderPos() {
        return mLeftBorderPos;
    }

    public void setLeftBorderPos(int leftBorderPos) {
        mLeftBorderPos = leftBorderPos;
    }

    public int getLeftBorderValue() {
        return mLeftBorderValue;
    }

    public void setLeftBorderValue(int leftBorderValue) {
        mLeftBorderValue = leftBorderValue;
    }

    public int getRightBorderPos() {
        return mRightBorderPos;
    }

    public void setRightBorderPos(int rightBorderPos) {
        mRightBorderPos = rightBorderPos;
    }

    public int getRightBorderValue() {
        return mRightBorderValue;
    }

    public void setRightBorderValue(int rightBorderValue) {
        mRightBorderValue = rightBorderValue;
    }
}
