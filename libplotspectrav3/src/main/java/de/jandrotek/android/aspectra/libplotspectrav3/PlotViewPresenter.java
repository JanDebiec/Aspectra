package de.jandrotek.android.aspectra.libplotspectrav3;

import android.util.Log;

import com.jjoe64.graphview.GraphView;

import de.jandrotek.android.aspectra.core.AspectraGlobals;

/**
 * Created by jan on 03.09.15.
 * TODO: mSpectraPlotCount must be able to handle more sprectra as only one
 */
public class PlotViewPresenter {

    private static final String TAG = "PlotViewPresenter";
    private PlotViewFragment mPlotViewFragment;
//    private int mItemListSizeAct = 0;
    private int mItemListSizeAct = 0;// actually used
    private int mDataLengthMax = 0;
    private int[] mFileDataLength;
    private int[] mPlotIntDemoValues;
    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = 0;//PLOT_DATA_SIZE;
    private int[][] mPlotIntValues;
    private int mCallerActivity = -1;

    public PlotViewPresenter(int callerActivity, PlotViewFragment fragment) {
        mCallerActivity = callerActivity;
        mPlotViewFragment = fragment;
//        mFileDataLength = new int[mSpectraPlotCount];
//        mFileIntValues = new int[mSpectraPlotCount][AspectraGlobals.eMaxSpectrumSize];

    }

//    public void init(PlotViewFragment plotViewFragment) {
//            mPlotViewFragment = plotViewFragment;
//    }

        public void updateSinglePlot(int index, int[] data) {
        int length = data.length;
        mPlotIntValues[index] = data;
        if (length > realPlotDataSize) {
            realPlotDataSize = length;
        }
        GraphView.GraphViewData[] realData = generateData( mPlotIntValues[index], length);
        if (mPlotViewFragment.mInitialized) {
            mPlotViewFragment.mDataSeries[index].resetData(realData);// in live view, here we get null exception
        }
    }

    public void updateFragmentPort(int start, int end) {
        mPlotViewFragment.updateGraphViewPort(start, end);
    }

    public void initDisplayInFragment() {
        mPlotViewFragment.createPlotSeries();
        for (int i = 0; i < mItemListSizeAct; i++) {
            updateSinglePlot(i, mPlotIntValues[i]);
        }
        mPlotViewFragment.updateGraphViewLength(mDataLengthMax);
    }


    private GraphView.GraphViewData[] generateData( int[] data, int length) {
        int realLength;
        GraphView.GraphViewData[] realData = new GraphView.GraphViewData[AspectraGlobals.eMaxSpectrumSize];

        try {
            if (length > AspectraGlobals.eMaxSpectrumSize) {
                realLength = AspectraGlobals.eMaxSpectrumSize;
            } else {
                realLength = length;
            }
            for (int i = 0; i < realLength; i++) {

                realData[i] = new GraphView.GraphViewData(i, data[i]);
            }
            //TODO: check in plot act length, and add needed data only for that length
            if (mItemListSizeAct > 1) {
                mDataLengthMax = findMaxDataLength();
            } else {
                mDataLengthMax = realLength;
            }
            for (int i = realLength; i < AspectraGlobals.eMaxSpectrumSize; i++) {
                realData[i] = new GraphView.GraphViewData(i, 0);
            }
        } catch (Exception exception) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "Exception caused by generateData()", exception);
            }
        }
        return realData;
    }

    private int findMaxDataLength() {
        int max = 0;
        int i;
        for (i = 0; i < mItemListSizeAct; i++) {
            if (mFileDataLength[i] > max) {
                max = mFileDataLength[i];
            }
        }
        return max;
    }

    // demoData should be generated in Model
//    private GraphView.GraphViewData[] generateDemoData() {
//        GraphView.GraphViewData[] demoData;
//        mPlotIntDemoValues = new int[PLOT_DATA_SIZE];
//        for (int i = 0; i < PLOT_DATA_SIZE / 2; i++)
//            mPlotIntDemoValues[i] = i;
//        for (int i = PLOT_DATA_SIZE / 2; i < PLOT_DATA_SIZE; i++)
//            mPlotIntDemoValues[i] = PLOT_DATA_SIZE - i;
//
//        demoData = new GraphView.GraphViewData[PLOT_DATA_SIZE];
//        for (int i = 0; i < PLOT_DATA_SIZE; i++) {
//
//            demoData[i] = new GraphView.GraphViewData(i, mPlotIntDemoValues[i]);
//        }
//        return demoData;
//    }


}
