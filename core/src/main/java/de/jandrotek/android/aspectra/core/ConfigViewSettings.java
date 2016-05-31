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
    private float[] mCrosstPointsH = new float[4];

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
        return mCrosstPointsH;
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
        float offsetX = 0.0f;
        float offsetY = 0.0f;
        float smallerX = mConfigViewWidth;
        float smallerY = mConfigViewHeight;
        float faktorK;
        float previewInConfigY;

        if (mSpectrumOrientationLandscape) {
            deltaX = mCameraPreviewWidth - mConfigViewWidth;
            if (deltaX > 1.0f) { // prefiewX bigger then configX
                faktorK = mConfigViewWidth / mCameraPreviewWidth;
                previewInConfigY = mCameraPreviewHeight * faktorK;
                offsetY = (mConfigViewHeight - previewInConfigY) / 2;
                smallerY = previewInConfigY;
            }
            mCrossPointsW[0] = offsetX;
            mCrossPointsW[3] = offsetX + smallerX;
            mCrosstPointsH[0] = offsetY;
            mCrosstPointsH[3] = offsetY + smallerY;

            mCrossPointsW[1] = offsetX + mConfigStartPercentX * smallerX / 100;
            mCrossPointsW[2] = offsetX + mConfigEndPercentX * smallerX / 100;

            mCrosstPointsH[1] = offsetY + mConfigStartPercentY * smallerY / 100;
            mCrosstPointsH[2] = offsetY + mConfigEndPercentY * smallerY / 100;
        } else {
//TODO: adapt to spectrum portrait orientation
            deltaX = mCameraPreviewWidth - mConfigViewWidth;
            if (deltaX > 1.0f) { // prefiewX bigger then configX
                faktorK = mConfigViewWidth / mCameraPreviewWidth;
                previewInConfigY = mCameraPreviewHeight * faktorK;
                offsetY = (mConfigViewHeight - previewInConfigY) / 2;
                smallerY = previewInConfigY;
            }
            mCrossPointsW[0] = offsetX;
            mCrossPointsW[3] = offsetX + smallerX;
            mCrosstPointsH[0] = offsetY;
            mCrosstPointsH[3] = offsetY + smallerY;

            mCrossPointsW[1] = offsetX + mConfigStartPercentX * smallerX / 100;
            mCrossPointsW[2] = offsetX + mConfigEndPercentX * smallerX / 100;

            mCrosstPointsH[1] = offsetY + mConfigStartPercentY * smallerY / 100;
            mCrosstPointsH[2] = offsetY + mConfigEndPercentY * smallerY / 100;
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
