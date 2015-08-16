/**
 * 14.03.2015 source in Github
 */
package de.jandrotek.android.aspectra.viewer;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumBase;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

// lib ver 3.


/**
 * The Controller-Part of PlotViewFragment
 * THe controller will be only used if interface through SpectrumFile
 */
public class PlotViewController
{
    private PlotViewFragment mPlotViewFragment;

    private ArrayList<String> mItems = null;
    private String[] mFileName = null;
    private SpectrumBase[] mSpectrumFile = null;
    private int[][] mFileIntValues;
    private int[] mFileDataLength;
    private int mItemlistSizeAct = 0;// actually used
    private int mItemlistSizeNew = 0;// max already used

    private int mDataLengthMax = 0;
    private int mIndex = -1;

    public PlotViewController(int param1, ArrayList<String> items) {
        if (param1 == AspectraGlobals.ACT_ITEM_VIEW_PLOT) {
            if (items != null) {
                mItems = items;
                mItemlistSizeAct = mItems.size();
//                mItemlistSizeNew = mItems.size();
            }
        }
    }

    /**
     * TODO:
     * Function must consider more use cases:
     * 0. start: fragment has 0 series
     * 1. restart: fgment has the same amount of series
     * 2. restart: fragment has smaller amount of serias as needed
     * 3. restart: fragment has bigger amount of series as needed
     *
     * seems that fragment can handle the task alone,
     * using createPlotSeries() function
     *
     * @param plotViewFragment
     */
    public void init(PlotViewFragment plotViewFragment) {
        mPlotViewFragment = plotViewFragment;
        // get series count from fragment mItemlistSizeAct
        // in switch consider each case
        if (mItemlistSizeAct > 0) {// must be new
            mFileName = new String[mItemlistSizeAct];
            mSpectrumFile = new SpectrumBase[mItemlistSizeAct];
            mFileIntValues = new int[mItemlistSizeAct][AspectraGlobals.eMaxSpectrumSize];
            mFileDataLength = new int[mItemlistSizeAct];
            int i = 0;

            for (String item : mItems) {

                // load file specified in mItem.content
                String fileName = item;
                mFileName[i] = SpectrumFiles.mPath + "/" + fileName;
                mSpectrumFile[i] = new SpectrumBase(mFileName[i]);
                try {
                    mFileDataLength[i] = mSpectrumFile[i].readValuesFromFile();
                    mFileIntValues[i] = mSpectrumFile[i].getValues();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
            mDataLengthMax = findMaxDataLength();
        }
    }

    private int findMaxDataLength() {
        int max = 0;
        int i = 0;
        for(i = 0; i < mItemlistSizeAct; i++){
            if(mFileDataLength[i] > max){
                max = mFileDataLength[i];
                mIndex = i;
            }
        }
        return max;
    }

    public void initDisplayInFragment() {
        mPlotViewFragment.createPlotSeries();
        for (int i = 0; i < mItemlistSizeAct; i++) {
            mPlotViewFragment.showPlot(i, mFileIntValues[i]);
        }
    }

}


