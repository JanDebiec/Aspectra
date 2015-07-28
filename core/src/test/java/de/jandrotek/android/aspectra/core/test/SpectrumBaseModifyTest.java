package de.jandrotek.android.aspectra.core.test;

import junit.framework.TestCase;

import de.jandrotek.android.aspectra.core.SpectrumBase;

/**
 * Created by jan on 26.07.15.
 */
public class SpectrumBaseModifyTest extends TestCase {

    SpectrumBase spectrum;
    private final int nInputDataSizeMinusOne = 256;

    protected void setUp() throws Exception {
        super.setUp();
        int[] inputData;

        spectrum = new SpectrumBase();

        inputData = new int[nInputDataSizeMinusOne + 1];
        for(int i = 0; i < nInputDataSizeMinusOne /2; i++){
            inputData[i] = i;
            inputData[nInputDataSizeMinusOne - i] = i;
        }
        inputData[nInputDataSizeMinusOne/2] = nInputDataSizeMinusOne/2;

        spectrum.setValues(inputData);


    }
    protected void tearDown() throws Exception {
        // do termination here, run on every test method

        super.tearDown();
    }

//    public void testDummy(){
//        assertEquals(true, false);
//    }

    public void testSpectrumCreate(){
        int size = spectrum.getDataSize();
        assertEquals(nInputDataSizeMinusOne + 1, size);
    }

    public void testSpectrumCreateBeginIndex(){
        int index = spectrum.getStartIndex();
        assertEquals(0, index);
    }

    public void testMoveRight25(){
        spectrum.moveData(25);
        int index = spectrum.getStartIndex();
        assertEquals(25, index);

    }
    public void testMoveRightSize25(){
        spectrum.moveData(25);
        int size = spectrum.getDataSize();
        assertEquals(nInputDataSizeMinusOne + 1 + 25, size);

    }

    // to move left, first move right to create buffer left
    public void testMoveLeft25(){
        spectrum.moveData(50);
        spectrum.moveData(-25);
        int index = spectrum.getStartIndex();
        assertEquals(25, index);

    }
    public void testMoveLeftSize25(){
        spectrum.moveData(50);
        spectrum.moveData(-25);
        int size = spectrum.getDataSize();
        assertEquals(nInputDataSizeMinusOne + 1 + 25, size);

    }

    public void testCompres2Times(){
        spectrum.stretchData(nInputDataSizeMinusOne / 2, 0.5f);
        int size = spectrum.getDataSize();
        assertEquals(nInputDataSizeMinusOne /2, size);

    }

    public void testStretch2Times(){
        spectrum.stretchData(nInputDataSizeMinusOne / 2, 2.0f);
        int size = spectrum.getDataSize();
        assertEquals(nInputDataSizeMinusOne * 2, size);

    }
}