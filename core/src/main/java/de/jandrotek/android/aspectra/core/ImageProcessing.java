package de.jandrotek.android.aspectra.core;

/**
 * Created by jan on 21.12.14.
 */

//import com.jandrotek.android.aspectra.lib.ExtendedLine;
//import com.jandrotek.android.aspectra.lib.SpectrumLine;


public class ImageProcessing {
  //  private SpectrumLine mSpectrumLine;
    private int mSizeX;
    private int mSizeY;

    private int mStartPercentX = 5;
    private int mEndPercentX = 95;
    private int mStartPercentY = 49;
    private int mEndPercentY = 50;

    private int[] mBinnedLine = null;


    // camera shot dimensions
    private int mPictureWidthX;
    private int mPictureHeightY;

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
        int indexStartX, indexW, index;
        int indexStartY, indexY;

        mSizeX = mPictureWidthX * (mEndPercentX - mStartPercentX) / 100;
        //mSizeY = mPictureHeightY * (mEndPercentY - mStartPercentY) / 100;
        indexStartX = mPictureWidthX * mStartPercentX / 100;
        indexStartY = mPictureHeightY * mStartPercentY / 100;
        if (mBinnedLine == null) {
            mBinnedLine = new int[mSizeX];
        } else {
            mBinnedLine = (int[]) resizeArray(mBinnedLine, mSizeX);
        }

        index = indexStartX + mPictureWidthX * indexStartY;

        //first line
        for (int x = 0; x < mSizeX; x++) {

            mBinnedLine[x] = inputArray[index] & 0xFF;
            index++;
        }

        //next lines
        indexY = indexStartY + 1;
        index = indexStartX + mPictureWidthX * indexY;
        for (int y = 1; y < mSizeY; y++) {
            for (int x = 0; x < mSizeX; x++) {
                mBinnedLine[x] += inputArray[index] & 0xFF;
                index++;
            }
            indexY++;
            index = indexStartX + mPictureWidthX * indexY;
        }

        return mBinnedLine;
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

    public void setPictureWidthX(int pictureWidthX) {
        mPictureWidthX = pictureWidthX;
    }

    public void setPictureHeightY(int pictureHeightY) {
        mPictureHeightY = pictureHeightY;
    }

    public void setScanAreaWidth(int scanAreaWidth) {
        mSizeY = scanAreaWidth;
    }
}
