package controller;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import model.BetterColorImageProcessor;
import model.BetterImageProcessor;
import model.Image;
import model.ImageProcessor;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the TextController class.
 */
public class TextControllerTest {

  private InputStream in;
  private PrintStream out;
  private ByteArrayOutputStream bytes;

  private String path = "/Users/ppaudel/Desktop/PDP/Assignment4/images/";

  @Before
  public void setup() {
    bytes = new ByteArrayOutputStream();
    out = new PrintStream(bytes);
  }

  public void setInputStream(String command) {
    this.in = new ByteArrayInputStream(command.getBytes());
  }

  class MockModel implements ImageProcessor {

    private StringBuilder log;

    public MockModel(StringBuilder log) {
      this.log = log;
    }

    @Override
    public HashMap<String, Image> splitIntoRGB(Image image) {
      log.append("rgb split " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image flipHorizontally(Image image) {
      log.append("horizontal flip " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image flipVertically(Image image) {
      log.append("vertical flip " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image grayscale(Image image) {
      log.append("grayscale " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image brightenOrDarken(Image image, double factor) {
      log.append("brighten " + factor + " " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image combineImages(Image redChannel, Image greenChannel, Image blueChannel) {
      log.append("combine red " + redChannel.getHeight() + ", " + redChannel.getWidth() + "\n");
      log.append("combine green " + greenChannel.getHeight() + ", "
              + greenChannel.getWidth() + "\n");
      log.append("combine blue " + blueChannel.getHeight() + ", " + blueChannel.getWidth());
      return null;
    }

    @Override
    public Image blur(Image image) {
      log.append("blur " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image sharpen(Image image) {
      log.append("sharpen " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image sepia(Image image) {
      log.append("sepia " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image computeValue(Image image) {
      log.append("value " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image computeIntensity(Image image) {
      log.append("intensity " + image.getHeight() + ", " + image.getWidth());
      return null;
    }

    @Override
    public Image computeLuma(Image image) {
      log.append("luma " + image.getHeight() + ", " + image.getWidth());
      return null;
    }
  }

  @Test
  public void testLoadImageException() {
    String command = "load abc.pdf abc";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals("File not found.", bytes.toString());
  }

  @Test
  public void testWrongCommandException() {
    String command =
            "unblur /Users/ritikadhall/Desktop/Fall23/PDP/Projects/Assignment4/manhattan.png man";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals("Command not found.Command not found.Command not found.", bytes.toString());
  }

  @Test
  public void testScriptFileException() {
    String command = "run xyz.txt";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals("Script file not found.", bytes.toString());
  }

  @Test
  public void testLoadImage() throws IOException {
    String command = "load " + path + "fox.png fox";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    File imageFile = new File(path + "fox.png");
    BufferedImage image = ImageIO.read(imageFile);
    assertEquals(350, image.getHeight());
    assertEquals(500, image.getWidth());
  }

  @Test
  public void testSaveImage() throws IOException {
    String command1 = "load " + path + "fox.png fox\n" +
            "save " + path + "new_fox.png fox\n" +
            "load " + path + "new_fox.png new_fox";
    setInputStream(command1);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    File imageFile = new File(path + "fox.png");
    BufferedImage image = ImageIO.read(imageFile);
    File newImageFile = new File(path + "new_fox.png");
    BufferedImage newImage = ImageIO.read(newImageFile);
    assertEquals(image.getHeight(), newImage.getHeight());
    assertEquals(image.getWidth(), newImage.getWidth());
  }

  @Test
  public void testScript() {
    String command1 = "run " + path + "script.txt";
    setInputStream(command1);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_flipped"));
    assertEquals(true, image_names.containsKey("fox_flipped_luma"));
  }

  @Test
  public void testBrighten() {
    String command = "load " + path + "fox.png fox\n" +
            "brighten 50 fox fox_brighten\n" +
            "save " + path + "fox_brighten.png fox_brighten";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_brighten"));
  }

  @Test
  public void testHorizontalFlip() {
    String command = "load " + path + "fox.png fox\n" +
            "horizontal-flip fox fox_horizontal";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_horizontal"));
  }

  @Test
  public void testVerticalFlip() {
    String command = "load " + path + "fox.png fox\n" +
            "vertical-flip fox fox_vertical";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_vertical"));
  }

  @Test
  public void testBlur() {
    String command = "load " + path + "fox.png fox\n" +
            "blur fox fox_blur";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_blur"));
  }

  @Test
  public void testSharpen() {
    String command = "load " + path + "fox.png fox\n" +
            "sharpen fox fox_sharpen";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_sharpen"));
  }

  @Test
  public void testSepia() {
    String command = "load " + path + "fox.png fox\n" +
            "sepia fox fox_sepia";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_sepia"));
  }

  @Test
  public void testRedComponent() {
    String command = "load " + path + "fox.png fox\n" +
            "red-component fox fox_red";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_red"));
  }

  @Test
  public void testGreenComponent() {
    String command = "load " + path + "fox.png fox\n" +
            "green-component fox fox_green";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_green"));
  }

  @Test
  public void testBlueComponent() {
    String command = "load " + path + "fox.png fox\n" +
            "blue-component fox fox_blue";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_blue"));
  }

  @Test
  public void testValueComponent() {
    String command = "load " + path + "fox.png fox\n" +
            "value-component fox fox_value";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_value"));
  }

  @Test
  public void testLumaComponent() {
    String command = "load " + path + "fox.png fox\n" +
            "luma-component fox fox_luma";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_luma"));
  }

  @Test
  public void testIntensityComponent() {
    String command = "load " + path + "fox.png fox\n" +
            "intensity-component fox fox_intensity";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_intensity"));
  }

  @Test
  public void testRGBSplit() {
    String command = "load " + path + "fox.png fox\n" +
            "rgb-split fox fox_red fox_green fox_blue";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_red"));
    assertEquals(true, image_names.containsKey("fox_green"));
    assertEquals(true, image_names.containsKey("fox_blue"));
  }

  @Test
  public void testRGBCombine() {
    String command = "load " + path + "fox.png fox\n" +
            "rgb-split fox fox_red fox_green fox_blue\n" +
            "rgb-combine fox_combine fox_red fox_green fox_blue";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    TextController controller = new TextController(model, in, out, image_names);
    controller.processCommand();
    assertEquals(true, image_names.containsKey("fox"));
    assertEquals(true, image_names.containsKey("fox_red"));
    assertEquals(true, image_names.containsKey("fox_green"));
    assertEquals(true, image_names.containsKey("fox_blue"));
    assertEquals(true, image_names.containsKey("fox_combine"));
  }

  //  @Test
  //  public void testMockBlur() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "blur fox fox_blur";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("blur 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockBrighten() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "brighten 50 fox fox_brighten\n" +
  //            "save " + path + "fox_brighten.png fox_brighten";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("brighten 50 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockHorizontalFlip() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "horizontal-flip fox fox_horizontal";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("horizontal flip 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockVerticalFlip() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "vertical-flip fox fox_vertical";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("vertical flip 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockSharpen() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "sharpen fox fox_sharpen";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("sharpen 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockSepia() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "sepia fox fox_sepia";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("sepia 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockValueComponent() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "value-component fox fox_value";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("value 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockLumaComponent() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "luma-component fox fox_luma";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("luma 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockIntensityComponent() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "intensity-component fox fox_intensity";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("intensity 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockRGBSplit() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "rgb-split fox fox_red fox_green fox_blue";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    assertEquals("rgb split 350, 500", log.toString());
  //  }
  //
  //  @Test
  //  public void testMockRGBCombine() {
  //    String command = "load " + path + "fox.png fox\n" +
  //            "rgb-split fox fox_red fox_green fox_blue\n" +
  //            "rgb-combine fox_combine fox_red fox_green fox_blue";
  //    setInputStream(command);
  //    StringBuilder log = new StringBuilder();
  //    ImageProcessor model = new MockModel(log);
  //    HashMap<String, Image> image_names = new HashMap<>();
  //    TextController controller = new TextController(model, in, out, image_names);
  //    controller.processCommand();
  //    String expected =
  //        "rgb split 350, 500combine red 350, 500\ncombine green 350, 500\ncombine blue 350, 500";
  //    assertEquals(expected, log.toString());
  //  }
}