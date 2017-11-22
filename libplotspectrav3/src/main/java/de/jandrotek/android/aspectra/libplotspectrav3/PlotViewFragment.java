/**
 * This file is part of Aspectra.
 *
 * Aspectra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aspectra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aspectra.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 *
 * Copyright Jan Debiec
 */
package de.jandrotek.android.aspectra.libplotspectrav3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import de.jandrotek.android.aspectra.core.AspectraGlobals;

/**
 * Modified version of fragment, here only View,
 * Presenter is in PlotViewPresenter
 * Controller is moved to app-specific PlotViewController
 */
public class PlotViewFragment extends Fragment
        implements View.OnCreateContextMenuListener {
    private static PlotViewFragment mFragment = null;
    private static final String TAG = "PlotViewFragment";

    private static final int eClassCreated = 0x01;
    private static final int eViewInitialized = 0x02;
    private static final int eContentInitialized = 0x04;
    private static final int eFUllInitialized =
            eClassCreated
            | eViewInitialized
            | eContentInitialized;
    private static final int eReady4Plot =
            eClassCreated
            | eViewInitialized;
    private static int mInitialization = 0;

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";

    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = 0;//PLOT_DATA_SIZE;
    private int mDataLengthMax = 0;

    // TODO: Rename and change types of parameters
    private int mParam1;

    private View mRootView = null;
    private static GraphView mGraphView = null;
    private FrameLayout mFrameLayout = null;
    //ver 3
    private GraphViewSeries[] mDataSeries = null;
    private GraphViewSeries.GraphViewSeriesStyle[] mGraphStyle;

    public void setItemlistSize(int mItemlistSize) {
        this.mItemlistSize = mItemlistSize;
    }

    private int mItemlistSize = 0;
    private int[] mColor;

    public boolean isFullInitialized() {
        return (mInitialization == eFUllInitialized);
    }

    public boolean isReady4Plot() {
        return (mInitialization == eReady4Plot);
    }

    private GraphViewSeries singleSerie = null;
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

        // empty array created
        if(mDataSeries == null) {
            mDataSeries = new GraphViewSeries[AspectraGlobals.eMaxPlotCount];
        }
        mDataLengthMax = PLOT_DATA_SIZE;
        mColor = new int[3];
        mColor[0] = Color.rgb(255, 0, 0);
        mColor[1] = Color.rgb(0, 255, 0);
        mColor[2] = Color.rgb(0, 0, 255);
        mInitialization |= eClassCreated;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_plot_view, container, false);
        if (mGraphView == null) {
            mGraphView = new LineGraphView(getActivity(), "");

            mGraphView.getGraphViewStyle().setTextSize(20);
            mGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
            mGraphView.getGraphViewStyle().setNumVerticalLabels(5);
        }
        registerForContextMenu(mGraphView);
        if(mFrameLayout == null) {
            mFrameLayout = (FrameLayout) mRootView.findViewById(R.id.flPlotView);
            mFrameLayout.addView(mGraphView);
        } else {
            mFrameLayout = (FrameLayout) mRootView.findViewById(R.id.flPlotView);
            mFrameLayout.removeView(mGraphView);
            mFrameLayout.addView(mGraphView);
        }
        mInitialization |= eViewInitialized;
        return mRootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInitialization = 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mGraphView != null) {
            ViewGroup parentViewGroup = (ViewGroup) mGraphView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
        mInitialization = eClassCreated;
    }
    @Override
    public void onStop() {
        super.onStop();
        clearPlotSeries();
        mInitialization = eClassCreated;
        mInitialization |= eViewInitialized;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

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

    public void updateGraphViewLength(int shownPlotLength) {
        realPlotDataSize = shownPlotLength;
        mGraphView.setViewPort(0, shownPlotLength);
        mGraphView.setManualYAxisBounds(AspectraGlobals.mPlotMaxValueY, 0);
    }

    public void updateGraphViewPort(int start, int end) {

        mGraphView.setViewPort(start, end);
    }

    public void createPlotSerie( int index, GraphView.GraphViewData[] realData) {
        int colorIndex = index % 3;

        try {
            mDataSeries[index] = new GraphViewSeries(
                    "",
                    new GraphViewSeries.GraphViewSeriesStyle(mColor[colorIndex], 1),

                    realData);
            mInitialization |= eContentInitialized;
            mGraphView.addSeries(mDataSeries[index]);
        } catch (NullPointerException e){
            Log.e(TAG, "NullPointerException by createPlotSerie");
        }

    }

    public void updateSinglePlot(int index, GraphView.GraphViewData[] realData){
        if(mDataSeries[index] == null){
            createPlotSerie(index, realData);
        } else {
            mDataSeries[index].resetData(realData);// in live view, here we get null exception
        }
    }

    public void clearPlotSeries(){
        mGraphView.removeAllSeries();
    }
}


