package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

import model.BetterColorImageProcessor;
import model.BetterImageProcessor;
import model.Image;

import static org.junit.Assert.assertEquals;

/**
 * Test class for AdvancedController controller.
 */
public class AdvancedControllerTest {

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

  @Test(expected = IllegalArgumentException.class)
  public void testWrongPercentageException() {
    String command =
            "load /Users/ritikadhall/Desktop/Image-Manipulation-and-"
                    + "Enhancement-main/res/fox.jpg f\n" +
                    "compress 110 f fc ";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    AdvancedController controller = new AdvancedController(model, in, out, image_names);

    controller.processCommand();
    assertEquals("Enter command:\n" +
            "Enter command:\n" +
            "Percentage should be between 0 to 100.", bytes.toString());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testBMWException() {
    String command =
            "load /Users/ritikadhall/Desktop/Image-Manipulation-and-" +
                    "Enhancement-main/res/fox.jpg f\n" +
                    "levels-adjust f fla 100 25 150";
    setInputStream(command);
    BetterImageProcessor model = new BetterColorImageProcessor();
    HashMap<String, Image> image_names = new HashMap<>();
    AdvancedController controller = new AdvancedController(model, in, out, image_names);
    Scanner sc = new Scanner(in);
    controller.processCommand();
    assertEquals("The values for black, mid and white should be in "
            + "ascending order.", bytes.toString());
  }
}