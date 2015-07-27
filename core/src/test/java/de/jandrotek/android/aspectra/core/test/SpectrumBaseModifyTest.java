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

    public void testDummy(){
        assertEquals(true, false);
    }
}