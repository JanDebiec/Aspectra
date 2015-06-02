package de.jandrotek.android.aspectra.viewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity extends ActionBarActivity
{
//    private static final boolean D = true;
//    private static final String TAG = "BaseActivity";

    protected static final int ACT_ITEM_LIVE_VIEW   = 0;
    protected static final int ACT_ITEM_VIEW_CONFIG = 1;
    protected static final int ACT_ITEM_VIEW_PLOT   = 2;
    protected static final int ACT_ITEM_ANALYZE     = 3;

    protected AspectraSettings mAspectraSettings;
    protected String mFileFolder;
    protected String mFileExt;


    protected boolean mCameraPresent = false;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       mAspectraSettings = new AspectraSettings();
       Context context = getApplicationContext();
       SharedPreferences prefs = PreferenceManager
               .getDefaultSharedPreferences(context);
       mAspectraSettings.connectPrefs(context, prefs);

       updateFromPreferences();

   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        mFileFolder = mAspectraSettings.getPrefsSpectraBasePath();
        mFileExt = mAspectraSettings.getPrefsSpectraExt();
        // the rest is updated local
    }

}

