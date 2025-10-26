package model;

/**
 * Represents a color pixel of an image with red, green, and blue values.
 */
public class ColorPixel extends AbstractPixel {

  private double red;
  private double green;
  private double blue;

  /**
   * Initializes a ColorPixel with specified RGB values.
   *
   * @param red Red value.
   * @param green Green value.
   * @param blue Blue value.
   */
  public ColorPixel(double red, double green, double blue) {
    this.red = validateColorValue(red);
    this.green = validateColorValue(green);
    this.blue = validateColorValue(blue);
  }

  /**
   * Returns the red value.
   *
   * @return Red value.
   */
  public double getRed() {
    return red;
  }


  /**
   * Returns the green value.
   *
   * @return Green value.
   */
  public double getGreen() {
    return green;
  }


  /**
   * Returns the blue value.
   *
   * @return Blue value.
   */
  public double getBlue() {
    return blue;
  }

}