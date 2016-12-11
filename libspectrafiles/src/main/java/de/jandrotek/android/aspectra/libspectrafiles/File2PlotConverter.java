package de.jandrotek.android.aspectra.libspectrafiles;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumBase;

/**
 * Created by PanJan on 02.11.2016.
 */

public class File2PlotConverter {

    private ArrayList<String> mFileNamesList = null;

    int[][] mPlotdataArray = null;
    private String[] mFileNames = null;
    private SpectrumBase[] mSpectrumFiles = null;
    private int[] mPlotDataLength;

    public File2PlotConverter() {
        mPlotdataArray = new int[AspectraGlobals.eMaxPlotCount][];
        mFileNames = new String[AspectraGlobals.eMaxPlotCount];
        mSpectrumFiles = new SpectrumBase[AspectraGlobals.eMaxPlotCount];
        mPlotDataLength = new int[AspectraGlobals.eMaxPlotCount];
        mPlotdataArray = new int[AspectraGlobals.eMaxPlotCount][];
    }

    public void init(ArrayList<String> fileNamesList) {
        mFileNamesList = fileNamesList;
        int size = mFileNamesList.size();
        if (size > AspectraGlobals.eMaxPlotCount) {
            size = AspectraGlobals.eMaxSpectrumSize;
        }
        for (int i = 0; i < size; i++) {
            String fileName = mFileNamesList.get(i);
            // load file specified in mItem.content
            mFileNames[i] = SpectrumFiles.mPath + "/" + fileName;
            mSpectrumFiles[i] = new SpectrumBase(mFileNames[i]);
            try {
                mPlotDataLength[i] = mSpectrumFiles[i].readValuesFromFile();
                mPlotdataArray[i] = mSpectrumFiles[i].getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public int[][] getPlotData() {
        return mPlotdataArray;
    }
}
