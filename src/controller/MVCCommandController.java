package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.AbstractPixel;
import model.BetterImageProcessor;
import model.ColorImage;
import model.ColorPixel;
import model.Image;
import view.IView;

/**
 * Controller class for the Graphical User Interface application.
 */
public class MVCCommandController implements IGraphicsController {
  protected Scanner sc;
  protected BetterImageProcessor model;
  protected ImageUtil util = new ImageUtil();
  protected HashMap<String, Image> image_names;
  private IView view;
  private ColorImage currentImage;
  private ColorImage originalImage;

  /**
   * Construct an object of the graphical user interface controller.
   *
   * @param model      an image processing model object
   * @param in         the input stream for the controller
   * @param out        the output stream for the controller
   * @param imageNames a HashMap of images to store all the images generated during processing
   * @param view       the view object that will interact with the user
   */
  public MVCCommandController(BetterImageProcessor model, InputStream in,
                              PrintStream out, HashMap<String, Image> imageNames, IView view) {
    this.model = model;
    this.sc = new Scanner(in);
    this.image_names = imageNames;
    this.view = view;
  }

  /**
   * Loads an image into the view.
   */
  @Override
  public void openFile() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "jpg", "png", "gif", "jpeg", "bmp", "ppm");
    fileChooser.setFileFilter(filter);

    int returnVal = fileChooser.showOpenDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
      try {
        ColorImage colorImage = util.getImage(file.getAbsolutePath());
        currentImage = colorImage;
        image_names.put("Original image", currentImage);
        image_names.put("current_image", currentImage);
        updateImageAndView(colorImage);
      } catch (IOException e) {
        view.showErrorMessage("Error reading image file: " + e.getMessage());
      }
    }
  }

  /**
   * Saves the current image in desired path.
   */
  @Override
  public void saveFile() {
    if (image_names.containsKey("current_key")) {
      view.showErrorMessage("No image to save.");
      return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Specify a file to save");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

    int userSelection = fileChooser.showSaveDialog((Component) view);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      String filePath = fileToSave.getAbsolutePath();
      try {
        String extension = getFileExtension(filePath);
        util.saveImage(image_names.get("current_image"), filePath, extension);
        System.out.println("Saved as: " + filePath);
      } catch (Exception ex) {
        view.showErrorMessage("Error saving the file: " + ex.getMessage());
      }
    }
  }

  /**
   * Performs adjust levels operations.
   *
   * @param levels the values of black, mid and white in a String array
   */
  @Override
  public void adjustLevels(String[] levels) {
    saveCurrentImageState();
    if (image_names.containsKey("current_image") && levels.length == 3) {
      try {
        int black = Integer.parseInt(levels[0].trim());
        int mid = Integer.parseInt(levels[1].trim());
        int white = Integer.parseInt(levels[2].trim());
        if (black >= 0 && black <= 255 && mid >= 0 && mid <= 255 && white >= 0 && white <= 255) {
          if (black <= mid && mid <= white) {
            System.out.println("Adjusting levels...");
            ColorImage adjustedImage = (ColorImage) model.adjustLevels(
                    image_names.get("current_image"),
                    black,
                    mid,
                    white);
            image_names.put("current_image", adjustedImage);
            updateImageAndView(adjustedImage);
          } else {
            view.showErrorMessage("Invalid levels. Black, mid and white values should" +
                    "be in ascending order.");
          }
        } else {
          view.showErrorMessage("Invalid levels. Please enter values between 0 and 255.");
        }
      } catch (NumberFormatException e) {
        view.showErrorMessage("Invalid input for levels adjust.");
      }
    } else {
      view.showErrorMessage("No image loaded to adjust levels.");
    }
  }

  /**
   * Compresses the image to desired percentage.
   *
   * @param compressionPercentage percentage to compress the image by
   */
  @Override
  public void compressImage(String compressionPercentage) {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      try {
        int percentage = Integer.parseInt(compressionPercentage.trim());
        if (percentage >= 0 && percentage <= 100) {
          System.out.println("Compressing Image...");
          ColorImage compressedImage = (ColorImage) model
                  .compress(image_names.get("current_image"), percentage);
          image_names.put("current_image", compressedImage);
          updateImageAndView(compressedImage);
        } else {
          view.showErrorMessage("Invalid compression percentage." +
                  "Please enter a value between 0 and 100.");
        }
      } catch (NumberFormatException e) {
        view.showErrorMessage("Invalid input for compression percentage.");
      }
    } else {
      view.showErrorMessage("No image loaded to apply compression.");
    }
  }

  /**
   * Performs an operation to get only the red components of the current image.
   * It eliminates the other color pixels.
   */
  @Override
  public void performRedComponentOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) (model.splitIntoRGB(image_names.get("current_image"))
              .get("redChannel"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to get component.");
    }
  }

  /**
   * Performs an operation to get only the green components of the current image.
   * It eliminates the other color pixels.
   */
  @Override
  public void performGreenComponentOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) (model.splitIntoRGB(image_names.get("current_image"))
              .get("greenChannel"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to get component.");
    }
  }

  /**
   * Performs an operation to get only the blue components of the current image.
   * It eliminates the other color pixels.
   */
  @Override
  public void performBlueComponentOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) (model.splitIntoRGB(image_names.get("current_image"))
              .get("blueChannel"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to get component.");
    }
  }

  /**
   * Perform a vertical flip operation on the current image.
   */
  @Override
  public void performVerticalFlipOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) model.flipVertically(image_names.get("current_image"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to flip vertically.");
    }
  }

  /**
   * Perform a horizontal flip operation on the current image.
   */
  @Override
  public void performHorizontalFlipOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) model.
              flipHorizontally(image_names.get("current_image"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to flip horizontally.");
    }
  }

  /**
   * Perform a blurring operation on the current image.
   */
  @Override
  public void performBlurOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) model.blur(image_names.get("current_image"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to apply blur.");
    }
  }

  /**
   * Perform a sharpening operation on the current image.
   */
  @Override
  public void performSharpenOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) model.sharpen(image_names.get("current_image"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to apply sharpen.");
    }
  }

  /**
   * Perform a greyscale operation on the current image.
   * It shows the current image in grey values.
   */
  @Override
  public void performGreyscaleOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) model.computeLuma(image_names.get("current_image"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to get luma greyscale component.");
    }
  }

  /**
   * Performs an operation that applies a sepia filter on the current image.
   */
  @Override
  public void performSepiaOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) model.sepia(image_names.get("current_image"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to apply sepia.");
    }
  }

  /**
   * Applies color correction operation on the current image.
   */
  @Override
  public void performColorCorrectionOperation() {
    saveCurrentImageState();
    if (image_names.containsKey("current_image")) {
      ColorImage resultImage = (ColorImage) model
              .colorCorrect(image_names.get("current_image"));
      image_names.put("current_image", resultImage);
      updateImageAndView(resultImage);
    } else {
      view.showErrorMessage("No image loaded to color correct.");
    }
  }

  /**
   * Performs split view on the current image for specified operation.
   * Operation is only applied to the percentage of the image specified.
   *
   * @param operation       the operation to apply
   * @param splitPercentage the percentage of image to apply operation on
   */
  @Override
  public void performSplitViewOperation(String operation, String splitPercentage) {
    if (image_names.containsKey("current_image")) {
      try {
        int percentage = Integer.parseInt(splitPercentage.trim());
        if (percentage >= 0 && percentage <= 100) {
          System.out.println("Splitting Image...");
          saveCurrentImageState();
          ColorImage resultImage = (ColorImage) model
                  .splitView(image_names.get("current_image"), operation, percentage);
          image_names.put("current_image", resultImage);
          updateImageAndView(resultImage);
        } else {
          view.showErrorMessage("Invalid split percentage.");
        }
      } catch (NumberFormatException e) {
        view.showErrorMessage("Invalid input for split percentage.");
      }
    } else {
      view.showErrorMessage("No image loaded to apply split view.");
    }
  }

  /**
   * Performs split view on the current image for levels adjust operation.
   *
   * @param operation       the operation to apply, 'levels-adjust'
   * @param splitPercentage the percentage of image to apply operation on
   * @param levels          the values for black, mid and white in a String array
   */
  @Override
  public void performSplitViewOperation(String operation, String splitPercentage, String[] levels) {
    if (image_names.containsKey("current_image")) {
      try {
        int percentage = Integer.parseInt(splitPercentage.trim());
        if (percentage >= 0 && percentage <= 100) {
          System.out.println("Splitting Image...");
          saveCurrentImageState();
          int black = Integer.parseInt(levels[0]);
          int mid = Integer.parseInt(levels[1]);
          int white = Integer.parseInt(levels[2]);
          ColorImage resultImage = (ColorImage) model.splitView(image_names.get("current_image"),
                  black, mid, white, percentage);
          image_names.put("current_image", resultImage);
          updateImageAndView(resultImage);
        } else {
          view.showErrorMessage("Invalid split percentage.");
        }
      } catch (NumberFormatException e) {
        view.showErrorMessage("Invalid input for split percentage.");
      }
    } else {
      view.showErrorMessage("No image loaded to apply split view.");
    }
  }

  /**
   * Revert the current image to the original image that was loaded in the application.
   */
  @Override
  public void revertToOriginalImage() {
    if (image_names.containsKey("Original image")) {
      currentImage = (ColorImage) image_names.get("Original image");
      originalImage = null; // Reset the original image
      image_names.put("current_image", currentImage);
      updateImageAndView(currentImage);
    } else {
      view.showErrorMessage("No previous image state to revert to.");
    }
  }

  /**
   * A method that gives the control of the program to the GUI controller.
   */
  @Override
  public void executeGUI() {
    view.addOps(this);
    view.makeVisible();
  }

  private void saveCurrentImageState() {
    if (image_names.containsKey("current_image")) {
      image_names.put("orginal_image", image_names.get("current_image"));
    }
  }

  private String getFileExtension(String fileName) {
    int i = fileName.lastIndexOf('.');
    if (i > 0) {
      return fileName.substring(i + 1);
    } else {
      return "png";
    }
  }

  private void updateImageAndView(ColorImage newImage) {
    currentImage = newImage;
    BufferedImage bufferedImage = convertToBufferedImage(newImage);
    ImageIcon imageIcon = new ImageIcon(bufferedImage);
    view.updateDisplayedImage(imageIcon);

    BufferedImage histogramImage = generateHistogram(newImage);
    view.updateHistogram(histogramImage);
  }

  private BufferedImage generateHistogram(ColorImage image) {
    ColorImage histogramImage = (ColorImage) model.histogram(image);
    return convertToBufferedImage(histogramImage);
  }

  private BufferedImage convertToBufferedImage(ColorImage colorImage) {
    int width = colorImage.getWidth();
    int height = colorImage.getHeight();
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        ColorPixel pixel = colorImage.getPixel(row, col);
        int red = (int) AbstractPixel.validateColorValue(pixel.getRed());
        int green = (int) AbstractPixel.validateColorValue(pixel.getGreen());
        int blue = (int) AbstractPixel.validateColorValue(pixel.getBlue());
        Color newColor = new Color(red, green, blue);
        bufferedImage.setRGB(col, row, newColor.getRGB());
      }
    }
    return bufferedImage;
  }
}
