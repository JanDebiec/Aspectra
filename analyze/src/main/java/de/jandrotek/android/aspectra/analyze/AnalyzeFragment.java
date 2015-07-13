package de.jandrotek.android.aspectra.analyze;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
                mSpectrumNameToEdit = mStaticSpectra.get(ARG_ITEM_REFERENCE);
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
                realDataToEdit = new GraphView.GraphViewData[mSpectrumToEditLength];

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mSpectrumNameReference != null) {

            mSpectrumNameAbsReference = SpectrumFiles.mPath + "/" + mSpectrumNameReference;
            mSpectrumReference = new SpectrumChr(mSpectrumAbsNameToEdit);
            try {
                GraphView.GraphViewData[] realDataEdit;
                mSpectrumToEditLength = mSpectrumToEdit.readValuesChr();
                mSpectrumToEditValues = mSpectrumToEdit.getValues();
                realDataReference = new GraphView.GraphViewData[mSpectrumToEditLength];

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

//        mGraphView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                if(action == MotionEvent.ACTION_DOWN) {
//
//                    if (!AspectraGlobals.mSavePlotInFile) {
//                        AspectraGlobals.mSavePlotInFile = true;
//                    }
//                }
//                return true; //processed
//            }
//
//        });

        //TODO: optional - activate scaling / zooming
        // both modi will be handled with Touch-view helper class, not only in viewer
        // in liveView is disabled, first in AnalyzeActivity
        FrameLayout mFrameLayout = (FrameLayout)rootView.findViewById(de.jandrotek.android.aspectra.libplotspectrav3.R.id.flPlotView);
        mFrameLayout.addView(mGraphView);

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
