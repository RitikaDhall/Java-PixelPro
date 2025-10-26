package model;

/**
 * Abstract class representing a pixel of an image. Contains methods
 * to ensure that the color values are clamped within the range 0-255.
 */
public abstract class AbstractPixel implements Pixel {

  /**
   * Ensures that the provided color value is within the range 0-255.
   *
   * @param value The color value to validate.
   * @return The validated color value.
   */
  public static double validateColorValue(double value) {
    if (value < 0) {
      return 0;
    }
    if (value > 255) {
      return 255;
    }
    return value;
  }
}