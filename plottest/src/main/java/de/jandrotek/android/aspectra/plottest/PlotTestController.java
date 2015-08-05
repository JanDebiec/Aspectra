package de.jandrotek.android.aspectra.plottest;

import android.content.Context;

import de.jandrotek.android.aspectra.core.SpectrumBase;

/**
 * Created by jan on 04.08.15.
 */
public class PlotTestController {

    private final  MainActivity mContext;

    public PlotTestController(MainActivity context){
        mContext = context;
        generateDemoSpectrum();
    }

    private final int eMoveDistance = 10;

    private SpectrumBase mSpectrum = null;

    public void PlotTestController(){
        generateDemoSpectrum();
    }

    public void generateDemoSpectrum(){
        mSpectrum = new SpectrumBase();
    }

    public void onButtonMoveLeft(){
        int[] data;
        mSpectrum.moveData(-eMoveDistance);
        // get data from spectrum
        data = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(data);
    }

    public void onButtonMoveRight(){
        int[] data;
        mSpectrum.moveData(eMoveDistance);

        // get data from spectrum
        data = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(data);
    }

    public void onButtonStretch(){
        int[] data;
        mSpectrum.stretchData(0, 2.0f);

        // get data from spectrum
        data = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(data);
    }

    public void onButtonSqeeze(){
        int[] data;
        mSpectrum.stretchData(0, 0.5f);

        // get data from spectrum
        data = mSpectrum.getValues();

        // push to PlotView
        mContext.updatePlot(data);
    }
}
