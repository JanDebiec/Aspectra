package de.jandrotek.android.aspectra.core.test;

import junit.framework.TestCase;

import de.jandrotek.android.aspectra.core.ArrayFunctions;

/**
 * Created by jan on 30.03.15.
 */
public class ArrayModifyTest extends TestCase {

    private final int nInputDataSizeMinusOne = 256;
    int[] inputData;

    protected void setUp() throws Exception {
        super.setUp();

        inputData = new int[nInputDataSizeMinusOne + 1];
        for(int i = 0; i < nInputDataSizeMinusOne /2; i++){
            inputData[i] = i;
            inputData[nInputDataSizeMinusOne - i] = i;
        }
        inputData[nInputDataSizeMinusOne/2] = nInputDataSizeMinusOne/2;
    }

    protected void tearDown() throws Exception {
        // do termination here, run on every test method

        super.tearDown();
    }

    private int findMaxInArray(int[] array){
        int pos = 0;
        int max = 0;
        int val;
        for(int i = 0; i < array.length; i++){
            val = array[i];
            if(val > max){
                max = val;
                pos = i;
            }
        }
        return pos;
    }

    public void testInputData(){
        int size = inputData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1);
    }

    public void testFindMaxInInput(){
        int maxPos = findMaxInArray(inputData);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    public void testAppendFront128(){
        int[] newData = ArrayFunctions.appendArrayFront(inputData, 128);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 128);
        assertEquals(nInputDataSizeMinusOne/2 + 128, maxPos);
    }

    public void testAppendBack128(){
        int[] newData = ArrayFunctions.appendArrayBack(inputData, 128);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 128);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    public void testAppendFront256(){
        int[] newData = ArrayFunctions.appendArrayFront(inputData, 256);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 256);
        assertEquals(nInputDataSizeMinusOne/2 + 256, maxPos);
    }

    public void testAppendBack256(){
        int[] newData = ArrayFunctions.appendArrayBack(inputData, 256);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 256);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    public void testAppendFront7(){
        int[] newData = ArrayFunctions.appendArrayFront(inputData, 7);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 7);
        assertEquals(nInputDataSizeMinusOne/2 + 7, maxPos);
    }

    public void testAppendBack7(){
        int[] newData = ArrayFunctions.appendArrayBack(inputData, 7);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 7);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    public void testMoveRight64(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 64);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 64);
        assertEquals(nInputDataSizeMinusOne/2 + 64, maxPos);
    }

    public void testMoveLeft64(){
        int[] newData = ArrayFunctions.moveArrayLeft(inputData, 64);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1);
        assertEquals(nInputDataSizeMinusOne/2 - 64, maxPos);
    }

    public void testMoveRight3(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 3);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 3);
        assertEquals(nInputDataSizeMinusOne/2 + 3, maxPos);
    }

    public void testMoveLeft3(){
        int[] newData = ArrayFunctions.moveArrayLeft(inputData, 3);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 3);
        assertEquals(nInputDataSizeMinusOne/2 - 3, maxPos);
    }

    /**
     * test:
     * input data size 257, maximum in 128
     * stretch: um fixedPoint 128 factor 2.0
     * new maximum should be placed at 128
     */
        public void testStretch1(){
            int[] newData = ArrayFunctions.stretchArray(inputData, nInputDataSizeMinusOne / 2, (float) 2.0);
            int maxPos = findMaxInArray(newData);
            int size = newData.length;
            assertEquals(size, nInputDataSizeMinusOne + 1);
            assertEquals(nInputDataSizeMinusOne/2, maxPos);
        }

    /**
     * test:
     * input data size 513, maximum in 128
     * stretch: um fixedPoint 0 factor 2.0
     * new maximum should be placed at 64
     */
    public void testStretch2(){
        int[] newDataA = ArrayFunctions.appendArrayBack(inputData, nInputDataSizeMinusOne);
        int[] newData = ArrayFunctions.stretchArray(newDataA, 0, (float) 2.0);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne * 2 + 1);
        assertEquals(nInputDataSizeMinusOne / 4, maxPos);
    }

    /**
     * test:
     * input data size 513, maximum in 128 + 257
     * stretch: um fixedPoint 512 factor 2.0
     * new maximum should be placed at 512 - 64
     */
    public void testStretch3(){
        int[] newDataA = ArrayFunctions.appendArrayFront(inputData, nInputDataSizeMinusOne);
        int[] newData = ArrayFunctions.stretchArray(newDataA, 512, (float) 2.0);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne * 2 + 1);
        assertEquals(nInputDataSizeMinusOne * 2 - 64, maxPos);
    }



}
