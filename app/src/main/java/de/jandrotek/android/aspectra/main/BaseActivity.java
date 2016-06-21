package de.jandrotek.android.aspectra.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.ConfigViewSettings;
import de.jandrotek.android.aspectra.libprefs.AspectraLiveViewPrefs;
import de.jandrotek.android.aspectra.libprefs.AspectraGlobalPrefsActivity;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

public class BaseActivity extends AppCompatActivity //ActionBarActivity
{

//    protected static final int ACT_ITEM_LIVE_VIEW   = 0;
//    protected static final int ACT_ITEM_VIEW_CONFIG = 1;
//    protected static final int ACT_ITEM_VIEW_PLOT   = 2;
//    protected static final int ACT_ITEM_ANALYZE     = 3;

    protected AspectraLiveViewPrefs mAspectraSettings = null;
    protected String mFileFolder;
    protected String mFileExt;
    protected SpectrumFiles mSpectrumFiles = null;
    protected boolean mSpectrumLanscapeOrientation = false;
    protected int mDeviceOrientation;

    protected int mStartPercentX;
    protected int mEndPercentX;
    protected int mStartPercentY;
    //    protected int mEndPercentY;
    protected int mScanAreaWidth;

    protected boolean mCameraPresent = false;
    protected ConfigViewSettings mViewSettings = null;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       if (mAspectraSettings == null) {
           mAspectraSettings = new AspectraLiveViewPrefs();
       }
       if (mSpectrumFiles == null) {
           mSpectrumFiles = new SpectrumFiles();
       }
       Context context = getApplicationContext();
       SharedPreferences prefs = PreferenceManager
               .getDefaultSharedPreferences(context);
       mAspectraSettings.connectPrefs(context, prefs);

       updateFromPreferences();
       setDeviceOrientationInViewSettings();

   }

    protected void setDeviceOrientationInViewSettings() {
        if (mViewSettings == null) {
            mViewSettings = ConfigViewSettings.getInstance();
        }
        mViewSettings.setDeviceOrientation(mDeviceOrientation);
//        mViewSettings.setSpectrumOrientationLandscape(mSpectrumLanscapeOrientation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AspectraGlobalPrefsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause(){
        super.onPause();
        mViewSettings.clearConfigStatus();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void updateFromPreferences() {
        mAspectraSettings.loadSettings();
        // used by all activities
        mFileFolder = mAspectraSettings.getPrefsSaveFolderName();
        mFileExt = mAspectraSettings.getPrefsExtensionName();
        mStartPercentX = mAspectraSettings.getPrefsWidthStart();
        mEndPercentX = mAspectraSettings.getPrefsWidthEnd();
        mStartPercentY = mAspectraSettings.getPrefsHeightStart();
        mScanAreaWidth = mAspectraSettings.getPrefsScanAreaWidth();

        mSpectrumLanscapeOrientation = mAspectraSettings.isPrefsLandscapeCameraOrientation();

        // the rest is updated local
//        updateConfViewSettings();
    }

    protected void updateConfViewSettings() {
        if (mViewSettings == null) {
            mViewSettings = ConfigViewSettings.getInstance();
        }
        mViewSettings.setSpectrumOrientationLandscape(mSpectrumLanscapeOrientation);
        mViewSettings.setConfigStartPercentX(mStartPercentX);
        mViewSettings.setConfigEndPercentX(mEndPercentX);
        mViewSettings.setConfigStartPercentY(mStartPercentY);
        mViewSettings.setAmountLinesY(mScanAreaWidth);
        if (mViewSettings.isConfigured())
            mViewSettings.calcCrossPoints();
    }

    public void getScreenOrientation() {
// Query what the orientation currently really is.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mDeviceOrientation = AspectraGlobals.DEVICE_ORIENTATION_PORTRAIT; // Portrait Mode

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mDeviceOrientation = AspectraGlobals.DEVICE_ORIENTATION_LANDSCAPE;   // Landscape mode
        } else {
            mDeviceOrientation = AspectraGlobals.DEVICE_ORIENTATION_UNKNOWN;
        }
        if (mViewSettings == null) {
            mViewSettings = ConfigViewSettings.getInstance();
        }
        mViewSettings.setDeviceOrientation(mDeviceOrientation);
    }
}

