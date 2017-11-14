package de.jandrotek.android.aspectra.main;

import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

import de.jandrotek.android.aspectra.core.ImageProcessing;

/**
 * Created by jan on 27.10.2017.
 */

public class CameraHandle {
    private static final String TAG = "CameraHandle";

    public void setActivity(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    private BaseActivity mActivity = null;
    private Camera mCamera = null;
    private ImageProcessing mImageProcessing = null;

    public List<Camera.Size> getSupportedPreviewSizes() {
        return mSupportedPreviewSizes;
    }

    private List<Camera.Size> mSupportedPreviewSizes;
    private int mDegResult;

    public int getResult() {
        return mDegResult;
    }

    public int getDegrees() {
        return mDegrees;
    }

    private int mDegrees = 0;

    public CameraHandle(){
//        mActivity = (BaseActivity) activity;
        if(mImageProcessing == null) {
            mImageProcessing = ImageProcessing.getInstance();
        }

    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters()
                    .getSupportedPreviewSizes();
//            if (mSurfaceCreated) requestLayout();
        }
    }
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
    public void setCameraDisplayOrientation(int cameraId) {
        mDegResult = getCameraDegResult(cameraId);
        mCamera.setDisplayOrientation(mDegResult);
        if(mImageProcessing == null) {
            mImageProcessing = ImageProcessing.getInstance();
        }
        mImageProcessing.setCameraOrientation(mDegResult);
        mActivity.setCameraOrientInImProc(mDegResult);
    }


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

//        int mDegResult;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mDegResult = (info.orientation + mDegrees) % 360;
            mDegResult = (360 - mDegResult) % 360;  // compensate the mirror
        } else {  // back-facing
            mDegResult = (info.orientation - mDegrees + 360) % 360;
        }
        return mDegResult;
    }

    public void setPreviewSize(Camera.Size cameraPreviewSize) {
//    try {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(cameraPreviewSize.width, cameraPreviewSize.height);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
            mCamera.setParameters(parameters);
        }
//    } catch(IOException exception) {
//        if (BuildConfig.DEBUG) {
//            Log.e(TAG, "IOException caused by setParameters()", exception);
//        }
//    }
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

    public int startPreview(Camera.Size cameraPreviewSize) {
        int imageFormat = 0;
        try {
            Camera.Parameters parameters;

            parameters = mCamera.getParameters();
            parameters.setPreviewSize(cameraPreviewSize.width, cameraPreviewSize.height);
            imageFormat = parameters.getPreviewFormat();

            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);

            mCamera.startPreview();
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception caused by setCameraParams()", e);
        }
        return imageFormat;
    }
}
