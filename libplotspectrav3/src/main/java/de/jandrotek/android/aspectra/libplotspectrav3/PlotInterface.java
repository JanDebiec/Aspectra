package de.jandrotek.android.aspectra.libplotspectrav3;

//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.GraphViewSeries;
//import com.jjoe64.graphview.LineGraphView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

/**
 * Created by jan on 16.06.15.
 */
public class PlotInterface {

    public static final int eModeEdit = 1;
    public static final int eModeZoom = 2;
    public static final int eModeExit = 3;

    private static final int eNumHorLabels = 5;
    private static final int eNumVertLabels = 4;
    private static final float eGraphTextSize = 20;
    private static final double ePlotPortStart = 0;
    private static final double ePlotPortEnd = 800;
//    private GraphView.GraphViewData[][] realData = null;
//    private int mDataLengthMax = 0;
//    private GraphView.GraphViewData[][] mData;
//    private GraphViewSeries[] mDataSeries;


//    // ver 3
//    public void showPlot(int[] data, int length){
//        if(mDataSeries != null) {
//            realPlotDataSize = length;
//            GraphView.GraphViewData[] graphdata = generateData(data, length);// here explode
//            mGraphView.setViewPort(0, realPlotDataSize);
//            mDataSeries[0].resetData(mData[0]);
////            mDataSeries[0].resetData(graphdata);
//        }
//    }
//
//    private void generateData(int[] data, int length) {
////        private GraphViewData[] generateData(int[] data, int length) {
////  variables as private in fragment, for speed up, GarbageCollection not needed
//        if(realData[0] == null){
//            realData[0] = new GraphView.GraphViewData[length];
//        }
//        for (int i=0; i<length; i++) {
//
//            realData[0][i] = new GraphView.GraphViewData(i, data[i]);
//        }
//        //TODO: check in plot act length, and add needed data only for that length
//
//        //for(int i = length; i < realPlotDataSize ; i++){
//        for(int i = length; i < mDataLengthMax ; i++){
//            realData[0][i] = new GraphView.GraphViewData(i, 0);
//        }
////        return realData[0];
//    }
//
//    //ver 3
//    private GraphView.GraphViewData[] generateDemoData(){
//        GraphView.GraphViewData[] demoData;
//        mPlotIntValues = new int[PLOT_DATA_SIZE];
//        for (int i = 0; i < PLOT_DATA_SIZE/2; i++)
//            mPlotIntValues[i] = i;
//        for (int i = PLOT_DATA_SIZE/2; i < PLOT_DATA_SIZE; i++)
//            mPlotIntValues[i] = PLOT_DATA_SIZE - i;
//
//        demoData = new GraphView.GraphViewData[PLOT_DATA_SIZE];
//        for (int i=0; i<PLOT_DATA_SIZE; i++) {
//
//            demoData[i] = new GraphView.GraphViewData(i, mPlotIntValues[i]);
//        }
//        return demoData;
//    }

//    public static void updateZoomMode(GraphView _graphView, int _mode) {
//        if(_mode == eModeZoom) {
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
//
//    public static GraphView createLineGraphSingle(GraphView.GraphViewData[] _data, Context _context) {
//        LineGraphView lineGraphView = null;
//        lineGraphView = new LineGraphView(_context, "");
//        // add data
//        GraphViewSeries.GraphViewSeriesStyle style = new GraphViewSeries.GraphViewSeriesStyle();
//        style.thickness = 1;
//        GraphViewSeries dataSeries = new GraphViewSeries("", style,  _data);
//        lineGraphView.addSeries(dataSeries);
//        lineGraphView.getGraphViewStyle().setTextSize(eGraphTextSize);
//        lineGraphView.getGraphViewStyle().setNumHorizontalLabels(eNumHorLabels);
//        lineGraphView.getGraphViewStyle().setNumVerticalLabels(eNumVertLabels);
//
//        GraphViewSeries.GraphViewSeriesStyle getStyle = dataSeries.getStyle();
//        // set view port, start=2, size=40
//        lineGraphView.setViewPort(ePlotPortStart, ePlotPortEnd);
//
//        return lineGraphView;
//    }

}
