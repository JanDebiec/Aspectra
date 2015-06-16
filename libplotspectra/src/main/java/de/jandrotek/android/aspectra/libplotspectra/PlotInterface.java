package de.jandrotek.android.aspectra.libplotspectra;

import com.jjoe64.graphview.GraphView;

/**
 * Created by jan on 16.06.15.
 */
public class PlotInterface {

    private static final int eModeEdit = 1;
    private static final int eModeZoom = 2;
    private static final int eModeExit = 3;

    private void updateZoomMode(GraphView _graphView, int _mode) {
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

}
