package de.jandrotek.android.aspectra.libplotspectra;

import android.content.Context;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

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

    public static void updateZoomMode(GraphView _graphView, int _mode) {
        if(_mode == eModeZoom) {
            // optional - activate scaling / zooming
            _graphView.setScrollable(true);
            _graphView.setScalable(true);
        }
        else {
            _graphView.setScrollable(false);
            _graphView.setScalable(false);

        }
    }

    public static GraphView createLineGraphSingle(GraphView.GraphViewData[] _data, Context _context) {
        LineGraphView lineGraphView = null;
        lineGraphView = new LineGraphView(_context, "");
        // add data
        GraphViewSeries.GraphViewSeriesStyle style = new GraphViewSeries.GraphViewSeriesStyle();
        style.thickness = 1;
        GraphViewSeries dataSeries = new GraphViewSeries("", style,  _data);
        lineGraphView.addSeries(dataSeries);
        lineGraphView.getGraphViewStyle().setTextSize(eGraphTextSize);
        lineGraphView.getGraphViewStyle().setNumHorizontalLabels(eNumHorLabels);
        lineGraphView.getGraphViewStyle().setNumVerticalLabels(eNumVertLabels);

        GraphViewSeries.GraphViewSeriesStyle getStyle = dataSeries.getStyle();
        // set view port, start=2, size=40
        lineGraphView.setViewPort(ePlotPortStart, ePlotPortEnd);

        return lineGraphView;
    }

}
