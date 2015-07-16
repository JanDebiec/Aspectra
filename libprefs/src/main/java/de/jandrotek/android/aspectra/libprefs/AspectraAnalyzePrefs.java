package de.jandrotek.android.aspectra.libprefs;

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
public class AspectraAnalyzePrefs {
    private Context mContext;
    private SharedPreferences mPrefs;
    //KEYs
    private static String mPREFS_REFERENCE_SPECTRUM_KEY;
    private static String mPREFS_EDITED_SPECTRUM_KEY;

    //DEFAULTs
    private String mDefaultSpectrumEdited;
    private String mDefaultSpectrumReference;

    public String getPrefsSpectrumEdited() {
        return mPrefsSpectrumEdited;
    }

    public void setPrefsSpectrumEdited(String mPrefsSpectrumEdited) {
        this.mPrefsSpectrumEdited = mPrefsSpectrumEdited;
    }

    public String getPrefsSpectrumReference() {
        return mPrefsSpectrumReference;
    }

    public void setPrefsSpectrumReference(String mPrefsSpectrumReference) {
        this.mPrefsSpectrumReference = mPrefsSpectrumReference;
    }

    //values
    private String mPrefsSpectrumEdited;
    private String mPrefsSpectrumReference;


    public void connectPrefs(Context context, SharedPreferences prefs){
        mContext = context;
        mPrefs = prefs;
    }

    public void loadSettings() {



        mDefaultSpectrumEdited = mContext.getResources().getString(R.string.PREFS_EDITED_SPECTRUM_DEFAULT);
        mPREFS_EDITED_SPECTRUM_KEY =  mContext.getResources().getString(R.string.PREFS_KEY_SPECTRA_BASEPATH);
        this.mPrefsSpectrumEdited = mPrefs.getString(mPREFS_EDITED_SPECTRUM_KEY, mDefaultSpectrumEdited);


        mDefaultSpectrumReference = mContext.getResources().getString(R.string.PREFS_REFERENCE_SPECTRUM_DEFAULT);
        mPREFS_REFERENCE_SPECTRUM_KEY =  mContext.getResources().getString(R.string.PREFS_REFERENCE_SPECTRUM_KEY);
        this.mPrefsSpectrumReference = mPrefs.getString(mPREFS_REFERENCE_SPECTRUM_KEY, mDefaultSpectrumReference);

    }

    public void saveSettings(){
        SharedPreferences.Editor editor = mPrefs.edit();


        mPREFS_REFERENCE_SPECTRUM_KEY =  mContext.getResources().getString(R.string.PREFS_REFERENCE_SPECTRUM_KEY);
        editor.putString(mPREFS_REFERENCE_SPECTRUM_KEY, mPrefsSpectrumReference);
        editor.apply();

        mPREFS_EDITED_SPECTRUM_KEY =  mContext.getResources().getString(R.string.PREFS_EDITED_SPECTRUM_KEY);
        editor.putString(mPREFS_EDITED_SPECTRUM_KEY, mPrefsSpectrumEdited);
        editor.apply();
    }
}

