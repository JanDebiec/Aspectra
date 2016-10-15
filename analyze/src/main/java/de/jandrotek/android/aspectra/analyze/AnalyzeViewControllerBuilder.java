package de.jandrotek.android.aspectra.analyze;

import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;

public class AnalyzeViewControllerBuilder {
    private int mPlotCount;
    private PlotViewFragment mPlotViewFragment;
    private PlotViewPresenter mPlotViewPresenter;
    private String mNameToEdit;
    private String mNameReference;

    public AnalyzeViewControllerBuilder setPlotCount(int plotCount) {
        mPlotCount = plotCount;
        return this;
    }

    public AnalyzeViewControllerBuilder setPlotViewFragment(PlotViewFragment plotViewFragment) {
        mPlotViewFragment = plotViewFragment;
        return this;
    }

    public AnalyzeViewControllerBuilder setPlotViewPresenter(PlotViewPresenter plotViewPresenter) {
        mPlotViewPresenter = plotViewPresenter;
        return this;
    }

    public AnalyzeViewControllerBuilder setNameToEdit(String nameToEdit) {
        mNameToEdit = nameToEdit;
        return this;
    }

    public AnalyzeViewControllerBuilder setNameReference(String nameReference) {
        mNameReference = nameReference;
        return this;
    }

    public AnalyzeViewController createAnalyzeViewController() {
        return new AnalyzeViewController(mPlotCount, mPlotViewFragment, mPlotViewPresenter, mNameToEdit, mNameReference);
    }
}