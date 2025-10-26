package model;

/**
 * A 2D grid representation of a color image using {@link ColorPixel} objects.
 */
public class ColorImage extends Image {

  /**
   * Initializes a new ColorImage with given dimensions.
   * Pixels default to black (R=0, G=0, B=0).
   *
   * @param height Image height in pixels
   * @param width  Image width in pixels
   */
  public ColorImage(int height, int width) {
    super(height, width);
  }

  /**
   * Get an array of the red component in the pixels of the image.
   *
   * @return a 2-D array of red component of the pixels
   */
  protected double[][] getRedChannel() {

    int height = getHeight();
    int width = getWidth();
    double[][] redChannel = new double[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ColorPixel pixel = (ColorPixel) getPixel(i, j);
        redChannel[i][j] = pixel.getRed();
      }
    }
    return redChannel;
  }

  /**
   * Get an array of the green component in the pixels of the image.
   *
   * @return a 2-D array of green component of the pixels
   */
  protected double[][] getGreenChannel() {

    int height = getHeight();
    int width = getWidth();
    double[][] redChannel = new double[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ColorPixel pixel = (ColorPixel) getPixel(i, j);
        redChannel[i][j] = pixel.getGreen();
      }
    }
    return redChannel;
  }

  /**
   * Get an array of the blue component in the pixels of the image.
   *
   * @return a 2-D array of blue component of the pixels
   */
  protected double[][] getBlueChannel() {

    int height = getHeight();
    int width = getWidth();
    double[][] blueChannel = new double[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        ColorPixel pixel = (ColorPixel) getPixel(i, j);
        blueChannel[i][j] = pixel.getBlue();
      }
    }
    return blueChannel;
  }

  /**
   * Sets all pixels in the image to black.
   */
  @Override
  protected void initializePixels() {
    for (int i = 0; i < getHeight(); i++) {
      for (int j = 0; j < getWidth(); j++) {
        pixels[i][j] = new ColorPixel(0, 0, 0);
      }
    }
  }


}