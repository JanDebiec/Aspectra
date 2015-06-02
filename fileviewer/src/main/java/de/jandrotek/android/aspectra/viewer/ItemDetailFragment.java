package de.jandrotek.android.aspectra.viewer;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

import de.jandrotek.android.aspectra.core.SpectrumChr;

//import com.jandrotek.aspectra.spectrumviewer.dummy.DummyContent;


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
    private GraphViewData[] mData;

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
        Log.d(TAG, "onCreate() called");

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
            int num = 2048;
            mData = new GraphViewData[num];
            for (int i=0; i<num; i++) {

                mData[i] = new GraphViewData(i, mFileIntValues[i]);
            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");

        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.name);
            // show data from loaded file in ChartView
        }
        mGraphView = new LineGraphView( getActivity(), "");
        // add data
        GraphViewSeriesStyle style = new GraphViewSeriesStyle();
        style.thickness = 1;
                GraphViewSeries dataSeries = new GraphViewSeries("", style,  mData);
        mGraphView.addSeries(dataSeries);
        mGraphView.getGraphViewStyle().setTextSize(20);
        mGraphView.getGraphViewStyle().setNumHorizontalLabels(5);
        mGraphView.getGraphViewStyle().setNumVerticalLabels(4);

        GraphViewSeriesStyle geSstyle = dataSeries.getStyle();
        // set view port, start=2, size=40
        mGraphView.setViewPort(0, 800);
        // optional - activate scaling / zooming
        mGraphView.setScrollable(true);
        mGraphView.setScalable(true);
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph);
        layout.addView(mGraphView);


        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


}
