package de.jandrotek.android.aspectra.libspectrafiles;

/** android part of SpectraFiles */

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import de.jandrotek.android.aspectra.core.FileWalker;

public class SpectrumFiles {
    private static final String TAG = "SpectraFiles";

//    private static ArrayList mFilelNameArrayList;
//    private static String[] mFilelNameListTemp;
    public static String[] mFilelNameListOutput = null;
    public static String mPath = "";
//    private FileWalker mFileWalker;

    private static String mFileExt = "";
    private static String mFileFolder;


//    public String getFileFolder() {
//		return mFileFolder;
//	}


	public void setFileFolder(String fileFolder) {
		mFileFolder = fileFolder;
	}


//	public static String getFileExt() {
//		return mFileExt;
//	}


	public void setFileExt (String fileExt) {
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
        if(mExternalStorageAvailable) {
            mPath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).toString() + "/" + mFileFolder;
            if(BuildConfig.DEBUG) {
                Log.d("TAG", "Path: " + mPath.toString());
            }
        }
    }

    public void searchForFiles(){
        // for every new search, old content should be cleared
        if(mFilelNameListOutput != null) {
            Arrays.fill(mFilelNameListOutput, null);
        }

        FileWalker fileWalker;
        updateExternalStorageState();
        if((mExternalStorageAvailable)) {

            fileWalker = new FileWalker(mPath);
            mFilelNameListOutput = fileWalker.search4Files(mFileExt);
        }
        else{
            if(BuildConfig.DEBUG) {
                Log.w("TAG", "media not availeable !");
            }
        }
    }

    public int scanFolderForFiles(String fileFolder, String fileExtension) {
        setFileFolder(fileFolder);
        setFileExt(fileExtension);
        searchForFiles();
        return(getFileListSize());
    }

//    public String[] getFilelList(){
//        return mFilelNameListOutput;
//    }

    public int getFileListSize(){
        return mFilelNameListOutput.length;
    }

    public static String generateSpectrumAspFileName(String fileExt){

        // prepare date as string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        return mPath  + "/" + currentDateandTime + "." + fileExt;
    }

    public static void saveStringToFile(String text, File target) throws IOException {
        FileOutputStream fos=new FileOutputStream(target);
        OutputStreamWriter out=new OutputStreamWriter(fos);

        out.write(text);
        out.flush();
        fos.getFD().sync();
        out.close();
    }

}
