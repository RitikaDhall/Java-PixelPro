package view;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import controller.IGraphicsController;

/**
 * This interface defines the methods used to create a graphical user interface for the
 * application.
 */
public interface IView {

  /**
   * Display the application graphical user interface.
   */
  void makeVisible();

  /**
   * Show a message in case an error is encountered.
   *
   * @param error the error encountered
   */
  void showErrorMessage(String error);

  /**
   * Update the current image displayed in the graphical user interface.
   *
   * @param image the image to be updated in display
   */
  void updateDisplayedImage(ImageIcon image);

  /**
   * Update the current histogram displayed in the graphical user interface.
   *
   * @param histogramImage the histogram to be updated in display
   */
  void updateHistogram(BufferedImage histogramImage);

  /**
   * Add operation listeners to the graphical user interface.
   *
   * @param operations the controller object that has the operations
   */
  void addOps(IGraphicsController operations);
}
