package de.jandrotek.android.aspectra.core.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jandrotek.android.aspectra.core.ArrayFunctions;

import static org.junit.Assert.assertEquals;

/**
 * Created by jan on 30.03.15.
 */
public class ArrayModifyTest {
//public class ArrayModifyTest extends TestCase {

    private final int nInputDataSizeMinusOne = 256;
    int[] inputData;

    @Before
    public void setUp() throws Exception {
//        super.setUp();

        inputData = new int[nInputDataSizeMinusOne + 1];
        for(int i = 0; i < nInputDataSizeMinusOne /2; i++){
            inputData[i] = i;
            inputData[nInputDataSizeMinusOne - i] = i;
        }
        inputData[nInputDataSizeMinusOne/2] = nInputDataSizeMinusOne/2;
    }

    @After
    public void tearDown() throws Exception {
        // do termination here, run on every test method

//        super.tearDown();
    }


    protected int findMaxInArray(int[] array) {
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

    @Test
    public void testInputData(){
        int size = inputData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1);
    }

    @Test
    public void testFindMaxInInput(){
        int maxPos = findMaxInArray(inputData);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    @Test
    public void testMoveRight64(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 64);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 64);
        assertEquals(nInputDataSizeMinusOne/2 + 64, maxPos);
    }

    @Test
    public void testMoveLeft64(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 64);
        int[] newData2 = ArrayFunctions.moveArrayLeft(newData, 64);
        int maxPos = findMaxInArray(newData2);
        int size = newData2.length;
        assertEquals(size, nInputDataSizeMinusOne + 1);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    @Test
    public void testMoveRight3(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 3);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, nInputDataSizeMinusOne + 1 + 3);
        assertEquals(nInputDataSizeMinusOne/2 + 3, maxPos);
    }

    @Test
    public void testMoveLeft3(){
        int[] newData = ArrayFunctions.moveArrayRight(inputData, 3);
        int[] newData2 = ArrayFunctions.moveArrayLeft(newData, 3);
        int maxPos = findMaxInArray(newData2);
        int size = newData2.length;
        assertEquals(size, nInputDataSizeMinusOne + 1);
        assertEquals(nInputDataSizeMinusOne/2, maxPos);
    }

    @Test
    public  void testStretch2(){
        int[] newData = ArrayFunctions.stretchArray(inputData, 2.0f);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, (nInputDataSizeMinusOne + 1)*2);
        assertEquals(nInputDataSizeMinusOne, maxPos);

    }

    @Test
    public  void testSqueez2(){
        int[] newData = ArrayFunctions.stretchArray(inputData, 0.5f);
        int maxPos = findMaxInArray(newData);
        int size = newData.length;
        assertEquals(size, (nInputDataSizeMinusOne + 1)/2);
        assertEquals((nInputDataSizeMinusOne+1)/4, maxPos);

    }
}
