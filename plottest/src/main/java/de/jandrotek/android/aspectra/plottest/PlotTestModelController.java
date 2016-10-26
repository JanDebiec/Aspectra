package de.jandrotek.android.aspectra.plottest;

import java.util.ArrayList;
import java.util.List;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumBase;

/**
 * Created by jan on 04.08.15.
 */
public class PlotTestModelController {

    private final  MainActivity mContext;
    private final static int PLOT_DATA_SIZE = 800;
    int[][] mData;// some spectra
    int mSpectraCount = 0;
    List<SpectrumBase> mSpectraList;

    public PlotTestModelController(MainActivity context){
        mContext = context;
        mData = new int[AspectraGlobals.eMaxPlotCount][];
        mSpectraList = new ArrayList<SpectrumBase>(AspectraGlobals.eMaxPlotCount);
        generateDemoSpectrum();
        mData[0]= mSpectrum.getValues();
    }

    private final int eMoveDistance = 10;
    private int mMoveAbs = 0;

    private SpectrumBase mSpectrum = null;

    public void PlotTestController(){
        generateDemoSpectrum();
    }

    public void generateDemoSpectrum(){
        mSpectrum = new SpectrumBase();
        int[] mPlotIntValues = new int[PLOT_DATA_SIZE];
        for (int i = 0; i < PLOT_DATA_SIZE/2; i++)
            mPlotIntValues[i] = i;
        for (int i = PLOT_DATA_SIZE/2; i < PLOT_DATA_SIZE; i++)
            mPlotIntValues[i] = PLOT_DATA_SIZE - i;
        mSpectrum.setValues(mPlotIntValues);
        mSpectraList.add(mSpectrum);
        mSpectraCount++;
    }

    public void initInterfaceToPresenter(){
        mData[0] = mSpectrum.getValues();
        mContext.initPlotPresenter(mData);
    }

    public void onButtonMoveLeft(){
        if(mMoveAbs >= eMoveDistance) {
            mMoveAbs -= eMoveDistance;
            mSpectrum.moveData(-eMoveDistance);
            // get mData from spectrum
            int[] data = mSpectrum.getValues();
            // push to PlotView
            mContext.updatePlot(data);
        }

    }

    public void onButtonMoveRight(){
        mMoveAbs += eMoveDistance;
        mSpectrum.moveData(eMoveDistance);

        // get mData from spectrum
        int[] data = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(data);
    }

    public void onButtonStretch(){
        mSpectrum.stretchData(0, 2.0f);

        // get mData from spectrum
        int[] data = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(data);
    }

    public void onButtonSqeeze(){
        mSpectrum.stretchData(0, 0.5f);

        // get mData from spectrum
        int[] data = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(data);
    }
}
