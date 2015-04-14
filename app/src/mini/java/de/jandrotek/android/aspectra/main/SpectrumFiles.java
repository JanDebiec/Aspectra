package de.jandrotek.android.aspectra.main;

/** android part of SpectraFiles */

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

import de.jandrotek.android.aspectra.core.FileWalker;

public class SpectrumFiles {
    private static final String TAG = "SpectraFiles";

    private static ArrayList mFilelNameArrayList;
    private static String[] mFilelNameListTemp;
    public static String[] mFilelNameListOutput;
    public static String mPath;
    private FileWalker mFileWalker;
    private String mFileExt = "spk";
    private String mFileFolder;


    public String getFileFolder() {
		return mFileFolder;
	}


	public void setFileFolder(String fileFolder) {
		mFileFolder = fileFolder;
	}


	public String getFileExt() {
		return mFileExt;
	}


	public void setFileExt(String fileExt) {
		mFileExt = fileExt;
	}

	boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;

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

    public void searchForFiles(){

        updateExternalStorageState();
        if(mExternalStorageAvailable) {
            mPath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).toString() + "/" + mFileFolder;
            Log.d("TAG", "Path: " + mPath.toString());

            mFileWalker = new FileWalker(mPath);
            mFilelNameListOutput = mFileWalker.search4Files(mFileExt);
        }
        else{
            Log.w("TAG", "media not availeable !");
        }

    }

    public String[] getFilelList(){
        return mFilelNameListOutput;
    }

    public int getFileListSize(){
        return mFilelNameListOutput.length;
    }

//    private String[] search4FilesInSubfolders(String _Path){
//    	File f = new File(_Path);
//    	String[] filelNameList;
//    	filelNameList = f.list();
//
//
//    	return filelNameList;
//    }

}
