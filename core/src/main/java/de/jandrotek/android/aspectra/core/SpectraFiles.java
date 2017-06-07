/**
 * This file is part of Aspectra.
 *
 * Aspectra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aspectra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aspectra.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 *
 * Copyright Jan Debiec
 */
package de.jandrotek.android.aspectra.core;

/** pure jave part */

//import android.os.Environment;
//import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/** NOT_USED
 * these class seem not to be used, we use SpectrumFiles
 * But SpectrumFiles uses android libs, is not pure java
 */
public class SpectraFiles {
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

//    void updateExternalStorageState() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            mExternalStorageAvailable = mExternalStorageWriteable = true;
//        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//            mExternalStorageAvailable = true;
//            mExternalStorageWriteable = false;
//        } else {
//            mExternalStorageAvailable = mExternalStorageWriteable = false;
//        }
//        //handleExternalStorageState(mExternalStorageAvailable,
//         //       mExternalStorageWriteable);
//    }

//TODO: move android related items extern, leave pure java
    public void searchForFiles(){

        // Android part
//        updateExternalStorageState();
//        if(mExternalStorageAvailable) {
//            mPath = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOWNLOADS).toString() + "/" + mFileFolder;
//            Log.d("TAG", "Path: " + mPath.toString());

            // place for new function: search4FilesInSubdirs()
            mFileWalker = new FileWalker(mPath);
            //File f = new File(mPath);
            mFilelNameListOutput = mFileWalker.search4Files(mFileExt);

    //    }
//        else{
//            Log.w("TAG", "media not availeable !");
 //       }

    }

    public String[] getFilelList(){
        return mFilelNameListOutput;
    }

    public int getFileListSize(){
        return mFilelNameListOutput.length;
    }

    private String[] search4FilesInSubfolders(String _Path){
    	File f = new File(_Path);
    	String[] filelNameList;
    	filelNameList = f.list();


    	return filelNameList;
    }

}
