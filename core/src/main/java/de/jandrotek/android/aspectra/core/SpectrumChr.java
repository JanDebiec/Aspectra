package de.jandrotek.android.aspectra.core;

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
 */
public class SpectrumChr  extends SpectrumBase{
    private int mSize = SPK_CHR_FILE_DEFAULT_SIZE;


	public SpectrumChr(String fileName){
        mFileName = fileName;
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
        mValues = new int[AspectraGlobals.eMaxSpectrumSize];
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

                mValues[k] = value;

                i++;
                k++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return k;
    }

    // getters, setters
    public int getDataSize() {
        return mSize;
    }

    public void setDataSize(int dataSize) {
        mSize = dataSize;
    }

    public int[] moveData(int offset) {
        int[] newData;
        if(offset >= 0) {
            newData = ArrayFunctions.moveArrayRight(mValues, offset);
        } else {
            newData = ArrayFunctions.moveArrayLeft(mValues, - offset);
        }
        mValues = newData;
        return mValues;
    }

    public int[] stretchData(int offset, float factor) {
        int[] newData;
        newData = ArrayFunctions.stretchArray(mValues, offset, factor);
        mValues = newData;
        return mValues;
    }

}
