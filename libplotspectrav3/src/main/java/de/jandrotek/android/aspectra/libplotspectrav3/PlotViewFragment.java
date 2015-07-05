/**
 * 14.03.2015 source in Github
 */
package de.jandrotek.android.aspectra.libplotspectrav3;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v7.app.ActionBarActivity;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumChr;
import de.jandrotek.android.aspectra.libspectrafiles.ListContent;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;

//import android.support.v7.app.AppCompatActivity;
// lib ver 3.
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlotViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlotViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlotViewFragment extends Fragment
    implements         View.OnCreateContextMenuListener
{
    public static final String ARG_ITEM_IDS = "item_ids";
        // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PLOT_DATA_SIZE = 1920;
    private int realPlotDataSize = PLOT_DATA_SIZE;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;

    private GraphView mGraphView;
    private int[] mPlotIntValues;
    //ver 3
    private GraphViewData[] mData;
    private GraphViewSeries mDataSeries;
    private GraphViewSeries.GraphViewSeriesStyle mGraphStyle;
    private GraphViewData[] realData;
    // ver 4
//    private LineGraphSeries<DataPointJan> mSeries1;
//    private LineGraphSeries<DataPointJan> mSeries2;
//    private DataPointJan[] realData;


    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mItems;
    private String mFileName;
    private SpectrumChr mSpectrumFile;
    private int[] mFileIntValues;

//    private boolean mFlagSavinggStarted = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Type of calling activity.
     * @param param2 count of plots to draw.
     * @return A new instance of fragment PlotViewFragment.
     */
    // TODO: Rename and change types and number of parameters
        public static PlotViewFragment newInstance(int param1, int param2) {
        PlotViewFragment fragment = new PlotViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlotViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int fileLength;
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
        //ver 3
        //realData = new GraphViewData[realPlotDataSize];
        realData = new GraphViewData[PLOT_DATA_SIZE];
        mData = generateDemoData();
        // ver 4
//        realData = new DataPointJan[PLOT_DATA_SIZE];
        if (getArguments().containsKey(ARG_ITEM_IDS)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItems = getArguments().getStringArrayList(ARG_ITEM_IDS);
//            mItems = ListContent.ITEM_MAP.get(getArguments().getStringArrayList(ARG_ITEM_IDS));


            // load file specified in mItem.content
            String fileName = mItems.get(0);
            mFileName = SpectrumFiles.mPath +"/" + fileName;
            mSpectrumFile = new SpectrumChr(mFileName);
            try{
                fileLength = mSpectrumFile.readValuesChr();
                mFileIntValues = mSpectrumFile.getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO: here is danger,what is bigger: mFileIntValues or 2048
            int num = 2048;
//            mData = new DataPointJan[num];
//            for (int i=0; i<num; i++) {
//
//                mData[i] = new DataPointJan(i, mFileIntValues[i]);
//            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_plot_view, container, false);

        mGraphView = new LineGraphView(getActivity(), "");
        // add data
        mGraphStyle = new GraphViewSeries.GraphViewSeriesStyle();
        mGraphStyle.thickness = 1;
        mData = generateDemoData();
        mDataSeries = new GraphViewSeries("", mGraphStyle, mData);
        mGraphView.addSeries(mDataSeries);
        mGraphView.getGraphViewStyle().setTextSize(20);
        mGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
        mGraphView.getGraphViewStyle().setNumVerticalLabels(4);

        GraphViewSeries.GraphViewSeriesStyle geSstyle = mDataSeries.getStyle();
        mGraphView.setViewPort(0, realPlotDataSize);
        registerForContextMenu(mGraphView);

        mGraphView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN) {

                if (!AspectraGlobals.mSavePlotInFile) {
                    AspectraGlobals.mSavePlotInFile = true;
                }
            }
            return true; //processed
            }

        });

        // alone is working, but with onClickListener not
        mGraphView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        //TODO: optional - activate scaling / zooming
        // both modi will be handled with Touch-view helper class, not only in viewer
        // in liveView is disabled, first in AnalyzeActivity
        if (mParam1 == AspectraGlobals.ACT_ITEM_ANALYZE) {
            //mGraphView.setScrollable(true);
            //mGraphView.setScalable(true);
        }
        else if(mParam1 == AspectraGlobals.ACT_ITEM_LIVE_VIEW) {

        }
        else if(mParam1 == AspectraGlobals.ACT_ITEM_VIEW_PLOT) {

        }
        FrameLayout mFrameLayout = (FrameLayout)rootView.findViewById(R.id.flPlotView);
        mFrameLayout.addView(mGraphView);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
// ver 4
//    public void showPlotOne(int[] data, int length){
//        if(mSeries1 != null) {
//            realPlotDataSize = length;
//            DataPointJan[] graphdata = generateData(data, length);// here explode
//
//            mGraphView.getViewport().setXAxisBoundsManual(true);
//            mGraphView.getViewport().setMinX(0);
//            mGraphView.getViewport().setMaxX(realPlotDataSize);
//            mSeries1.resetData(graphdata);
//        }
//    }

    // ver 3
    public void showPlotOne(int[] data, int length){
        if(mDataSeries != null) {
            realPlotDataSize = length;
            GraphViewData[] graphdata = generateData(data, length);// here explode
            mGraphView.setViewPort(0, realPlotDataSize);
            mDataSeries.resetData(graphdata);
        }
    }

    //ver 4
//    private DataPointJan[] generateData(int[] data, int length) {
//        DataPointJan baseDataPoint = new DataPointJan();
//        // TODO:  variables as private in fragment, for speed up, GarbageCollection not needed
//        for (int i=0; i<length; i++) {
//
//            baseDataPoint.setX(i);
//            baseDataPoint.setY(data[i]);
////            DataPointJan v = new DataPointJan(i, data[i]);
//            realData[i] = baseDataPoint;
//        }
//        //TODO: check in plot act length, and add needed data only for that length
//
//        //for(int i = length; i < realPlotDataSize ; i++){
//        for(int i = length; i < PLOT_DATA_SIZE ; i++){
////            DataPointJan v = new DataPointJan(i, 0);
//            baseDataPoint.setX(i);
//            baseDataPoint.setY(0);
//            realData[i] = baseDataPoint;
//        }
//        return realData;
//    }

// ver 3
        private GraphViewData[] generateData(int[] data, int length) {
//  variables as private in fragment, for speed up, GarbageCollection not needed
            for (int i=0; i<length; i++) {

                realData[i] = new GraphViewData(i, data[i]);
            }
            //TODO: check in plot act length, and add needed data only for that length

            //for(int i = length; i < realPlotDataSize ; i++){
                for(int i = length; i < PLOT_DATA_SIZE ; i++){
                realData[i] = new GraphViewData(i, 0);
            }
        return realData;
    }


    // ver 4
//    private DataPointJan[] generateDemoData(){
//        DataPointJan[] demoData;
//        mPlotIntValues = new int[PLOT_DATA_SIZE];
//        for (int i = 0; i < PLOT_DATA_SIZE/2; i++)
//            mPlotIntValues[i] = i;
//        for (int i = PLOT_DATA_SIZE/2; i < PLOT_DATA_SIZE; i++)
//            mPlotIntValues[i] = PLOT_DATA_SIZE - i;
//
//        demoData = new DataPointJan[PLOT_DATA_SIZE];
//        for (int i=0; i<PLOT_DATA_SIZE; i++) {
//
//            demoData[i] = new DataPointJan(i, mPlotIntValues[i]);
//        }
//        return demoData;
//    }

    //ver 3
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


