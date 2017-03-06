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
package de.jandrotek.android.aspectra.libspectrafiles;

/** android part of SpectraFiles */

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.FileWalker;
import de.jandrotek.android.aspectra.core.SpectrumAsp;

public class SpectrumFiles {
    private static final String TAG = "SpectraFiles";

    public static String[] mFilelNameListOutput = null;
    public static String[] mFilelNameListOutputUnsorted = null;
    public static String mPath = "";

    private static String mFileExt = "";
    private static String mFileFolder = "aspectra";

	public void setFileFolder(String fileFolder) {
		mFileFolder = fileFolder;
	}

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
                Log.d("TAG", "Path: " + mPath);
            }
        }
    }

    public void searchForFiles(){
        // for every new search, old content should be cleared
        if(mFilelNameListOutputUnsorted != null) {
            Arrays.fill(mFilelNameListOutputUnsorted, null);
        }

        FileWalker fileWalker;
        updateExternalStorageState();
        if((mExternalStorageAvailable)) {

            fileWalker = new FileWalker(mPath);
            mFilelNameListOutputUnsorted = fileWalker.search4Files(mFileExt);
            mFilelNameListOutput = sortArrayFromNewest(mFilelNameListOutputUnsorted);

        }
        else{
            if(BuildConfig.DEBUG) {
                Log.w("TAG", "media not available !");
            }
        }
    }

    private String[] sortArrayFromNewest(String[] unsortedArray) {
        Arrays.sort(unsortedArray);
        int arraySize = unsortedArray.length;
        String[] outputArray = new String[arraySize];
        for(int i = 0; i < arraySize; i++){
            outputArray[arraySize - 1 - i] = unsortedArray[i];
        }
        return outputArray;
    }

    public int scanFolderForFiles(String fileFolder, String fileExtension) {
        setFileFolder(fileFolder);
        setFileExt(fileExtension);
        searchForFiles();
        return(getFileListSize());
    }

    public int getFileListSize(){
        return mFilelNameListOutput.length;
    }

    public static String generateSpectrumAspFileName(String fileExt){

        // prepare date as string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        return currentDateandTime + "." + fileExt;
//        return mPath  + "/" + currentDateandTime + "." + fileExt;
    }

    /**
     * TODO: move function to java-lib
     * @param text
     * @param target
     * @throws IOException
     */
    public static void saveStringToFile(String text, File target) throws IOException {
        FileOutputStream fos=new FileOutputStream(target);
        OutputStreamWriter out=new OutputStreamWriter(fos);

        out.write(text);
        out.flush();
        fos.getFD().sync();
        out.close();
    }

    /**
     * TODO: move check of external storage out of function,
     * then it could be POPJ file
     * @param fileName
     * @return
     */
    public File getTarget(String fileName) {
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
//            Toast.makeText(this, f.toString(), Toast.LENGTH_SHORT)
//                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (f);

    }

    public String savePlotToFile(int[] data) {
        String fileName = generateSpectrumAspFileName(mFileExt);
        File f;
        SpectrumAsp mSpectrum = new SpectrumAsp(fileName);
        mSpectrum.setData(data, AspectraGlobals.eNoNormalize);
        f = getTarget(fileName);
        new SaveSpectrumTask(mSpectrum.toString(), f).execute();
        return fileName;
    }

    //    //TODO: refactor: SpectrumAsp as parameter, work should be done in Spectrum
    public class SaveSpectrumTask extends AsyncTask<Void, Void, Void> {
        private Exception e = null;
        private final String text;
        private final File target;

        SaveSpectrumTask(String text, File target) {
            this.text = text;
            this.target = target;
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                SpectrumFiles.saveStringToFile(text, target);
            } catch (Exception e) {
                this.e = e;
            } finally {
                AspectraGlobals.mSavePlotInFile = false;
            }
            return (null);
        }

        @Override
        protected void onPostExecute(Void arg0) {
//            if (e != null) {
//                boom(e);
//            }
        }
    }


//    private void boom(Exception e) {
//        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
//                .show();
//        Log.e(getClass().getSimpleName(), "Exception saving file", e);
//    }

}
