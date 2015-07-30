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

    public void testMoveRight64(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 64);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 64);
        assertEquals(nInputDataSizeMinusOne/2 + 64, maxPos);
    }

    public void testMoveLeft64(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 64);
        int[] newData2 = ArrayFunctions.moveArrayLeft(newData, 64);
        int maxPos = findMaxInArray(newData2);
        int size = newData2.length;
        assertEquals(size, nInputDataSizeMinusOne + 1);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    public void testMoveRight3(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 3);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 3);
        assertEquals(nInputDataSizeMinusOne/2 + 3, maxPos);
    }

    public void testMoveLeft3(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 3);
        int[] newData2 = ArrayFunctions.moveArrayLeft(newData, 3);
        int maxPos = findMaxInArray(newData2);
        int size = newData2.length;
        assertEquals(size, nInputDataSizeMinusOne + 1);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    public  void testStretch2(){
        int[] newData = ArrayFunctions.stretchArray(inputData, 2.0f);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, (nInputDataSizeMinusOne + 1)*2);
        assertEquals(nInputDataSizeMinusOne, maxPos);

    }

    public  void testSqueez2(){
        int[] newData = ArrayFunctions.stretchArray(inputData, 0.5f);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, (nInputDataSizeMinusOne + 1)/2);
        assertEquals((nInputDataSizeMinusOne+1)/4, maxPos);

    }
}
