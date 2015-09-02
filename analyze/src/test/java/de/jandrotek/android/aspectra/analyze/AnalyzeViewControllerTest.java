package de.jandrotek.android.aspectra.analyze;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jan on 02.09.15.
 */
public class AnalyzeViewControllerTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCalcNewPositions() throws Exception {
        float actual = 221;
        float expected = 221;
        assertEquals("Conversion from celsius to fahrenheit failed", expected,
                actual, 0.001);
    }
}