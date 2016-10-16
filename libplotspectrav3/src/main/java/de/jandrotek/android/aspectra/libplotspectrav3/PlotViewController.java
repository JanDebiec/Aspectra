/**
 * 14.03.2015 source in Github
 */
package de.jandrotek.android.aspectra.libplotspectrav3;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumBase;
//import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
//import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

/**
 * The Controller-Part of PlotViewFragment
 * THe controller will be only used if interface through SpectrumFile
 */
public class PlotViewController
{
    private PlotViewFragment mPlotViewFragment;
    public PlotViewPresenter mPlotViewPresenter;

    private ArrayList<String> mItems = null;
    private String[] mFileNames = null;
    private SpectrumBase[] mSpectrumFiles = null;
    private int[][] mFilesIntValues;
    private int[] mFilesDataLength;
    private int mItemlistSizeAct = 0;// actually used
    private int mItemlistSizeNew = 0;// max already used

    private int mDataLengthMax = 0;
    private int mIndex = -1;

    public PlotViewController(int param1, ArrayList<String> items, int intemsCount) {
        if (param1 == AspectraGlobals.ACT_ITEM_VIEW_PLOT) {
            if (items != null) {
                mItems = items;
                mItemlistSizeAct = mItems.size();
            } else {
                mItemlistSizeAct = intemsCount;
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
        if (mPlotViewPresenter == null) {
            mPlotViewPresenter = new PlotViewPresenter(mItemlistSizeAct, mPlotViewFragment);
        }
        // get series count from fragment mItemlistSizeAct
        // in switch consider each case
        if (mItemlistSizeAct > 0) {// must be new
            mFileNames = new String[mItemlistSizeAct];
            mSpectrumFiles = new SpectrumBase[mItemlistSizeAct];
            mFilesIntValues = new int[mItemlistSizeAct][AspectraGlobals.eMaxSpectrumSize];
            mFilesDataLength = new int[mItemlistSizeAct];
            int i = 0;

            if (mItems != null) {
                for (String item : mItems) {

                    // load file specified in mItem.content
                    String fileName = item;
                    mFileNames[i] = SpectrumFiles.mPath + "/" + fileName;
                    mSpectrumFiles[i] = new SpectrumBase(mFileNames[i]);
                    try {
                        mFilesDataLength[i] = mSpectrumFiles[i].readValuesFromFile();
                        mFilesIntValues[i] = mSpectrumFiles[i].getValues();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            } else {
                mFilesIntValues[i] = new int[AspectraGlobals.eMaxSpectrumSize];
            }
            mDataLengthMax = findMaxDataLength();
        }
    }

    private int findMaxDataLength() {
        int max = 0;
        int i;
        for(i = 0; i < mItemlistSizeAct; i++){
            if(mFilesDataLength[i] > max){
                max = mFilesDataLength[i];
                mIndex = i;
            }
        }
        return max;
    }

    public void initDisplayInFragment() {
        mPlotViewFragment.createPlotSeries();
        for (int i = 0; i < mItemlistSizeAct; i++) {
            mPlotViewPresenter.updateSinglePlot(i, mFilesIntValues[i]);
        }
        mPlotViewFragment.updateGraphView(mDataLengthMax);
    }

}


