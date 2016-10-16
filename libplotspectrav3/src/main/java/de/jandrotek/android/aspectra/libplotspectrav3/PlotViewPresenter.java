package de.jandrotek.android.aspectra.libplotspectrav3;

import android.util.Log;

import com.jjoe64.graphview.GraphView;

import de.jandrotek.android.aspectra.core.AspectraGlobals;

/**
 * Created by jan on 03.09.15.
 */
public class PlotViewPresenter {

    private static final String TAG = "PlotViewPresenter";
    private PlotViewFragment mFragment;
    private int mSpectraPlotCount = 0;
    private int[] mFileDataLength;
    private int[] mPlotIntDemoValues;
    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = 0;//PLOT_DATA_SIZE;
    private int[][] mFileIntValues;

    public PlotViewPresenter(int spectraPlotCount, PlotViewFragment fragment) {
        this.mSpectraPlotCount = spectraPlotCount;
        mFragment = fragment;
        mFileDataLength = new int[mSpectraPlotCount];
        mFileIntValues = new int[mSpectraPlotCount][AspectraGlobals.eMaxSpectrumSize];

    }

    public void updateSinglePlot(int index, int[] data) {
        int length = data.length;
        mFileIntValues[index] = data;
        if (length > realPlotDataSize) {
            realPlotDataSize = length;
        }
        GraphView.GraphViewData[] realData = generateData(index, mFileIntValues[index], length);
        if (mFragment.mInitialized) {
            mFragment.mDataSeries[index].resetData(realData);// in live view, here we get null exception
        }
    }

    public void updateFragmentPort(int start, int end) {
        mFragment.updateGraphViewPort(start, end);
    }


    //TODO: check the index boundaries, move to presenter
    private GraphView.GraphViewData[] generateData(int index, int[] data, int length) {
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
            if (mSpectraPlotCount > 1) {
                realPlotDataSize = findMaxDataLength();
            } else {
                realPlotDataSize = realLength;
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
        for (i = 0; i < mSpectraPlotCount; i++) {
            if (mFileDataLength[i] > max) {
                max = mFileDataLength[i];
            }
        }

        return max;
    }

    private GraphView.GraphViewData[] generateDemoData() {
        GraphView.GraphViewData[] demoData;
        mPlotIntDemoValues = new int[PLOT_DATA_SIZE];
        for (int i = 0; i < PLOT_DATA_SIZE / 2; i++)
            mPlotIntDemoValues[i] = i;
        for (int i = PLOT_DATA_SIZE / 2; i < PLOT_DATA_SIZE; i++)
            mPlotIntDemoValues[i] = PLOT_DATA_SIZE - i;

        demoData = new GraphView.GraphViewData[PLOT_DATA_SIZE];
        for (int i = 0; i < PLOT_DATA_SIZE; i++) {

            demoData[i] = new GraphView.GraphViewData(i, mPlotIntDemoValues[i]);
        }
        return demoData;
    }


}
