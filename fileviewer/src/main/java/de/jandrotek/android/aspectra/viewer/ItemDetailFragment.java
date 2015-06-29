package de.jandrotek.android.aspectra.viewer;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

// ver 3
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GraphView.GraphViewData;
//import com.jjoe64.graphview.GraphViewSeries;
//import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
//import com.jjoe64.graphview.LineGraphView;

// ver 4
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.util.Random;

import de.jandrotek.android.aspectra.core.SpectrumChr;

import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    private static final String TAG = "DetailItemsFrag";
   /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ListContent.SpectrumItem mItem;
    private String mFileName;
    private SpectrumChr mSpectrumFile;
    private int[] mFileIntValues;
    private GraphView mGraphView;
    private DataPoint[] mData;
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    private DataPoint[] realData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
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
            mSpectrumFile = new SpectrumChr(mFileName);
            try{
            	fileLength = mSpectrumFile.readValuesChr();
            	mFileIntValues = mSpectrumFile.getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //TODO: here is danger,what is bigger: mFileIntValues or 2048
            int num = 2048;
            mData = new DataPoint[num];
            for (int i=0; i<num; i++) {

                mData[i] = new DataPoint(i, mFileIntValues[i]);
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onViewCreated");
        }
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.name);
            // show data from loaded file in ChartView
        }
        mGraphView = new GraphView( getActivity());
        // add data
//        GraphViewSeriesStyle style = new GraphViewSeriesStyle();
//        style.thickness = 1;
        mSeries1  = new LineGraphSeries<DataPoint>(generateData());
//        GraphViewSeries dataSeries = new GraphViewSeries("", style,  mData);
        mGraphView.addSeries(mSeries1);
//        mGraphView.getGraphViewStyle().setTextSize(20);
//        mGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
//        mGraphView.getGraphViewStyle().setNumVerticalLabels(4);

//        GraphViewSeriesStyle geSstyle = mSeries1.getStyle();
        // set view port, start=2, size=40
//        mGraphView.setViewPort(0, 800);
        mGraphView.getViewport().setMinX(0);
        mGraphView.getViewport().setMaxX(800);
        // optional - activate scaling / zooming
//        mGraphView.setScrollable(true);
//        mGraphView.setScalable(true);
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph);
        layout.addView(mGraphView);


        return rootView;
    }

    private DataPoint[] generateData() {
        int count = 30;
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            double x = i;
            double f = mRand.nextDouble()*0.15+0.3;
            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
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


//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        // Send the event to the host activity
//        mCallback.onArticleSelected(position);
//    }
}
