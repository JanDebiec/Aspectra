/**
 * This file is part of Aspectra.
 *
 * Aspectra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aspectra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aspectra.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 *
 * Copyright Jan Debiec
 */
package de.jandrotek.android.aspectra.main;

/**
 * Created by jan on 21.12.14.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.ConfigViewSettings;
import de.jandrotek.android.aspectra.core.ImageProcessing;

@SuppressWarnings({"ALL", "deprecation"})
@SuppressLint("ViewConstructor")
public class CameraPreview  extends ViewGroup implements SurfaceHolder.Callback,
        Camera.PreviewCallback {
    private static final String TAG = "CameraPreview";
//    private Camera mCamera = null;
    private CameraHandle mCameraHandle;
    private SurfaceHolder mCameraHolder;
    private ConfigViewSettings mViewSettings = null;
    private Size mCameraPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private boolean mSurfaceCreated = false;
    private int mCameraOwnPreviewWidth;
    private int mCameraOwnPreviewHeight;
    private int mCameraSizeinViewWidth;
    private int mCameraSizeinViewHeight;
    private int mDeviceOrientation;
    private int mResult;

    public int getResult() {
        return mResult;
    }

    public int getDegrees() {
        return mDegrees;
    }

    private int mDegrees = 0;


    public void setDeviceOrientation(int deviceOrientation) {
        mDeviceOrientation = deviceOrientation;
    }


    private byte[] mFrameData = null;
    private int mImageFormat;
    private boolean mbProcessing = false;
    public boolean isProcessingShouldRun() {
        return mbProcessingShouldRun;
    }
    public void setProcessingShouldRun(boolean mbProcessingShouldRun) {
        this.mbProcessingShouldRun = mbProcessingShouldRun;
    }

    private boolean mbProcessingShouldRun = false;

    private Activity mActivity = null;
    private Handler mLVActHandler = null;
    //Handler mVCActHandler = null;
    private ImageProcessing mImageProcessing;

    public CameraPreview(Context context, int activityId) {
        super(context);
        SurfaceView surfaceView;
        //Context mContext = context;
        mActivity = (Activity) context;
        if(activityId == AspectraGlobals.ACT_ITEM_LIVE_VIEW) {
            LiveViewActivity lvActivity = (LiveViewActivity) context;
            mLVActHandler = lvActivity.getHandler();
//        } else if(activityId == BaseActivity.ACT_ITEM_VIEW_CONFIG){
//            ViewConfigActivity vcActivity = (ViewConfigActivity) context;
//            // act. no need for handler in that activity
//            //mVCActHandler = vcActivity.getHandler();
        }
        //as in AOSP sample
        surfaceView = new SurfaceView(context);
        addView(surfaceView);

        mCameraHolder = surfaceView.getHolder();
        mCameraHolder.addCallback(this);
        mCameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mViewSettings = ConfigViewSettings.getInstance();
    }

    @Override
    public void onPreviewFrame(byte[] arg0, Camera arg1) {
        if(mbProcessingShouldRun) {
            // At preview mode, the frame data will push to here.
            if (mImageFormat == ImageFormat.NV21) {
                // TODO: check which format can we support                mCamera.setPreviewDisplay(holder);

                // We only accept the NV21(YUV420) format.
                if (!mbProcessing) {
                    mFrameData = arg0;
                    if (mLVActHandler != null) {
                        mLVActHandler.post(doImageProcessing);
                    }
                }
            }// else TODO add info, toast
        }
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

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
//    throws Exception {
    {
    //TODO: check configuration: portrait/landscape, disable by portrait

        try {
            Parameters parameters;

            parameters = mCamera.getParameters();
            parameters.setPreviewSize(mCameraOwnPreviewWidth, mCameraOwnPreviewHeight);
            requestLayout();
            mImageFormat = parameters.getPreviewFormat();

            setCameraDisplayOrientation(0, mCamera);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);

            mCamera.startPreview();
            mCamera.setParameters(parameters);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception caused by setCameraParams()", e);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
//        try {
            mCameraHandle.setPreview(holder, this);
//        } catch (IOException exception) {
//            if(BuildConfig.DEBUG) {
//                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
//            }
//        }
        if (mCameraPreviewSize == null) requestLayout();
        mSurfaceCreated = true;
    }

//    public void setPreview(SurfaceHolder holder) {
//        if (mCamera != null) {
//            mCamera.setPreviewDisplay(holder);
//            mCamera.setPreviewCallback(this);
//        }
//    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        mCameraHandle.stopPreview();
    }

//    public void stopPreview() {
//        if (mCamera != null) {
//            mCamera.stopPreview();
//            mCamera.setPreviewCallback(null);
//        }
//    }

    private Runnable doImageProcessing = new Runnable() {
        public void run() {
            mbProcessing = true;

            try {
                int[] line = mImageProcessing.extractBinnedLine(mFrameData);
                Message completeMessage =
                        mLVActHandler.obtainMessage(AspectraGlobals.eMessageCompleteLine, line);
                completeMessage.sendToTarget();
            }
            catch (Exception e){
                Log.e(TAG, "Exception caused by mImageProcessing()", e);
            }
            mbProcessing = false;
        }
    };

    private void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
         mResult = mCameraHandle.getCameraDegResult(cameraId);
        camera.setDisplayOrientation(mResult);
    }

//    private int getCameraDegResult(int cameraId) {
//        Camera.CameraInfo info =
//                new Camera.CameraInfo();
//        Camera.getCameraInfo(cameraId, info);
//        int displayRotation = mActivity.getWindowManager().getDefaultDisplay()
//                     .getRotation();
////        int mDegrees = 0;
//        switch (displayRotation) {
//            case Surface.ROTATION_0: mDegrees = 0; break;
//            case Surface.ROTATION_90: mDegrees = 90; break;
//            case Surface.ROTATION_180: mDegrees = 180; break;
//            case Surface.ROTATION_270: mDegrees = 270; break;
//        }
//
////        int mResult;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            mResult = (info.orientation + mDegrees) % 360;
//            mResult = (360 - mResult) % 360;  // compensate the mirror
//        } else {  // back-facing
//            mResult = (info.orientation - mDegrees + 360) % 360;
//        }
//        return mResult;
//    }

//    private void setAutofocusToInfinity(){
//        Parameters parameters;
//
//        parameters = mCamera.getParameters();
//        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
//        mCamera.setParameters(parameters);
//
//    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - h);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - h);
                }
            }
        }
        return optimalSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int viewOwnWidth = resolveSize(getSuggestedMinimumWidth(),
                widthMeasureSpec);
        final int viewOwnHeight = resolveSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        setMeasuredDimension(viewOwnWidth, viewOwnHeight);

        if (mSupportedPreviewSizes != null) {
            if (mDeviceOrientation == AspectraGlobals.DEVICE_ORIENTATION_LANDSCAPE) {
                mCameraPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, viewOwnWidth,
                        viewOwnHeight);
            } else if (mDeviceOrientation == AspectraGlobals.DEVICE_ORIENTATION_PORTRAIT) {
                mCameraPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, viewOwnHeight,
                        viewOwnWidth);
            }
            mCameraOwnPreviewWidth = mCameraPreviewSize.width;
            mCameraOwnPreviewHeight = mCameraPreviewSize.height;
            mImageProcessing.setPictureSize(mCameraOwnPreviewWidth, mCameraOwnPreviewHeight);
            mImageProcessing.configureBinningArea();
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "mCameraOwnPreviewWidth = " + mCameraOwnPreviewWidth + ", mCameraOwnPreviewHeight = " + mCameraOwnPreviewHeight);
            }

        }

        if ( (mCameraPreviewSize != null)) {
//        if ((mCamera != null) && (mCameraPreviewSize != null)) {
            try {
                mCameraHandle.setPreviewSize(mCameraPreviewSize);
            } catch (Exception exception) {
                    Log.e(TAG, "IOException caused by mCamera.setParameters()", exception);
            }
        }
    }

//    private void setPreviewSize(Size cameraPreviewSize) {
//        Parameters parameters = mCamera.getParameters();
//        parameters.setPreviewSize(cameraPreviewSize.width, cameraPreviewSize.height);
//        parameters.setFocusMode(Parameters.FOCUS_MODE_INFINITY);
//        mCamera.setParameters(parameters);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            // child = SurfaceView
            final View child = getChildAt(0);

            final int viewOwnWidth = r - l;
            final int viewOwnHeight = b - t;

            if ((viewOwnHeight > 0) && (viewOwnWidth > 0)) {
                // in portrait mode, camera own height and weith are different as SurfaceView width and height
                // in lanscape mode are the same
                // if orientation portrait, change w with h
                if (mDeviceOrientation == AspectraGlobals.DEVICE_ORIENTATION_LANDSCAPE) {
                    mCameraOwnPreviewWidth = viewOwnWidth;
                    mCameraOwnPreviewHeight = viewOwnHeight;

                    // from camera, mResult of getOptiomalPreviewSize()
                    if (mCameraPreviewSize != null) {
                        mCameraSizeinViewWidth = mCameraPreviewSize.width;
                        mCameraSizeinViewHeight = mCameraPreviewSize.height;
                    }
                } else if (mDeviceOrientation == AspectraGlobals.DEVICE_ORIENTATION_PORTRAIT) {
                    //noinspection SuspiciousNameCombination
                    mCameraSizeinViewHeight = viewOwnWidth;
                    //noinspection SuspiciousNameCombination
                    mCameraSizeinViewWidth = viewOwnHeight;

                    // from camera, mResult of getOptiomalPreviewSize()
                    if (mCameraPreviewSize != null) {
                        //noinspection SuspiciousNameCombination
                        mCameraSizeinViewHeight = mCameraPreviewSize.width;
                        //noinspection SuspiciousNameCombination
                        mCameraSizeinViewWidth = mCameraPreviewSize.height;
                    }
                }

                if ((mCameraSizeinViewWidth > 0) && (mCameraSizeinViewHeight > 0)) {

                    // Center the child SurfaceView within the parent.
                    // resolve the variables for debugging:
                    int child_left, child_top, child_right, child_bottom;

                    //alpha and beta are the angles between diagonals, on the right side
                    // alpha for parent, beta for child
                    int angle_beta;
                    int angle_alpha;

                    //calculate beta
                    angle_beta = viewOwnWidth * mCameraSizeinViewHeight;
                    //calculate alpha
                    angle_alpha = viewOwnHeight * mCameraSizeinViewWidth;

                    // if child placed vertical, free spaces left and right
                    if (angle_beta > angle_alpha) {
                        final int scaledChildWidth = angle_alpha / mCameraSizeinViewHeight;
                        child_left = (viewOwnWidth - scaledChildWidth) / 2;
                        child_top = 0;
                        child_right = (viewOwnWidth + scaledChildWidth) / 2;
                        child_bottom = viewOwnHeight;
                    } else { // child placed horizontal, free space top and bottom
                        final int scaledChildHeight = angle_beta / mCameraSizeinViewWidth;
                        child_left = 0;
                        child_top = (viewOwnHeight - scaledChildHeight) / 2;
                        child_right = viewOwnWidth;
                        child_bottom = (viewOwnHeight + scaledChildHeight) / 2;
                    }
                    child.layout(child_left, child_top, child_right, child_bottom);
                    if (mViewSettings == null) {
                        mViewSettings = ConfigViewSettings.getInstance();
                    }
                    int previewWidth = child_right - child_left;
                    int previewHeight = child_bottom - child_top;
                    mViewSettings.setCameraPreviewDimensions(previewWidth, previewHeight);

                }
            }
        }
    }


    // getter and setters
    public void setProcessing(ImageProcessing imageProcessing) {
        mImageProcessing = imageProcessing;
    }

}
