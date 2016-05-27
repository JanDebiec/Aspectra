package de.jandrotek.android.aspectra.core;

/**
 * Created by jan on 21.12.14.
 */

//TODO: add possibility to change axis

/**
 * for changing axis in picture, new names conventions will be used:
 * X = axis for lenth of spectrum, can be width of camera picture by landscape
 * or height of camera picture by portrait
 * <p>
 * Y = axis for width of spectrum (count of lines to bin)
 * can be height of camera picture bu landscape orientation
 * or width of camera picture by portrait
 * <p>
 * Camera picture width = longer side of camera picture
 * height = smaller side of camera picture
 */
//import com.jandrotek.android.aspectra.lib.ExtendedLine;
//import com.jandrotek.android.aspectra.lib.SpectrumLine;


public class ImageProcessing {
    private int mAxisToBin; // axis "senkrecht" to spectrum
    private int mAxisToCalc; // axis parallel to spectrum

    public boolean isOrientationLandscape() {
        return mOrientationLandscape;
    }

    public void setOrientationLandscape(boolean orientationLandscape) {
        mOrientationLandscape = orientationLandscape;
    }

    private boolean mOrientationLandscape = true;

  //  private SpectrumLine mSpectrumLine;
    private int mSizeX;
    private int mSizeY;

    private int mStartPercentX = 5;
    private int mEndPercentX = 95;
    private int mStartPercentY = 49;
    private int mEndPercentY = 50;

    private int[] mBinnedLine = null;

    private int mIndexStartX;
    private int mIndexStartY;
    // camera shot dimensions
    private int mPictureSizeX; // before mPicureWidthX
    private int mPictureSizeY; // before mPictureSizeY

    public ImageProcessing() {
    }


    public void processImage(byte[] inputArray){
        int[] binnedLine;

        binnedLine = extractBinnedLine(inputArray);
//        mSpectrumLine = new com.jandrotek.android.aspectra.lib.SpectrumLine(binnedLine);
//        //TODO: implement switch, what should be calculated,
//        // if peakFind, or spectrum compare...
//        mSpectrumLine.searchInSteps(2);
//        List<Peak> listOfFoundPeaks = mSpectrumLine.getPeaks();

    }

    public int[] extractBinnedLine(byte[] inputArray) {
        if (mOrientationLandscape) {
            return extractBinnedLineLand(inputArray);
        } else {
            return extractBinnedLinePort(inputArray);
        }
    }

    //TODO: adapt to spectrum in height
    private int[] extractBinnedLinePort(byte[] inputArray)
            throws ArrayIndexOutOfBoundsException {
        int indexW, index;
        int indexY;

        try {

            //TODO: move the lines to configuration
            configureBinningArea();

            index = mIndexStartX + mPictureSizeX * mIndexStartY;

            //first line
            for (int x = 0; x < mSizeX; x++) {

                mBinnedLine[x] = inputArray[index] & 0xFF;
                index++;
            }

            //next lines
            indexY = mIndexStartY + 1;
            index = mIndexStartX + mPictureSizeX * indexY;
            for (int y = 1; y < mSizeY; y++) {
                for (int x = 0; x < mSizeX; x++) {
                    mBinnedLine[x] += inputArray[index] & 0xFF;
                    index++;
                }
                indexY++;
                index = mIndexStartX + mPictureSizeX * indexY;
            }

        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return mBinnedLine;
    }

    private int[] extractBinnedLineLand(byte[] inputArray)
    throws ArrayIndexOutOfBoundsException {
        int indexW, index;
        int indexY;

        try {

            //TODO: move the lines to configuration
            configureBinningArea();

            index = mIndexStartX + mPictureSizeX * mIndexStartY;

            //first line
            for (int x = 0; x < mSizeX; x++) {

                mBinnedLine[x] = inputArray[index] & 0xFF;
                index++;
            }

            //next lines
            indexY = mIndexStartY + 1;
            index = mIndexStartX + mPictureSizeX * indexY;
            for (int y = 1; y < mSizeY; y++) {
                for (int x = 0; x < mSizeX; x++) {
                    mBinnedLine[x] += inputArray[index] & 0xFF;
                    index++;
                }
                indexY++;
                index = mIndexStartX + mPictureSizeX * indexY;
            }

        }
        catch (ArrayIndexOutOfBoundsException e){

        }
        return mBinnedLine;
    }

    private void configureBinningArea() {
        if (mOrientationLandscape) {
            mSizeX = mPictureSizeX * (mEndPercentX - mStartPercentX) / 100;
            //mSizeY = mPictureSizeY * (mEndPercentY - mStartPercentY) / 100;
            mIndexStartX = mPictureSizeX * mStartPercentX / 100;
            mIndexStartY = mPictureSizeY * mStartPercentY / 100;
            if (mBinnedLine == null) {
                mBinnedLine = new int[mSizeX];
            } else {
                mBinnedLine = (int[]) resizeArray(mBinnedLine, mSizeX);
            }
        } else {

        }
    }

    /**
     * Reallocates an array with a new size, and copies the contents
     * of the old array to the new array.
     * @param oldArray  the old array, to be reallocated.
     * @param newSize   the new array size.
     * @return          A new array with the same contents.
     */
    private Object resizeArray (Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(
                elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0)
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        return newArray;
    }

    // getters, setters
    public void setEndPercentX(int endPercentX) {
        mEndPercentX = endPercentX;
    }

    public void setStartPercentX(int startPercentX) {
        mStartPercentX = startPercentX;
    }

    public void setStartPercentY(int startPercentW) {
        mStartPercentY = startPercentW;
    }

    public void setEndPercentY(int endPercentW) {
        mEndPercentY = endPercentW;
    }

    public void setPictureSizeX(int pictureSizeX) {
        mPictureSizeX = pictureSizeX;
    }

    public void setPictureSizeY(int pictureSizeY) {
        mPictureSizeY = pictureSizeY;
    }

    public void setScanAreaWidth(int scanAreaWidth) {
        mSizeY = scanAreaWidth;
    }
}
