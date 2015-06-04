package de.jandrotek.android.aspectra.viewer;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jan on 01.01.15. similar concept to ArobotSettings.
 * with one KEY and DEFAULT defined in R.string
 * This class is responsible for loading the last used settings for
 * the application.
 * Saving is managed in PrefsActivity
 * Every item has 3 defines: mPREFS_KEY, mDefaultValueString, mPrefsValue
 * Strings are defined in file: prefs_default.xml
 * TODO: add checking limits, and ev update and save limited  values
 */
public class AspectraSettings {
    private Context mContext;
    private SharedPreferences mPrefs;
    //KEYs
    private static String mPREFS_KEY_WIDTH_START;
    private static String mPREFS_KEY_WIDTH_END;
    private static String mPREFS_KEY_HEIGHT_START;
    private static String mPREFS_KEY_HEIGHT_END;
    private static String mPREFS_KEY_SCAN_AREA_WIDTH;
    private static String mPREFS_KEY_SPECTRA_BASEPATH;
    private static String mPREFS_KEY_SPECTRA_LENGTH;
    private static String mPREFS_KEY_SPECTRA_EXTENSION;

    //DEFAULTs
    private String mDefaultWidthStart;
    private String mDefaultWidthEnd;
    private String mDefaultHeightStart;
    private String mDefaultHeightEnd;
    private String mDefaultScanAreaWidth;
    private String mDefaultSpectraBasePath;
    private String mDefaultSpectraLength;
    private String mDefaultSpectraExtension;

    //values
    private int mPrefsWidthStart;
    private int mPrefsWidthEnd;
    private int mPrefsHeightStart;
    private int mPrefsHeightEnd;

    private int mPrefsSpectraLength;
    private String mPrefsSpectraBasePath;
    private String mPrefsSpectraExt;
    private int mPrefsScanAreaWidth;

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

    public String getPrefsSpectraBasePath() {
        return mPrefsSpectraBasePath;
    }

    public void setPrefsSpectraBasePath(String mPrefsSpectraBasePath) {
        this.mPrefsSpectraBasePath = mPrefsSpectraBasePath;
    }

    public String getPrefsSpectraExt() {
        return mPrefsSpectraExt;
    }

    public void setPrefsSpectraExt(String mPrefsSpectraExt) {
        this.mPrefsSpectraExt = mPrefsSpectraExt;
    }


    public void connectPrefs(Context context, SharedPreferences prefs){
        mContext = context;
        mPrefs = prefs;
    }


    public void loadSettings() {

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

        mDefaultSpectraBasePath = mContext.getResources().getString(R.string.DEFAULT_KEY_SPECTRA_BASEPATH);
        mPREFS_KEY_SPECTRA_BASEPATH =  mContext.getResources().getString(R.string.PREFS_KEY_SPECTRA_BASEPATH);
        this.mPrefsSpectraBasePath = mPrefs.getString(mPREFS_KEY_SPECTRA_BASEPATH, mDefaultSpectraBasePath);


        mDefaultSpectraExtension = mContext.getResources().getString(R.string.DEFAULT_KEY_SPECTRA_EXTENSION);
        mPREFS_KEY_SPECTRA_EXTENSION =  mContext.getResources().getString(R.string.PREFS_KEY_SPECTRA_EXTENSION);
        this.mPrefsSpectraExt = mPrefs.getString(mPREFS_KEY_SPECTRA_EXTENSION, mDefaultSpectraExtension);

    }

    public void saveSettings(){
        SharedPreferences.Editor editor = mPrefs.edit();

        mPREFS_KEY_WIDTH_START =  mContext.getResources().getString(R.string.PREFS_KEY_WIDTH_START);
        editor.putString(mPREFS_KEY_WIDTH_START, Integer.toString(mPrefsWidthStart));
        editor.commit();

        mPREFS_KEY_WIDTH_END =  mContext.getResources().getString(R.string.PREFS_KEY_WIDTH_END);
        editor.putString(mPREFS_KEY_WIDTH_END, Integer.toString(mPrefsWidthEnd));
        editor.commit();

        mPREFS_KEY_HEIGHT_START =  mContext.getResources().getString(R.string.PREFS_KEY_HEIGHT_START);
        editor.putString(mPREFS_KEY_HEIGHT_START, Integer.toString(mPrefsHeightStart));
        editor.commit();

        mPREFS_KEY_HEIGHT_END =  mContext.getResources().getString(R.string.PREFS_KEY_HEIGHT_END);
        editor.putString(mPREFS_KEY_HEIGHT_END, Integer.toString(mPrefsHeightEnd));
        editor.commit();

        mPREFS_KEY_SCAN_AREA_WIDTH =  mContext.getResources().getString(R.string.PREFS_KEY_SCAN_AREA_WIDTH);
        editor.putString(mPREFS_KEY_SCAN_AREA_WIDTH, Integer.toString(mPrefsScanAreaWidth));
        editor.commit();

        mPREFS_KEY_SPECTRA_LENGTH =  mContext.getResources().getString(R.string.PREFS_KEY_SPECTRA_LENGTH);
        editor.putString(mPREFS_KEY_SPECTRA_LENGTH, Integer.toString(mPrefsSpectraLength));
        editor.commit();

        mPREFS_KEY_SPECTRA_BASEPATH =  mContext.getResources().getString(R.string.PREFS_KEY_SPECTRA_BASEPATH);
        editor.putString(mPREFS_KEY_SPECTRA_BASEPATH, (mDefaultSpectraBasePath));
        editor.commit();

        mPREFS_KEY_SPECTRA_EXTENSION =  mContext.getResources().getString(R.string.PREFS_KEY_SPECTRA_EXTENSION);
        editor.putString(mPREFS_KEY_SPECTRA_EXTENSION, (mDefaultSpectraExtension));
        editor.commit();
    }
}

