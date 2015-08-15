/**
 * 14.03.2015 source in Github
 */
package de.jandrotek.android.aspectra.libplotspectrav3;

import android.net.Uri;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView.GraphViewData;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumBase;
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
    private static PlotViewController mController;
    private static PlotViewFragmentV mPlotViewFragment;

    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = PLOT_DATA_SIZE;

    // TODO: Rename and change types of parameters
    private static int mParam1;

    private static ArrayList<String> mItems = null;
    private static String[] mFileName = null;
    private static SpectrumBase[] mSpectrumFile = null;
    private static int[][] mFileIntValues;
    private static int[] mFileDataLength;
    private static int mItemlistSize = 0;
    private static int mDataLengthMax = 0;
    private static int mIndex = -1;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Type of calling activity.
     * @param items Araylist of spectrumFiles names.
     * @return A new instance of fragment PlotViewFragment_notToUse.
     * TODO: items as parameter is not a perfect idea, but what is better?
     */
     public static PlotViewController newInstance(int param1, ArrayList<String> items) {
        if(mController == null) {
            mController = new PlotViewController();
            mParam1 = param1;

            if (param1 == AspectraGlobals.ACT_ITEM_VIEW_PLOT) {
                if(items != null) {
                    mItems = items;
                    mItemlistSize = mItems.size();
                }
                create();
            } else { //TODO: then ???
            }
        }
        return mController;
    }

    private PlotViewController() {
    }

    // TODO: COntroller is normal class, content of create should be moved to constructor
    public static void create() {

        if(mItemlistSize > 0) {
            mPlotViewFragment = PlotViewFragmentV.newInstance(mItemlistSize);
            mFileName = new String[mItemlistSize];
            mSpectrumFile = new SpectrumBase[mItemlistSize];
            mFileIntValues = new int[mItemlistSize][AspectraGlobals.eMaxSpectrumSize];
            mFileDataLength = new int[mItemlistSize];
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
        } else { //we don't use items, only int[]
            // TODO; define what to do
        }
    }

    private static int findMaxDataLength(){
        int max = 0;
        int i = 0;
        for(i = 0; i < mItemlistSize; i++){
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



}


