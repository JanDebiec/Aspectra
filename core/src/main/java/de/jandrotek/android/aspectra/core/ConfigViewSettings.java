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


    // some helpers for control of configuration
    private int mConfigStatus = 0;

    public void clearConfigStatus() {
        mConfigStatus = 0;
    }

    private final int eSpectrumOrientFlag = 0x01;
    private final int eDeviceOrientFlag = 0x02;
    private final int ePercentsFlag = 0x04;
    private final int eCameraPreviewFlag = 0x08;
    private final int eViewDimFlag = 0x10;
    private final int eEndPercentFlag = 0x20;

    private final int eNeededConfig =
            eSpectrumOrientFlag +
                    eDeviceOrientFlag +
                    ePercentsFlag +
                    eCameraPreviewFlag +
                    eViewDimFlag +
                    eEndPercentFlag;

    public void setSpectrumOrientationLandscape(boolean spectrumOrientationLandscape) {
        mSpectrumOrientationLandscape = spectrumOrientationLandscape;
        mConfigStatus |= eSpectrumOrientFlag;
        if (mConfigStatus == eNeededConfig)
            calcCrossPoints();
    }

    private boolean mSpectrumOrientationLandscape = true;
    private int mDeviceOrientation = AspectraGlobals.DEVICE_ORIENTATION_LANDSCAPE;

    public void setDeviceOrientation(int deviceOrientation) {
        mDeviceOrientation = deviceOrientation;
        mConfigStatus |= eDeviceOrientFlag;
        if (mConfigStatus == eNeededConfig)
            calcCrossPoints();
    }

    public boolean isNewCrossPoints() {
        return mNewCrossPoints;
    }

    public void setNewCrossPoints(boolean mNewCrossPoints) {
        this.mNewCrossPoints = mNewCrossPoints;
    }

    private boolean mNewCrossPoints = false;
//
//    private  boolean mConfViewConfigured = false;
//    private  boolean mCamPreviewConfigured = false;
//
//    public void setPercentsConfigured(boolean percentsConfigured) {
//        mPercentsConfigured = percentsConfigured;
//    }
//
//    private boolean mPercentsConfigured = false;

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
        if (mConfigStatus == eNeededConfig)
            return true;
        else
            return false;
    }

    public float[] getPointsW() {
        return mCrossPointsW;
    }

    public float[] getPointsH() {
        return mCrosstPointsH;
    }

    public void setConfigViewDimensions(float widthX, float heightY) {
        mConfigStatus |= eViewDimFlag;
        mConfigViewWidth = widthX;
        mConfigViewHeight = heightY;
        if (mConfigStatus == eNeededConfig)
            calcCrossPoints();
    }

    public void setCameraPreviewDimensions(int previewWidth, int previewHeight) {
        mCameraPreviewWidth = (float) previewWidth;
        mCameraPreviewHeight = (float) previewHeight;
        if (mSpectrumOrientationLandscape) {
            mConfigEndPercentY = mConfigStartPercentY + (mAmountLinesY * 100) / mCameraPreviewHeight;
        } else { // Spectrum PORTRAIT
            mConfigEndPercentY = mConfigStartPercentY + (mAmountLinesY * 100) / mCameraPreviewWidth;
        }
        mConfigStatus |= eCameraPreviewFlag;
        if (mConfigStatus == eNeededConfig)
            calcCrossPoints();
    }

    public void calcCrossPoints() {
        float deltaX;
        float deltaY;
        float offsetX = 0.0f;
        float offsetY = 0.0f;
        float smallerX = mConfigViewWidth;
        float smallerY = mConfigViewHeight;
        float faktorK;
        float previewInConfigX;
        float previewInConfigY;

        if (mDeviceOrientation == AspectraGlobals.DEVICE_ORIENTATION_LANDSCAPE) {
            deltaX = mCameraPreviewWidth - mConfigViewWidth;
//            if (deltaX > 1.0f) { // prefiewX bigger then configX
                faktorK = mConfigViewWidth / mCameraPreviewWidth;
                previewInConfigY = mCameraPreviewHeight * faktorK;
                offsetY = (mConfigViewHeight - previewInConfigY) / 2;
                smallerY = previewInConfigY;
//            }
            if (mSpectrumOrientationLandscape) {
                mCrossPointsW[0] = offsetX;
                mCrossPointsW[3] = offsetX + smallerX;

                mCrosstPointsH[0] = offsetY;
                mCrosstPointsH[3] = offsetY + smallerY;

                mCrossPointsW[1] = offsetX + mConfigStartPercentX * smallerX / 100;
                mCrossPointsW[2] = offsetX + mConfigEndPercentX * smallerX / 100;

                mCrosstPointsH[1] = offsetY + mConfigStartPercentY * smallerY / 100;
                mCrosstPointsH[2] = offsetY + mConfigEndPercentY * smallerY / 100;
            } else { // spectrum portrait
                mCrossPointsW[0] = offsetX;
                mCrossPointsW[3] = offsetX + smallerX;

                mCrosstPointsH[0] = offsetY;
                mCrosstPointsH[3] = offsetY + smallerY;

                mCrossPointsW[1] = offsetX + mConfigStartPercentY * smallerX / 100;
                mCrossPointsW[2] = offsetX + mConfigEndPercentY * smallerX / 100;

                mCrosstPointsH[1] = offsetY + mConfigStartPercentX * smallerY / 100;
                mCrosstPointsH[2] = offsetY + mConfigEndPercentX * smallerY / 100;
            }
        } else if (mDeviceOrientation == AspectraGlobals.DEVICE_ORIENTATION_PORTRAIT) {
            // for camera preview hidht should be considered as view-width, and preview-width as view-height
            deltaY = mCameraPreviewHeight - mConfigViewHeight;
//            if (deltaY > 1.0f) { // preViewY bigger then configY
            faktorK = mConfigViewHeight / mCameraPreviewHeight;
            previewInConfigX = mCameraPreviewWidth * faktorK;
                offsetX = (mConfigViewWidth - previewInConfigX) / 2;
                smallerX = previewInConfigX;
//            }
            if (mSpectrumOrientationLandscape) {
                mCrossPointsW[0] = offsetX;
                mCrossPointsW[3] = offsetX + smallerX;

                mCrosstPointsH[0] = offsetY;
                mCrosstPointsH[3] = offsetY + smallerY;

                mCrossPointsW[1] = offsetX + mConfigStartPercentY * smallerX / 100;
                mCrossPointsW[2] = offsetX + mConfigEndPercentY * smallerX / 100;

                mCrosstPointsH[1] = offsetY + mConfigStartPercentX * smallerY / 100;
                mCrosstPointsH[2] = offsetY + mConfigEndPercentX * smallerY / 100;
            } else { // spectrum portrait
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
        mNewCrossPoints = true;

    }

    public void setPercent(float widthStartX, float widthEndX, float heightStartY, float deltaLinesY) {
        mConfigStatus |= ePercentsFlag;
        mConfigStartPercentX = widthStartX;
        mConfigEndPercentX = widthEndX;
        mConfigStartPercentY = heightStartY;
        mAmountLinesY = deltaLinesY;
//        if(mCamPreviewConfigured) {
        if ((mConfigStatus & eCameraPreviewFlag) == eCameraPreviewFlag) {
            mConfigEndPercentY = mConfigStartPercentY + (mAmountLinesY * 100) / mCameraPreviewHeight;
            mConfigStatus |= eEndPercentFlag;
        }
        if (mConfigStatus == eNeededConfig)
            calcCrossPoints();
        calcCrossPoints();
    }

    public void setConfigStartPercentX(int configStartPercentX) {
        mConfigStartPercentX = configStartPercentX;
        mConfigStatus |= ePercentsFlag;
        if (mConfigStatus == eNeededConfig)
            calcCrossPoints();
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

    public void setAmountLinesY(float amountLinesY) {
        mAmountLinesY = amountLinesY;
    }
}
