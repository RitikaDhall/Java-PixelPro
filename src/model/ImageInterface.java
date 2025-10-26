package model;

/**
 * Interface representing the behavior of an image comprising a 2D grid of Pixel objects.
 */
public interface ImageInterface {

  /**
   * Retrieves the pixel at the specified coordinates.
   *
   * @param x The x-coordinate of the pixel.
   * @param y The y-coordinate of the pixel.
   * @return The pixel at the specified coordinates.
   */
  AbstractPixel getPixel(int x, int y);

  /**
   * Sets the pixel at the specified coordinates.
   *
   * @param x     The x-coordinate of the pixel.
   * @param y     The y-coordinate of the pixel.
   * @param pixel The pixel to set at the specified coordinates.
   */
  void setPixel(int x, int y, ColorPixel pixel);

  /**
   * Retrieves the width of the image.
   *
   * @return The width of the image.
   */
  int getWidth();

  /**
   * Retrieves the height of the image.
   *
   * @return The height of the image.
   */
  int getHeight();
}
