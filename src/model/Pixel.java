package model;

/**
 * Interface representing a pixel with utilities for color validation.
 */
public interface Pixel {
  /**
   * Validates and clamps a color value between 0 and 255.
   *
   * @param value Color value to validate.
   * @return Clamped value between 0 and 255.
   */
  static double validateColorValue(double value) {
    if (value < 0) {
      return 0;
    }
    if (value > 255) {
      return 255;
    }
    return value;
  }
}
