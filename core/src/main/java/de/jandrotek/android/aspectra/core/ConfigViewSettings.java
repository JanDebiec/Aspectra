package de.jandrotek.android.aspectra.core;

/**
 * Created by jan on 21.01.15.
 * changed 30.05.2016
 * we have dimensions and settings :
 * X : spectrum length, Y spectrum width
 * and
 * width: camera longer side, height: camera shorter side.
 * For SpectrumLanscapeOrientation:
 * X == width, Y == height
 * For SpectrumPortraitOrientation
 * X == height, Y = width

 */
public class ConfigViewSettings {

    private static ConfigViewSettings mInstance = null;

    public void setSpectrumOrientationLandscape(boolean mSpectrumOrientationLandscape) {
        this.mSpectrumOrientationLandscape = mSpectrumOrientationLandscape;
    }

    private boolean mSpectrumOrientationLandscape = true;

    private  boolean mConfViewConfigured = false;
    private  boolean mCamPreviewConfigured = false;
    private  boolean mPersentsConfigured = false;

    private float mConfigViewWidth;
    private float mConfigViewHeight;
    private float mCameraPreviewWidth;
    private float mCameraPreviewHeight;

    private float mConfigStartPercentX = 10;
    private float mConfigEndPercentX = 90;
    private float mConfigStartPercentY = 49;
    private float mConfigEndPercentY = 51;


    private float mAmountLinesY;

    private float[] mCrossPointsW = new float[4];
    private float[] mCrossPointsH = new float[4];

    public static ConfigViewSettings getInstance(){
        if(mInstance == null) {
            mInstance = new ConfigViewSettings();
        }
        return mInstance;
    }

    private ConfigViewSettings(){

    }

    public boolean isConfigured(){
         return ((mConfViewConfigured) && (mCamPreviewConfigured) && (mPersentsConfigured));
    }

    public float[] getPointsW() {
        return mCrossPointsW;
    }

    public float[] getPointsH() {
        return mCrossPointsH;
    }

    public void setConfigViewDimensions(float widthX, float heightY) {
        mConfViewConfigured = true;
        mConfigViewWidth = widthX;
        mConfigViewHeight = heightY;
    }

    public void setCameraPreviewDimensions(int widthX, int heightY) {
        mCamPreviewConfigured = true;
        mCameraPreviewWidth = (float) widthX;
        mCameraPreviewHeight = (float) heightY;
        mConfigEndPercentY = mConfigStartPercentY + (mAmountLinesY * 100) / mCameraPreviewHeight;
    }

    public void calcCrossPoints() {
        float deltaX;
        float deltaY;
        float offsetW = 0.0f;
        float offsetH = 0.0f;
        float smallerW = mConfigViewWidth;
        float smallerH = mConfigViewHeight;
        float faktorK;
        float previewInConfigH;

        if (mSpectrumOrientationLandscape) {
            deltaX = mCameraPreviewWidth - mConfigViewWidth;
//            if (deltaX > 1.0f) { // prefiewX bigger then configX
                faktorK = mConfigViewWidth / mCameraPreviewWidth;
            previewInConfigH = mCameraPreviewHeight * faktorK;
            offsetH = (mConfigViewHeight - previewInConfigH) / 2;
            smallerH = previewInConfigH;
//            } else {
//                faktorK = mConfigViewWidth / mCameraPreviewWidth;
//                previewInConfigY = mCameraPreviewHeight * faktorK;
//                offsetY = (previewInConfigY - mConfigViewHeight) / 2;
//                smallerY = mConfigViewHeight;
//            }

            //TODO here is a bug, offsetY must be calculated
            mCrossPointsW[0] = offsetW;
            mCrossPointsW[3] = offsetW + smallerW;
            mCrossPointsH[0] = offsetH;
            mCrossPointsH[3] = offsetH + smallerH;

            mCrossPointsW[1] = offsetW + mConfigStartPercentX * smallerW / 100;
            mCrossPointsW[2] = offsetW + mConfigEndPercentX * smallerW / 100;

            mCrossPointsH[1] = offsetH + mConfigStartPercentY * smallerH / 100;
            mCrossPointsH[2] = offsetH + mConfigEndPercentY * smallerH / 100;
        } else {
//TODO: adapt to spectrum portrait orientation
            deltaX = mCameraPreviewWidth - mConfigViewWidth;
//            if (deltaX > 1.0f) { // prefiewX bigger then configX
                faktorK = mConfigViewWidth / mCameraPreviewWidth;
            previewInConfigH = mCameraPreviewHeight * faktorK;
            offsetH = (mConfigViewHeight - previewInConfigH) / 2;
            smallerH = previewInConfigH;
//            }

            mCrossPointsW[0] = offsetW;
            mCrossPointsW[3] = offsetW + smallerW;

            mCrossPointsH[0] = offsetH;
            mCrossPointsH[3] = offsetH + smallerH;

            mCrossPointsW[1] = offsetW + mConfigStartPercentY * smallerW / 100;
            mCrossPointsW[2] = offsetW + mConfigEndPercentY * smallerW / 100;

            mCrossPointsH[1] = offsetH + mConfigStartPercentX * smallerH / 100;
            mCrossPointsH[2] = offsetH + mConfigEndPercentX * smallerH / 100;
        }
    }

    public void setPercent(float widthStartX, float widthEndX, float heightStartY, float deltaLinesY) {

        mConfigStartPercentX = widthStartX;
        mConfigEndPercentX = widthEndX;
        mConfigStartPercentY = heightStartY;
        mAmountLinesY = deltaLinesY;
        if(mCamPreviewConfigured) {
            mConfigEndPercentY = mConfigStartPercentY + (mAmountLinesY * 100) / mCameraPreviewHeight;
        }
        mPersentsConfigured = true;
    }

    public float getConfigStartPercentX() {
        return mConfigStartPercentX;
    }

    public float getConfigEndPercentX() {
        return mConfigEndPercentX;
    }

    public float getConfigStartPercentY() {
        return mConfigStartPercentY;
    }

    public float getConfigEndPercentY() {
        return mConfigEndPercentY;
    }

    public void setConfigStartPercentX(int configStartPercentX) {
        mConfigStartPercentX = configStartPercentX;
    }

    public void setConfigEndPercentX(int configEndPercentX) {
        mConfigEndPercentX = configEndPercentX;
    }

    public void setConfigStartPercentY(int configStartPercentY) {
        mConfigStartPercentY = configStartPercentY;
    }

    public void setConfigEndPercentY(int configEndPercentY) {
        mConfigEndPercentY = configEndPercentY;
    }

    public float getAmountLinesY() {
        return mAmountLinesY;
    }

    public void setAmountLinesY(float amountLinesY) {
        mAmountLinesY = amountLinesY;
    }
}
