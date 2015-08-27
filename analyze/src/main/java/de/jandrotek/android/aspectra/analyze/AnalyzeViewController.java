package de.jandrotek.android.aspectra.analyze;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
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

    //    private enum SpectrumNumber  {SpectrumToEdit, SpectrumReference};
    private static final int eSpectrumToEdit = 0;
    private static final int eSpectrumReference = 1;
    private String[] mSpectrumNames;
    private String[] mSpectrumAbsolutePathNames;
    private int[] mSpectrumLength;
    private int[][] mSpectrumToEditValues = null;
    private int mSpectrumLengthMax;
    private SpectrumBase[] mSpectrumToShow;
    // --Commented out by Inspection (25.08.15 08:14):private boolean mSpectrumAlreadyEdited = false;
    // --Commented out by Inspection (25.08.15 08:14):private Map<String, String> mSpectraMap;
    public static boolean mCalcBusy = false;
    // --Commented out by Inspection (25.08.15 08:14):private int[][] mFilesIntValues;
    private int mItemlistSizeAct = 2;// actually used
    private int[] mStartIndex;
    private int[] mStartIndexNew;

    public AnalyzeViewController() {
        mStartIndex = new int[mItemlistSizeAct];
        mSpectrumLength = new int[mItemlistSizeAct];
        mSpectrumNames = new String[mItemlistSizeAct];
        mSpectrumToShow = new SpectrumBase[mItemlistSizeAct];
        mSpectrumAbsolutePathNames = new String[mItemlistSizeAct];
        mSpectrumToEditValues = new int[mItemlistSizeAct][AspectraGlobals.eMaxSpectrumSize];
    }

    public void init(PlotViewFragment plotViewFragment, String nameToEdit, String nameReference) {
        mPlotViewFragment = plotViewFragment;
        mSpectrumNames[eSpectrumToEdit] = nameToEdit;
        mSpectrumNames[eSpectrumReference] = nameReference;

    }

    public void initDisplayInFragment() {
        mPlotViewFragment.createPlotSeries();
        for (int i = 0; i < mItemlistSizeAct; i++) {
            mPlotViewFragment.updateSinglePlot(i, mSpectrumToEditValues[i]);
        }
    }

    public void updateSpectraView(int spectrumLengthMax) {
        if (mPlotViewFragment != null) {
            mPlotViewFragment.updateSinglePlot(eSpectrumReference, mSpectrumToEditValues[eSpectrumReference]);
            mPlotViewFragment.updateSinglePlot(eSpectrumToEdit, mSpectrumToEditValues[eSpectrumToEdit]);
            mPlotViewFragment.updateGraphView(spectrumLengthMax);
        }
    }

    public int generateGraphViewData() {
        for (int i = 0; i < mItemlistSizeAct; i++) {
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
        mSpectrumLengthMax = Math.max(mSpectrumLength[eSpectrumToEdit], mSpectrumLength[eSpectrumReference]);
        mSpectrumToEditValues[eSpectrumToEdit] = mSpectrumToShow[eSpectrumToEdit].getValues();
        updateSpectraView(mSpectrumLengthMax);
        mPlotViewFragment.updateGraphView(mSpectrumLengthMax);
    }

    /**
     * function for control movement of spaectra in plot
     *
     * @param _movement try to position one spectra at zero.
     *                  By moving right, reference stays at 0, "edit" moves right
     *                  By moving left, if startIndex of Edit is > 0, then ref stays at 0,
     *                  if not then Edit will be positioned at 0 and Ref will be moved rights.
     *                  Second option: only part > 0 will  be shown (but not erased!)
     *                  This is not so good, because the pot size will be only so big as reference.
     *                  By first option, we have possibilities to resize the window
     */
    public void calcNewSpectraPositions(int _movement) {
        int movementEdit;
        int movementRef;

        mStartIndex[eSpectrumToEdit] = mSpectrumToShow[eSpectrumToEdit].getStartIndex();
        mStartIndex[eSpectrumReference] = mSpectrumToShow[eSpectrumReference].getStartIndex();
        mStartIndexNew[eSpectrumToEdit] = mStartIndex[eSpectrumToEdit];
        if (mStartIndexNew[eSpectrumToEdit] < 0) {
            int offsetRef = -mStartIndexNew[eSpectrumToEdit];
            mStartIndexNew[eSpectrumToEdit] = 0;
            mStartIndexNew[eSpectrumReference] = offsetRef;
        }

        movementEdit = mStartIndexNew[eSpectrumToEdit] - mStartIndex[eSpectrumToEdit];
        movementRef = mStartIndexNew[eSpectrumReference] - mStartIndex[eSpectrumReference];

        if (movementEdit != 0) {
            mSpectrumToShow[eSpectrumToEdit].moveData(movementEdit);
        }

        if (movementRef != 0) {
            mSpectrumToShow[eSpectrumReference].moveData(movementRef);
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

