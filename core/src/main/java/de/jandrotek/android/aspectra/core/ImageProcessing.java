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

public class ImageProcessing {
    private static ImageProcessing mProcessing = null;

    // helper for control configuration
    private static final int eCameraDimensionSet = 0x1;
    private static final int ePercensSet = 0x2;
    private static final int eSpectrumOrientationSet = 0x4;
    private static final int eCameraMirroredSet = 0x8;
    private static final int eNeededConfig = eCameraDimensionSet + ePercensSet + eSpectrumOrientationSet + eCameraMirroredSet;
    private int mConfigStatus = 0;

    public void clearCameraConfigFlag() {
        mConfigStatus &= ~eCameraDimensionSet;
    }

    public void clearSpectrumConfigFlag() {
        mConfigStatus &= ~eSpectrumOrientationSet;
    }

    public void clearPercentConfigFlag() {
        mConfigStatus &= ~ePercensSet;
    }
    private int mAxisToBin; // axis "senkrecht" to spectrum
    private int mAxisToCalc; // axis parallel to spectrum

    // some devices (f.i. N7 have data mirrored in both axis, x, y
    public void setCameraDataMirrored(boolean cameraDataMirrored) {
        mConfigStatus |= eCameraMirroredSet;
        mCameraDataMirrored = cameraDataMirrored;
    }

    private boolean mCameraDataMirrored = false;

    private boolean mSpectrumOrientationLandscape = true;

    private int mSizeX;
    private int mSizeY;
    private int mShiftToNormalize;

    private int mStartPercentX = 5;
    private int mEndPercentX = 95;
    private int mStartPercentY = 49;
    private int mEndPercentY = 50;

    private int[] mBinnedLine = null;
    private int[] mTempLine = null;
    private int[] mDemoLine = null;
    private static final int eDemoLineSize = 100;

    private int mIndexStartW;
    private int mIndexStartH;
    // camera shot dimensions
    private int mPictureSizeWidth; // before mPicureWidthX
    private int mPictureSizeHeight; // before mPictureSizeHeight

    public static ImageProcessing getInstance() {
        if (mProcessing == null) {
            mProcessing = new ImageProcessing();
        }
        return mProcessing;
    }


    private ImageProcessing() {
        // create DemoLine
        mDemoLine = new int[eDemoLineSize];
        for (int i = 0; i < eDemoLineSize; i++) {
            mDemoLine[i] = i;
        }
    }


//    public void processImage(byte[] inputArray){
//        int[] binnedLine;
//
//        binnedLine = extractBinnedLine(inputArray);
////        mSpectrumLine = new com.jandrotek.android.aspectra.lib.SpectrumLine(binnedLine);
////        //TODO: implement switch, what should be calculated,
////        // if peakFind, or spectrum compare...
////        mSpectrumLine.searchInSteps(2);
////        List<Peak> listOfFoundPeaks = mSpectrumLine.getPeaks();
//
//    }

    public boolean isConfigFull() {
        return (mConfigStatus == eNeededConfig);
    }

    public int[] extractBinnedLine(byte[] inputArray) {
        boolean configFull = isConfigFull();
        if (configFull) {
            if (mSpectrumOrientationLandscape) {
                if (mCameraDataMirrored)
                    return extractBinnedLineLandM(inputArray);
                else
                    return extractBinnedLineLand(inputArray);
            } else {
                if (mCameraDataMirrored)
                    return extractBinnedLinePortM(inputArray);
                else
                    return extractBinnedLinePort(inputArray);
            }
        } else {
            return mDemoLine;
        }
    }

    // version for N7 with flipped data in both axis
    private int[] extractBinnedLinePortM(byte[] inputArray)
            throws ArrayIndexOutOfBoundsException {
        int indexW;
        int indexH;
        int temp;

        try {

            // another method:
            // main loop: every index of spectrum binned line
            // internal loop:
            // we add (bin) pixels for every spectrum index

            for (int x = 0; x < mSizeX; x++) {
                mBinnedLine[x] = 0;
                indexH = mIndexStartH - x;
                indexW = mPictureSizeWidth * indexH + mIndexStartW;
                temp = 0;
                for (int y = 0; y < mSizeY; y++) {
                    temp += inputArray[indexW] & 0xFF;
                    indexW--;
                }
                if (mShiftToNormalize <= 0) {
                    mBinnedLine[x] = temp << -mShiftToNormalize;
                } else {
                    mBinnedLine[x] = temp >> mShiftToNormalize;
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return mBinnedLine;
    }

    // version for N7 with flipped data
    private int[] extractBinnedLineLandM(byte[] inputArray)
            throws ArrayIndexOutOfBoundsException {
        int indexW;
        int indexH = mIndexStartH;
        try {

            indexW = mIndexStartW + mPictureSizeWidth * indexH;

            //first line
            for (int x = 0; x < mSizeX; x++) {

                mTempLine[x] = inputArray[indexW] & 0xFF;
                indexW--;
            }

            //next lines
            for (int y = 1; y < mSizeY; y++) {
                indexH--;
                indexW = mIndexStartW + mPictureSizeWidth * indexH;
                for (int x = 0; x < mSizeX; x++) {
                    mTempLine[x] += inputArray[indexW] & 0xFF;
                    indexW--;
                }
            }
            for (int x = 0; x < mSizeX; x++) {
                if (mShiftToNormalize <= 0) {
                    mBinnedLine[x] = mTempLine[x] << -mShiftToNormalize;
                } else {
                    mBinnedLine[x] = mTempLine[x] >> mShiftToNormalize;
                }
            }

        }
        catch (ArrayIndexOutOfBoundsException e){

        }
        return mBinnedLine;
    }

    private int[] extractBinnedLinePort(byte[] inputArray)
            throws ArrayIndexOutOfBoundsException {
        int indexW;
        int indexH;
        int temp;

        try {

            // another method:
            // main loop: every index of spectrum binned line
            // internal loop:
            // we add (bin) pixels for every spectrum index

            for (int x = 0; x < mSizeX; x++) {
                mBinnedLine[x] = 0;
                temp = 0;
                indexH = mIndexStartH + x;
                indexW = mIndexStartW + mPictureSizeWidth * indexH;

                for (int y = 0; y < mSizeY; y++) {
                    temp += inputArray[indexW] & 0xFF;
                    indexW++;
                }
                // normalize
                if (mShiftToNormalize <= 0) {
                    mBinnedLine[x] = temp << -mShiftToNormalize;
                } else {
                    mBinnedLine[x] = temp >> mShiftToNormalize;
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return mBinnedLine;
    }

    private int[] extractBinnedLineLand(byte[] inputArray)
            throws ArrayIndexOutOfBoundsException {
        int indexW;
        int indexH = mIndexStartH;

        try {

            indexW = mIndexStartW + mPictureSizeWidth * indexH;

            //first line
            for (int x = 0; x < mSizeX; x++) {

                mTempLine[x] = inputArray[indexW] & 0xFF;
                indexW++;
            }

            //next lines
            for (int y = 1; y < mSizeY; y++) {
                indexH++;
                indexW = mIndexStartW + mPictureSizeWidth * indexH;
                for (int x = 0; x < mSizeX; x++) {
                    mTempLine[x] += inputArray[indexW] & 0xFF;
                    indexW++;
                }
            }
            // normalize
            for (int x = 0; x < mSizeX; x++) {
                if (mShiftToNormalize <= 0) {
                    mBinnedLine[x] = mTempLine[x] << -mShiftToNormalize;
                } else {
                    mBinnedLine[x] = mTempLine[x] >> mShiftToNormalize;
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return mBinnedLine;
    }

    public void setSpectrumOrientationLandscape(boolean _SpectrumOrientationLandscape) {
        mConfigStatus |= eSpectrumOrientationSet;
        mSpectrumOrientationLandscape = _SpectrumOrientationLandscape;
    }

    public void configureBinningArea() {
        boolean configFull = isConfigFull();
        if (configFull) {
            if (mCameraDataMirrored) { // N7
                if (mSpectrumOrientationLandscape) {
                    mSizeX = mPictureSizeWidth * (mEndPercentX - mStartPercentX) / 100;
                    mIndexStartW = mPictureSizeWidth * (100 - mStartPercentX) / 100;
                    mIndexStartH = mPictureSizeHeight * (100 - mStartPercentY) / 100;
                } else {
                    mSizeX = mPictureSizeHeight * (mEndPercentX - mStartPercentX) / 100;
                    mIndexStartW = mPictureSizeWidth * (100 - mStartPercentY) / 100;
                    mIndexStartH = mPictureSizeHeight * (100 - mStartPercentX) / 100;
                }
            } else { // data not mirrored; N5, GalNex
                if (mSpectrumOrientationLandscape) {
                    mSizeX = mPictureSizeWidth * (mEndPercentX - mStartPercentX) / 100;
                    mIndexStartW = mPictureSizeWidth * mStartPercentX / 100;
                    mIndexStartH = mPictureSizeHeight * mStartPercentY / 100;
                } else {
                    mSizeX = mPictureSizeHeight * (mEndPercentX - mStartPercentX) / 100;
                    mIndexStartW = mPictureSizeWidth * mStartPercentY / 100;
                    mIndexStartH = mPictureSizeHeight * mStartPercentX / 100;
                }
            }
            if (mBinnedLine == null) {
                mBinnedLine = new int[mSizeX];
            } else {
                mBinnedLine = (int[]) resizeArray(mBinnedLine, mSizeX);
            }
            if (mTempLine == null) {
                mTempLine = new int[mSizeX];
            } else {
                mTempLine = (int[]) resizeArray(mTempLine, mSizeX);
            }

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
        mConfigStatus |= ePercensSet;
    }

    public void setStartPercentX(int startPercentX) {
        mStartPercentX = startPercentX;
    }

    public void setStartPercentY(int startPercentW) {
        mStartPercentY = startPercentW;
    }

    public void setPictureSize(int pictureSizeWidth, int pictureSizeHeight) {
        mConfigStatus |= eCameraDimensionSet;
        mPictureSizeWidth = pictureSizeWidth;
        mPictureSizeHeight = pictureSizeHeight;
    }

    public void setScanAreaWidth(int scanAreaWidth) {
        mSizeY = scanAreaWidth;
        if (mSizeY == 1) {
            mShiftToNormalize = -2;
        } else if (mSizeY == 2) {
            mShiftToNormalize = -1;
        } else if (mSizeY == 4) {
            mShiftToNormalize = 0;
        } else if (mSizeY == 8) {
            mShiftToNormalize = 1;
        } else if (mSizeY == 16) {
            mShiftToNormalize = 2;
        } else if (mSizeY == 32) {
            mShiftToNormalize = 3;
        } else if (mSizeY == 64) {
            mShiftToNormalize = 4;
        } else if (mSizeY == 128) {
            mShiftToNormalize = 5;
        } else {
            mShiftToNormalize = 5;
        }
    }
}
