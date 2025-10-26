package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import model.BetterImageProcessor;
import model.ColorImage;
import model.Image;

/**
 * This class represents a text-based controller for an image processing application.
 * It reads commands from a given input stream and processes images based on these commands.
 */
public class TextController implements IController {

  protected Scanner sc;
  protected BetterImageProcessor model;
  protected ImageUtil util;
  protected HashMap<String, Image> image_names;
  protected boolean quit;

  /**
   * Constructs a TextController with a given image processing model and an input stream.
   *
   * @param model The image processing model to be used.
   * @param in    The input stream from which commands are read.
   */
  public TextController(BetterImageProcessor model, InputStream in,
                        PrintStream out, HashMap<String, Image> imageNames) {
    this.model = model;
    this.sc = new Scanner(in);
    this.image_names = imageNames;
    util = new ImageUtil();
    quit = false;
  }

  /**
   * Begins the process of reading and executing commands from the input stream.
   */
  @Override
  public void processCommand() {
    executeCommands(this.sc);
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
      execute(command);
    }
  }

  protected void execute(String command) {
    String image_name;
    String dest_image_name;
    String path;
    String extension;
    Image newImage;
    HashMap<String, Image> componentImages;

    // Split the command into array
    String[] tokens = command.split("\\s+");

    switch (tokens[0]) {
      case "load":
        path = tokens[1];
        int lastDotIndex = path.lastIndexOf('.');
        extension = path.substring(lastDotIndex + 1);
        image_name = tokens[2];
        ColorImage image;
        try {
          if (Objects.equals(extension, "ppm")) {
            image = util.readPPM(path);
          } else {
            image = util.getImage(path);
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        image_names.put(image_name, image);
        break;
      case "save":
        path = tokens[1];
        extension = path.split("[.]")[1];
        image_name = tokens[2];
        if (Objects.equals(extension, "ppm")) {
          util.savePPM(image_names.get(image_name), path);
        } else {
          util.saveImage(image_names.get(image_name), path, extension);
        }
        break;
      case "red-component":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        componentImages = model.splitIntoRGB(image_names.get(image_name));
        image_names.put(dest_image_name, componentImages.get("redChannel"));
        break;
      case "green-component":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        componentImages = model.splitIntoRGB(image_names.get(image_name));
        image_names.put(dest_image_name, componentImages.get("greenChannel"));
        break;
      case "blue-component":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        componentImages = model.splitIntoRGB(image_names.get(image_name));
        image_names.put(dest_image_name, componentImages.get("blueChannel"));
        break;
      case "value-component":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.computeValue(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;
      case "luma-component":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.computeLuma(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;
      case "intensity-component":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.computeIntensity(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;
      case "horizontal-flip":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.flipHorizontally(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;
      case "vertical-flip":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.flipVertically(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;
      case "brighten":
        int increment = Integer.parseInt(tokens[1]);
        image_name = tokens[2];
        dest_image_name = tokens[3];
        newImage = model.brightenOrDarken(image_names.get(image_name), increment);
        image_names.put(dest_image_name, newImage);
        break;
      case "rgb-split":
        image_name = tokens[1];
        String dest_image_name_red = tokens[2];
        String dest_image_name_green = tokens[3];
        String dest_image_name_blue = tokens[4];
        componentImages = model.splitIntoRGB(image_names.get(image_name));
        image_names.put(dest_image_name_red, componentImages.get("redChannel"));
        image_names.put(dest_image_name_green, componentImages.get("greenChannel"));
        image_names.put(dest_image_name_blue, componentImages.get("blueChannel"));
        break;
      case "rgb-combine":
        dest_image_name = tokens[1];
        String image_name_red = tokens[2];
        String image_name_green = tokens[3];
        String image_name_blue = tokens[4];
        newImage = model.combineImages(image_names.get(image_name_red),
                image_names.get(image_name_green),
                image_names.get(image_name_blue));
        image_names.put(dest_image_name, newImage);
        break;
      case "blur":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.blur(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;
      case "sharpen":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.sharpen(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;
      case "sepia":
        image_name = tokens[1];
        dest_image_name = tokens[2];
        newImage = model.sepia(image_names.get(image_name));
        image_names.put(dest_image_name, newImage);
        break;

      case "run":
        System.out.println();
        String scriptPath = tokens[1];
        try {
          Scanner fileScanner = new Scanner(new File(scriptPath));
          executeCommands(fileScanner);
          fileScanner.close();
        } catch (FileNotFoundException e) {
          System.out.println("Script file not found: " + scriptPath);
        }
        quit = true;
        break;

      case "q":
        quit = true;
        break;

      default:
        System.out.println("Command not found.");
        break;
    }
  }
}