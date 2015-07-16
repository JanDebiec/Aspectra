package de.jandrotek.android.aspectra.analyze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;

import de.jandrotek.android.aspectra.core.SpectrumAsp;
import de.jandrotek.android.aspectra.libprefs.AspectraAnalyzePrefs;
import de.jandrotek.android.aspectra.libprefs.AspectraLiveViewPrefs;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

import static de.jandrotek.android.aspectra.analyze.R.string.PREFS_KEY_EXTENSION_NAME;

public class AnalyzeListActivity extends ActionBarActivity
        implements AnalyzeListFragment.Callbacks {

    private static final String TAG = "ListItemsAct";
    private AspectraAnalyzePrefs mAnalyzeSettings;
    private String mFileFolder;
    private String mFileExt;
    private String mSpectrumWork = null;
    private String mSpectrumRef = null;
    private SpectrumFiles mSpectrumFiles = null;
    private int mFileListSize = 0;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate() called");
        }

        mAnalyzeSettings = new AspectraAnalyzePrefs();
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        mAnalyzeSettings.connectPrefs(context, prefs);

        updateFromPreferences();

        if(mSpectrumFiles == null) {
            mSpectrumFiles = new SpectrumFiles();
            mFileListSize = mSpectrumFiles.scanFolderForFiles(mFileFolder,mFileExt );
        }

        setContentView(R.layout.activity_analyze_list);

//        if (findViewById(R.id.item_detail_container) != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-large and
//            // res/values-sw600dp). If this view is present, then the
//            // activity should be in two-pane mode.
//            mTwoPane = true;
//
//            // In two-pane mode, list items should be given the
//            // 'activated' state when touched.
//            ((AnalyzeListFragment) getFragmentManager()
//                    .findFragmentById(R.id.item_list))
//                    .setActivateOnItemClick(true);
//        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFromPreferences();
        mFileListSize = mSpectrumFiles.scanFolderForFiles(mFileFolder,mFileExt );
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analyze_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_help){

        }
        else if (id == R.id.action_about){

        }
        return false;
    }

    @Override
    public void onItemSelected(Map<String, String> spectraNames) {
//        if (mTwoPane) {
//            // t will be fixed later, first we go with single pane
//
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putStringArrayList(PlotViewFragment.ARG_ITEM_IDS, filesNames);
//            PlotViewFragment fragment = new PlotViewFragment();
//            fragment.setArguments(arguments);
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.item_detail_container, fragment)
//                    .commit();
//
//        } else {
            Bundle arguments = new Bundle();
            if(spectraNames.containsKey(AnalyzeFragment.ARG_ITEM_EDIT)){
                mSpectrumWork = spectraNames.get(AnalyzeFragment.ARG_ITEM_EDIT);
                mAnalyzeSettings.setPrefsSpectrumEdited(mSpectrumWork);
                mAnalyzeSettings.saveSettings();
            }
            if (spectraNames.containsKey(AnalyzeFragment.ARG_ITEM_REFERENCE)){
                mSpectrumRef = spectraNames.get(AnalyzeFragment.ARG_ITEM_REFERENCE);
                mAnalyzeSettings.setPrefsSpectrumReference(mSpectrumRef);
                mAnalyzeSettings.saveSettings();
            }
            arguments.putString(AnalyzeFragment.ARG_ITEM_EDIT, mSpectrumWork);
            arguments.putString(AnalyzeFragment.ARG_ITEM_REFERENCE, mSpectrumRef);
            Intent detailIntent = new Intent(this, AnalyzeActivity.class);
            detailIntent.putExtras(arguments);
            startActivity(detailIntent);
//        }
    }

    protected void updateFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       String folderNameKey = this.getResources().getString(R.string.PREFS_KEY_FOLDER_NAME);
        String folderNameDefault = this.getResources().getString(R.string.DEFAULT_FOLDER_NAME);
        mFileFolder = prefs.getString(folderNameKey, folderNameDefault);
        String extensionKey = this.getResources().getString(R.string.PREFS_KEY_EXTENSION_NAME);
        String extensionDefault = this.getResources().getString(R.string.DEFAULT_EXTENSION_NAME);
        mFileExt = prefs.getString(extensionKey, extensionDefault);
        mAnalyzeSettings.loadSettings();
        mSpectrumRef = mAnalyzeSettings.getPrefsSpectrumReference();
        mSpectrumWork = mAnalyzeSettings.getPrefsSpectrumEdited();
    }
}
