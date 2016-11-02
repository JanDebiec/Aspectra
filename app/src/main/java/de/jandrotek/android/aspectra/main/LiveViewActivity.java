package de.jandrotek.android.aspectra.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.ImageProcessing;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;

/**
 * here comes the source from MainActivity_libprefs, handling CameraViewFragment,
 * and PlotViewFragment_notToUse
 */

public class LiveViewActivity extends BaseActivity
//        implements CameraViewFragment.OnFragmentInteractionListener
//        PlotViewFragment.OnFragmentInteractionListener
{
    private static final String TAG = "LiveViewActivity";

    private static CameraViewFragment mCameraViewFragment;
    private static PlotViewFragment mPlotViewFragment;
    private PlotViewPresenter mPlotViewPresenter;

    private static int mPreviewWidthX;
    private static int mPreviewHeightY;

    public Handler getHandler() {
        return mHandler;
    }

    private ImageProcessing mImageProcessing = null;

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */

    private final SpectrumHandler mHandler = new SpectrumHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> dummyItems = null;

        if (mSpectrumLanscapeOrientation) {
            setContentView(R.layout.activity_live_view_cam_land);
        } else {
            setContentView(R.layout.activity_live_view_cam_port);
        }
        if (savedInstanceState == null) {
            mCameraViewFragment = CameraViewFragment.newInstance( AspectraGlobals.ACT_ITEM_LIVE_VIEW);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentHolderCameraView, mCameraViewFragment)
                    .commit();
            mPlotViewFragment = PlotViewFragment.newInstance(1);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fvPlotView, mPlotViewFragment)
                    .commit();
        }
        if(mImageProcessing == null) {
            mImageProcessing = ImageProcessing.getInstance();
        }
        mCameraViewFragment.setImageProcessing(mImageProcessing);

        mPlotViewPresenter = new PlotViewPresenter(1, mPlotViewFragment);

        // set both orientations in child
        getScreenOrientation();
        mCameraViewFragment.setDeviceOrientation(mDeviceOrientation);
        updateFromPreferences();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_live_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
            if (id == R.id.action_save) {
                AspectraGlobals.mSavePlotInFile = true;
                return true;
            } else if (id == R.id.action_list) {
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("de.jandrotek.android.aspectra.viewer.ItemListActivity");
                startActivity( LaunchIntent );
                return true;
           }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mCameraViewFragment != null){
            // get the preview size from CamPreview,
            // will be needed in ConfigView
            mPreviewWidthX = mCameraViewFragment.getPreviewWidthX();
            mPreviewHeightY = mCameraViewFragment.getPreviewHeightY();

            mCameraViewFragment.onPause();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mCameraViewFragment != null){
            mCameraViewFragment.onStop();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateFromPreferences();

        //TODO: suppose it os not needed:
        if(mPlotViewFragment == null) {
            mPlotViewFragment = PlotViewFragment.newInstance(1);
        }
        getScreenOrientation();
        mCameraViewFragment.setDeviceOrientation(mDeviceOrientation);
        setDeviceOrientationInViewSettings();
        updateConfViewSettings();
        configureImageProcessing();
    }


    //@Override
    protected void updateFromPreferences(){
        super.updateFromPreferences();
        mSpectrumFiles.setFileFolder(mFileFolder);
        mSpectrumFiles.setFileExt(mFileExt);
        if (mImageProcessing == null) {
            mImageProcessing = ImageProcessing.getInstance();
        }
        configureImageProcessing();
    }

    private void configureImageProcessing() {
        mImageProcessing.setStartPercentX(mStartPercentX);
        mImageProcessing.setEndPercentX(mEndPercentX);
        mImageProcessing.setStartPercentY(mStartPercentY);
        mImageProcessing.setScanAreaWidth(mScanAreaWidth);
        mImageProcessing.setSpectrumOrientationLandscape(mSpectrumLanscapeOrientation);
        mImageProcessing.setCameraDataMirrored(mCameraDataMirrored);
    }

// moved to SpectrumFiles
//    public class SaveSpectrumTask extends AsyncTask<Void, Void, Void> {
//        private Exception e=null;
//        private final String text;
//        private final File target;
//
//        SaveSpectrumTask(String text, File target) {
//            this.text=text;
//            this.target=target;
//        }
//
//        @Override
//        protected Void doInBackground(Void... args) {
//            try {
//                SpectrumFiles.saveStringToFile(text, target);
//            }
//            catch (Exception e) {
//                this.e=e;
//            }
//            finally {
//                AspectraGlobals.mSavePlotInFile = false;
//            }
//            return(null);
//        }
//
//        @Override
//        protected void onPostExecute(Void arg0) {
//            if (e != null) {
//                boom(e);
//            }
//        }
//    }
//
//
//     private void boom(Exception e) {
//        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
//                .show();
//        Log.e(getClass().getSimpleName(), "Exception saving file", e);
//    }

    private class SpectrumHandler extends Handler {
        private final WeakReference<LiveViewActivity> mActivity;

        public SpectrumHandler(LiveViewActivity activity) {
            mActivity = new WeakReference<LiveViewActivity>(activity);
        }

        @Override
        public void handleMessage(Message inputMessage) {
            LiveViewActivity activity = mActivity.get();
            if (activity != null) {
                int messId = inputMessage.what;
                if(messId == AspectraGlobals.eMessageCompleteLine) {
                    //TODO: interface to presenter
                    // check if PlotFragment already prepared for data
                    // by first run presenter.init
                    // by next runs update plot
                    int[] data = (int[])inputMessage.obj;
                    int length = data.length;
                    if(mPlotViewFragment.isReady4Plot()){
                        // input for presenter.input is 2 dimensional array
                        int [][] arrayOfData = new int[1][];
                        arrayOfData[0] = data;
                        mPlotViewPresenter.init(1, arrayOfData);//TODO:
                        mPlotViewPresenter.updateFragmentPort(0, length);
                    } else if (mPlotViewFragment.isFullInitialized()){
                        mPlotViewPresenter.updateSinglePlot(0, data);//TODO:
                        mPlotViewPresenter.updateFragmentPort(0, length);
                    } else {
                        if(BuildConfig.DEBUG) {
                            Log.e(TAG, "PVPresenter is not initialized");
                        }
                    }
                    if(AspectraGlobals.mSavePlotInFile){
                        try {
                            String fileName = mSpectrumFiles.savePlotToFile(data);
                            Toast.makeText(activity, fileName, Toast.LENGTH_SHORT)
                                    .show();
                        } catch (Exception e) {
                            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG)
                                    .show();
                            Log.e(getClass().getSimpleName(), "Exception saving file", e);

                        }

                        AspectraGlobals.mSavePlotInFile = false;
//                        //TODO: run task in controller, the only input: data
//                        // but to make a toast we need fileName
//                        File f;
//                        String fileName = SpectrumFiles.generateSpectrumAspFileName(mFileExt);
//                        SpectrumAsp mSpectrum = new SpectrumAsp(fileName);
//                        mSpectrum.setData(data, AspectraGlobals.eNoNormalize);
//                        f =  mSpectrumFiles.getTarget(fileName);
//                        new SaveSpectrumTask(mSpectrum.toString(),f).execute();

                    }
                } else if (messId == AspectraGlobals.eMessagePreviewSize) {
                    int[] data = (int[]) inputMessage.obj;
                    mPreviewWidthX = data[0];
                    mPreviewHeightY = data[1];
                    //TODO: check if needed and proper value
//                    updatePreviewSizeInConfigView();
                }
            }
        }
    }
}
