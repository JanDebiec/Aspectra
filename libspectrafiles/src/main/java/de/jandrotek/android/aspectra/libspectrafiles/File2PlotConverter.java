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
            try {
                String fileName = mFileNamesList.get(i);
                // load file specified in mItem.content
                mFileNames[i] = SpectrumFiles.mPath + "/" + fileName;
                mSpectrumFiles[i] = new SpectrumBase(mFileNames[i]);
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
