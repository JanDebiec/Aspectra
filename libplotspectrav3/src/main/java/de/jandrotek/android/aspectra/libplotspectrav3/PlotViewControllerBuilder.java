package de.jandrotek.android.aspectra.libplotspectrav3;

import java.util.ArrayList;

public class PlotViewControllerBuilder {
    private int param1;
    private ArrayList<String> items;
    private int intemsCount = 1; // minimal count if not initialized
    private PlotViewController mController = null;

    public PlotViewControllerBuilder setParam1(int param1) {
        this.param1 = param1;
        return this;
    }

    public PlotViewControllerBuilder setItems(ArrayList<String> items) {
        this.items = items;
        return this;
    }

    public PlotViewControllerBuilder setIntemsCount(int intemsCount) {
        this.intemsCount = intemsCount;
        return this;
    }

    public PlotViewController createPlotViewController() {
        if (mController == null) {
            mController = new PlotViewController(param1, items, intemsCount);
        }
        return mController;
    }

//    public PlotViewController getPlotViewController() {
//        return mController;
//    }
}