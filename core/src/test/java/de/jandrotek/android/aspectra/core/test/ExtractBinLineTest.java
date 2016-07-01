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
            int indexY = y * ePictureWidth;
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
        configLanscape();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 64);
    }

    @Test
    public void extractLinePortrait() {
        int[] binnedLine;
        int lineSize = -1;
        configPortrait();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 48);
    }

    @Test
    public void extractLineLandscapeIndexZero() {
        int[] binnedLine;
        int lineSize = -1;
        int valueAtZero = -1;
        configLanscape();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 64);
        valueAtZero = binnedLine[0];
        assertEquals(valueAtZero, 256);
    }

    @Test
    public void extractLinePortraitIndexZero() {
        int[] binnedLine;
        int lineSize = -1;
        int valueAtZero = -1;
        configPortrait();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 48);
        valueAtZero = binnedLine[0];
        assertEquals(valueAtZero, 262);
    }

    @Test
    public void extractLineLandscapeIndexTen() {
        int[] binnedLine;
        int lineSize = -1;
        int valueAtZero = -1;
        configLanscape();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 64);
        valueAtZero = binnedLine[10];
        assertEquals(valueAtZero, 296);
    }

    @Test
    public void extractLinePortraitIndexTen() {
        int[] binnedLine;
        int lineSize = -1;
        int valueAtZero = -1;
        configPortrait();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 48);
        valueAtZero = binnedLine[10];
        assertEquals(valueAtZero, 262);
    }

    private void configLanscape() {
        oImageProcessing.setCameraDataMirrored(false);
        oImageProcessing.setStartPercentX(10);
        oImageProcessing.setStartPercentY(10);
        oImageProcessing.setEndPercentX(20);
        oImageProcessing.setScanAreaWidth(4);
        oImageProcessing.setPictureSize(ePictureWidth, ePictureHeight);
        oImageProcessing.setSpectrumOrientationLandscape(true);
        oImageProcessing.configureBinningArea();
    }

    private void configPortrait() {
        oImageProcessing.setCameraDataMirrored(false);
        oImageProcessing.setStartPercentX(10);
        oImageProcessing.setStartPercentY(10);
        oImageProcessing.setEndPercentX(20);
        oImageProcessing.setScanAreaWidth(4);
        oImageProcessing.setPictureSize(ePictureWidth, ePictureHeight);
        oImageProcessing.setSpectrumOrientationLandscape(false);
        oImageProcessing.configureBinningArea();
    }

    // mirror verrsions
    @Test
    public void extractLineLandscapeM() {
        int[] binnedLine;
        int lineSize = -1;
        configLanscapeM();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 64);
    }

    @Test
    public void extractLinePortraitM() {
        int[] binnedLine;
        int lineSize = -1;
        configPortraitM();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 48);
    }

    @Test
    public void extractLineLandscapeIndexZeroM() {
        int[] binnedLine;
        int lineSize = -1;
        int valueAtZero = -1;
        configLanscapeM();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 64);
        valueAtZero = binnedLine[0];
        assertEquals(valueAtZero, 256);
    }

    @Test
    public void extractLinePortraitIndexZeroM() {
        int[] binnedLine;
        int lineSize = -1;
        int valueAtZero = -1;
        configPortraitM();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 48);
        valueAtZero = binnedLine[0];
        assertEquals(valueAtZero, 262);
    }

    @Test
    public void extractLineLandscapeIndexTenM() {
        int[] binnedLine;
        int lineSize = -1;
        int valueAtZero = -1;
        configLanscapeM();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 64);
        valueAtZero = binnedLine[10];
        assertEquals(valueAtZero, 296);
    }

    @Test
    public void extractLinePortraitIndexTenM() {
        int[] binnedLine;
        int lineSize = -1;
        int valueAtZero = -1;
        configPortraitM();
        binnedLine = oImageProcessing.extractBinnedLine(pictureData);
        lineSize = binnedLine.length;
        assertEquals(lineSize, 48);
        valueAtZero = binnedLine[10];
        assertEquals(valueAtZero, 262);
    }

    private void configLanscapeM() {
        oImageProcessing.setCameraDataMirrored(true);
        oImageProcessing.setStartPercentX(10);
        oImageProcessing.setStartPercentY(10);
        oImageProcessing.setEndPercentX(20);
        oImageProcessing.setScanAreaWidth(4);
        oImageProcessing.setPictureSize(ePictureWidth, ePictureHeight);
        oImageProcessing.setSpectrumOrientationLandscape(true);
        oImageProcessing.configureBinningArea();
    }

    private void configPortraitM() {
        oImageProcessing.setCameraDataMirrored(true);
        oImageProcessing.setStartPercentX(10);
        oImageProcessing.setStartPercentY(10);
        oImageProcessing.setEndPercentX(20);
        oImageProcessing.setScanAreaWidth(4);
        oImageProcessing.setPictureSize(ePictureWidth, ePictureHeight);
        oImageProcessing.setSpectrumOrientationLandscape(false);
        oImageProcessing.configureBinningArea();
    }


}