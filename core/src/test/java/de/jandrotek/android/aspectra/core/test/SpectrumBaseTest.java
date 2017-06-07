package de.jandrotek.android.aspectra.core.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.SpectrumAsp;

import static org.junit.Assert.assertEquals;

/**
 * Created by jan on 28.03.15.
 */
public class SpectrumBaseTest {
    //public class SpectrumBaseTest extends TestCase {
    SpectrumAsp mSpectrumAsp;

    @Before
    public void setUp() throws Exception {
//        super.setUp();
        mSpectrumAsp = new SpectrumAsp();

        // do initialization here, run on every test method
    }


    @After
    public void tearDown() throws Exception {
        // do termination here, run on every test method

//        super.tearDown();
    }

    @Test
     public void testEmptyNameAfterConstruct() {
         String name = mSpectrumAsp.getFileName();
         assertEquals(name, "");
     }

    @Test
    public void testNameAfterDefine() {
        String nameSet = "abcdefg";
        mSpectrumAsp.setFileName(nameSet);
        String name = mSpectrumAsp.getFileName();
        assertEquals(name, nameSet);
    }

    @Test
    public void testEmptyDataAfterConstruct() {
        int size = mSpectrumAsp.getDataSize();
        assertEquals(size, 0);
    }

    @Test
    public void testEmptyTypeAfterConstruct() {
        int type = mSpectrumAsp.getDataType();
        assertEquals(type, AspectraGlobals.eTypeUnkown);
    }

    @Test
    public void testDataAfterDefine() {
        int[] extData = new int[64];
        mSpectrumAsp.setData(extData,0);
        int size = mSpectrumAsp.getDataSize();
        assertEquals(size, 64);
    }

    @Test
    public void testTypeAfterDefine() {
        int[] extData = new int[64];
        mSpectrumAsp.setData(extData,0);
        int type = mSpectrumAsp.getDataType();
        assertEquals(type, AspectraGlobals.eTypeInt32);
    }

}
