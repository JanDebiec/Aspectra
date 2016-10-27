package de.jandrotek.android.aspectra.libplotspectrav3;

import android.util.Log;

import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.List;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumBase;

/**
 * Created by jan on 03.09.15.
 * TODO: mSpectraPlotCount must be able to handle more sprectra as only one
 */
public class PlotViewPresenter {

    private static final String TAG = "PlotViewPresenter";
    private PlotViewFragment mPlotViewFragment;
    private int mItemListSizeAct = 0;// actually used
    private int mDataLengthMax = 0;
    private int[] mPlotDataLength;
    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = 0;//PLOT_DATA_SIZE;
    private int mCallerActivity = -1;

    public PlotViewPresenter(int callerActivity, PlotViewFragment fragment) {
        mCallerActivity = callerActivity;
        mPlotViewFragment = fragment;

    }

    public void init(int plotCount, int [][] data) {
        if(plotCount <= AspectraGlobals.eMaxPlotCount) {
            //TODO message to fragment, how many spectra
            mPlotViewFragment.setItemlistSize(plotCount);
            mItemListSizeAct = plotCount;
        } else {
            mPlotViewFragment.setItemlistSize(AspectraGlobals.eMaxPlotCount);
            mItemListSizeAct = AspectraGlobals.eMaxPlotCount;
        }
        mPlotDataLength = new int[AspectraGlobals.eMaxPlotCount];

        for (int i = 0; i < mItemListSizeAct; i++) {// must be new
            addPlot(data[i]);
        }

    }

    public void addPlot(int [] data){
        //TODO increment spectra count in fragment
//        mItemListSizeAct++;
        createSinglePlot(mItemListSizeAct - 1, data);
    }

    public void updateSinglePlot(int index, int[] data) {
        int length = data.length;
        if (length > realPlotDataSize) {
            realPlotDataSize = length;
        }
        GraphView.GraphViewData[] realData = generateData(data, length);
        if (mPlotViewFragment.mInitialized) {
            mPlotViewFragment.mDataSeries[index].resetData(realData);// in live view, here we get null exception
        }
    }

    public void createSinglePlot(int index, int[] data) {
        int length = data.length;
        if (length > realPlotDataSize) {
            realPlotDataSize = length;
        }
        GraphView.GraphViewData[] realData = generateData(data, length);
        mPlotViewFragment.createPlotSerie(realData, index);
    }

    public void updateFragmentPort(int start, int end) {
        mPlotViewFragment.updateGraphViewPort(start, end);
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
            if (mPlotDataLength[i] > max) {
                max = mPlotDataLength[i];
            }
        }
        return max;
    }
}
