package de.jandrotek.android.aspectra.libprefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Aspectra live view prefs.
 * Global prefs are handled in AspGlobalPrefsActivity
 *
 * Created by jan on 01.01.15. similar concept to ArobotSettings.
 * with one KEY and DEFAULT defined in R.string
 * This class is responsible for loading the last used settings for
 * the application.
 * Saving is managed in PrefsActivity
 * Every item has 3 defines: mPREFS_KEY, mDefaultValueString, mPrefsValue
 * Strings are defined in file: prefs_default.xml
 * TODO: add checking limits, and ev update and save limited  values
 */
public class AspectraLiveViewPrefs {
    private Context mContext;
    private SharedPreferences mPrefs;
    //KEYs
    private static String mPREFS_KEY_WIDTH_START;
    private static String mPREFS_KEY_WIDTH_END;
    private static String mPREFS_KEY_HEIGHT_START;
    private static String mPREFS_KEY_HEIGHT_END;
    private static String mPREFS_KEY_SCAN_AREA_WIDTH;
    private static String mPREFS_KEY_SPECTRA_LENGTH;
    private static String mPREFS_KEY_LANSCAPE_ORIENTATION;
    private static String mPREFS_KEY_FOLDER_NAME;
    private static String mPREFS_KEY_EXTENSION_NAME;

    //values
    private int mPrefsWidthStart;
    private int mPrefsWidthEnd;
    private int mPrefsHeightStart;
    private int mPrefsHeightEnd;
    private boolean mPrefsLandscapeCameraOrientation;
    private String mPrefsLandscapeCameraOrientationString;

    public String getPrefsExtensionName() {
        return mPrefsExtensionName;
    }

    public String getPrefsSaveFolderName() {
        return mPrefsSaveFolderName;
    }

    private String mPrefsExtensionName;
    private String mPrefsSaveFolderName;

    private int mPrefsSpectraLength;
    private int mPrefsScanAreaWidth;

    public boolean isPrefsLandscapeCameraOrientation() {
        return mPrefsLandscapeCameraOrientation;
    }

    public void setPrefsLandscapeCameraOrientation(boolean prefsLandscapeCameraOrientation) {
        mPrefsLandscapeCameraOrientation = prefsLandscapeCameraOrientation;
    }


    public int getPrefsWidthStart() {
        return mPrefsWidthStart;
    }

    public int getPrefsWidthEnd() {
        return mPrefsWidthEnd;
    }

    public int getPrefsHeightStart() {
        return mPrefsHeightStart;
    }

    public int getPrefsHeightEnd() {
        return mPrefsHeightEnd;
    }

    public void setPrefsWidthStart(int prefsWidthStart) {
        mPrefsWidthStart = prefsWidthStart;
    }

    public void setPrefsWidthEnd(int prefsWidthEnd) {
        mPrefsWidthEnd = prefsWidthEnd;
    }

    public void setPrefsHeightStart(int prefsHeightStart) {
        mPrefsHeightStart = prefsHeightStart;
    }

    public void setPrefsHeightEnd(int prefsHeightEnd) {
        mPrefsHeightEnd = prefsHeightEnd;
    }

    public int getPrefsScanAreaWidth() {
        return mPrefsScanAreaWidth;
    }

    public void setPrefsScanAreaWidth(int prefsScanAreaWidth) {
        mPrefsScanAreaWidth = prefsScanAreaWidth;
    }
    public int getPrefsSpectraLength() {
        return mPrefsSpectraLength;
    }

    public void setPrefsSpectraLength(int mPrefsSpectraLength) {
        this.mPrefsSpectraLength = mPrefsSpectraLength;
    }

    public void connectPrefs(Context context, SharedPreferences prefs){
        mContext = context;
        mPrefs = prefs;
    }


    public void loadSettings() {

        String mDefaultWidthStart;
        String mDefaultWidthEnd;
        String mDefaultHeightStart;
        String mDefaultHeightEnd;
        String mDefaultScanAreaWidth;
        String mDefaultSpectraLength;
        String mDefaultFolderName;
        String mDefaultExtensionName;
//        Boolean mDefaultLandscapeOrientation = false;

        mDefaultWidthStart = mContext.getResources().getString(R.string.DEFAULT_WIDTH_START);
        mPREFS_KEY_WIDTH_START =  mContext.getResources().getString(R.string.PREFS_KEY_WIDTH_START);
        this.mPrefsWidthStart = Integer.parseInt(mPrefs.getString(mPREFS_KEY_WIDTH_START, mDefaultWidthStart));

        mDefaultWidthEnd = mContext.getResources().getString(R.string.DEFAULT_WIDTH_END);
        mPREFS_KEY_WIDTH_END =  mContext.getResources().getString(R.string.PREFS_KEY_WIDTH_END);
        this.mPrefsWidthEnd = Integer.parseInt(mPrefs.getString(mPREFS_KEY_WIDTH_END, mDefaultWidthEnd));

        mDefaultHeightStart = mContext.getResources().getString(R.string.DEFAULT_HEIGHT_START);
        mPREFS_KEY_HEIGHT_START =  mContext.getResources().getString(R.string.PREFS_KEY_HEIGHT_START);
        this.mPrefsHeightStart = Integer.parseInt(mPrefs.getString(mPREFS_KEY_HEIGHT_START, mDefaultHeightStart));

        mDefaultHeightEnd = mContext.getResources().getString(R.string.DEFAULT_HEIGHT_END);
        mPREFS_KEY_HEIGHT_END =  mContext.getResources().getString(R.string.PREFS_KEY_HEIGHT_END);
        this.mPrefsHeightEnd = Integer.parseInt(mPrefs.getString(mPREFS_KEY_HEIGHT_END, mDefaultHeightEnd));

        mDefaultScanAreaWidth = mContext.getResources().getString(R.string.DEFAULT_SCAN_AREA_WIDTH);
        mPREFS_KEY_SCAN_AREA_WIDTH =  mContext.getResources().getString(R.string.PREFS_KEY_SCAN_AREA_WIDTH);
        this.mPrefsScanAreaWidth = Integer.parseInt(mPrefs.getString(mPREFS_KEY_SCAN_AREA_WIDTH, mDefaultScanAreaWidth));

        mDefaultSpectraLength = mContext.getResources().getString(R.string.DEFAULT_KEY_SPECTRA_LENGTH);
        mPREFS_KEY_SPECTRA_LENGTH =  mContext.getResources().getString(R.string.PREFS_KEY_SPECTRA_LENGTH);
        this.mPrefsSpectraLength = Integer.parseInt(mPrefs.getString(mPREFS_KEY_SPECTRA_LENGTH, mDefaultSpectraLength));

//        mStrDefaultLandscapeOrientation = mContext.getResources().getString(R.string.DEFAULT_KEY_LANDSCAPE_ORIENTATION);
        mPREFS_KEY_LANSCAPE_ORIENTATION = mContext.getResources().getString(R.string.PREFS_KEY_LANDSCAPE_ORIENTATION);
        this.mPrefsLandscapeCameraOrientation = mPrefs.getBoolean(mPREFS_KEY_LANSCAPE_ORIENTATION, true);

                //Boolean.getBoolean(mPrefsLandscapeCameraOrientationString);

        mDefaultFolderName = mContext.getResources().getString(R.string.DEFAULT_FOLDER_NAME);
        mPREFS_KEY_FOLDER_NAME = mContext.getResources().getString(R.string.PREFS_KEY_FOLDER_NAME);
        this.mPrefsSaveFolderName = mPrefs.getString(mPREFS_KEY_FOLDER_NAME, mDefaultFolderName);

        mDefaultExtensionName = mContext.getResources().getString(R.string.DEFAULT_EXTENSION_NAME);
        mPREFS_KEY_EXTENSION_NAME = mContext.getResources().getString(R.string.PREFS_KEY_EXTENSION_NAME);
        this.mPrefsSaveFolderName = mPrefs.getString(mPREFS_KEY_EXTENSION_NAME, mDefaultExtensionName);
    }

    public void saveSettings(){
        SharedPreferences.Editor editor = mPrefs.edit();

        mPREFS_KEY_WIDTH_START =  mContext.getResources().getString(R.string.PREFS_KEY_WIDTH_START);
        editor.putString(mPREFS_KEY_WIDTH_START, Integer.toString(mPrefsWidthStart));
        editor.apply();

        mPREFS_KEY_WIDTH_END =  mContext.getResources().getString(R.string.PREFS_KEY_WIDTH_END);
        editor.putString(mPREFS_KEY_WIDTH_END, Integer.toString(mPrefsWidthEnd));
        editor.apply();

        mPREFS_KEY_HEIGHT_START =  mContext.getResources().getString(R.string.PREFS_KEY_HEIGHT_START);
        editor.putString(mPREFS_KEY_HEIGHT_START, Integer.toString(mPrefsHeightStart));
        editor.apply();

        mPREFS_KEY_HEIGHT_END =  mContext.getResources().getString(R.string.PREFS_KEY_HEIGHT_END);
        editor.putString(mPREFS_KEY_HEIGHT_END, Integer.toString(mPrefsHeightEnd));
        editor.apply();

        mPREFS_KEY_SCAN_AREA_WIDTH =  mContext.getResources().getString(R.string.PREFS_KEY_SCAN_AREA_WIDTH);
        editor.putString(mPREFS_KEY_SCAN_AREA_WIDTH, Integer.toString(mPrefsScanAreaWidth));
        editor.apply();

        mPREFS_KEY_SPECTRA_LENGTH =  mContext.getResources().getString(R.string.PREFS_KEY_SPECTRA_LENGTH);
        editor.putString(mPREFS_KEY_SPECTRA_LENGTH, Integer.toString(mPrefsSpectraLength));
        editor.apply();


//        mPREFS_KEY_LANSCAPE_ORIENTATION = mContext.getResources().getString(R.string.PREFS_KEY_LANDSCAPE_ORIENTATION);
//        editor.putString(mPREFS_KEY_LANSCAPE_ORIENTATION, Boolean.toString(mPrefsLandscapeCameraOrientation));
//        editor.apply();

    }
}

