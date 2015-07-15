package de.jandrotek.android.aspectra.analyze;

import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SpinnerAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.HashMap;
import java.util.Map;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumAsp;
import de.jandrotek.android.aspectra.core.SpectrumChr;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

/**
 * A placeholder fragment containing a simple view.
 */
public class AnalyzeFragment extends Fragment {

    public static final String ARG_ITEM_REFERENCE = "item_reference";
    public static final String ARG_ITEM_EDIT = "item_edit";

    private String mSpectrumNameToEdit;
    private String mSpectrumAbsNameToEdit;
    private String mSpectrumNameReference;
    private String mSpectrumNameAbsReference;
    private int mSpectrumToEditLength;
    private int mSpectrumReferenceLength;
    private int mSpectrumLengthMax;
    private SpectrumChr mSpectrumToEdit;
    private SpectrumChr mSpectrumReference;
    private int[] mSpectrumToEditValues = null;
    private int[] mSpectrumReferenceValues = null;
    private int mColorEdit = Color.rgb(255, 0, 0);
    private int mColorRef = Color.rgb(0, 0, 255);
    private GraphView.GraphViewData[] realDataReference;
    private GraphView.GraphViewData[] realDataToEdit;
    private static Map<String, String> mStaticSpectra;
    private GraphViewSeries mDataSeriesEdit;
    private GraphViewSeries mDataSeriesRef;

    private LineGraphView mGraphView;

    private ActionBar.OnNavigationListener mOnNavigationListener;

    public static AnalyzeFragment newInstance(Map<String, String> spectra) {
        AnalyzeFragment fragment = new AnalyzeFragment();
        if (spectra.containsKey(AnalyzeFragment.ARG_ITEM_EDIT)) {
            mStaticSpectra.put(ARG_ITEM_EDIT, spectra.get(ARG_ITEM_EDIT));
        }
        if (spectra.containsKey(AnalyzeFragment.ARG_ITEM_REFERENCE)) {
            mStaticSpectra.put(ARG_ITEM_REFERENCE, spectra.get(ARG_ITEM_REFERENCE));
        }
        return fragment;
    }


    public AnalyzeFragment() {
        mStaticSpectra = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_ITEM_REFERENCE)) {
                mSpectrumNameReference = getArguments().getString(ARG_ITEM_REFERENCE);
            }
        }
        if (mStaticSpectra.containsKey(ARG_ITEM_REFERENCE)) {
            mSpectrumNameReference = mStaticSpectra.get(ARG_ITEM_REFERENCE);
        }

        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_ITEM_EDIT)) {
                mSpectrumNameReference = getArguments().getString(ARG_ITEM_EDIT);
            }
        }
        if (mStaticSpectra.containsKey(ARG_ITEM_EDIT)) {
                mSpectrumNameToEdit = mStaticSpectra.get(ARG_ITEM_EDIT);
        }
        if (mSpectrumNameToEdit != null) {
            mSpectrumAbsNameToEdit = SpectrumFiles.mPath + "/" + mSpectrumNameToEdit;
            mSpectrumToEdit = new SpectrumChr(mSpectrumAbsNameToEdit);
            try {
                GraphView.GraphViewData[] realDataEdit;
                mSpectrumToEditLength = mSpectrumToEdit.readValuesChr();
                mSpectrumToEditValues = mSpectrumToEdit.getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mSpectrumNameReference != null) {

            mSpectrumNameAbsReference = SpectrumFiles.mPath + "/" + mSpectrumNameReference;
            mSpectrumReference = new SpectrumChr(mSpectrumNameAbsReference);
            try {
                GraphView.GraphViewData[] realDataEdit;
                mSpectrumReferenceLength = mSpectrumReference.readValuesChr();
                mSpectrumReferenceValues= mSpectrumReference.getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mSpectrumLengthMax = Math.max(mSpectrumToEditLength, mSpectrumReferenceLength);
        realDataToEdit = new GraphView.GraphViewData[mSpectrumLengthMax];
        realDataReference = new GraphView.GraphViewData[mSpectrumLengthMax];
    }


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_ITEM_EDIT, mSpectrumNameToEdit);
        outState.putString(ARG_ITEM_REFERENCE, mSpectrumNameReference);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mSpectrumNameReference = savedInstanceState.getString(ARG_ITEM_REFERENCE);
            mSpectrumNameToEdit = savedInstanceState.getString(ARG_ITEM_EDIT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(de.jandrotek.android.aspectra.libplotspectrav3.R.layout.fragment_plot_view, container, false);

        ActionBar actionbar = ((AnalyzeActivity)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.analyze_actionbar_list,
                android.R.layout.simple_spinner_dropdown_item);

        mGraphView = new LineGraphView(getActivity(), "");

        if(mSpectrumToEditValues != null) {
            realDataToEdit = generateData(mSpectrumToEditValues, mSpectrumLengthMax);
            GraphViewSeries mDataSeriesEdit = new GraphViewSeries(
                    "",
                    new GraphViewSeries.GraphViewSeriesStyle(mColorEdit, 1),
                    realDataToEdit);
            mGraphView.addSeries(mDataSeriesEdit);
        }

        if(mSpectrumReferenceValues != null) {
            realDataReference = generateData(mSpectrumReferenceValues, mSpectrumLengthMax);
            GraphViewSeries mDataSeriesEdit = new GraphViewSeries(
                    "",
                    new GraphViewSeries.GraphViewSeriesStyle(mColorRef, 1),
                    realDataReference);
            mGraphView.addSeries(mDataSeriesEdit);
        }

        mGraphView.getGraphViewStyle().setTextSize(20);
        mGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
        mGraphView.getGraphViewStyle().setNumVerticalLabels(4);

        mGraphView.setViewPort(0, mSpectrumLengthMax);
        registerForContextMenu(mGraphView);

        //TODO: optional - activate scaling / zooming
        // both modi will be handled with Touch-view helper class, not only in viewer
        // in liveView is disabled, first in AnalyzeActivity
        FrameLayout mFrameLayout = (FrameLayout)rootView.findViewById(de.jandrotek.android.aspectra.libplotspectrav3.R.id.flPlotView);
        mFrameLayout.addView(mGraphView);
        setRetainInstance(true);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        mOnNavigationListener = new ActionBar.OnNavigationListener() {
            String[] strings = getResources().getStringArray(R.array.analyze_actionbar_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemID){
//                mSelectedChildFragmentID = position;
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                // check if one child already exists, not yet
//                if((mAsciiViewer == null) && (mTiltViewer == null) && (mLRViewer == null)){
////					if (position == SELECTED_LEFTRIGHT_CHILD){
////						mLRViewer = new LeftRightViewFragment();
////						transaction.add(R.id.child_fragment, mLRViewer).commit();
////					}
//                    if(position == SELECTED_ASCIIDATA_CHILD){
//                        mAsciiViewer = new AsciiViewFragment();
//                        transaction.add(R.id.child_fragment, mAsciiViewer).commit();
//                    }
//                    else if(position == SELECTED_TILTVIEW_CHILD){ // if child == tiltView
//                        mTiltViewer = new TiltViewFragment();
//                        transaction.add(R.id.child_fragment, mTiltViewer).commit();
//                    }
//                } else { // one child alredy exists
////					if (position == SELECTED_LEFTRIGHT_CHILD){
////						if(mLRViewer == null){
////							mLRViewer = new LeftRightViewFragment();
////												}
////						transaction.replace(R.id.child_fragment, mLRViewer).commit();
////					}
//                    if(position == SELECTED_ASCIIDATA_CHILD){
//                        if(mAsciiViewer == null){
//                            mAsciiViewer = new AsciiViewFragment();
//                        }
//                        transaction.replace(R.id.child_fragment, mAsciiViewer).commit();
//                    }
//                    else if(position == SELECTED_TILTVIEW_CHILD){ // if
//                        if(mTiltViewer == null){
//                            mTiltViewer = new TiltViewFragment();
//                        }
//                        transaction.replace(R.id.child_fragment, mTiltViewer).commit();
//                    }
//                }
                return true;
            }
        };
        actionbar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
        return rootView;
    }
    private GraphView.GraphViewData[] generateData(int[] data, int length) {
        GraphView.GraphViewData[]    realData = new GraphView.GraphViewData[length];
        for (int i=0; i<length; i++) {

            realData[i] = new GraphView.GraphViewData(i, data[i]);
        }
        //TODO: check in plot act length, and add needed data only for that length

        for(int i = length; i < mSpectrumLengthMax ; i++){
            realData[i] = new GraphView.GraphViewData(i, 0);
        }
        return realData;
    }

}
