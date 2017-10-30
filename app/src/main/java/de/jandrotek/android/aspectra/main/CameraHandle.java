package de.jandrotek.android.aspectra.main;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by jan on 27.10.2017.
 */

public class CameraHandle {
    private static final String TAG = "CameraHandle";

    private Activity mActivity = null;
    private Camera mCamera = null;
    private int mResult;

    public int getResult() {
        return mResult;
    }

    public int getDegrees() {
        return mDegrees;
    }

    private int mDegrees = 0;

//    public CameraHandle(){
    public CameraHandle(Context context){
        mActivity = (Activity) context;

    }

//    public void setCamera(Camera camera) {
//        mCamera = camera;
//        if (mCamera != null) {
//            mSupportedPreviewSizes = mCamera.getParameters()
//                    .getSupportedPreviewSizes();
//            if (mSurfaceCreated) requestLayout();
//        }
//    }
//
//    public void switchCamera(Camera camera) {
//        setCamera(camera);
//        try {
//            camera.setPreviewDisplay(mCameraHolder);
//        } catch (IOException exception) {
//            if(BuildConfig.DEBUG) {
//                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
//            }
//        }
//    }

    public  int getCameraDegResult(int cameraId) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int displayRotation = mActivity.getWindowManager().getDefaultDisplay()
                .getRotation();
//        int mDegrees = 0;
        switch (displayRotation) {
            case Surface.ROTATION_0: mDegrees = 0; break;
            case Surface.ROTATION_90: mDegrees = 90; break;
            case Surface.ROTATION_180: mDegrees = 180; break;
            case Surface.ROTATION_270: mDegrees = 270; break;
        }

//        int mResult;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mResult = (info.orientation + mDegrees) % 360;
            mResult = (360 - mResult) % 360;  // compensate the mirror
        } else {  // back-facing
            mResult = (info.orientation - mDegrees + 360) % 360;
        }
        return mResult;
    }

    public void setPreviewSize(Camera.Size cameraPreviewSize) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(cameraPreviewSize.width, cameraPreviewSize.height);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        mCamera.setParameters(parameters);
    }

    public void setPreview(SurfaceHolder holder, CameraPreview preview) {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.setPreviewCallback(preview);
            }
        } catch (IOException exception) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
            }
        }

    }

    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
        }
    }

}
