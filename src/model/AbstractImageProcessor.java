package model;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Abstract class providing a foundation for image processing.
 */
public abstract class AbstractImageProcessor implements ImageProcessor {
  /**
   * Applies a convolution kernel to an image.
   *
   * @param image  Input image.
   * @param kernel Convolution kernel.
   * @return Processed image.
   */
  protected Image applyKernel(Image image, double[][] kernel) {
    int width = image.getWidth();
    int height = image.getHeight();
    ColorImage returnImage = new ColorImage(height, width);

    int kHeight = kernel.length;
    int kWidth = kernel[0].length;

    if (kHeight % 2 == 0 || kWidth % 2 == 0) {
      throw new IllegalArgumentException("Kernel dimensions should be odd.");
    }

    int kCenterY = kHeight / 2;
    int kCenterX = kWidth / 2;

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        double sumRed = 0;
        double sumGreen = 0;
        double sumBlue = 0;

        for (int kRow = 0; kRow < kHeight; kRow++) {
          for (int kCol = 0; kCol < kWidth; kCol++) {
            int imageCol = col + kCol - kCenterX;
            int imageRow = row + kRow - kCenterY;

            if (imageCol >= 0 && imageCol < width && imageRow >= 0 && imageRow < height) {
              ColorPixel pixel = (ColorPixel) image.getPixel(imageRow, imageCol);
              sumRed += pixel.getRed() * kernel[kRow][kCol];
              sumGreen += pixel.getGreen() * kernel[kRow][kCol];
              sumBlue += pixel.getBlue() * kernel[kRow][kCol];
            }
          }
        }

        // Clamp the values using AbstractPixel.validateColorValue
        sumRed = AbstractPixel.validateColorValue(sumRed);
        sumGreen = AbstractPixel.validateColorValue(sumGreen);
        sumBlue = AbstractPixel.validateColorValue(sumBlue);

        returnImage.setPixel(row, col, new ColorPixel(sumRed, sumGreen, sumBlue));
      }
    }

    return returnImage;
  }

  protected double[] multiplyMatrix(double[][] matrix, double[] vector) {
    double[] result = new double[vector.length];
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < vector.length; j++) {
        result[i] += matrix[i][j] * vector[j];
      }
    }
    return result;
  }

  protected ColorPixel adjustPixel(ColorPixel pixel, double a, double b, double c) {
    double newRed = AbstractPixel.validateColorValue(a * Math.pow(pixel.getRed(), 2)
            + b * pixel.getRed() + c);
    double newGreen = AbstractPixel.validateColorValue(a * Math.pow(pixel.getGreen(), 2)
            + b * pixel.getGreen() + c);
    double newBlue = AbstractPixel.validateColorValue(a * Math.pow(pixel.getBlue(), 2)
            + b * pixel.getBlue() + c);

    return new ColorPixel(newRed, newGreen, newBlue);
  }

  protected Image mergeForSplitView(Image original, Image processed, int percentage) {
    int width = original.getWidth();
    int splitColumn = width * percentage / 100;
    int lineWidth = Math.max(1, width / 400);
    int lineStart = splitColumn - lineWidth / 2;
    int lineEnd = lineStart + lineWidth;

    ColorImage mergedImage = new ColorImage(original.getHeight(), width);
    ColorPixel greenPixel = new ColorPixel(0, 255, 0);

    for (int i = 0; i < original.getHeight(); i++) {
      for (int j = 0; j < width; j++) {
        if (j >= lineStart && j < lineEnd) {
          mergedImage.setPixel(i, j, greenPixel);
        } else if (j <= splitColumn) {
          mergedImage.setPixel(i, j, processed.getPixel(i, j));
        } else {
          mergedImage.setPixel(i, j, original.getPixel(i, j));
        }
      }
    }
    return mergedImage;
  }

  protected Image adjustColorBalance(Image image, int redOffset, int greenOffset, int blueOffset) {
    ColorImage adjustedImage = new ColorImage(image.getHeight(), image.getWidth());
    for (int row = 0; row < image.getHeight(); row++) {
      for (int col = 0; col < image.getWidth(); col++) {
        ColorPixel originalPixel = (ColorPixel) image.getPixel(row, col);
        double newRed = AbstractPixel.validateColorValue(originalPixel.getRed() + redOffset);
        double newGreen = AbstractPixel.validateColorValue(originalPixel.getGreen() + greenOffset);
        double newBlue = AbstractPixel.validateColorValue(originalPixel.getBlue() + blueOffset);
        adjustedImage.setPixel(row, col, new ColorPixel(newRed, newGreen, newBlue));
      }
    }
    return adjustedImage;
  }

  protected int getMaxFrequency(int[] histogram) {
    int max = 0;
    for (int freq : histogram) {
      if (freq > max) {
        max = freq;
      }
    }
    return max;
  }

  protected void drawHistogram(Graphics g, int[] histogram, Color color) {
    g.setColor(color);
    int maxFrequency = getMaxFrequency(histogram);
    for (int i = 0; i < 255; i++) {
      int scaledFrequency1 = (histogram[i] * 256) / maxFrequency;
      int scaledFrequency2 = (histogram[i + 1] * 256) / maxFrequency;
      g.drawLine(i, 256 - scaledFrequency1, i + 1, 256 - scaledFrequency2);
    }
  }

  protected void drawGrid(Graphics g) {
    g.setColor(Color.LIGHT_GRAY);
    for (int i = 0; i <= 256; i += 16) {
      g.drawLine(i, 0, i, 256);
      g.drawLine(0, i, 256, i);
    }
  }
}
