package de.jandrotek.android.aspectra.core;

/**
 * Created by jan on 09.12.14.
 */
/** after checking after some days, i must realize, concept is not perfect:
 * steps to use class:
 * 1. define AreaOfInterest: as part of whole picture
 * 2. read AreaOfInterest in Extended line
 * 3. bin line into array of int
 * 4. the result is created after binning
 *
 * But concept is not ready yet: setting for POI should be static,
 * intern arrays> byte, int, could be static too, for better speed.
 * Then the memory cleaning need not be run
 *
 * CLASS WILL BE NOT USED
 *
 */
public class ExtendedLine {
    private static ExtendedLine mInstance = null;
    private int mStartX;
    private int mEndX;
    private int mStartY;
    private int mEndY;
    private int mSizeX;
    private int mSizeY;
    //private byte[] mInputArray; /// in YUV format

    private byte[] mLinesOfInterest = null; /// in YUV format
    private int[] mBinnedLine = null;

    private int mOrigSizeX;
    private int mOrigSizeY;
    private byte[] mInputArray;

    /**
     * define size of original camera picture
     * @param origSizeX
     * @param origSizeY
     */
    //TODO: change init of ExtendedLine, size will be later, at runTime defined
    private ExtendedLine(){
        //mOrigSizeX = origSizeX;
        //mOrigSizeY = origSizeY;
        mStartX = 0;
        mEndX = 0;
        mStartY = 0;
        mEndY = 0;
        mSizeX = 0;
        mSizeY = 0;
    }

    public static ExtendedLine getInstance() {
        if (mInstance == null) {

            mInstance = new ExtendedLine();
        }
        return mInstance;
    }

    public void resizeInstance(int sizeX, int sizeY) {
        mOrigSizeX = sizeX;
        mOrigSizeY = sizeY;
        mStartX = 0;
        mEndX = 0;
        mStartY = 0;
        mEndY = 0;
        mSizeX = 0;
        mSizeY = 0;
    }

    //TODO: add check of indexes, max size, .e.t.c.
    /**
     * defining Area of Interest from original picture
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     */
    public void setPOI(int startX, int endX, int startY, int endY){
        // check for startX
        mStartX = 0;
        mEndX = 0;
        mStartY = 0;
        mEndY = 0;
        mSizeX = 0;
        mSizeY = 0;

        if(startX >= 0 ){
            if(startX < mOrigSizeX){
                mStartX = startX;
            }
            else {
                mStartX = mOrigSizeX - 2;
            }
        } else {
            mStartX = 0;
        }
        // check for endX
        if(endX > mStartX){
            if(endX < mOrigSizeX){
                mEndX = endX;
            } else {
                mEndX = mOrigSizeX - 1;
            }
        }else {
            mEndX = mStartX + 1;
        }

        //mStartY = startY;
        // check for startY
        if(startY >= 0 ){
            if(startY < mOrigSizeY){
                mStartY = startY;
            }
            else {
                mStartY = mOrigSizeY - 2;
            }
        } else {
            mStartY = 0;
        }
        // check for endY
        if(endY > mStartY){
            if(endY < mOrigSizeY){
                mEndY = endY;
            } else {
                mEndY = mOrigSizeY - 1;
            }
        }else {
            mEndY = mStartY + 1;
        }

        mSizeX = mEndX - mStartX;
        mSizeY = mEndY - mStartY;
        if(mLinesOfInterest == null) {
            mLinesOfInterest = new byte[mSizeX * mSizeY];
            mBinnedLine = new int[mSizeX];
        } else {
            mLinesOfInterest = (byte[]) resizeArray(mLinesOfInterest, (mSizeX * mSizeY));
            mBinnedLine = (int[]) resizeArray(mBinnedLine, mSizeX);
        }
    }

    public int getSizeLinesOfInterrest(){
        return mLinesOfInterest.length;
    }

    public int getSizeBinnedLine(){
        return mBinnedLine.length;
    }


    /**
     * we get only y value, ignoring u,v
     * @param inputArray: original camera picture in YUV format
     * we consider only first part: Y
     */
    public void extractLinesOfInterest(byte[] inputArray){
        int x = mStartX;
        int y = mStartY;
        int loiX = 0;
        int loiY = 0;

        while(y < mEndY){
            while(x < mEndX){
                mLinesOfInterest[loiY * mSizeX + loiX] =
                        inputArray[y * mOrigSizeX + x];
                x++;
                loiX++;
            }
            loiX = 0;
            x = mStartX;
            y++;
            loiY++;

        }
    }

    public void binLines(){

        //first line
        for(int x = 0; x < mSizeX; x++){
            mBinnedLine[x] = mLinesOfInterest[x];
        }
        //next lines
        if(mSizeY > 0){
            for(int y = 1; y < mSizeY; y++){
                for(int x = 0; x < mSizeX; x++){
                    mBinnedLine[x] += mLinesOfInterest[(y) * mSizeX + ( x)];
                }
            }
        }
    }

    public int[] getBinnedLine() {
        return mBinnedLine;
    }

    void process(byte[] inputArray){
        //define POI:
        //setPOI()
        extractLinesOfInterest(inputArray);
        binLines();
    }

    public int[] processAndGetBinnedLine(byte[] inputArray) {
        //mInputArray = inputArray;
        extractLinesOfInterest(inputArray);
        binLines();
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
}
