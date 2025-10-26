package controller;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import model.BetterImageProcessor;
import model.Image;

/**
 * Extension of TextController to handle the image operations.
 */
public class AdvancedController extends TextController {

  /**
   * Constructs a TextController with a given image processing model and an input stream.
   *
   * @param model The image processing model to be used.
   * @param in    The input stream from which commands are read.
   */
  public AdvancedController(BetterImageProcessor model, InputStream in,
                            PrintStream out, HashMap<String, Image> imageNames) {
    super(model, in, out, imageNames);
  }

  /**
   * Executes commands read from the provided scanner.
   *
   * @param sc Scanner from which commands are read.
   */
  @Override
  public void executeCommands(Scanner sc) {
    while (!quit) {
      System.out.println("Enter command:");
      if (!sc.hasNext()) {
        break;
      }
      String command = sc.nextLine();
      executeAdv(command);
    }
  }

  private void executeAdv(String command) {
    String image_name;
    String dest_image_name;
    Image newImage;
    int percentage;

    // Split the command into array
    String[] tokens = command.split("\\s+");

    switch (tokens[0]) {

      case "compress":
        percentage = Integer.parseInt(tokens[1]);
        image_name = tokens[2];
        dest_image_name = tokens[3];
        if (percentage > 100 || percentage < 0) {
          System.out.println("Percentage should be between 0 to 100.");
        }
        newImage = model.compress(image_names.get(image_name), percentage);
        image_names.put(dest_image_name, newImage);
        break;

      case "histogram":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.histogram(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;

      case "color-correct":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        if (tokens.length == 5 && Objects.equals(tokens[3], "split")) {
          percentage = Integer.parseInt(tokens[4]);
          if (percentage > 100 || percentage < 0) {
            System.out.println("Percentage for split cannot be less than 0 or grater than 100.");
            break;
          }
          newImage = model.splitView(image_names.get(image_name), "color-correct", percentage);
        } else {
          newImage = model.colorCorrect(image_names.get(image_name));
        }
        image_names.put(dest_image_name, newImage);
        break;

      case "levels-adjust":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        int black = Integer.parseInt(tokens[3]);
        int mid = Integer.parseInt(tokens[4]);
        int white = Integer.parseInt(tokens[5]);

        if (black > mid || mid > white || black > white) {
          System.out.println("The values for black, mid and white should be in ascending order.");
        }
        if (black < 0 || black > 255 || mid < 0 || mid > 255 || white < 0 || white > 255) {
          System.out.println("The values for black, mid and white should be between 0 and 255.");
        }

        if (tokens.length == 8 && Objects.equals(tokens[6], "split")) {
          percentage = Integer.parseInt(tokens[7]);
          if (percentage > 100 || percentage < 0) {
            System.out.println("Percentage for split cannot be less than 0 or grater than 100.");
            break;
          }
          newImage = model.splitView(image_names.get(image_name), black, mid, white, percentage);
        } else {
          newImage = model.adjustLevels(image_names.get(image_name), black, mid, white);
        }

        image_names.put(dest_image_name, newImage);
        break;

      case "blur":
        // same as text controller except for an if condition
        image_name = tokens[1];
        dest_image_name = tokens[2];
        if (tokens.length == 5 && Objects.equals(tokens[3], "split")) {
          percentage = Integer.parseInt(tokens[4]);
          if (percentage > 100 || percentage < 0) {
            System.out.println("Percentage for split cannot be less than 0 or grater than 100.");
            break;
          }
          newImage = model.splitView(image_names.get(image_name), "blur", percentage);
        } else {
          newImage = model.blur(image_names.get(image_name));
        }
        image_names.put(dest_image_name, newImage);
        break;

      case "sharpen":
        image_name = tokens[1];
        dest_image_name = tokens[2];

        if (tokens.length == 5 && Objects.equals(tokens[3], "split")) {
          percentage = Integer.parseInt(tokens[4]);
          if (percentage > 100 || percentage < 0) {
            System.out.println("Percentage for split cannot be less than 0 or grater than 100.");
            break;
          }
          newImage = model.splitView(image_names.get(image_name), "sharpen", percentage);
        } else {
          newImage = model.sharpen(image_names.get(image_name));
        }
        image_names.put(dest_image_name, newImage);
        break;

      case "sepia":
        image_name = tokens[1];
        dest_image_name = tokens[2];

        if (tokens.length == 5 && Objects.equals(tokens[3], "split")) {
          percentage = Integer.parseInt(tokens[4]);
          if (percentage > 100 || percentage < 0) {
            System.out.println("Percentage for split cannot be less than 0 or grater than 100.");
            break;
          }
          newImage = model.splitView(image_names.get(image_name), "sepia", percentage);
        } else {
          newImage = model.sepia(image_names.get(image_name));
        }
        image_names.put(dest_image_name, newImage);
        break;

      case "luma-component":
        image_name = tokens[1];
        dest_image_name = tokens[2];

        if (tokens.length == 5 && Objects.equals(tokens[3], "split")) {
          percentage = Integer.parseInt(tokens[4]);
          if (percentage > 100 || percentage < 0) {
            System.out.println("Percentage for split cannot be less than 0 or grater than 100.");
            break;
          }
          newImage = model.splitView(image_names.get(image_name), "grayscale", percentage);
        } else {
          newImage = model.computeLuma(image_names.get(image_name));
        }
        image_names.put(dest_image_name, newImage);
        break;
      case "q":
        quit = true;
        break;
      default:
        // If the command is not recognized here, let the base class handle it
        super.execute(command);
    }
  }
}