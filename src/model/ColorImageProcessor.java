package model;

import java.util.HashMap;

/**
 * A Class that can perform operations on images and return the resulting images.
 */

public class ColorImageProcessor extends AbstractImageProcessor {
  /**
   * Split an image into its individual R, G, B components.
   *
   * @param image The input image to be split.
   * @return A list containing three images, each representing one of the R, G, B components.
   */
  @Override
  public HashMap<String, Image> splitIntoRGB(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();
    HashMap<String, Image> imageList = new HashMap<>();

    ColorImage redChannel = new ColorImage(height, width);
    ColorImage greenChannel = new ColorImage(height, width);
    ColorImage blueChannel = new ColorImage(height, width);

    imageList.put("redChannel", redChannel);
    imageList.put("greenChannel", greenChannel);
    imageList.put("blueChannel", blueChannel);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(row, col);
        double red = pixel.getRed();
        double green = pixel.getGreen();
        double blue = pixel.getBlue();

        redChannel.setPixel(row, col, new ColorPixel(red, 0, 0));
        greenChannel.setPixel(row, col, new ColorPixel(0, green, 0));
        blueChannel.setPixel(row, col, new ColorPixel(0, 0, blue));
      }
    }
    return imageList;
  }


  /**
   * Flip an image horizontally.
   *
   * @param image The input image to be flipped.
   * @return A new image that is a horizontal flip of the original.
   */
  @Override
  public Image flipHorizontally(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();

    ColorImage returnImage = new ColorImage(height, width);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        returnImage.setPixel(row, col, image.getPixel(row, width - col - 1));
      }
    }
    return returnImage;
  }

  /**
   * Flip an image vertically.
   *
   * @param image The input image to be flipped.
   * @return A new image that is a vertical flip of the original.
   */
  @Override
  public Image flipVertically(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();

    ColorImage returnImage = new ColorImage(height, width);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        returnImage.setPixel(row, col, image.getPixel(height - row - 1, col));
      }
    }
    return returnImage;
  }

  /**
   * Convert a color or grayscale image into grayscale.
   *
   * @param image The input image (can be ColorImage or GreyscaleImage) to be converted.
   * @return A new GreyscaleImage.
   */
  @Override
  public Image grayscale(Image image) {
    int height = image.getHeight();
    int width = image.getWidth();
    ColorImage grayscaleImage = new ColorImage(height, width);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(i, j);
        double greyValue = 0.2126 * pixel.getRed() + 0.7152 * pixel.getGreen()
                + 0.0722 * pixel.getBlue();
        grayscaleImage.setPixel(i, j, new ColorPixel(greyValue, greyValue, greyValue));
      }
    }
    return grayscaleImage;
  }

  /**
   * Brighten or darken an image based on the provided factor.
   *
   * @param image    The input image to be modified.
   * @param constant A double value representing the degree to which
   *                 the image should be brightened or darkened.
   *                 Values greater than 1 will brighten, values
   *                 between 0 and 1 will darken.
   * @return A new image that has been brightened or darkened.
   */
  @Override
  public Image brightenOrDarken(Image image, double constant) {
    int height = image.getHeight();
    int width = image.getWidth();
    ColorImage returnImage = new ColorImage(height, width);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(row, col);
        double red = AbstractPixel.validateColorValue(pixel.getRed() + constant);
        double green = AbstractPixel.validateColorValue(pixel.getGreen() + constant);
        double blue = AbstractPixel.validateColorValue(pixel.getBlue() + constant);

        returnImage.setPixel(row, col, new ColorPixel(red, green, blue));
      }
    }
    return returnImage;
  }

  /**
   * Combine three grayscale images into a single color image.
   * The R, G, B values of the output image come from the three input images.
   *
   * @param redChannel   The grayscale image representing the red channel.
   * @param greenChannel The grayscale image representing the green channel.
   * @param blueChannel  The grayscale image representing the blue channel.
   * @return A new ColorImage composed of the three grayscale images.
   */
  @Override
  public Image combineImages(Image redChannel, Image greenChannel, Image blueChannel) {
    if (
            redChannel.getHeight() != greenChannel.getHeight() ||
                    redChannel.getHeight() != blueChannel.getHeight() ||
                    greenChannel.getHeight() != blueChannel.getHeight() ||
                    redChannel.getWidth() != greenChannel.getWidth() ||
                    redChannel.getWidth() != blueChannel.getWidth() ||
                    greenChannel.getWidth() != blueChannel.getWidth()
    ) {
      throw new IllegalArgumentException("Images must be of the same size.");
    }

    int height = redChannel.getHeight();
    int width = redChannel.getWidth();

    ColorImage returnImage = new ColorImage(height, width);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {

        ColorPixel redPixel = (ColorPixel) (redChannel.getPixel(row, col));
        ColorPixel greenPixel = (ColorPixel) (greenChannel.getPixel(row, col));
        ColorPixel bluePixel = (ColorPixel) (blueChannel.getPixel(row, col));

        returnImage.setPixel(
                row, col,
                new ColorPixel(
                        redPixel.getRed(),
                        greenPixel.getGreen(),
                        bluePixel.getBlue()
                )
        );
      }
    }

    return returnImage;
  }

  /**
   * Blur the input image.
   *
   * @param image The input image to be blurred.
   * @return A new blurred image.
   */
  @Override
  public Image blur(Image image) {

    double[][] kernel = {
            {0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}
    };

    return applyKernel(image, kernel);
  }

  /**
   * Sharpen the input image.
   *
   * @param image The input image to be sharpened.
   * @return A new sharpened image.
   */
  @Override
  public Image sharpen(Image image) {
    double[][] kernel = {
            {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, 1.0 / 4, 1.0 / 4, 1.0 / 4, -1.0 / 8},
            {-1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8, -1.0 / 8}
    };

    return applyKernel(image, kernel);
  }

  /**
   * Convert an image into sepia.
   *
   * @param image The input image to be converted.
   * @return A new sepia-toned image.
   */
  @Override
  public Image sepia(Image image) {
    if (!(image instanceof ColorImage)) {
      throw new IllegalArgumentException("The image should be of type ColorImage.");
    }
    int height = image.getHeight();
    int width = image.getWidth();

    ColorImage sepiaImage = new ColorImage(height, width);
    double[][] sepiaMatrix = {
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    };

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(i, j);
        double[] rgb = {pixel.getRed(), pixel.getGreen(), pixel.getBlue()};
        double[] transformed = multiplyMatrix(sepiaMatrix, rgb);

        sepiaImage.setPixel(i, j, new ColorPixel(transformed[0], transformed[1], transformed[2]));
      }
    }
    return sepiaImage;
  }

  /**
   * Computes the maximum channel value for each pixel and creates
   * a grayscale image based on the computed values.
   *
   * @param image The input color image.
   * @return A grayscale image where each pixel's value is based on the maximum
   *        channel value of the corresponding pixel in the input image.
   */
  public Image computeValue(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    ColorImage returnImage = new ColorImage(height, width);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(row, col);
        double value = Math.max(pixel.getRed(), Math.max(pixel.getGreen(), pixel.getBlue()));
        returnImage.setPixel(row, col, new ColorPixel(value, value, value));
      }
    }

    return returnImage;
  }

  /**
   * Computes the average intensity of each pixel and creates a grayscale image
   * based on the computed intensities.
   *
   * @param image The input color image.
   * @return A grayscale image where each pixel's intensity is the average of the
   *          red, green, and blue values of the corresponding pixel in the input image.
   */
  public Image computeIntensity(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    ColorImage returnImage = new ColorImage(height, width);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(row, col);
        double intensity = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3.0;
        returnImage.setPixel(row, col, new ColorPixel(intensity, intensity, intensity));
      }
    }

    return returnImage;
  }

  /**
   * Computes the luma value for each pixel based on standard coefficients and creates
   * a grayscale image based on the computed luma values.
   *
   * @param image The input color image.
   * @return A grayscale image where each pixel's luma value is calculated using the
   *        standard coefficients for the red, green, and blue channels of the corresponding
   *        pixel in the input image.
   */
  public Image computeLuma(Image image) {
    int width = image.getWidth();
    int height = image.getHeight();
    ColorImage returnImage = new ColorImage(height, width);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(row, col);
        double luma = 0.2126 * pixel.getRed() + 0.7152 * pixel.getGreen()
                + 0.0722 * pixel.getBlue();
        returnImage.setPixel(row, col, new ColorPixel(luma, luma, luma));
      }
    }

    return returnImage;
  }

}
