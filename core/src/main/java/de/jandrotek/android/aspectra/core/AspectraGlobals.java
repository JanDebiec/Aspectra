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
package de.jandrotek.android.aspectra.core;

/**
 * Created by jan on 20.01.15.
 */
public class AspectraGlobals {

    public static final int eMessageCompleteLine = 0;
    public static final int eMessagePreviewSize = 1;
    public static int mPreviewWidthX;
    public static int mPreviewHeightY;
    public static boolean mSavePlotInFile;
    //public static String mExtensionAsp = "asp";
    public static final int eNoNormalize = -1;
    public static final int eNormalize1024 = 1024;
    public static final int eTypeUnkown     = 0;
    public static final int eTypeFloat32    = 1;
    public static final int eTypeInt32      = 2;
    public static final int eTypeInt16      = 3;

    public static final int ACT_ITEM_LIVE_VIEW   = 0;
    public static final int ACT_ITEM_VIEW_CONFIG = 1;
    public static final int ACT_ITEM_VIEW_PLOT   = 2;
    public static final int ACT_ITEM_ANALYZE     = 3;
    public static final int eMaxPlotCount = 5;


    public static final int eMaxSpectrumSize = 1024;

    public static final String ARG_ITEM_IDS = "item_ids";

    public static final int DEVICE_ORIENTATION_UNKNOWN = 0;
    public static final int DEVICE_ORIENTATION_PORTRAIT = 1;
    public static final int DEVICE_ORIENTATION_LANDSCAPE = 2;
    public static final int mPlotMaxValueY = 1024;

    public static final int CAMERA_ORIENTATION_UNKNOWN = -1;
    public static final int CAMERA_ORIENTATION_PORTRAIT_TOP =270;
    public static final int CAMERA_ORIENTATION_PORTRAIT_BOTTOM = 90;
    public static final int CAMERA_ORIENTATION_LANDSCAPE_LEFT = 180;
    public static final int CAMERA_ORIENTATION_LANDSCAPE_RIGHT = 0;

}
