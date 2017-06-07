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

import java.util.List;

/**
 * Created by jan on 09.12.14.
 */
public class SpectrumLine {
    private int mSize = 0;
    private int[] mLine = null;
    private List<de.jandrotek.android.aspectra.core.Peak> mPeakList;

    public SpectrumLine(int[] inputLine){
        mLine = inputLine;
        mSize = mLine.length;
    }

    public int getFoundPeaksCount(){
        return mPeakList.size();
    }


    private Peak searchPeak(SearchArea searchArea){
        Peak peak = new Peak();
        int start = 0;
        int end = 0;
        int tempValue;
        int tempPosition;
        int MaxValue;
        int MaxPosition;

        if (searchArea.getGo2Right()) {
            start = searchArea.getStart();
            end = searchArea.getEnd();
        } else {
            start = searchArea.getEnd();
            end = searchArea.getStart();
        }

        MaxValue = mLine[start];
        MaxPosition = start;
        for(int i = start; i < end; i++){
            tempValue = mLine[i];
            if(tempValue > MaxValue){
                MaxValue = tempValue;
                MaxPosition = i;
            }

        }
        peak.setPosition(MaxPosition);
        peak.setValue(MaxValue);

        return peak;
    }

    /**
     * searching peaks in steps:
     * step 0, going from start in left to end in right, search maximum, peak0
     * step 1; two runs from peak0 to left and from peak0 to right finding peaks 1 and 2
     * step 2: searching in 4 areas: the borders are: left-end, peak1, oeak0, peak2, right-end
     * @param steps
     */
    public void searchInSteps(int steps){
        int start;
        int stop;
        boolean go2RFlag;

        for (int i = 0; i < steps; i++){
            if(steps == 1){
                // search first max peak 0
                start = 0;
                stop = mSize;
                go2RFlag = true;
                SearchArea area0 = new SearchArea(start,stop, go2RFlag);
                Peak peak0 = searchPeak(area0);
                mPeakList.add(peak0);
            } else if (i == 2){
                // search peak 1,2
                // first search from peak[0] to left
                start = mPeakList.get(0).getPosition();
                stop = 0;
                go2RFlag = false;
                SearchArea area1 = new SearchArea(start,stop, go2RFlag);
                Peak peak1 = searchPeak(area1);
                mPeakList.add(peak1);
                // then search from peak[0] to right
                start = mPeakList.get(0).getPosition();
                stop = mSize;
                go2RFlag = true;
                SearchArea area2 = new SearchArea(start,stop, go2RFlag);
                Peak peak2 = searchPeak(area2);
                mPeakList.add(peak2);
            } else if (i == 3){
                // search  peaks 3,4,5,6
            }

        }
    }

    public List<Peak> getPeaks(){
        return mPeakList;
    }

    private class SearchArea {
        private int mStart;
        private int mEnd;
        private boolean mGo2Right;

        public SearchArea(int start, int end, boolean go2Right){
            mStart = start;
            mEnd = end;
            mGo2Right = go2Right;
        }

        public int getStart() {
            return mStart;
        }

        public int getEnd() {
            return mEnd;
        }

        public boolean getGo2Right() {
            return mGo2Right;
        }
    }
}
