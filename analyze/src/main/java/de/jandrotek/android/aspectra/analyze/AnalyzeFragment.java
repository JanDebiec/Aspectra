package de.jandrotek.android.aspectra.analyze;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;

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
    private SpectrumAsp mSpectrumToEdit;
    private SpectrumAsp mSpectrumReference;
    private int[] mSpectrumToEditValues;
    private int[] mSpectrumReferenceValues;
    private int mColorEdit = Color.rgb(255, 0, 0);
    private int mColorRef = Color.rgb(0, 0, 255);
    private GraphView.GraphViewData[] realDataReference;
    private GraphView.GraphViewData[] realDataToEdit;
    private static Map<String, String> mStaticSpectra;

    public static AnalyzeFragment newInstance(Map<String, String> spectra) {
        AnalyzeFragment fragment = new AnalyzeFragment();
//        Bundle args = new Bundle();
//        mStaticSectra

        if (spectra.containsKey(AnalyzeFragment.ARG_ITEM_EDIT)) {
//            args.putString(AnalyzeFragment.ARG_ITEM_EDIT, spectra.get(AnalyzeFragment.ARG_ITEM_EDIT));
            mStaticSpectra.put(ARG_ITEM_EDIT, spectra.get(ARG_ITEM_EDIT));
        }
        if (spectra.containsKey(AnalyzeFragment.ARG_ITEM_REFERENCE)) {
//            args.putString(AnalyzeFragment.ARG_ITEM_REFERENCE, spectra.get(AnalyzeFragment.ARG_ITEM_REFERENCE));
            mStaticSpectra.put(ARG_ITEM_REFERENCE, spectra.get(ARG_ITEM_REFERENCE));
        }
//        fragment.setArguments(args);
        return fragment;
    }


    public AnalyzeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(ARG_ITEM_EDIT)) {
                mSpectrumNameToEdit = getArguments().getString(ARG_ITEM_EDIT);
            } else if (mStaticSpectra.containsKey(ARG_ITEM_EDIT)) {
                mSpectrumNameToEdit = mStaticSpectra.get(ARG_ITEM_EDIT);
            }

            if (getArguments().containsKey(ARG_ITEM_EDIT)) {
                mSpectrumNameToEdit = getArguments().getString(ARG_ITEM_EDIT);
            } else if (mStaticSpectra.containsKey(ARG_ITEM_EDIT)) {
                mSpectrumNameToEdit = mStaticSpectra.get(ARG_ITEM_EDIT);
            }
        }
        if (mSpectrumNameToEdit != null) {
            mSpectrumAbsNameToEdit = SpectrumFiles.mPath + "/" + mSpectrumNameToEdit;
            mSpectrumToEdit = new SpectrumAsp(mSpectrumAbsNameToEdit);
            try {
                GraphView.GraphViewData[] realDataEdit;
                mSpectrumToEditLength = mSpectrumToEdit.readFile();
                mSpectrumToEditValues = mSpectrumToEdit.getValues();
                realDataEdit = new GraphView.GraphViewData[mSpectrumToEditLength];

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mSpectrumNameReference != null) {

            mSpectrumNameAbsReference = SpectrumFiles.mPath + "/" + mSpectrumNameReference;
            mSpectrumReference = new SpectrumAsp(mSpectrumAbsNameToEdit);
            try {
                GraphView.GraphViewData[] realDataEdit;
                mSpectrumToEditLength = mSpectrumToEdit.readFile();
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
        return inflater.inflate(R.layout.fragment_analyze, container, false);
    }
}
