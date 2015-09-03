package de.jandrotek.android.aspectra.analyze;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumBase;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

/**
 * Controller part for AnalyzeView. In the past, all was in AnalyzeActivity and Fragment
 * Similar tasks as PlotViewController in fileViewer
 * <p/>
 * Created by jan on 19.08.15.
 */
public class AnalyzeViewController {

    private PlotViewFragment mPlotViewFragment;
    private PlotViewPresenter mPlotViewPresenter;

    private static final int eSpectrumToEdit = 0;
    private static final int eSpectrumReference = 1;
    private String[] mSpectrumNames;
    private String[] mSpectrumAbsolutePathNames;
    private int[] mSpectrumLength;
    private int[][] mSpectrumToEditValues = null;
    private int mSpectrumLengthMax;
    private SpectrumBase[] mSpectrumToShow;
    public static boolean mCalcBusy = false; // can be still used
    private int mItemListSizeAct = 2;// actually used
    private int[] mStartIndexOld;
    private int[] mStartIndexNew;
    private int[] mMovement;

    public AnalyzeViewController() {
        mStartIndexOld = new int[mItemListSizeAct];
        mStartIndexNew = new int[mItemListSizeAct];
        mMovement = new int[mItemListSizeAct];
        mSpectrumLength = new int[mItemListSizeAct];
        mSpectrumNames = new String[mItemListSizeAct];
        mSpectrumToShow = new SpectrumBase[mItemListSizeAct];
        mSpectrumAbsolutePathNames = new String[mItemListSizeAct];
        mSpectrumToEditValues = new int[mItemListSizeAct][AspectraGlobals.eMaxSpectrumSize];
    }

    public void init(PlotViewFragment plotViewFragment,
                     PlotViewPresenter plotViewPresenter,
                     String nameToEdit, String nameReference) {
        mPlotViewFragment = plotViewFragment;
        mPlotViewPresenter = plotViewPresenter;
        mSpectrumNames[eSpectrumToEdit] = nameToEdit;
        mSpectrumNames[eSpectrumReference] = nameReference;
    }

    public void initDisplayInFragment() {
        mPlotViewFragment.createPlotSeries();
        for (int i = 0; i < mItemListSizeAct; i++) {
            mPlotViewPresenter.updateSinglePlot(i, mSpectrumToEditValues[i]);
        }
    }

    public void updateSpectraView(int spectrumLengthMax) {
        if (mPlotViewFragment != null) {
            mPlotViewPresenter.updateSinglePlot(eSpectrumReference, mSpectrumToEditValues[eSpectrumReference]);
            mPlotViewPresenter.updateSinglePlot(eSpectrumToEdit, mSpectrumToEditValues[eSpectrumToEdit]);
            mPlotViewFragment.updateGraphView(spectrumLengthMax);
        }
    }

    public int generateGraphViewData() {
        for (int i = 0; i < mItemListSizeAct; i++) {
            if (mSpectrumNames[i] != null) {
                mSpectrumAbsolutePathNames[i] = SpectrumFiles.mPath + "/" + mSpectrumNames[i];
                mSpectrumToShow[i] = new SpectrumBase(mSpectrumAbsolutePathNames[i]);
                try {
                    mSpectrumLength[i] = mSpectrumToShow[i].readValuesFromFile();
                    mSpectrumToEditValues[i] = mSpectrumToShow[i].getValues();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        mSpectrumLengthMax = Math.max(mSpectrumLength[eSpectrumToEdit], mSpectrumLength[eSpectrumReference]);
        return mSpectrumLengthMax;
    }


    public void updateEditedSpectrumInFragment() {
        mSpectrumLength[eSpectrumToEdit] = mSpectrumToShow[eSpectrumToEdit].getDataSize();
        mSpectrumLength[eSpectrumReference] = mSpectrumToShow[eSpectrumReference].getDataSize();
        mSpectrumLengthMax = Math.max(mSpectrumLength[eSpectrumToEdit], mSpectrumLength[eSpectrumReference]);
        mSpectrumToEditValues[eSpectrumToEdit] = mSpectrumToShow[eSpectrumToEdit].getValues();
        mSpectrumToEditValues[eSpectrumReference] = mSpectrumToShow[eSpectrumReference].getValues();
        updateSpectraView(mSpectrumLengthMax);
        mPlotViewFragment.updateGraphView(mSpectrumLengthMax);
    }

    /**
     * function for control movement of spectra in plot
     *
     * @param _movement try to position one spectra at zero.
     *
     * By moving right, reference stays at 0, "edit" moves right
     * By moving left, if startIndex of Edit is > 0, then ref stays at 0,
     * if not then Edit will be positioned at 0 and Ref will be moved rights.
     * Second option: only part > 0 will  be shown (but not erased!)
     * This is not so good, because the pot size will be only so big as reference.
     * By first option, we have possibilities to resize the window
     */
    public void calcNewSpectraPositions(int _movement) {
        getOldPositions();
        calcNewPositions(_movement);
        moveSpectra();
    }

    private void getOldPositions() {
        mStartIndexOld[eSpectrumToEdit] = mSpectrumToShow[eSpectrumToEdit].getStartIndex();
        mStartIndexOld[eSpectrumReference] = mSpectrumToShow[eSpectrumReference].getStartIndex();
    }


    /**
     * pure math, without any connections, should be tested !
     *
     * @param _movement positive to the right
     */
    public void calcNewPositions(int _movement) {

        int offsetLeft;
        int spectrumAtLeft;

        // calc new positions
        mStartIndexNew[eSpectrumToEdit] = mStartIndexOld[eSpectrumToEdit] + _movement;
        mStartIndexNew[eSpectrumReference] = mStartIndexOld[eSpectrumReference];

        // find which one os at left
        if (mStartIndexNew[eSpectrumToEdit] >= mStartIndexNew[eSpectrumReference]) {
            spectrumAtLeft = eSpectrumReference;
        } else {
            spectrumAtLeft = eSpectrumToEdit;
        }

        //place one spectrum, most left at zero
        offsetLeft = mStartIndexNew[spectrumAtLeft];
        if (offsetLeft != 0) {
            mStartIndexNew[eSpectrumToEdit] -= offsetLeft;
            mStartIndexNew[eSpectrumReference] -= offsetLeft;
        }

        mMovement[eSpectrumToEdit] = mStartIndexNew[eSpectrumToEdit] - mStartIndexOld[eSpectrumToEdit];
        mMovement[eSpectrumReference] = mStartIndexNew[eSpectrumReference] - mStartIndexOld[eSpectrumReference];
    }

    private void moveSpectra() {
        if (mMovement[eSpectrumToEdit] != 0) {
            mSpectrumToShow[eSpectrumToEdit].moveData(mMovement[eSpectrumToEdit]);
        }

        if (mMovement[eSpectrumReference] != 0) {
            mSpectrumToShow[eSpectrumReference].moveData(mMovement[eSpectrumReference]);
        }
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
////                    if(startIndex < -factor) { // additional we must append left reference
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

    //************************************************************************************
    // these functions will be used only in testing
    //************************************************************************************
    public int[] getStartIndexNew() {
        return mStartIndexNew;
    }

    public void setStartIndexOld(int[] startIndexOld) {
        mStartIndexOld = startIndexOld;
    }

    public void setStartIndexNew(int[] startIndexNew) {
        mStartIndexNew = startIndexNew;
    }

    public int[] getMovement() {
        return mMovement;
    }
}

