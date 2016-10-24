package de.jandrotek.android.aspectra.plottest;

import de.jandrotek.android.aspectra.core.SpectrumBase;

/**
 * Created by jan on 04.08.15.
 */
public class PlotTestController {

    private final  MainActivity mContext;
    private final static int PLOT_DATA_SIZE = 800;
    int[] mData;// some spectra

    public PlotTestController(MainActivity context){
        mContext = context;
        generateDemoSpectrum();
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
        mData = mSpectrum.getValues();
    }

    public void onButtonMoveLeft(int i){
        if(mMoveAbs >= eMoveDistance) {
            mMoveAbs -= eMoveDistance;
            mSpectrum.moveData(-eMoveDistance);
            // get mData from spectrum
            mData = mSpectrum.getValues();
        }

        // push to PlotView
        mContext.updatePlot(mData);
    }

    public void onButtonMoveRight(){
        mMoveAbs += eMoveDistance;
        mSpectrum.moveData(eMoveDistance);

        // get mData from spectrum
        mData = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(mData);
    }

    public void onButtonStretch(){
        mSpectrum.stretchData(0, 2.0f);

        // get mData from spectrum
        mData = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(mData);
    }

    public void onButtonSqeeze(){
        mSpectrum.stretchData(0, 0.5f);

        // get mData from spectrum
        mData = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(mData);
    }
}
