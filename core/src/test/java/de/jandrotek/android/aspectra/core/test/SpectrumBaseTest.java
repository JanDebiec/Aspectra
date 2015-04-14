package de.jandrotek.android.aspectra.core.test;

import junit.framework.TestCase;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumAsp;

/**
 * Created by jan on 28.03.15.
 */
public class SpectrumBaseTest extends TestCase {
    SpectrumAsp mSpectrumAsp;

    protected void setUp() throws Exception {
        super.setUp();
        mSpectrumAsp = new SpectrumAsp();

        // do initialization here, run on every test method
    }



    protected void tearDown() throws Exception {
        // do termination here, run on every test method

        super.tearDown();
    }

     public void testEmptyNameAfterConstruct() {
         String name = mSpectrumAsp.getFileName();
         assertEquals(name, "");
     }

    public void testNameAfterDefine() {
        String nameSet = "abcdefg";
        mSpectrumAsp.setFileName(nameSet);
        String name = mSpectrumAsp.getFileName();
        assertEquals(name, nameSet);
    }

    public void testEmptyDataAfterConstruct() {
        int size = mSpectrumAsp.getDataSize();
        assertEquals(size, 0);
    }

    public void testEmptyTypeAfterConstruct() {
        int type = mSpectrumAsp.getDataType();
        assertEquals(type, AspectraGlobals.eTypeUnkown);
    }

    public void testDataAfterDefine() {
        int[] extData = new int[64];
        mSpectrumAsp.setData(extData,0);
        int size = mSpectrumAsp.getDataSize();
        assertEquals(size, 64);
    }

    public void testTypeAfterDefine() {
        int[] extData = new int[64];
        mSpectrumAsp.setData(extData,0);
        int type = mSpectrumAsp.getDataType();
        assertEquals(type, AspectraGlobals.eTypeInt32);
    }

}
