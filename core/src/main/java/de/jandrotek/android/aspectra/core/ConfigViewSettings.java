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

    private float mConfigWidthX;
    private float mConfigHeightY;
    private float mPreviewWidthX;
    private float mPreviewHeightY;

    private float mWidthStartPercentX = 10;
    private float mWidthEndPercentX = 90;
    private float mHeightStartPercentY = 49;
    private float mHeightEndPercentY = 51;


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

    public float[] getPointsX(){
        return mCrossPointsW;
    }

    public float[] getPointsY(){
        return mCrosstPointsH;
    }

    public void setConfigViewDimensions(float widthX, float heightY) {
        mConfViewConfigured = true;
        mConfigWidthX = widthX;
        mConfigHeightY = heightY;
    }

    public void setCameraPreviewDimensions(int widthX, int heightY) {
        mCamPreviewConfigured = true;
        mPreviewWidthX = (float)widthX;
        mPreviewHeightY = (float)heightY;
        mHeightEndPercentY = mHeightStartPercentY + (mAmountLinesY * 100) / mPreviewHeightY;
    }

    public void calcCrossPoints() {
        float deltaX;
        float deltaY;
        float offsetX = 0.0f;
        float offsetY = 0.0f;
        float smallerX = mConfigWidthX;
        float smallerY = mConfigHeightY;
        float faktorK;
        float previewInConfigY;

        if (mSpectrumOrientationLandscape) {
            deltaX = mPreviewWidthX - mConfigWidthX;
            if (deltaX > 1.0f) { // prefiewX bigger then configX
                faktorK = mConfigWidthX / mPreviewWidthX;
                previewInConfigY = mPreviewHeightY * faktorK;
                offsetY = (mConfigHeightY - previewInConfigY) / 2;
                smallerY = previewInConfigY;
            }
            mCrossPointsW[0] = offsetX;
            mCrossPointsW[3] = offsetX + smallerX;
            mCrosstPointsH[0] = offsetY;
            mCrosstPointsH[3] = offsetY + smallerY;

            mCrossPointsW[1] = offsetX + mWidthStartPercentX * smallerX / 100;
            mCrossPointsW[2] = offsetX + mWidthEndPercentX * smallerX / 100;

            mCrosstPointsH[1] = offsetY + mHeightStartPercentY * smallerY / 100;
            mCrosstPointsH[2] = offsetY + mHeightEndPercentY * smallerY / 100;
        } else {
//TODO: adapt to spectrum portrait orientation
            deltaX = mPreviewWidthX - mConfigWidthX;
            if (deltaX > 1.0f) { // prefiewX bigger then configX
                faktorK = mConfigWidthX / mPreviewWidthX;
                previewInConfigY = mPreviewHeightY * faktorK;
                offsetY = (mConfigHeightY - previewInConfigY) / 2;
                smallerY = previewInConfigY;
            }
            mCrossPointsW[0] = offsetX;
            mCrossPointsW[3] = offsetX + smallerX;
            mCrosstPointsH[0] = offsetY;
            mCrosstPointsH[3] = offsetY + smallerY;

            mCrossPointsW[1] = offsetX + mWidthStartPercentX * smallerX / 100;
            mCrossPointsW[2] = offsetX + mWidthEndPercentX * smallerX / 100;

            mCrosstPointsH[1] = offsetY + mHeightStartPercentY * smallerY / 100;
            mCrosstPointsH[2] = offsetY + mHeightEndPercentY * smallerY / 100;
        }
    }

    public void setPercent(float widthStartX, float widthEndX, float heightStartY, float deltaLinesY) {

        mWidthStartPercentX = widthStartX;
        mWidthEndPercentX = widthEndX;
        mHeightStartPercentY = heightStartY;
        mAmountLinesY = deltaLinesY;
        if(mCamPreviewConfigured) {
            mHeightEndPercentY = mHeightStartPercentY + (mAmountLinesY * 100) / mPreviewHeightY;
        }
        mPersentsConfigured = true;
    }

    public float getWidthStartPercentX() {
        return mWidthStartPercentX;
    }

    public float getWidthEndPercentX() {
        return mWidthEndPercentX;
    }

    public float getHeightStartPercentY() {
        return mHeightStartPercentY;
    }

    public float getHeightEndPercentY() {
        return mHeightEndPercentY;
    }

    public void setWidthStartPercentX(int widthStartPercentX) {
        mWidthStartPercentX = widthStartPercentX;
    }

    public void setWidthEndPercentX(int widthEndPercentX) {
        mWidthEndPercentX = widthEndPercentX;
    }

    public void setHeightStartPercentY(int heightStartPercentY) {
        mHeightStartPercentY = heightStartPercentY;
    }

    public void setHeightEndPercentY(int heightEndPercentY) {
        mHeightEndPercentY = heightEndPercentY;
    }

    public float getAmountLinesY() {
        return mAmountLinesY;
    }

    public void setAmountLinesY(float amountLinesY) {
        mAmountLinesY = amountLinesY;
    }
}
