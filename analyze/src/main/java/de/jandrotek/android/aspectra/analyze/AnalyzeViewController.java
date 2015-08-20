package de.jandrotek.android.aspectra.analyze;

import java.util.Map;

import de.jandrotek.android.aspectra.core.SpectrumBase;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

/**
 * Controller part for AnalyzeView. Befor all was in AnalyzeActivity and Fragment
 * Similar tasks as PlotViewController inm fileViewer
 * <p/>
 * Created by jan on 19.08.15.
 */
public class AnalyzeViewController {

    private PlotViewFragment mPlotViewFragment;

    private String mSpectrumNameToEdit;
    private String mSpectrumAbsNameToEdit;
    private String mSpectrumNameReference;
    private String mSpectrumNameAbsReference;
    private int mSpectrumToEditLength;
    private int mSpectrumReferenceLength;
    private int[] mSpectrumToEditValues = null;
    private int[] mSpectrumReferenceValues = null;
    private int mSpectrumLengthMax;
    private SpectrumBase mSpectrumToEdit;
    private SpectrumBase mSpectrumToEditBackup;
    private boolean mSpectrumAlreadyEdited = false;
    private SpectrumBase mSpectrumReference;
    private Map<String, String> mSpectraMap;
    public static boolean mCalcBusy = false;
    private int[][] mFilesIntValues;
    private int mItemlistSizeAct = 0;// actually used

    public AnalyzeViewController() {

    }

    public void init(PlotViewFragment plotViewFragment, String nameToEdit, String nameReference) {
        mPlotViewFragment = plotViewFragment;
        mSpectrumNameToEdit = nameToEdit;
        mSpectrumNameReference = nameReference;

    }

    public void initDisplayInFragment() {
        mPlotViewFragment.createPlotSeries();
        for (int i = 0; i < mItemlistSizeAct; i++) {
            mPlotViewFragment.updateSinglePlot(i, mFilesIntValues[i]);
        }
        mPlotViewFragment.updateGraphView();
    }

    public void updateSpectraView(int mSpectrumLengthMax) {
        if (mPlotViewFragment != null) {
            mPlotViewFragment.updateSinglePlot(1, mSpectrumReferenceValues);
            mPlotViewFragment.updateSinglePlot(0, mSpectrumToEditValues);
            mPlotViewFragment.updateGraphView();
        }
    }

    public void generateGraphViewData() {
        if (mSpectrumNameToEdit != null) {
            mSpectrumAbsNameToEdit = SpectrumFiles.mPath + "/" + mSpectrumNameToEdit;
            mSpectrumToEdit = new SpectrumBase(mSpectrumAbsNameToEdit);
            try {
                mSpectrumToEditLength = mSpectrumToEdit.readValuesFromFile();
                mSpectrumToEditValues = mSpectrumToEdit.getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mSpectrumNameReference != null) {

            mSpectrumNameAbsReference = SpectrumFiles.mPath + "/" + mSpectrumNameReference;
            mSpectrumReference = new SpectrumBase(mSpectrumNameAbsReference);
            try {
                mSpectrumReferenceLength = mSpectrumReference.readValuesFromFile();
                mSpectrumReferenceValues = mSpectrumReference.getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mSpectrumLengthMax = Math.max(mSpectrumToEditLength, mSpectrumReferenceLength);

    }


    public void updateEditedSpectrumInFragment() {
        mSpectrumToEditLength = mSpectrumToEdit.getDataSize();
        mSpectrumLengthMax = Math.max(mSpectrumToEditLength, mSpectrumReferenceLength);
        mSpectrumToEditValues = mSpectrumToEdit.getValues();
        updateSpectraView(mSpectrumLengthMax);
        mPlotViewFragment.updateGraphView();
    }

//    public class CalcTask extends AsyncTask<Void, Void, Void> {
//        private final int action;
//        private final float factor;
//        private final float staticPoint;
//
//        CalcTask(int action, float factor, float staticPoint){
//            this.action = action;
//            this.factor = factor;
//            this.staticPoint = staticPoint;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
////            mCalcBusy = true;
//            if (action == TouchView.ePlotAction_Move) {
//                if(factor < 0){ // move left
//                    int startIndex = mSpectrumToEdit.getStartIndex();
//                    // cheap and dirty handling, moving left, cut the data
//                    // moving left proper handling needs modify both spectra, edit and ref
//                    // and after moving right, again modify both
//
////                    if(startIndex < -factor) { // additinal we must append left reference
////                        mSpectrumReference.moveData((int) factor + startIndex );
////                        mSpectrumToEdit.moveData((int) startIndex);
////                    } else { // startIndex bigger as move
//                    mSpectrumToEdit.moveData((int) factor);
////                    }
//                } else { // move right
//                    mSpectrumToEdit.moveData((int) factor);
//                }
//            }
//            return(null);
//        }
//
//        @Override
//        protected void onPostExecute(Void arg0) {
//            updateEditedSpectrumInFragment();
//            mCalcBusy = false;
//        }

}

