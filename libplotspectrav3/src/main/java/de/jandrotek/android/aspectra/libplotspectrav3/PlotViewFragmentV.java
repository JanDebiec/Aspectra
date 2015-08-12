/**
 * 14.03.2015 source in Github
 */
package de.jandrotek.android.aspectra.libplotspectrav3;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
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

// lib ver 3.

// temporary on rescue branch


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlotViewFragmentV.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlotViewFragmentV#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Modified version of fragment, here only View, Controller is moved to
 * PlotViewFragmentC
 */
public class PlotViewFragmentV extends Fragment
    implements         View.OnCreateContextMenuListener
{
    private static PlotViewFragmentV mFragment = null;
    //    public static final String ARG_ITEM_IDS = "item_ids";
        // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    private static int mMaxValueY = 4096;

    private static final int PLOT_DATA_SIZE = AspectraGlobals.eMaxSpectrumSize;
    private int realPlotDataSize = PLOT_DATA_SIZE;

    // TODO: Rename and change types of parameters
    private int mParam1;
//    private int mParam2;

    private GraphView mGraphView;
    private int[] mPlotIntValues;
    //ver 3
    GraphViewSeries mDataSeries;
    private GraphViewSeries.GraphViewSeriesStyle[] mGraphStyle;
    private GraphViewData[][] realData = null;

//    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mItems;
    private int[][] mFileIntValues;
    private int[] mFileDataLength;
    private int mItemlistSize = 0;
    private int mDataLengthMax = 0;
    private int[] mColor;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 count f plots to draw.
     * @return A new instance of fragment PlotViewFragmentV.
     * TODO: items as parameter is not a perfect idea, but what is better?
     */
    public static PlotViewFragmentV newInstance(int param1) {
       if(mFragment == null) {
           mFragment = new PlotViewFragmentV();
           Bundle args = new Bundle();
           args.putInt(ARG_PARAM1, param1);
           mFragment.setArguments(args);
       }
        return mFragment;
    }

    public PlotViewFragmentV() {
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
        mFileDataLength = new int[mItemlistSize];
        mDataLengthMax = PLOT_DATA_SIZE;
        mColor = new int[3];
        mColor[0] = Color.rgb(255, 0, 0);
        mColor[1] = Color.rgb(0, 255, 0);
        mColor[2] = Color.rgb(0, 0, 255);
    }

    private int findMaxDataLength(){
        int max = 0;
        int index = -1;
        int i = 0;
        for(i = 0; i < mItemlistSize; i++){
            if(mFileDataLength[i] > max){
                max = mFileDataLength[i];
                index = i;
            }
        }

        return max;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_plot_view, container, false);

        mGraphView = new LineGraphView(getActivity(), "");

        realData[0] = generateDemoData();
        mDataSeries = new GraphViewSeries(
                "",
                new GraphViewSeries.GraphViewSeriesStyle(mColor[0], 1),
                realData[0]);
        mGraphView.addSeries(mDataSeries);
        mGraphView.getGraphViewStyle().setTextSize(20);
        mGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
        mGraphView.getGraphViewStyle().setNumVerticalLabels(5);
        mGraphView.setViewPort(0, mDataLengthMax);
        registerForContextMenu(mGraphView);


//        //TODO: optional - activate scaling / zooming
//        // both modi will be handled with Touch-view helper class, not only in viewer
//        // in liveView is disabled, first in AnalyzeActivity
//        if (mParam1 == AspectraGlobals.ACT_ITEM_ANALYZE) {
//            //mGraphView.setScrollable(true);
//            //mGraphView.setScalable(true);
//        }
//        else if(mParam1 == AspectraGlobals.ACT_ITEM_LIVE_VIEW) {
//
//        }
//        else if(mParam1 == AspectraGlobals.ACT_ITEM_VIEW_PLOT) {
//
//        }
        FrameLayout mFrameLayout = (FrameLayout)rootView.findViewById(R.id.flPlotView);
        mFrameLayout.addView(mGraphView);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private boolean performActions(MenuItem item){
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    public void showPlot(int index, int[] data){
        int length = data.length;
        if(mDataSeries != null) {
            realPlotDataSize = length;
            generateData(index, data, length);
            mGraphView.setViewPort(0, realPlotDataSize);
            mGraphView.setManualYAxisBounds(mMaxValueY, 0);
            mDataSeries.resetData(realData[index]);
        }
   }

    private void generateData(int index, int[] data, int length) {
        int realLength;
        if(length > AspectraGlobals.eMaxSpectrumSize){
            realLength = AspectraGlobals.eMaxSpectrumSize;
        } else {
            realLength = length;
        }
        if(realData[index] == null){
            realData[index] = new GraphViewData[length];
        }
        for (int i=0; i<realLength; i++) {

            realData[index][i] = new GraphViewData(i, data[i]);
        }
        //TODO: check in plot act length, and add needed data only for that length
        if(mParam1 > 1) {
            mDataLengthMax = findMaxDataLength();
        }
        else {
            mDataLengthMax = realLength;
        }


        for(int i = realLength; i < mDataLengthMax ; i++){
            realData[index][i] = new GraphViewData(i, 0);
        }
    }

    private GraphViewData[] generateDemoData(){
        GraphViewData[] demoData;
        mPlotIntValues = new int[PLOT_DATA_SIZE];
        for (int i = 0; i < PLOT_DATA_SIZE/2; i++)
            mPlotIntValues[i] = i;
        for (int i = PLOT_DATA_SIZE/2; i < PLOT_DATA_SIZE; i++)
            mPlotIntValues[i] = PLOT_DATA_SIZE - i;

        demoData = new GraphViewData[PLOT_DATA_SIZE];
        for (int i=0; i<PLOT_DATA_SIZE; i++) {

            demoData[i] = new GraphViewData(i, mPlotIntValues[i]);
        }
        return demoData;
    }
}


