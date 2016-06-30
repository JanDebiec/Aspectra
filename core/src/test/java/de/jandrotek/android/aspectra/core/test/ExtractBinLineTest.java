package de.jandrotek.android.aspectra.core.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jandrotek.android.aspectra.core.ImageProcessing;

import static org.junit.Assert.assertEquals;

/**
 * Created by jan on 30.06.16.
 */

public class ExtractBinLineTest {

    private final int ePictureWidth = 640;
    private final int ePictureHeight = 480;
    private final int nPictureDataSize = ePictureWidth * ePictureHeight;
    byte[] pictureData;
    private ImageProcessing oImageProcessing;

    @Before
    public void setUp() throws Exception {
        oImageProcessing = ImageProcessing.getInstance();
        pictureData = new byte[nPictureDataSize];
        for (int y = 0; y < ePictureHeight; y++) {
            int indexY = y + ePictureWidth;
            for (int x = 0; x < ePictureWidth; x++) {
                pictureData[indexY + x] = (byte) (x & 0xFF);
            }
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInputData() {
        int size = pictureData.length;
        assertEquals(size, nPictureDataSize);
    }

    @Test
    public void configureProcessing() {
        oImageProcessing.setSpectrumOrientationLandscape(true);
        boolean configFull = oImageProcessing.isConfigFull();
        assertEquals(configFull, false);
    }


}