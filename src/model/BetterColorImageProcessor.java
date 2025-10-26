package model;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * An Extension of the ColorImageProcess that handles more image operations.
 */
public class BetterColorImageProcessor
        extends ColorImageProcessor
        implements BetterImageProcessor {

  @Override
  public Image compress(Image image, int percentage) {
    if (!(image instanceof ColorImage)) {
      throw new IllegalArgumentException("The image should be of type ColorImage.");
    }
    ColorImage colorImage = (ColorImage) image;

    if (percentage > 100 || percentage < 0) {
      throw new IllegalArgumentException("Percentage should be between 0 to 100.");
    }

    int width = image.getWidth();
    int height = image.getHeight();
    double[][] redChannel = colorImage.getRedChannel();
    double[][] greenChannel = colorImage.getGreenChannel();
    double[][] blueChannel = colorImage.getBlueChannel();

    ColorImage compressedImage = new ColorImage(height, width);

    int maxSide = Math.max(width, height);
    int paddingSize = findNextPowerOf2(maxSide);

    double[][] paddedRed = padArray(redChannel, height, width, paddingSize);
    double[][] paddedGreen = padArray(greenChannel, height, width, paddingSize);
    double[][] paddedBlue = padArray(blueChannel, height, width, paddingSize);

    double[][] haarRed = haar(paddedRed);
    double[][] haarGreen = haar(paddedGreen);
    double[][] haarBlue = haar(paddedBlue);

    List<Double> uniqueValues = getUniqueAbsoluteValues(haarRed, haarGreen, haarBlue);
    int index = Math.round(uniqueValues.size() * ((float) percentage / 100));
    double threshold = uniqueValues.get(index);
    haarRed = removeBelowThreshold(haarRed, threshold);
    haarGreen = removeBelowThreshold(haarGreen, threshold);
    haarBlue = removeBelowThreshold(haarBlue, threshold);

    double[][] invHaarRed = invHaar(haarRed);
    double[][] invHaarGreen = invHaar(haarGreen);
    double[][] invHaarBlue = invHaar(haarBlue);

    double[][] unpaddedRed = unpadArray(invHaarRed, height, width, paddingSize);
    double[][] unpaddedGreen = unpadArray(invHaarGreen, height, width, paddingSize);
    double[][] unpaddedBlue = unpadArray(invHaarBlue, height, width, paddingSize);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        compressedImage.setPixel(i, j, new ColorPixel(unpaddedRed[i][j],
                unpaddedGreen[i][j], unpaddedBlue[i][j]));
      }
    }

    return compressedImage;
  }

  /**
   * Method to create a histogram.
   * @param image the input image to produce histogram.
   * @return Histogram image
   */
  @Override
  public Image histogram(Image image) {

    HashMap<Color, int[]> histogramMap =  computeHistogram(image);

    BufferedImage histogramBufferedImage = new BufferedImage(
            256,
            256,
            BufferedImage.TYPE_INT_ARGB);
    Graphics g = histogramBufferedImage.getGraphics();

    drawGrid(g);

    drawHistogram(g, histogramMap.get(Color.RED), Color.RED);
    drawHistogram(g, histogramMap.get(Color.GREEN), Color.GREEN);
    drawHistogram(g, histogramMap.get(Color.BLUE), Color.BLUE);

    ColorImage histogramColorImage = new ColorImage(256, 256);
    for (int i = 0; i < 256; i++) {
      for (int j = 0; j < 256; j++) {
        int rgb = histogramBufferedImage.getRGB(i, j);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        histogramColorImage.setPixel(j, i, new ColorPixel(red, green, blue));
      }
    }

    return histogramColorImage;

  }

  /**
   * Helper method to create a histogram.
   * @param image original image.
   * @return a Color and Int array Hashmap
   */

  public HashMap<Color, int[]> computeHistogram(Image image) {
    int[] redHistogram = new int[256];
    int[] greenHistogram = new int[256];
    int[] blueHistogram = new int[256];

    for (int row = 0; row < image.getHeight(); row++) {
      for (int col = 0; col < image.getWidth(); col++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(row, col);
        redHistogram[(int) pixel.getRed()]++;
        greenHistogram[(int) pixel.getGreen()]++;
        blueHistogram[(int) pixel.getBlue()]++;
      }
    }

    HashMap<Color, int[]> histograms = new HashMap<>();
    histograms.put(Color.RED, redHistogram);
    histograms.put(Color.GREEN, greenHistogram);
    histograms.put(Color.BLUE, blueHistogram);

    return histograms;
  }

  @Override
  public Image colorCorrect(Image image) {
    HashMap<Color, int[]> histograms = computeHistogram(image);

    int redPeak = findMeaningfulPeak(histograms.get(Color.RED));
    int greenPeak = findMeaningfulPeak(histograms.get(Color.GREEN));
    int bluePeak = findMeaningfulPeak(histograms.get(Color.BLUE));

    int averagePeak = (redPeak + greenPeak + bluePeak) / 3;

    return adjustColorBalance(
            image,
            averagePeak - redPeak,
            averagePeak - greenPeak,
            averagePeak - bluePeak);
  }

  private int findMeaningfulPeak(int[] histogram) {
    int peakValue = 10;
    for (int i = 11; i < 245; i++) {
      if (histogram[i] > histogram[peakValue]) {
        peakValue = i;
      }
    }
    return peakValue;
  }

  @Override
  public Image adjustLevels(Image image, int black, int mid, int white) {
    double varA = Math.pow(black, 2) * (mid - white) - black * (Math.pow(mid, 2)
            - Math.pow(white, 2))
            + mid * Math.pow(white, 2) - mid * Math.pow(white, 2);
    double varA_a = -black * (128 - 255) + 128 * white - 255 * mid;
    double varA_b = Math.pow(black, 2) * (128 - 255) + 255 * Math.pow(mid, 2)
            - 128 * Math.pow(white, 2);
    double varA_c = Math.pow(black, 2) * (255 * mid - 128 * white) - black * (255 * Math.pow(mid, 2)
            - 128 * Math.pow(white, 2));

    double a = varA_a / varA;
    double b = varA_b / varA;
    double c = varA_c / varA;

    ColorImage adjustedImage = new ColorImage(image.getHeight(), image.getWidth());

    for (int row = 0; row < image.getHeight(); row++) {
      for (int col = 0; col < image.getWidth(); col++) {
        ColorPixel originalPixel = (ColorPixel) image.getPixel(row, col);
        adjustedImage.setPixel(row, col, adjustPixel(originalPixel, a, b, c));
      }
    }

    return adjustedImage;
  }

  @Override
  public Image splitView(Image image, String operation, int percentage) {
    Image processedImage = applyOperation(image, operation);
    return mergeForSplitView(image, processedImage, percentage);
  }

  public Image splitView(Image image, int black, int mid, int white, int percentage) {
    Image processedImage = adjustLevels(image, black, mid, white);
    return mergeForSplitView(image, processedImage, percentage);
  }

  private Image applyOperation(Image image, String operation) {
    switch (operation) {
      case "blur":
        return blur(image);
      case "sharpen":
        return sharpen(image);
      case "sepia":
        return sepia(image);
      case "grayscale":
        return grayscale(image);
      case "color-correct":
        return colorCorrect(image);
      default:
        return image;
    }
  }

  private double[] transform(double[] arr) {

    int length = arr.length;
    double[] avg = new double[length / 2];
    double[] diff = new double[length / 2];
    double[] result = new double[length];

    for (int i = 0; i < length - 1; i += 2) {
      double a = arr[i];
      double b = arr[i + 1];
      double av = (a + b) / Math.sqrt(2);
      double di = (a - b) / Math.sqrt(2);
      avg[i / 2] = av;
      diff[i / 2] = di;
    }
    System.arraycopy(avg, 0, result, 0, avg.length);
    System.arraycopy(diff, 0, result, avg.length, diff.length);

    return result;
  }

  private double[] invert(double[] arr) {
    int length = arr.length;
    double[] avg = new double[length / 2];
    double[] diff = new double[length / 2];
    double[] result = new double[length];

    int j = length / 2;
    for (int i = 0; i < length / 2; i++) {
      double a = arr[i];
      double b = arr[j];
      double av = (a + b) / Math.sqrt(2);
      double di = (a - b) / Math.sqrt(2);
      avg[i] = av;
      diff[i] = di;
      j++;
    }

    for (int i = 0; i < length; i += 2) {
      result[i] = avg[i / 2];
      result[i + 1] = diff[i / 2];
    }

    return result;
  }

  private double[][] haar(double[][] arr) {
    int c = arr.length;

    while (c > 1) {
      for (int i = 0; i < c; i++) {
        double[] t = new double[c];
        for (int j = 0; j < c; j++) {
          t[j] = arr[i][j];
        }
        t = transform(t);
        System.arraycopy(t, 0, arr[i], 0, c);
      }

      double[] temp = new double[c];
      for (int col = 0; col < c; col++) {
        for (int row = 0; row < c; row++) {
          temp[row] = arr[row][col];
        }
        temp = transform(temp);
        for (int row = 0; row < c; row++) {
          arr[row][col] = temp[row];
        }
      }
      c = c / 2;
    }
    return arr;
  }

  private double[][] invHaar(double[][] arr) {
    int c = 2;

    while (c <= arr.length) {
      double[] temp = new double[c];
      for (int col = 0; col < c; col++) {
        for (int row = 0; row < c; row++) {
          temp[row] = arr[row][col];
        }
        temp = invert(temp);
        for (int row = 0; row < c; row++) {
          arr[row][col] = temp[row];
        }
      }

      for (int i = 0; i < c; i++) {
        double[] t = new double[c];
        for (int j = 0; j < c; j++) {
          t[j] = arr[i][j];
        }
        t = invert(t);
        System.arraycopy(t, 0, arr[i], 0, c);
      }
      c = c * 2;
    }
    return arr;
  }

  private int findNextPowerOf2(int number) {

    if ((number & (number - 1)) == 0) {
      return number;
    }
    int result = 1;
    while (result < number) {
      result <<= 1;
    }
    return result;
  }

  private double[][] padArray(double[][] arr, int height, int width, int paddingSize) {
    double[][] paddedArray = new double[paddingSize][paddingSize];
    for (int i = 0; i < height; i++) {
      System.arraycopy(arr[i], 0, paddedArray[i], 0, Math.min(width, paddingSize));
    }
    return paddedArray;
  }

  private double[][] unpadArray(double[][] arr, int height, int width, int paddingSize) {
    double[][] unpaddedArray = new double[height][width];
    for (int i = 0; i < height; i++) {
      System.arraycopy(arr[i], 0, unpaddedArray[i], 0, Math.min(width, paddingSize));
    }
    return unpaddedArray;
  }

  private List<Double> getUniqueAbsoluteValues(double[][]... arrays) {
    TreeSet<Double> uniqueValues = new TreeSet<>();

    for (double[][] array : arrays) {
      for (double[] row : array) {
        for (double element : row) {
          uniqueValues.add(Math.abs(element));
        }
      }
    }
    return new ArrayList<>(uniqueValues);
  }

  private double[][] removeBelowThreshold(double[][] arr, double threshold) {
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < arr.length; j++) {
        if (arr[i][j] <= threshold) {
          arr[i][j] = 0;
        }
      }
    }
    return arr;
  }
}
