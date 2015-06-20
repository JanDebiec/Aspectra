package de.jandrotek.android.aspectram.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumAsp;
import de.jandrotek.android.aspectra.libplotspectra.PlotViewFragment;
import de.jandrotek.android.aspectra.libprefs.SettingsActivity;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

//import de.jandrotek.android.aspectra.core.FileUtils;

/**
 * here comes the source from MainActivity_libprefs, handling CameraViewFragment,
 * and PlotViewFragment
 */

/** TODO: change structure to one activity with various fragments:
 * CameraView always, second fragment: plot or new configFragment
 * Use animations
 * pro: Back button works consistent,
 *      Activity stack consistent
 *      Material concept
 * con: to learn some Materials concept
 */

//public class LiveViewActivity extends ActionBarActivity
public class LiveViewActivity extends BaseActivity
        implements CameraViewFragment.OnCVFragmentInteractionListener,
        PlotViewFragment.OnFragmentInteractionListener,
        ConfigFragment.OnConfigFragmentInteractionListener
{

    private static CameraViewFragment mCameraViewFragment;
    private static PlotViewFragment mPlotViewFragment;
    private static ConfigFragment mConfigFragment;
    private static int mPreviewWidthX;
    private static int mPreviewHeightY;
    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;
    private String mFileFolder = "aspectra";
    //public static boolean mSavePlotInFile = false;// fragment must change the value
    private boolean mFlagConfigStarted = false;
    private static final int eActRightFragPlot = 0;
    private static final int eActRightFragConfig = 1;
    private int mActRightFragState = eActRightFragPlot;

    private int mPersentStartW;
    private int mPersentEndW;
    private int mPersentStartH;
    private int mDeltaLinesY;
    private boolean mPrefsChanged = false;

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

        // material enable transitions
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_view);
        if (savedInstanceState == null) {
            mCameraViewFragment = CameraViewFragment.newInstance( AspectraGlobals.ACT_ITEM_LIVE_VIEW);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentHolderCameraView, mCameraViewFragment)
                    .commit();
            mPlotViewFragment = PlotViewFragment.newInstance(AspectraGlobals.ACT_ITEM_LIVE_VIEW);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentHolderRightFragmentView, mPlotViewFragment)
                    .commit();

            // prepare fragment, will be used later
            mConfigFragment = ConfigFragment.newInstance("a","b");
        }

        updateFromPreferences();
        updateCamerFragmFromPrefs();
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


    private void switchRightFragment(){
        mFlagConfigStarted = true;

        getWindow().setExitTransition(new Slide());
        getWindow().setEnterTransition(new Slide());
        if(mActRightFragState == eActRightFragPlot) { // if we are in LiveView
            updateConfigFragmFromPrefs();

            mCameraViewFragment.cameraProcessingShouldRun(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentHolderRightFragmentView, mConfigFragment, "a")
                    .commit();
            mActRightFragState = eActRightFragConfig;
            mPrefsChanged = false;
            //acceptNewPersentSettings();
        } else if (mActRightFragState == eActRightFragConfig) { // we are in config view

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentHolderRightFragmentView, mPlotViewFragment)
                    .commit();
            mActRightFragState = eActRightFragPlot;
                acceptNewPersentSettings();
            updateCamerFragmFromPrefs();
            mCameraViewFragment.cameraProcessingShouldRun(true);
        }
    }

    public void acceptNewPersentSettings() {

            mAspectraSettings.setPrefsWidthStart(mPersentStartW);
            mAspectraSettings.setPrefsWidthEnd(mPersentEndW);
        mAspectraSettings.setPrefsHeightStart(mPersentStartH);
        mAspectraSettings.setPrefsScanAreaWidth(mDeltaLinesY);

        if (mPrefsChanged) {
            mAspectraSettings.saveSettings();
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

            if (id == R.id.action_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.action_save) {
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
    public void onCameraViewFragmentInteraction(Uri uri){

    // do whatever you wish with the uri
        switchRightFragment();
    }



    @Override
    public void onConfigFragmentInteraction(int startPercentX, int endPercentX, int startPercentY, int deltaLinesY){
        mPrefsChanged = true;
        mPersentStartW = startPercentX;
        mPersentEndW = endPercentX;
        mPersentStartH = startPercentY;
        mDeltaLinesY = deltaLinesY;

        if (mCameraViewFragment != null) {
            mCameraViewFragment.updateBorderInConfigView(startPercentX, endPercentX, startPercentY, deltaLinesY);
        }
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
    }

    //@Override
    protected void updateCamerFragmFromPrefs(){
        super.updateFromPreferences();
        updateOwnVarsPrefs();
        if(mCameraViewFragment != null){
            mCameraViewFragment.setStartPercentHX(mPersentStartW);
            mCameraViewFragment.setEndPercentHX(mPersentEndW);
            mCameraViewFragment.setStartPercentVY(mPersentStartH);
//            mCameraViewFragment.setEndPercentVY(mAspectraSettings.getPrefsHeightEnd());
            mCameraViewFragment.setScanAreaWidth(mDeltaLinesY);
            mCameraViewFragment.updateBorderPercents();
        }
    }

    protected void updateOwnVarsPrefs() {
        super.updateFromPreferences();
        mPersentStartW = mAspectraSettings.getPrefsWidthStart();
        mPersentEndW = mAspectraSettings.getPrefsWidthEnd();
        mPersentStartH = mAspectraSettings.getPrefsHeightStart();
        mDeltaLinesY = mAspectraSettings.getPrefsScanAreaWidth();
    }

    protected void updateConfigFragmFromPrefs(){
        super.updateFromPreferences();
        updateOwnVarsPrefs();

        if(mConfigFragment != null){
            mConfigFragment.setPersentStartW(mPersentStartW);
            mConfigFragment.setPersentEndW(mPersentEndW);
            mConfigFragment.setPersentStartH(mPersentStartH);
            mConfigFragment.setDeltaLinesY(mDeltaLinesY);
//            mConfigFragment.updateSeekBars();

        }
    }

    public class SaveSpectrumTask extends AsyncTask<Void, Void, Void> {
        private Exception e=null;
        private String text;
        private File target;

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
            AspectraGlobals.mSavePlotInFile = false;
            return(null);
        }

        @Override
        protected void onPostExecute(Void arg0) {
            if (e != null) {
                boom(e);
            }
        }
    }

    private File getTarget(String fileName) {
        File f = null;
        try {
            if(!mExternalStorageWriteable) {
                updateExternalStorageState();
            }
            if(mExternalStorageWriteable) {
                //root = this.getExternalFilesDir(null);
                String mRootPath = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).toString();

                String mFullPath = mRootPath + "/" + mFileFolder;
                //TODO check if mFileFolder exists, if not create
                File pathToFolder = new File(mFullPath);
                if(!pathToFolder.exists()){
                    // create folder
                    pathToFolder.mkdir();
                }

                String sFileName = mFullPath + "/" + fileName;
                f = new File(sFileName);
              } else {
                if(BuildConfig.DEBUG) {
                    Log.w("TAG", "media not available !");
                }
            }
            Toast.makeText(this, f.toString(), Toast.LENGTH_SHORT)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (f);

    }


    void updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
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
                    try {
                        mPlotViewFragment.showPlot(data, length);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(AspectraGlobals.mSavePlotInFile){
                        AspectraGlobals.mSavePlotInFile = false;
                        String fileName = SpectrumFiles.generateSpectrumAspFileName(mFileExt);
                        SpectrumAsp mSpectrum = new SpectrumAsp(fileName);
                        mSpectrum.setData(data, AspectraGlobals.eNoNormalize);
                        new SaveSpectrumTask(mSpectrum.toString(), getTarget(fileName)).execute();
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
