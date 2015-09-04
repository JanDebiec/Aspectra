package de.jandrotek.android.aspectra.core;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * class for reading Chroco-Spectra files.
 * original Chroco files, *.spk,
 * 		without header, default length:, data type:
 *
 * class will be moved to external JAVA library
 * for testing with JUnit from IntelliJ
 * @author jan
 *
 * 27.07.2015   class changed from abstract to normal class,
 *              can be used directly, f.i.as CHR spectrum
 *              Asp spectrum will be extended from Base
 *
 * TODO: where is saving? should be here
 */
public class SpectrumBase {
    protected String mFileName;
    protected static final String mExtensionAsp = "asp";
    protected static final String mExtensionChr = "spk";
    //private String mFileNameWithPath;
    protected int[] mValues;
    protected int mStartIndex;
    protected int mEndIndex;
    protected int mDataSize;

    public int getStartIndex() {
        return mStartIndex;
    }

    protected static final int SPK_CHR_FILE_DEFAULT_SIZE = 800;

    public int getDataSize() {
        return mEndIndex;//mValues.length;
    }

    public void setFileName(String fileName){
        mFileName = fileName;
	}

	public String getFileName(){
		return mFileName;
	}

    /**
     * @return only pure data, without zeros at the beginning, or end
     */
    public int[] getValues(){

        return mValues;
    }

    public int[] getWholeSpectrum() {
        int[] newData;
        newData = ArrayFunctions.moveArrayRight(mValues, mStartIndex);
        return newData;
    }

    public void setValues(int[] data){
        this.mValues = data;
        this.mEndIndex = data.length;
        this.mStartIndex = 0;
    }

    private int mSize = SPK_CHR_FILE_DEFAULT_SIZE;

    public SpectrumBase(String fileName){
        mFileName = fileName;
    }
    public SpectrumBase(){

    }


    /**
     * function to read Chroc *.spk files
     * @return size of read spectrum
     * @throws Exception
     */
    public int readValuesFromFile() {
//    public int readValuesFromFile() throws Exception {
        int i = 0;
        int k = 0;
        int value;
        int[] values = new int[AspectraGlobals.eMaxSpectrumSize];
//        mValues = new int[AspectraGlobals.eMaxSpectrumSize];
        try {
            File file;
            file = new File(mFileName);// TODO:here we need the whole name with path
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //StringBuffer stringBuffer = new StringBuffer();
            String line;

            // go to real date, first some lines are not important
            while ((line = bufferedReader.readLine()) != null && (i < 5)) {
                i++;
            }
            while (((line = bufferedReader.readLine()) != null) && (k < SPK_CHR_FILE_DEFAULT_SIZE)) {

                try {
                    value = Integer.parseInt(line);
                    if(value < 0)
                        value = 0;
                } catch (NumberFormatException e) {
                    //Will Throw exception!
                    //do something! anything to handle the exception.
                    value = 0;
                }

                values[k] = value;

                i++;
                k++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mStartIndex = 0;
        mEndIndex = k;
        mValues = ArrayUtils.subarray(values, mStartIndex, mEndIndex);
        return k;
    }

    // getters, setters
    public void setDataSize(int dataSize) {
        mEndIndex = dataSize;
    }

    public int[] moveData(int offset) {
//        int[] newData;
//        if(offset >= 0) { // move to the right
//            newData = ArrayFunctions.moveArrayRight(mValues, offset);
//        } else { // move to the left
//            newData = ArrayFunctions.moveArrayLeft(mValues, - offset);
//        }
//        mValues = newData;
        mStartIndex += offset;
        mEndIndex += offset;
        return mValues;
    }

    public int[] moveData_oldVersion(int offset) {
        int[] newData;
        if(offset >= 0) { // move to the right
            newData = ArrayFunctions.moveArrayRight(mValues, offset);
        } else { // move to the left
            newData = ArrayFunctions.moveArrayLeft(mValues, - offset);
        }
        mValues = newData;
        mStartIndex += offset;
        mEndIndex += offset;
        return mValues;
    }

    //TODO: check working and update indexies, add offset
    public int[] stretchData(int offset, float factor) {
        int[] newData;
        newData = ArrayFunctions.stretchArray(mValues, factor);
        mValues = newData;
        mEndIndex = newData.length;
        return mValues;
    }

}
