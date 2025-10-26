package model;

/**
 * This interface provides methods for adding more image processing operations.
 */
public interface BetterImageProcessor extends ImageProcessor {

  /**
   * Compress an image by a percentage.
   * @param image the input image to be compressed.
   * @param percentage the percentage by which the image will be compressed.
   * @return the compressed image.
   */
  Image compress(Image image, int percentage);

  /**
   * Produce a histogram of an image.
   * @param image the input image to produce histogram.
   * @return an image with the histogram.
   */
  Image histogram(Image image);

  /**
   * Color correct an image.
   * @param image the input image to color correct.
   * @return a color corrected image.
   */
  Image colorCorrect(Image image);

  Image adjustLevels(Image image, int black, int mid, int white);

  /**
   * Preview operations in a part of the image.
   * @param image the input image to preview operation on.
   * @param percentage the percentage on which to apply the operation.
   * @return an image with an operation applied to a part of it.
   */
  Image splitView(Image image, String operation, int percentage);

  /**
   * Preview operations in a part of the image.
   * @param image the input image to preview operation on.
   * @param percentage the percentage on which to apply the operation.
   * @return an image with an operation applied to a part of it.
   */
  Image splitView(Image image, int black, int mid, int white, int percentage);
}
