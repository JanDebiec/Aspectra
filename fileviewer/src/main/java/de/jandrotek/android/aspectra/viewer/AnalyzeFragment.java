package de.jandrotek.android.aspectra.viewer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumAsp;
import de.jandrotek.android.aspectra.core.SpectrumChr;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;
import de.jandrotek.android.aspectra.libtouch.TouchView;

//import com.jandrotek.aspectra.spectrumviewer.dummy.DummyContent;


/**
 * TODO: load Asp files, if Chr then convert to Asp
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class AnalyzeFragment extends Fragment
    implements TouchView.OnTouchViewInteractionListener
{
    private static final String TAG = "DetailItemsFrag";
   /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private int mMaxSpectrumSize = AspectraGlobals.eMaxSpectrumSize;

//    private static final int eModeEdit = 1;
//    private static final int eModeZoom = 2;
//    private static final int eModeExit = 3;
    private int mAnalyzeMode = eModeEdit;


    /**
     * The dummy content this fragment is presenting.
     */
    private ListContent.SpectrumItem mItem;
    private String mFileName;
    private SpectrumChr mSpectrumChr;
    private static final int eNumHorLabels = 5;
    private static final int eNumVertLabels = 4;
    private static final float eGraphTextSize = 20;
    private static final double ePlotPortStart = 0;
    private static final double ePlotPortEnd = 800;


    public void setSpectrum2Analyze(SpectrumAsp spectrum2Analyze) {
        this.mSpectrum2Analyze = mSpectrum2Analyze;
    }

    public void setSpectrumReference(SpectrumAsp spectrumReference) {
        this.mSpectrumReference = mSpectrumReference;
    }

    public void setSpectrumChr(SpectrumChr spectrumChr) {
        this.mSpectrumChr = mSpectrumChr;
    }

    private SpectrumAsp mSpectrum2Analyze = null;
    private SpectrumAsp mSpectrumReference = null;

    private int[] mFileIntValues;
    private GraphView mGraphView;
    private GraphViewData[] mData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AnalyzeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int fileLength;
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate() called");
        }

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ListContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            // load file specified in mItem.content
            mFileName = SpectrumFiles.mPath +"/" + mItem.name;
//            mSpectrumChr = new SpectrumChr(mFileName);
            mSpectrum2Analyze = new SpectrumAsp(mFileName);
            try{
            	mFileIntValues = mSpectrum2Analyze.getValues();
                fileLength = mFileIntValues.length;

            } catch (Exception e) {
                e.printStackTrace();
            }
            // prepare dummy data
            mData = new GraphViewData[mMaxSpectrumSize];
            for (int i=0; i<mMaxSpectrumSize; i++) {

                mData[i] = new GraphViewData(i, mFileIntValues[i]);
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onViewCreated");
        }
        View rootView = inflater.inflate(R.layout.fragment_analyze, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.name);
            // show data from loaded file in ChartView
        }
        mGraphView = createLineGraphSingle(mData);

        updateZoomMode(mGraphView);
        FrameLayout layout = (FrameLayout) rootView.findViewById(R.id.fvPlotAnalyzeView);
        layout.addView(mGraphView);


        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onStart");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onResume");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onPause");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onStop");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onDestroy");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    public void onTouchViewInteraction(int _toolId, float _value){
        if(mAnalyzeMode == eModeEdit){
            // check what a tool; move or stretch
            // recalc the spectrum-copy
            // update plot

        } // else ignore
    }

    private GraphView createLineGraphSingle(GraphViewData[] _data) {
        LineGraphView lineGraphView = null;
        lineGraphView = new LineGraphView( getActivity(), "");
        // add data
        GraphViewSeriesStyle style = new GraphViewSeriesStyle();
        style.thickness = 1;
        GraphViewSeries dataSeries = new GraphViewSeries("", style,  _data);
        lineGraphView.addSeries(dataSeries);
        lineGraphView.getGraphViewStyle().setTextSize(eGraphTextSize);
        lineGraphView.getGraphViewStyle().setNumHorizontalLabels(eNumHorLabels);
        lineGraphView.getGraphViewStyle().setNumVerticalLabels(eNumVertLabels);

        GraphViewSeriesStyle getStyle = dataSeries.getStyle();
        // set view port, start=2, size=40
        lineGraphView.setViewPort(ePlotPortStart, ePlotPortEnd);

        return lineGraphView;
    }

//    private void updateZoomMode(GraphView _graphView) {
//        if(mAnalyzeMode == eModeZoom) {
//            // optional - activate scaling / zooming
//            _graphView.setScrollable(true);
//            _graphView.setScalable(true);
//        }
//        else {
//            _graphView.setScrollable(false);
//            _graphView.setScalable(false);
//
//        }
//    }

    private setPlotAnalyzeMode(int _mode){
        if(_mode == eModeEdit){

        }
        else if(_mode == eModeZoom){

        }
        else {

        }
    }

}
