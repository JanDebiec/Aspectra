package de.jandrotek.android.aspectra.libplotspectrav3;

import java.util.ArrayList;

public class PlotViewControllerBuilder {
    private int param1;
    private ArrayList<String> items;
    private int intemsCount = 1; // minimal count if not initialized

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
        return new PlotViewController(param1, items, intemsCount);
    }
}