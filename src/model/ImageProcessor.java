package model;

import java.util.HashMap;

/**
 * The ImageProcessor interface provides methods for various image processing operations.
 */
public interface ImageProcessor {

  /**
   * Split an image into its individual R, G, B components.
   *
   * @param image The input image to be split.
   * @return A list containing three images, each representing one of the R, G, B components.
   */
  HashMap<String, Image> splitIntoRGB(Image image);

  /**
   * Flip an image horizontally.
   *
   * @param image The input image to be flipped.
   * @return A new image that is a horizontal flip of the original.
   */
  Image flipHorizontally(Image image);

  /**
   * Flip an image vertically.
   *
   * @param image The input image to be flipped.
   * @return A new image that is a vertical flip of the original.
   */
  Image flipVertically(Image image);

  /**
   * Convert a color or grayscale image into grayscale.
   *
   * @param image The input image (can be ColorImage or GreyscaleImage) to be converted.
   * @return A new GreyscaleImage.
   */
  Image grayscale(Image image);

  /**
   * Brighten or darken an image based on the provided factor.
   *
   * @param image  The input image to be modified.
   * @param factor A double value representing the degree to which
   *               the image should be brightened or darkened.
   *               Values greater than 1 will brighten, values
   *               between 0 and 1 will darken.
   * @return A new image that has been brightened or darkened.
   */
  Image brightenOrDarken(Image image, double factor);

  /**
   * Combine three grayscale images into a single color image.
   * The R, G, B values of the output image come from the three input images.
   *
   * @param redChannel   The grayscale image representing the red channel.
   * @param greenChannel The grayscale image representing the green channel.
   * @param blueChannel  The grayscale image representing the blue channel.
   * @return A new ColorImage composed of the three grayscale images.
   */
  Image combineImages(Image redChannel, Image greenChannel, Image blueChannel);

  /**
   * Blur the input image.
   *
   * @param image The input image to be blurred.
   * @return A new blurred image.
   */
  Image blur(Image image);

  /**
   * Sharpen the input image.
   *
   * @param image The input image to be sharpened.
   * @return A new sharpened image.
   */
  Image sharpen(Image image);

  /**
   * Convert an image into sepia.
   *
   * @param image The input image to be converted.
   * @return A new sepia-toned image.
   */
  Image sepia(Image image);


  /**
   * Computes the maximum channel value for each pixel and creates
   * a grayscale image based on the computed values.
   *
   * @param image The input color image.
   * @return A grayscale image where each pixel's value is based on the maximum
   *          channel value of the corresponding pixel in the input image.
   */
  Image computeValue(Image image);

  /**
   * Computes the average intensity of each pixel and creates a grayscale image
   * based on the computed intensities.
   *
   * @param image The input color image.
   * @return A grayscale image where each pixel's intensity is the average of the
   *         red, green, and blue values of the corresponding pixel in the input image.
   */
  Image computeIntensity(Image image);

  /**
   * Computes the luma value for each pixel based on standard coefficients and creates
   * a grayscale image based on the computed luma values.
   *
   * @param image The input color image.
   * @return A grayscale image where each pixel's luma value is calculated using the
   *          standard coefficients for the red, green, and blue channels of the corresponding
   *          pixel in the input image.
   */
  Image computeLuma(Image image);
}
