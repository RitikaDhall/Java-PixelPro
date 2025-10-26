package model;

import org.junit.Before;
import org.junit.Test;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import controller.ImageUtil;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the BetterColorImageProcessor model.
 */
public class BetterColorImageProcessorTest {

  private ImageUtil util = new ImageUtil();
  BetterColorImageProcessor processor;
  Image sampleImage = new ColorImage(3, 3);

  @Before
  public void setUp() {
    processor = new BetterColorImageProcessor();
    sampleImage = new ColorImage(3, 3);
  }

  @Test
  public void testHistogramUsingComputeHistograms() {

    ColorPixel pixel1 = new ColorPixel(100, 150, 200);
    ColorPixel pixel2 = new ColorPixel(200, 150, 100);
    sampleImage.setPixel(0, 0, pixel1);
    sampleImage.setPixel(0, 1, pixel2);

    HashMap<Color, int[]> histograms = processor.computeHistogram(sampleImage);

    int[] redHistogram = histograms.get(Color.RED);
    int[] greenHistogram = histograms.get(Color.GREEN);
    int[] blueHistogram = histograms.get(Color.BLUE);

    // Verify histogram values
    assertEquals(1, redHistogram[100]);
    assertEquals(1, redHistogram[200]);
    assertEquals(2, greenHistogram[150]);
  }

  @Test
  public void testAdjustLevels() {
    ColorPixel pixel = new ColorPixel(50, 100, 150);
    sampleImage.setPixel(1, 1, pixel);

    int black = 20;
    int mid = 110;
    int white = 240;

    Image adjustedImage = processor.adjustLevels(sampleImage, black, mid, white);

    ColorPixel adjustedPixel = (ColorPixel) adjustedImage.getPixel(1, 1);

    double a = 0.006072261072261072;
    double b = -5.056060606060606;
    double c = 98.6923076923077;

    double expectedAdjustedRed = AbstractPixel.validateColorValue(
            a * Math.pow(pixel.getRed(), 2) + b * pixel.getRed() + c);
    double expectedAdjustedGreen = AbstractPixel.validateColorValue(
            a * Math.pow(pixel.getGreen(), 2) + b * pixel.getGreen() + c);
    double expectedAdjustedBlue = AbstractPixel.validateColorValue(
            a * Math.pow(pixel.getBlue(), 2) + b * pixel.getBlue() + c);

    assertEquals(expectedAdjustedRed, adjustedPixel.getRed(), 0.001);
    assertEquals(expectedAdjustedGreen, adjustedPixel.getGreen(), 0.001);
    assertEquals(expectedAdjustedBlue, adjustedPixel.getBlue(), 0.001);
  }

  @Test
  public void testColorCorrection() {
    // Set up an image with a known color distribution
    ColorPixel pixel = new ColorPixel(80, 100, 120);
    for (int i = 0; i < sampleImage.getHeight(); i++) {
      for (int j = 0; j < sampleImage.getWidth(); j++) {
        sampleImage.setPixel(i, j, pixel);
      }
    }

    // Apply color correction
    Image correctedImage = processor.colorCorrect(sampleImage);

    // Calculate the expected values based on the color correction logic
    int[] redHistogram = new int[256];
    int[] greenHistogram = new int[256];
    int[] blueHistogram = new int[256];
    redHistogram[80] = 1;
    greenHistogram[100] = 1;
    blueHistogram[120] = 1;

    int redPeak = 80; // As per your findMeaningfulPeak logic
    int greenPeak = 100; // As per your findMeaningfulPeak logic
    int bluePeak = 120; // As per your findMeaningfulPeak logic

    int averagePeak = (redPeak + greenPeak + bluePeak) / 3;
    int redOffset = averagePeak - redPeak;
    int greenOffset = averagePeak - greenPeak;
    int blueOffset = averagePeak - bluePeak;

    ColorPixel correctedPixel = (ColorPixel) correctedImage.getPixel(1, 1);
    double expectedRed = AbstractPixel.validateColorValue(pixel.getRed() + redOffset);
    double expectedGreen = AbstractPixel.validateColorValue(pixel.getGreen() + greenOffset);
    double expectedBlue = AbstractPixel.validateColorValue(pixel.getBlue() + blueOffset);

    assertEquals(expectedRed, correctedPixel.getRed(), 0.001);
    assertEquals(expectedGreen, correctedPixel.getGreen(), 0.001);
    assertEquals(expectedBlue, correctedPixel.getBlue(), 0.001);
  }

  @Test
  public void testCompresss() throws IOException {

    double[][] red = new double[][] {
            {50, 30, 60},
            {40, 100, 50},
            {20, 60, 80}
    };

    double[][] green = new double[][] {
            {10, 20, 50},
            {20, 150, 70},
            {80, 60, 100}
    };

    double[][] blue = new double[][] {
            {70, 90, 20},
            {30, 120, 60},
            {40, 30, 40}
    };

    double[][] redResult = new double[][] {
            {41.24999999999996, 41.24999999999996, 68.74999999999996},
            {41.24999999999996, 41.24999999999996, 68.74999999999994},
            {19.999999, 19.999999, 19.999999999}
    };

    double[][] greenResult = new double[][] {
            {64.99999999999996, 4.999999999999985, 64.99999999999996},
            {4.999999999999985, 64.99999999999996, 64.99999999999996},
            {69.99999999999996, 69.99999999999996, 109.99999999999996}
    };

    double[][] blueResult = new double[][] {
            {77.49999999999994, 77.49999999999994, 19.99999999999999},
            {77.49999999999994, 77.49999999999994, 19.99999999999999},
            {17.499999999999986, 17.499999999999986, 9.999999999999993}
    };

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        sampleImage.setPixel(i, j, new ColorPixel(red[i][j], green[i][j], blue[i][j]));
      }
    }

    Image compressed = processor.compress(sampleImage, 50);

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        ColorPixel pixel = (ColorPixel) compressed.getPixel(i, j);
        assertEquals(redResult[i][j], pixel.getRed(), 0.001);
        assertEquals(greenResult[i][j], pixel.getGreen(), 0.001);
        assertEquals(blueResult[i][j], pixel.getBlue(), 0.001);
      }
    }
  }

}