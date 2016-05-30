package de.jandrotek.android.aspectra.main;

/**
 * Created by jan on 21.12.14.
 */
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Camera.Parameters;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.ImageProcessing;

@SuppressLint("ViewConstructor")
public class CameraPreview  extends ViewGroup implements SurfaceHolder.Callback,
        Camera.PreviewCallback {
    private static final String TAG = "CameraPreview";
    private Camera mCamera = null;
//    private SurfaceView mSurfaceView;
    private SurfaceHolder mCameraHolder;
    //private static FragmentActivity mActivity = null;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private boolean mSurfaceCreated = false;
    private int mPreviewWidthX;
    private int mPreviewHeightY;

//    private int mStartPercentH = 0;
//    private int mStartIndexH;
//    private int mEndIndexH;
//    private int mEndPercentH = 100;
//    private int mLineLength;
//    private int mEndPercentV = 50;
//    private int mStartPercentV = 49;

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
            }
        }

    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters()
                    .getSupportedPreviewSizes();
            if (mSurfaceCreated) requestLayout();
        }
    }

    public void switchCamera(Camera camera) {
        setCamera(camera);
        try {
            camera.setPreviewDisplay(mCameraHolder);
        } catch (IOException exception) {
            if(BuildConfig.DEBUG) {
                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
            }

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
//    throws Exception {
    {
    //TODO: check configuration: portrait/landscape, disable by portrait

        try {
            Parameters parameters;

            parameters = mCamera.getParameters();
            mPreviewWidthX = mPreviewSize.width;
            mPreviewHeightY = mPreviewSize.height;
            parameters.setPreviewSize(mPreviewWidthX, mPreviewHeightY);
            requestLayout();

            // for later use, in ConfiActivity should be known globally
            AspectraGlobals.mPreviewWidthX = mPreviewWidthX;
            AspectraGlobals.mPreviewHeightY = mPreviewHeightY;
            if(BuildConfig.DEBUG) {
                Log.i(TAG, "width = " + mPreviewWidthX + ", height = " + mPreviewHeightY);
            }
            // send message, that size is already known
            int[] previewSize = new int[2];
            previewSize[0] = mPreviewWidthX;
            previewSize[1] = mPreviewHeightY;

            if(mLVActHandler != null) {
                Message configMessage =
                        mLVActHandler.obtainMessage(AspectraGlobals.eMessagePreviewSize, previewSize);
                configMessage.sendToTarget();
            }

            mImageProcessing.setPictureSizeWidth(mPreviewWidthX);
            mImageProcessing.setPictureSizeHeight(mPreviewHeightY);
            mImageFormat = parameters.getPreviewFormat();

            setCameraDisplayOrientation(0, mCamera);

            mCamera.setParameters(parameters);

            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception caused by setCameraParams()", e);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.setPreviewCallback(this);
            }
        } catch (IOException exception) {
            if(BuildConfig.DEBUG) {
                Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
            }
        }
        if (mPreviewSize == null) requestLayout();
        mSurfaceCreated = true;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
        }
    }

    private Runnable doImageProcessing = new Runnable() {
        public void run() {
            mbProcessing = true;
//            int[] line = extractLine(mFrameData);

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
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int displayRotation = mActivity.getWindowManager().getDefaultDisplay()
                     .getRotation();
        int degrees = 0;
        switch (displayRotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private void setAutofocusToInfinity(){
        Parameters parameters;

        parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        mCamera.setParameters(parameters);

    }

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
        final int width = resolveSize(getSuggestedMinimumWidth(),
                widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
                    height);
        }

        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

            try {

                mCamera.setParameters(parameters);
            } catch (Exception exception) {
//                if(BuildConfig.DEBUG) {

                    Log.e(TAG, "IOException caused by mCamera.setParameters()", exception);
  //              }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            mPreviewWidthX = width;
            mPreviewHeightY = height;
            if (mPreviewSize != null) {
                mPreviewWidthX = mPreviewSize.width;
                mPreviewHeightY = mPreviewSize.height;
            }

            // configure ImageProcessing
            mImageProcessing.setPictureSizeWidth(mPreviewWidthX);
            mImageProcessing.setPictureSizeHeight(mPreviewHeightY);

            // Center the child SurfaceView within the parent.
            if (width * mPreviewHeightY > height * mPreviewWidthX) {
                final int scaledChildWidth = mPreviewWidthX * height
                        / mPreviewHeightY;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = mPreviewHeightY * width
                        / mPreviewWidthX;
                child.layout(0, (height - scaledChildHeight) / 2, width,
                        (height + scaledChildHeight) / 2);
            }
        }
    }


    // getter and setters
    public void setProcessing(ImageProcessing imageProcessing) {
        mImageProcessing = imageProcessing;
    }


    public int getPreviewWidthX() {
        return mPreviewWidthX;
    }

    public int getPreviewHeightY() {
        return mPreviewHeightY;
    }


//    public void setStartPercentH(int startPercent) {
//        mStartPercentH = startPercent;
////        if(mImageProcessing != null) {
////            mImageProcessing.setStartPercentX(startPercent);
////        }
//    }

//    public void setEndPercentH(int endPercent) {
//        mEndPercentH = endPercent;
////        mImageProcessing.setEndPercentX(endPercent);
//    }


//    public void setStartPercentV(int startPercentV) {
//        mStartPercentV = startPercentV;
////        mImageProcessing.setStartPercentY(startPercentV);
//    }



//    public void setEndPercentV(int endPercentV) {
//        mEndPercentV = endPercentV;
////        mImageProcessing.setEndPercentY(endPercentV);
//    }


}
