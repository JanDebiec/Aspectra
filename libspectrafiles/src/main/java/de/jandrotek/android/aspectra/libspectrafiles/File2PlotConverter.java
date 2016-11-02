package de.jandrotek.android.aspectra.libspectrafiles;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;

/**
 * Created by PanJan on 02.11.2016.
 */

public class File2PlotConverter {

    private ArrayList<String> mFileNamesList = null;

    int[][] mPlotdataArray = null;

    public File2PlotConverter(ArrayList<String> fileNamesList) {
        mFileNamesList = fileNamesList;
    }

    public void init() {
        mPlotdataArray = new int[AspectraGlobals.eMaxPlotCount][];
        int size = mFileNamesList.size();
        if (size > AspectraGlobals.eMaxPlotCount) {
            size = AspectraGlobals.eMaxSpectrumSize;
        }
        for (int i = 0; i < size; i++) {
            String fileName = mFileNamesList.get(i);
            //TODO
        }
    }

    public int[][] getPlotData() {
        return mPlotdataArray;
    }
}
