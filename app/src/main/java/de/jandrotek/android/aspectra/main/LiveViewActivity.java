package de.jandrotek.android.aspectra.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumAsp;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewController;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewControllerBuilder;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

//import de.jandrotek.android.aspectra.libplotspectra.PlotViewFragment_notToUse;
//import de.jandrotek.android.aspectra.core.FileUtils;

/**
 * here comes the source from MainActivity_libprefs, handling CameraViewFragment,
 * and PlotViewFragment_notToUse
 */

public class LiveViewActivity extends BaseActivity
        implements CameraViewFragment.OnFragmentInteractionListener
//        PlotViewFragment.OnFragmentInteractionListener
{

    private static CameraViewFragment mCameraViewFragment;
    private static PlotViewFragment mPlotViewFragment;
    private PlotViewPresenter mPlotViewPresenter;

    private static int mPreviewWidthX;
    private static int mPreviewHeightY;
    private PlotViewController mPlotViewController;

    public Handler getHandler() {
        return mHandler;
    }

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */

    private final MyHandler mHandler = new MyHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> dummyItems = null;
        //TODO call prefs, to see which orientation should we use,
        // configure proper elements to work portrait or landscape mode
        // separate decide, how spectrum should be calculated, in X or in Y from camera view

        updateFromPreferences();
        mPlotViewController = new PlotViewControllerBuilder().setParam1(AspectraGlobals.ACT_ITEM_VIEW_PLOT).createPlotViewController();
        setContentView(R.layout.activity_live_view);

        if(mScreenLandscapeOrientation){

        } else {

        }
        if (savedInstanceState == null) {
            mCameraViewFragment = CameraViewFragment.newInstance( AspectraGlobals.ACT_ITEM_LIVE_VIEW);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentHolderCameraView, mCameraViewFragment)
                    .commit();
            mPlotViewFragment = PlotViewFragment.newInstance(1);
            mPlotViewController.init(mPlotViewFragment);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fvPlotView, mPlotViewFragment)
                    .commit();
        }
        mPlotViewPresenter = mPlotViewController.mPlotViewPresenter;
    }

    //TODO: set proper handling of configuration: portrait/landscape
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }


    private static void updatePreviewSizeInConfigView() {

        mCameraViewFragment.mConfigLinesView.setPreviewDimensions(mPreviewWidthX, mPreviewHeightY);
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
                Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("de.jandrotek.android.aspectra.viewer");
                startActivity( LaunchIntent );
                return true;
           }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    // do whatever you wish with the uri
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
        mSpectrumFiles.setFileFolder(mFileFolder);
        mSpectrumFiles.setFileExt(mFileExt);
        mPlotViewController.initDisplayInFragment();// must be called when fragment already exists
    }

    //@Override
    protected void updateFromPreferences(){
        super.updateFromPreferences();
        if(mCameraViewFragment != null){
            mCameraViewFragment.setStartPercentHX(mAspectraSettings.getPrefsWidthStart());
            mCameraViewFragment.setEndPercentHX(mAspectraSettings.getPrefsWidthEnd());
            mCameraViewFragment.setStartPercentVY(mAspectraSettings.getPrefsHeightStart());
            mCameraViewFragment.setEndPercentVY(mAspectraSettings.getPrefsHeightEnd());
            mCameraViewFragment.setScanAreaWidth(mAspectraSettings.getPrefsScanAreaWidth());
            mCameraViewFragment.updateBorderPercents();
        }
    }

//    //TODO: refactor: SpectrumAsp as parameter, work should be done in Spectrum
//    public class SaveSpectrumTask extends AsyncTask<Void, Void, Void> {
//        private Exception e = null;
//        private final SpectrumAsp spectrum;
//        private final File target;
//
//        SaveSpectrumTask(SpectrumAsp spectrum, File target) {
//            this.spectrum = spectrum;
//            this.target = target;
//        }
//        @Override
//        protected Void doInBackground(Void... args) {
//            try {
////                spectrum.saveFile(target);
////                SpectrumFiles.saveStringToFile(text, target);
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

    public class SaveSpectrumTask extends AsyncTask<Void, Void, Void> {
        private Exception e=null;
        private final String text;
        private final File target;

        SaveSpectrumTask(String text, File target) {
            this.text=text;
            this.target=target;
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                SpectrumFiles.saveStringToFile(text, target);
            }
            catch (Exception e) {
                this.e=e;
            }
            finally {
                AspectraGlobals.mSavePlotInFile = false;
            }
            return(null);
        }

        @Override
        protected void onPostExecute(Void arg0) {
            if (e != null) {
                boom(e);
            }
        }
    }


     private void boom(Exception e) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
                .show();
        Log.e(getClass().getSimpleName(), "Exception saving file", e);
    }

    private class MyHandler extends Handler {
        private final WeakReference<LiveViewActivity> mActivity;

        public MyHandler(LiveViewActivity activity) {
            mActivity = new WeakReference<LiveViewActivity>(activity);
        }

        @Override
        public void handleMessage(Message inputMessage) {
            LiveViewActivity activity = mActivity.get();
            if (activity != null) {
                int messId = inputMessage.what;
                if(messId == AspectraGlobals.eMessageCompleteLine) {
                    int[] data = (int[])inputMessage.obj;
                    int length = data.length;
                    mPlotViewPresenter.updateSinglePlot(0, data);//TODO:
                    mPlotViewPresenter.updateFragmentPort(0, length);
                    if(AspectraGlobals.mSavePlotInFile){
                        //TODO: run task in controller, the only input: data
                        // but to make a toast we need fileName
                        File f;
                        AspectraGlobals.mSavePlotInFile = false;
                        String fileName = SpectrumFiles.generateSpectrumAspFileName(mFileExt);
                        SpectrumAsp mSpectrum = new SpectrumAsp(fileName);
                        mSpectrum.setData(data, AspectraGlobals.eNoNormalize);
                        f =  mSpectrumFiles.getTarget(fileName);
                        new SaveSpectrumTask(mSpectrum.toString(),f).execute();
                        Toast.makeText(activity, f.toString(), Toast.LENGTH_SHORT)
                                .show();

                    }
                } else  if (messId == AspectraGlobals.eMessagePreviewSize){
                    int[] data = (int[])inputMessage.obj;
                    mPreviewWidthX = data[0];
                    mPreviewHeightY = data[1];
                    updatePreviewSizeInConfigView();
                }
            }
        }
    }
}
