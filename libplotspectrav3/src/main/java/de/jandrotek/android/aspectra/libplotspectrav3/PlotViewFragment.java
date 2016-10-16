/**
 * 14.03.2015 source in Github
 */
package de.jandrotek.android.aspectra.libplotspectrav3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;

/**
 * Modified version of fragment, here only View, Controller is moved to
 * PlotViewController
 */
public class PlotViewFragment extends Fragment
        implements View.OnCreateContextMenuListener {
    private static PlotViewFragment mFragment = null;
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";

    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = 0;//PLOT_DATA_SIZE;
    private int mDataLengthMax = 0;

    // TODO: Rename and change types of parameters
    private int mParam1;

    private GraphView mGraphView;
    private int[] mPlotIntValues;
    //ver 3
    public GraphViewSeries[] mDataSeries = null;
    private GraphViewSeries.GraphViewSeriesStyle[] mGraphStyle;
    private GraphViewData[][] realData = null;

    private ArrayList<String> mItems;
    private int[] mFileDataLength;
    private int mItemlistSize = 0;
    private int[] mColor;
    public boolean mInitialized = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 count of plots to draw.
     * @return A new instance of fragment PlotViewFragment.
     */
    public static PlotViewFragment newInstance(int param1) {
        if (mFragment == null) {
            mFragment = new PlotViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        mFragment.setArguments(args);
        }
        return mFragment;
    }

    public PlotViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        } else {
            mParam1 = 1;
        }
        mItemlistSize = mParam1;

        realData = new GraphViewData[mItemlistSize][AspectraGlobals.eMaxSpectrumSize];

        //TODO do we need instance here?
        if (mDataSeries == null) {
            mDataSeries = new GraphViewSeries[mItemlistSize];
        }
        mDataLengthMax = PLOT_DATA_SIZE;
        mColor = new int[3];
        mColor[0] = Color.rgb(255, 0, 0);
        mColor[1] = Color.rgb(0, 255, 0);
        mColor[2] = Color.rgb(0, 0, 255);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_plot_view, container, false);
// here i can create series too
        mGraphView = new LineGraphView(getActivity(), "");
        mGraphView.getGraphViewStyle().setTextSize(20);
        mGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
        mGraphView.getGraphViewStyle().setNumVerticalLabels(5);
        registerForContextMenu(mGraphView);
        FrameLayout mFrameLayout = (FrameLayout) rootView.findViewById(R.id.flPlotView);
        mFrameLayout.addView(mGraphView);

        return rootView;
    }

    public void createPlotSeries() {
        int actCount;
        if (mDataSeries != null) {
            actCount = mDataSeries.length;
        } else {
            actCount = 0;
        }
        for (int i = 0; i < mParam1; i++) {
            int colorIndex = i % 3;

            mDataSeries[i] = new GraphViewSeries(
                    "",
                    new GraphViewSeries.GraphViewSeriesStyle(mColor[colorIndex], 1),
                    realData[i]);
            mGraphView.addSeries(mDataSeries[i]);
        }
        realPlotDataSize = 0;
        mInitialized = true;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        mInitialized = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInitialized = false;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mInitialized = false;
    }

    //TODO will not working
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.plot_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean result = performActions(item);
        if (!result) {
            result = super.onContextItemSelected(item);
        }
        return result;
    }

    private boolean performActions(MenuItem item) {
        return true;
    }

    public void updateGraphView(int shownPlotLength) {
        realPlotDataSize = shownPlotLength;
        mGraphView.setViewPort(0, shownPlotLength);
        mGraphView.setManualYAxisBounds(AspectraGlobals.mPlotMaxValueY, 0);
    }

    public void updateGraphViewPort(int start, int end) {

        mGraphView.setViewPort(start, end);
    }

}


