/**
 * 14.03.2015 source in Github
 */
package de.jandrotek.android.aspectra.viewer;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumBase;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragmentV;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

// lib ver 3.


/**
 * The Controller-Part of PlotViewFragment
 * THe controller will be only used if interface to SpectrumFile
 * ?? and if only int[], maybe too ??
 * TODO: organize interface: input files or int[] arrays
 * TODO: refactor constructor
 */
public class PlotViewController
{
    private PlotViewController mController;
    private PlotViewFragmentV mPlotViewFragment;

    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = PLOT_DATA_SIZE;

    // TODO: Rename and change types of parameters
    private int mParam1;

    private ArrayList<String> mItems = null;
    private String[] mFileName = null;
    private SpectrumBase[] mSpectrumFile = null;
    private int[][] mFileIntValues;
    private int[] mFileDataLength;
    private int mItemlistSizeAct = 0;// actually used
    private int mItemlistSizeNew = 0;// max already used

    private int mDataLengthMax = 0;
    private int mIndex = -1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Type of calling activity.
     * @param items Araylist of spectrumFiles names.
     * @return A new instance of fragment PlotViewFragment_notToUse.
     * TODO: items as parameter is not a perfect idea, but what is better?
     */
    public PlotViewController(int param1, ArrayList<String> items) {
        mParam1 = param1;
        if (param1 == AspectraGlobals.ACT_ITEM_VIEW_PLOT) {
            if (items != null) {
                mItems = items;
                mItemlistSizeAct = mItems.size();
//                mItemlistSizeNew = mItems.size();
            }
//            create();
        }
    }

    /**
     * TODO:
     * Function must consider more use cases:
     * 0. start: fragment has 0 series
     * 1. restart: fgment has the same amount of series
     * 2. restart: fragment has smaller amount of serias as needed
     * 3. restart: fragment has bigger amount of series as needed
     * @param plotViewFragment
     */
    public void init(PlotViewFragmentV plotViewFragment) {
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



//    private boolean performActions(MenuItem item){
//        return true;
//    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public void showPlot(int index, int[] data){
        mPlotViewFragment.showPlot(index, data);
    }

    /**
     * check if all of plots already exist, if not, first add series to graph,
     * then update plot
     */
    public void initDisplayInFragment() {
        mPlotViewFragment.createPlotSeries();
        for (int i = 0; i < mItemlistSizeAct; i++) {
            mPlotViewFragment.showPlot(i, mFileIntValues[i]);
        }
    }

}


