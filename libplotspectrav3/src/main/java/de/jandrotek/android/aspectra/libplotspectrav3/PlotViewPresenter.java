package de.jandrotek.android.aspectra.libplotspectrav3;

import com.jjoe64.graphview.GraphView;

import de.jandrotek.android.aspectra.core.AspectraGlobals;

/**
 * Created by jan on 03.09.15.
 */
public class PlotViewPresenter {

    //    private GraphView.GraphViewData[][] realData = null;
//    private int realPlotDataSize = 0;//PLOT_DATA_SIZE;
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
//        realData = new GraphView.GraphViewData[mSpectraPlotCount][AspectraGlobals.eMaxSpectrumSize];
        mFileDataLength = new int[mSpectraPlotCount];
        mFileIntValues = new int[mSpectraPlotCount][AspectraGlobals.eMaxSpectrumSize];

    }

    public void updateSinglePlot(int index, int[] data) {
        int length = data.length;
        mFileIntValues[index] = data;
//        if (mDataSeries[index] != null) {
        if (length > realPlotDataSize) {
            realPlotDataSize = length;
        }
        GraphView.GraphViewData[] realData = generateData(index, mFileIntValues[index], length);
        mFragment.mDataSeries[index].resetData(realData);
//        }
    }


    //TODO: check the index boundaries, move to presenter
    private GraphView.GraphViewData[] generateData(int index, int[] data, int length) {
        int realLength;
        GraphView.GraphViewData[] realData = new GraphView.GraphViewData[AspectraGlobals.eMaxSpectrumSize];

        if (length > AspectraGlobals.eMaxSpectrumSize) {
            realLength = AspectraGlobals.eMaxSpectrumSize;
        } else {
            realLength = length;
        }
        realLength = length;
//        if(realData[index] == null){
//            realData[index] = new GraphView.GraphViewData[length];
//        }
        for (int i = 0; i < realLength; i++) {

            realData[i] = new GraphView.GraphViewData(i, data[i]);
        }
        //TODO: check in plot act length, and add needed data only for that length
        if (mSpectraPlotCount > 1) {
            realPlotDataSize = findMaxDataLength();
        } else {
            realPlotDataSize = realLength;
        }


        for (int i = realLength; i < realPlotDataSize; i++) {
            realData[i] = new GraphView.GraphViewData(i, 0);
        }
        return realData;
    }

    private int findMaxDataLength() {
        int max = 0;
        int index = -1;
        int i = 0;
        for (i = 0; i < mSpectraPlotCount; i++) {
            if (mFileDataLength[i] > max) {
                max = mFileDataLength[i];
                index = i;
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
