package model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class to test the model part of the program.
 */
public class ColorImageProcessorTest {

  private ColorImageProcessor processor;
  private ColorImage sampleImage;

  @Before
  public void setUp() {
    processor = new ColorImageProcessor();
    sampleImage = new ColorImage(3, 3);
  }

  @Test
  public void testSplitIntoRGB() {
    ColorPixel pixel = new ColorPixel(120, 130, 140);
    sampleImage.setPixel(1, 1, pixel);

    HashMap<String, Image> result = processor.splitIntoRGB(sampleImage);

    ColorImage redChannel = (ColorImage) result.get("redChannel");
    ColorImage greenChannel = (ColorImage) result.get("greenChannel");
    ColorImage blueChannel = (ColorImage) result.get("blueChannel");

    assertEquals(120, ((ColorPixel) redChannel.getPixel(1, 1)).getRed(), 0.001);
    assertEquals(130, ((ColorPixel) greenChannel.getPixel(1, 1)).getGreen(), 0.001);
    assertEquals(140, ((ColorPixel) blueChannel.getPixel(1, 1)).getBlue(), 0.001);
  }

  @Test
  public void testFlipHorizontally() {
    sampleImage.setPixel(0, 0, new ColorPixel(255, 255, 255));
    sampleImage.setPixel(1, 1, new ColorPixel(255, 255, 255));
    sampleImage.setPixel(2, 2, new ColorPixel(255, 255, 255));

    Image flipped = processor.flipHorizontally(sampleImage);

    assertEquals(255, ((ColorPixel) flipped.getPixel(0, 2)).getRed(), 0.001);
    assertEquals(255, ((ColorPixel) flipped.getPixel(1, 1)).getGreen(), 0.001);
    assertEquals(255, ((ColorPixel) flipped.getPixel(2, 0)).getBlue(), 0.001);
  }

  @Test
  public void testFlipVertically() {
    sampleImage.setPixel(0, 0, new ColorPixel(255, 0, 0));
    sampleImage.setPixel(1, 1, new ColorPixel(0, 255, 0));
    sampleImage.setPixel(2, 2, new ColorPixel(0, 0, 255));

    Image flipped = processor.flipVertically(sampleImage);

    assertEquals(255, ((ColorPixel) flipped.getPixel(2, 0)).getRed(), 0.001);
    assertEquals(255, ((ColorPixel) flipped.getPixel(1, 1)).getGreen(), 0.001);
    assertEquals(255, ((ColorPixel) flipped.getPixel(0, 2)).getBlue(), 0.001);
  }

  @Test
  public void testGrayscale() {
    sampleImage.setPixel(0, 0, new ColorPixel(255, 0, 0));
    sampleImage.setPixel(1, 1, new ColorPixel(0, 255, 0));
    sampleImage.setPixel(2, 2, new ColorPixel(0, 0, 255));

    Image grayscale = processor.grayscale(sampleImage);

    double expectedRedGray = 0.2126 * 255;
    double expectedGreenGray = 0.7152 * 255;
    double expectedBlueGray = 0.0722 * 255;

    assertEquals(expectedRedGray, ((ColorPixel) grayscale.getPixel(0, 0)).getRed(), 0.001);
    assertEquals(expectedGreenGray, ((ColorPixel) grayscale.getPixel(1, 1)).getGreen(), 0.001);
    assertEquals(expectedBlueGray, ((ColorPixel) grayscale.getPixel(2, 2)).getBlue(), 0.001);
  }

  @Test
  public void testBrightenOrDarken() {
    sampleImage.setPixel(1, 1, new ColorPixel(100, 100, 100));

    Image brightened = processor.brightenOrDarken(sampleImage, 50.0);

    assertEquals(150, ((ColorPixel) brightened.getPixel(1, 1)).getRed(), 0.001);
    assertEquals(150, ((ColorPixel) brightened.getPixel(1, 1)).getGreen(), 0.001);
    assertEquals(150, ((ColorPixel) brightened.getPixel(1, 1)).getBlue(), 0.001);
  }

  @Test
  public void testCombineImages() {
    ColorImage redChannel = new ColorImage(1, 1);
    ColorImage greenChannel = new ColorImage(1, 1);
    ColorImage blueChannel = new ColorImage(1, 1);

    redChannel.setPixel(0, 0, new ColorPixel(255, 0, 0));
    greenChannel.setPixel(0, 0, new ColorPixel(0, 255, 0));
    blueChannel.setPixel(0, 0, new ColorPixel(0, 0, 255));

    Image combined = processor.combineImages(redChannel, greenChannel, blueChannel);

    assertEquals(255, ((ColorPixel) combined.getPixel(0, 0)).getRed(), 0.001);
    assertEquals(255, ((ColorPixel) combined.getPixel(0, 0)).getGreen(), 0.001);
    assertEquals(255, ((ColorPixel) combined.getPixel(0, 0)).getBlue(), 0.001);
  }

  @Test
  public void testBlur() {
    // Assuming the blur method is working correctly, we can just
    // check if the output image is different from the input.
    Image blurred = processor.blur(sampleImage);
    assertNotEquals(sampleImage.getPixel(1, 1), blurred.getPixel(1, 1));
  }

  @Test
  public void testSharpen() {
    // Similar to blur, we will check if the output image is different from the input.
    Image sharpened = processor.sharpen(sampleImage);
    assertNotEquals(sampleImage.getPixel(1, 1), sharpened.getPixel(1, 1));
  }

  @Test
  public void testSepia() {
    sampleImage.setPixel(1, 1, new ColorPixel(100, 150, 200));

    Image sepia = processor.sepia(sampleImage);

    double redValue = 0.393 * 100 + 0.769 * 150 + 0.189 * 200;
    double greenValue = 0.349 * 100 + 0.686 * 150 + 0.168 * 200;
    double blueValue = 0.272 * 100 + 0.534 * 150 + 0.131 * 200;

    assertEquals(redValue, ((ColorPixel) sepia.getPixel(1, 1)).getRed(), 0.001);
    assertEquals(greenValue, ((ColorPixel) sepia.getPixel(1, 1)).getGreen(), 0.001);
    assertEquals(blueValue, ((ColorPixel) sepia.getPixel(1, 1)).getBlue(), 0.001);
  }
}
