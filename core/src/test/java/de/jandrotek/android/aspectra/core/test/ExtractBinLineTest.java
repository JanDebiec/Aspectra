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
        oImageProcessing.clearCameraConfigFlag();
        oImageProcessing.clearPercentConfigFlag();
        oImageProcessing.clearSpectrumConfigFlag();
    }

    @Test
    public void testInputData() {
        int size = pictureData.length;
        assertEquals(size, nPictureDataSize);
    }

    @Test
    public void confProcessingOrientation() {
        oImageProcessing.setSpectrumOrientationLandscape(true);
        boolean configFull = oImageProcessing.isConfigFull();
        assertEquals(configFull, false);
    }

    @Test
    public void confProcessingSize() {
        oImageProcessing.setPictureSize(ePictureWidth, ePictureHeight);
        boolean configFull = oImageProcessing.isConfigFull();
        assertEquals(configFull, false);
    }

    @Test
    public void confProcessingPercent() {
        oImageProcessing.setStartPercentX(10);
        oImageProcessing.setStartPercentY(10);
        oImageProcessing.setEndPercentX(20);
        oImageProcessing.setScanAreaWidth(16);
        boolean configFull = oImageProcessing.isConfigFull();
        assertEquals(configFull, false);
    }

    @Test
    public void confProcessingFull() {
        oImageProcessing.setStartPercentX(10);
        oImageProcessing.setStartPercentY(10);
        oImageProcessing.setEndPercentX(20);
        oImageProcessing.setScanAreaWidth(16);
        oImageProcessing.setPictureSize(ePictureWidth, ePictureHeight);
        oImageProcessing.setSpectrumOrientationLandscape(true);
        boolean configFull = oImageProcessing.isConfigFull();
        assertEquals(configFull, true);
    }

    @Test
    public void extractLineLandscape() {
        int[] binnedLine;
        int lineSize = -1;
        oImageProcessing.setStartPercentX(10);
        oImageProcessing.setStartPercentY(10);
        oImageProcessing.setEndPercentX(20);
        oImageProcessing.setScanAreaWidth(4);
        oImageProcessing.setPictureSize(ePictureWidth, ePictureHeight);
        oImageProcessing.setSpectrumOrientationLandscape(true);
//        boolean configFull = oImageProcessing.isConfigFull();
        oImageProcessing.configureBinningArea();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 64);
    }


}