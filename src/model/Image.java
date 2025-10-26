package model;

/**
 * Abstract class representing an image comprising a 2D grid of Pixel objects.
 */
public abstract class Image implements ImageInterface {

  /**
   * 2D array representing the pixels of the image.
   */
  protected ColorPixel[][] pixels;
  private int width;
  private int height;

  /**
   * Constructs an Image with the specified width and height.
   * Initializes all pixels based on the type of image (color or greyscale).
   *
   * @param width  The width of the image.
   * @param height The height of the image.
   */
  public Image(int height, int width) {
    this.width = width;
    this.height = height;
    this.pixels = new ColorPixel[height][width];
    initializePixels();
  }

  /**
   * Abstract method for initializing pixels.
   */
  protected abstract void initializePixels();

  /**
   * Gets the pixel at specified coordinates.
   *
   * @param x X-coordinate.
   * @param y Y-coordinate.
   * @return Pixel at (x, y).
   */
  public ColorPixel getPixel(int x, int y) {
    return pixels[x][y];
  }

  /**
   * Sets the pixel at specified coordinates.
   *
   * @param x     X-coordinate.
   * @param y     Y-coordinate.
   * @param pixel Pixel to set.
   */
  public void setPixel(int x, int y, ColorPixel pixel) {
    pixels[x][y] = pixel;
  }

  /**
   * Returns the image's width.
   *
   * @return Image width.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns the image's height.
   *
   * @return Image height.
   */
  public int getHeight() {
    return height;
  }
}