package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import javax.imageio.ImageIO;

import model.AbstractPixel;
import model.ColorImage;
import model.ColorPixel;
import model.Image;

/**
 * Utility class for handling image operations.
 */
public class ImageUtil {


  /**
   * Reads an image from the given path and returns a ColorImage object.
   *
   * @param path the file path of the image to be read.
   * @return the ColorImage object representation of the read image.
   * @throws IOException if an I/O error occurs while reading the image.
   */
  public ColorImage getImage(String path) throws IOException {
    BufferedImage image;
    File file = new File(path);
    image = ImageIO.read(file);

    int width = image.getWidth();
    int height = image.getHeight();
    ColorImage newImage = new ColorImage(height, width);
    Color color;
    ColorPixel pixel;

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        color = new Color(image.getRGB(col, row));
        pixel = new ColorPixel(color.getRed(), color.getGreen(), color.getBlue());
        newImage.setPixel(row, col, pixel);
      }
    }
    return newImage;
  }

  /**
   * Saves a given Image object to the specified path with the given extension.
   *
   * @param image     the Image object to be saved.
   * @param path      the destination file path.
   * @param extension the file format extension (e.g., "png" or "jpg").
   */
  public void saveImage(Image image, String path, String extension) {
    int height = image.getHeight();
    int width = image.getWidth();
    int imageType;
    if (Objects.equals(extension, "png")) {
      imageType = 6;
    } else {
      imageType = 5;
    }
    BufferedImage b = new BufferedImage(width, height, imageType);
    File newImageFile = new File(path);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        ColorPixel pixel = (ColorPixel) image.getPixel(row, col);
        int red = (int) AbstractPixel.validateColorValue(pixel.getRed());
        int green = (int) AbstractPixel.validateColorValue(pixel.getGreen());
        int blue = (int) AbstractPixel.validateColorValue(pixel.getBlue());
        Color newColor = new Color(red, green, blue);
        b.setRGB(col, row, newColor.getRGB());
      }
    }
    try {
      ImageIO.write(b, extension, newImageFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println("Saved image");
  }


  /**
   * Reads a PPM image file and returns its ColorImage representation.
   *
   * @param filename the path of the PPM file.
   * @return the ColorImage object representation of the read PPM file.
   */
  public ColorImage readPPM(String filename) {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();
    System.out.println("Maximum value of a color in this file (usually 255): " + maxValue);

    ColorImage newImage = new ColorImage(height, width);
    ColorPixel pixel;

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        pixel = new ColorPixel(r, g, b);
        newImage.setPixel(i, j, pixel);
      }
    }
    return newImage;
  }

  /**
   * Saves a given Image object as a PPM file to the specified path.
   *
   * @param image the Image object to be saved as PPM.
   * @param path  the destination file path for the PPM file.
   */
  public void savePPM(Image image, String path) {
    try {
      FileWriter writer = new FileWriter(new File(path));

      int width = image.getWidth();
      int height = image.getHeight();

      writer.write("P3\n");
      writer.write(width + " " + height + "\n");
      writer.write("255\n");

      for (int row = 0; row < height; row++) {
        for (int col = 0; col < width; col++) {
          ColorPixel pixel = (ColorPixel) image.getPixel(row, col);
          int red = (int) AbstractPixel.validateColorValue(pixel.getRed());
          int green = (int) AbstractPixel.validateColorValue(pixel.getGreen());
          int blue = (int) AbstractPixel.validateColorValue(pixel.getBlue());

          writer.write(red + " " + green + " " + blue + " ");
        }
        writer.write("\n");
      }

      writer.close();

      System.out.println("Saved PPM image");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
