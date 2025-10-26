package controller;

/**
 * Interface defining operations for GUI application controllers.
 */
public interface IGraphicsController {

  /**
   * A method that gives the control of the program to the GUI controller.
   */
  void executeGUI();

  /**
   * Load an image into the view from a file.
   */
  void openFile();

  /**
   * Save the current image in a file.
   */
  void saveFile();

  /**
   * Performs the levels adjust operation using black, mid and white values.
   *
   * @param levels the values for black, mid and white in a String array
   */
  void adjustLevels(String[] levels);

  /**
   * Performs an operation to get only the red components of the current image.
   * It eliminates the other color pixels.
   */
  void performRedComponentOperation();

  /**
   * Performs an operation to get only the green components of the current image.
   * It eliminates the other color pixels.
   */
  void performGreenComponentOperation();

  /**
   * Performs an operation to get only the blue components of the current image.
   * It eliminates the other color pixels.
   */
  void performBlueComponentOperation();

  /**
   * Perform a vertical flip operation on the current image.
   */
  void performVerticalFlipOperation();

  /**
   * Perform a horizontal flip operation on the current image.
   */
  void performHorizontalFlipOperation();

  /**
   * Perform a blurring operation on the current image.
   */
  void performBlurOperation();

  /**
   * Perform a sharpening operation on the current image.
   */
  void performSharpenOperation();

  /**
   * Perform a greyscale operation on the current image.
   * It shows the current image in grey values.
   */
  void performGreyscaleOperation();

  /**
   * Performs an operation that applies a sepia filter on the current image.
   */
  void performSepiaOperation();

  /**
   * Applies color correction operation on the current image.
   */
  void performColorCorrectionOperation();

  /**
   * Revert the current image to the original image that was loaded in the application.
   */
  void revertToOriginalImage();

  /**
   * Compress the current image by a desired percentage.
   *
   * @param compressionPercentage the percentage by which we want to compress the image
   */
  void compressImage(String compressionPercentage);

  /**
   * Performs split view on the current image for specified operation.
   * Operation is only applied to the percentage of the image specified.
   *
   * @param operation       the operation to apply
   * @param splitPercentage the percentage of image to apply operation on
   */
  void performSplitViewOperation(String operation, String splitPercentage);

  /**
   * Performs split view on the current image for levels adjust operation.
   *
   * @param operation       the operation to apply, 'levels-adjust'
   * @param splitPercentage the percentage of image to apply operation on
   * @param levels          the values for black, mid and white in a String array
   */
  void performSplitViewOperation(String operation, String splitPercentage, String[] levels);
}