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
 * The Controller- Part of PlotViewFragment
 * TODO: organize interface: input files or int[] arrays
 * TODO: refactor constructor
 */
public class PlotViewFragmentC
{
    private static PlotViewFragmentC mController;
    public static final String ARG_ITEM_IDS = "item_ids";
        // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = PLOT_DATA_SIZE;

    // TODO: Rename and change types of parameters
    private static int mParam1;
    private int mParam2;

    private OnFragmentInteractionListener mListener;
    private static ArrayList<String> mItems;
    private String[] mFileName;
    private SpectrumBase[] mSpectrumFile;
    private int[][] mFileIntValues;
    private int[] mFileDataLength;
    private int mItemlistSize = 0;
    private int mDataLengthMax = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Type of calling activity.
     * @param items Araylist of spectrumFiles names.
     * @return A new instance of fragment PlotViewFragment.
     * TODO: items as parameter is not a perfect idea, but what is better?
     */
        public PlotViewFragmentC newInstance(int param1, ArrayList<String> items) {
        if(mController == null) {
            mController = new PlotViewFragmentC();
            mParam1 = param1;

            if (param1 == AspectraGlobals.ACT_ITEM_VIEW_PLOT) {
                mItems = items;
                create();
            }
        }
        return mController;
    }

    public PlotViewFragmentC() {
        // Required empty public constructor
    }

    // TODO: COntroller is normal class, content of create should be moved to constructor
    public void create() {
            mItemlistSize = mItems.size();
            mFileName = new String[mItemlistSize];
            mSpectrumFile = new SpectrumBase[mItemlistSize];
            mFileIntValues = new int[mItemlistSize][AspectraGlobals.eMaxSpectrumSize];
//            realData = new GraphViewData[mItemlistSize][AspectraGlobals.eMaxSpectrumSize];
            mFileDataLength = new int[mItemlistSize];
            int i = 0;

            for(String item : mItems){

                // load file specified in mItem.content
                String fileName = item;
                mFileName[i] = SpectrumFiles.mPath +"/" + fileName;
                mSpectrumFile[i] = new SpectrumBase(mFileName[i]);
                try{
                    GraphViewData[] tempRealData;
                    mFileDataLength[i] = mSpectrumFile[i].readValuesFromFile();
                    mFileIntValues[i] = mSpectrumFile[i].getValues();
                    tempRealData = new GraphViewData[mFileDataLength[i]];
//                    realData[i] = tempRealData;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
            mDataLengthMax = findMaxDataLength();
//        } else {
//            mItemlistSize = 1;
//            mFileDataLength = new int[mItemlistSize];
//            mDataLengthMax = PLOT_DATA_SIZE;
//        }
    }

    private int findMaxDataLength(){
        int max = 0;
        int index = -1;
        int i = 0;
        for(i = 0; i < mItemlistSize; i++){
            if(mFileDataLength[i] > max){
                max = mFileDataLength[i];
                index = i;
            }
        }

        return max;
    }



    private boolean performActions(MenuItem item){
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    public void showPlot(int index, int[] data, int length){
//        if(mDataSeries != null) {
//            realPlotDataSize = length;
//            generateData(index, data, length);
//            mGraphView.setViewPort(0, realPlotDataSize);
//            mDataSeries.resetData(realData[index]);
//        }
//    }


}


