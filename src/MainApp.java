import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import controller.AdvancedController;
import controller.IController;
import controller.MVCCommandController;
import model.BetterColorImageProcessor;
import model.Image;
import view.ImageGraphicsView;

/**
 * Main application entry point.
 */
public class MainApp {

  /**
   * Entry point for the application.
   * Initializes the model, an image storage, and a controller to process commands.
   *
   * @param args Command-line arguments (not used).
   */
  public static void main(String[] args) {
    // Create the image processing model
    BetterColorImageProcessor model = new BetterColorImageProcessor();

    // Create a map for storing named images
    HashMap<String, Image> imageNames = new HashMap<>();

    ImageGraphicsView view = new ImageGraphicsView();

    // Initialize the MVCCommandController with the model and other necessary components
    if (args.length > 0 && args[0].equals("-text")) {
      IController controller = new AdvancedController(model, System.in, System.out, imageNames);
      controller.processCommand();
    } else if (args.length > 0 && args[0].equals("-file")) {

      IController controller = new AdvancedController(model, System.in, System.out, imageNames);
      String scriptFileName = args[1];
      try {
        Scanner fileScanner = new Scanner(new File(scriptFileName));
        controller.executeCommands(fileScanner);
        fileScanner.close();
      } catch (FileNotFoundException e) {
        System.out.println("Script file not found: " + scriptFileName);
        controller.processCommand();
      }

    } else {
      MVCCommandController controller = new MVCCommandController(model, System.in,
              System.out, imageNames, view);
      controller.executeGUI();
    }
  }
}