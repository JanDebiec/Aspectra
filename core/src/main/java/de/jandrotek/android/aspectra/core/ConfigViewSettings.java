package de.jandrotek.android.aspectra.core;

/**
 * Created by jan on 21.01.15.
 */
public class ConfigViewSettings {

    private static ConfigViewSettings mInstance = null;

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

    private float[] mWidthPointsX = new float[4];
    private float[] mHeightPointsY = new float[4];

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
        return mWidthPointsX;
    }

    public float[] getPointsY(){
        return mHeightPointsY;
    }

    public void setConfigDimensions(float widthX, float heightY){
        mConfViewConfigured = true;
        mConfigWidthX = widthX;
        mConfigHeightY = heightY;
    }

    public void setPreviewDimensions(int widthX, int heightY){
        mCamPreviewConfigured = true;
        mPreviewWidthX = (float)widthX;
        mPreviewHeightY = (float)heightY;
        mHeightEndPercentY = mHeightStartPercentY + (mAmountLinesY * 100) / mPreviewHeightY;
    }

    public void calcXYPoints() {
        float deltaX;
        float deltaY;
        float offsetX = 0.0f;
        float offsetY = 0.0f;
        float smallerX = mConfigWidthX;
        float smallerY = mConfigHeightY;
        float faktorK;
        float previewInConfigY;

        deltaX = mPreviewWidthX - mConfigWidthX;
        if(deltaX > 1.0f) { // prefiewX bigger then configX
            faktorK = mConfigWidthX / mPreviewWidthX;
            previewInConfigY = mPreviewHeightY * faktorK;
            offsetY = (mConfigHeightY - previewInConfigY) / 2;
            smallerY = previewInConfigY;
        }

//        deltaY = mConfigHeightY - mPreviewHeightY;
//        if(deltaY > 1.0f) { // we have offset in X, 1.0f because of roundup error
//            offsetY = deltaY / 2;
//            smallerY = mPreviewHeightY;
//        }

        mWidthPointsX[0] = offsetX;
        mWidthPointsX[3] = offsetX + smallerX;
        mHeightPointsY[0] = offsetY;
        mHeightPointsY[3] = offsetY + smallerY;

        mWidthPointsX[1] = offsetX + mWidthStartPercentX * smallerX / 100;
        mWidthPointsX[2] = offsetX + mWidthEndPercentX * smallerX / 100;

        mHeightPointsY[1] = offsetY +  mHeightStartPercentY * smallerY / 100;
        mHeightPointsY[2] = offsetY + mHeightEndPercentY * smallerY / 100;
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
