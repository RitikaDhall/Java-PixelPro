package controller;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.ImageIcon;

import model.BetterImageProcessor;
import model.Image;
import view.IView;

import static org.junit.Assert.assertEquals;

/**
 * Test class for GUI Controller.
 */
public class MVCCommandControllerTest {

  private StringBuilder log;
  private MockModel model;
  private MockView view;

  class MockView implements IView {

    private String command;

    public MockView(StringBuilder innerLog, String command) {
      log = innerLog;
      this.command = command;
    }

    @Override
    public void addOps(IGraphicsController operations) {

      if (Objects.equals(command, "blur")) {
        operations.performBlurOperation();
      } else if (Objects.equals(command, "red component")) {
        operations.performRedComponentOperation();
      } else if (Objects.equals(command, "green component")) {
        operations.performGreenComponentOperation();
      } else if (Objects.equals(command, "blue component")) {
        operations.performBlueComponentOperation();
      } else if (Objects.equals(command, "horizontal flip")) {
        operations.performHorizontalFlipOperation();
      } else if (Objects.equals(command, "vertical flip")) {
        operations.performVerticalFlipOperation();
      } else if (Objects.equals(command, "sharpen")) {
        operations.performSharpenOperation();
      } else if (Objects.equals(command, "sepia")) {
        operations.performSepiaOperation();
      } else if (Objects.equals(command, "greyscale")) {
        operations.performGreyscaleOperation();
      } else if (Objects.equals(command, "color-correct")) {
        operations.performColorCorrectionOperation();
      } else if (Objects.equals(command, "compress")) {
        operations.compressImage("50");
      } else if (Objects.equals(command, "levels-adjust")) {
        String[] levels = {"10", "20", "30"};
        operations.adjustLevels(levels);
      } else if (Objects.equals(command, "blur split")) {
        operations.performSplitViewOperation("blur", "50");
      } else {
        log.append("Invalid command.");
      }
    }

    @Override
    public void makeVisible() {
      return;
    }

    @Override
    public void showErrorMessage(String error) {
      return;
    }

    @Override
    public void updateDisplayedImage(ImageIcon image) {
      return;
    }

    @Override
    public void updateHistogram(BufferedImage histogramImage) {
      return;
    }
  }

  class MockModel implements BetterImageProcessor {

    public MockModel(StringBuilder innerlog) {
      log = innerlog;
    }

    @Override
    public Image compress(Image image, int percentage) {
      log.append("compress called.");
      return image;
    }

    @Override
    public Image histogram(Image image) {
      log.append("histogram called.");
      return image;
    }

    @Override
    public Image colorCorrect(Image image) {
      log.append("colorCorrect called.");
      return image;
    }

    @Override
    public Image adjustLevels(Image image, int black, int mid, int white) {
      log.append("adjustLevels called.");
      return image;
    }

    @Override
    public Image splitView(Image image, String operation, int percentage) {
      log.append("splitView called.");
      return image;
    }

    @Override
    public Image splitView(Image image, int black, int mid, int white, int percentage) {
      log.append("splitView for adjustLevels called.");
      return image;
    }

    @Override
    public HashMap<String, Image> splitIntoRGB(Image image) {
      log.append("splitIntoRGB called.");
      HashMap<String, Image> images = new HashMap<>();
      images.put("dummy", image);
      return images;
    }

    @Override
    public Image flipHorizontally(Image image) {
      log.append("flipHorizontally called.");
      return image;
    }

    @Override
    public Image flipVertically(Image image) {
      log.append("flipVertically called.");
      return image;
    }

    @Override
    public Image grayscale(Image image) {
      log.append("grayscale called.");
      return image;
    }

    @Override
    public Image brightenOrDarken(Image image, double factor) {
      log.append("brightenOrDarken called.");
      return image;
    }

    @Override
    public Image combineImages(Image redChannel, Image greenChannel, Image blueChannel) {
      log.append("combineImages called.");
      return redChannel;
    }

    @Override
    public Image blur(Image image) {
      log.append("blur called.");
      return image;
    }

    @Override
    public Image sharpen(Image image) {
      log.append("sharpen called.");
      return image;
    }

    @Override
    public Image sepia(Image image) {
      log.append("sepia called.");
      return image;
    }

    @Override
    public Image computeValue(Image image) {
      log.append("computeValue called.");
      return image;
    }

    @Override
    public Image computeIntensity(Image image) {
      log.append("computeIntensity called.");
      return image;
    }

    @Override
    public Image computeLuma(Image image) {
      log.append("computeLuma called.");
      return image;
    }
  }

  @Test
  public void testBlur() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "blur");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("blur called.histogram called.", log.toString());
  }

  @Test
  public void testRedComponent() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "red component");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("splitIntoRGB called.histogram called.", log.toString());
  }

  @Test
  public void testGreenComponent() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "green component");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("splitIntoRGB called.histogram called.", log.toString());
  }

  @Test
  public void testBlueComponent() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "blue component");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("splitIntoRGB called.histogram called.", log.toString());
  }

  @Test
  public void testHorizontalFlip() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "horizontal flip");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("flipHorizontally called.histogram called.", log.toString());
  }

  @Test
  public void testVerticalFlip() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "vertical flip");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("flipVertically called.histogram called.", log.toString());
  }

  @Test
  public void testSharpen() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "sharpen");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("sharpen called.histogram called.", log.toString());
  }

  @Test
  public void testSepia() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "sepia");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("sepia called.histogram called.", log.toString());
  }

  @Test
  public void testGreyscale() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "greyscale");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("grayscale called.histogram called.", log.toString());
  }

  @Test
  public void testColorCorrect() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "color-correct");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("colorCorrect called.histogram called.", log.toString());
  }

  @Test
  public void testCompress() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "compress");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("compress called.histogram called.", log.toString());
  }

  @Test
  public void testLevelsAdjust() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "levels-adjust");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("adjustLevels called.histogram called.", log.toString());
  }

  @Test
  public void testSplit() throws IOException {
    log = new StringBuilder();
    IView view = new MockView(log, "blur split");
    BetterImageProcessor model = new MockModel(log);
    HashMap<String, Image> imageNames = new HashMap<>();
    ImageUtil util = new ImageUtil();
    Image myImage = util.getImage("/Users/ritikadhall/Desktop/" +
            "Image-Manipulation-and-Enhancement/res/fox.jpg");
    imageNames.put("Original image", myImage);
    imageNames.put("current_image", myImage);
    IGraphicsController controller = new MVCCommandController(model, System.in,
            System.out, imageNames, view);
    controller.executeGUI();
    assertEquals("splitView called.histogram called.", log.toString());
  }
}