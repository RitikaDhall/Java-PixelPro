package view;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

import controller.IGraphicsController;

/**
 * A class that represents the view for the graphical user interface of the application.
 */
public class ImageGraphicsView extends JFrame implements IView {

  private JPanel mainPanel;
  private JButton fileOpenButton;
  private JButton fileSaveButton;
  private JButton redComponentButton;
  private JButton greenComponentButton;
  private JButton blueComponentButton;
  private JButton horizontalFlipButton;
  private JButton verticalFlipButton;
  private JButton blurButton;
  private JButton sharpenButton;
  private JButton sepiaButton;
  private JButton lumaComponentButton;
  private JButton colorCorrectButton;
  private JButton compressButton;
  private JButton levelsAdjustButton;
  private JButton undoButton;
  private JTextField compressPercentageField;
  private JTextField blackValueField;
  private JTextField midValueField;
  private JTextField whiteValueField;
  private JTextField splitPercentageField;
  private JToggleButton splitToggleButton;
  private JLabel imageLabel;
  private JLabel histogramLabel;
  private String splitPercentage;
  private boolean isPreview;
  private String previewOperation;

  /**
   * Constructor to build a view object for a graphical user interface.
   */
  public ImageGraphicsView() {
    super("Image Manipulation");
    setSize(1500, 1000);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    setupImagePanel();
    setupFilesPanel();
    setupOperationButtonsPanel();
    setupAdjustLevelsPanel();
    setupCompressBoxesPanel();
    setupSplitViewPanel();

    isPreview = false;
  }

  private void setupImagePanel() {
    JPanel imagePanel = new JPanel();
    imagePanel.setBorder(BorderFactory.createTitledBorder("Image and Histogram"));
    GridBagLayout gbl = new GridBagLayout();
    imagePanel.setLayout(gbl);
    GridBagConstraints gbc = new GridBagConstraints();
    mainPanel.add(imagePanel);

    // Setup for image display
    imageLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.8;
    gbc.weighty = 1.0;
    gbc.fill = GridBagConstraints.BOTH;
    imagePanel.add(imageScrollPane, gbc);

    // Setup for histogram display
    histogramLabel = new JLabel();
    JScrollPane histogramScrollPane = new JScrollPane(histogramLabel);
    gbc.gridx = 1;
    gbc.weightx = 0.2;
    imagePanel.add(histogramScrollPane, gbc);
  }

  /**
   * Update the current image displayed in the graphical user interface.
   *
   * @param image the image to be updated in display
   */
  @Override
  public void updateDisplayedImage(ImageIcon image) {
    imageLabel.setIcon(image);
    imageLabel.revalidate();
    imageLabel.repaint();
  }

  /**
   * Update the current histogram displayed in the graphical user interface.
   *
   * @param histogramImage the histogram to be updated in display
   */
  @Override
  public void updateHistogram(BufferedImage histogramImage) {
    histogramLabel.setIcon(new ImageIcon(histogramImage));
    histogramLabel.revalidate();
    histogramLabel.repaint();
  }

  private void setupFilesPanel() {
    JPanel filesPanel = new JPanel();
    filesPanel.setBorder(BorderFactory.createTitledBorder("File"));
    filesPanel.setLayout(new FlowLayout());
    mainPanel.add(filesPanel);

    fileOpenButton = new JButton("Open File");
    filesPanel.add(fileOpenButton);

    fileSaveButton = new JButton("Save File");
    filesPanel.add(fileSaveButton);
  }

  private void setupOperationButtonsPanel() {
    JPanel operationButtonsPanel = new JPanel();
    operationButtonsPanel.setLayout(new GridLayout(2, 5, 20, 10));
    mainPanel.add(operationButtonsPanel);

    redComponentButton = new JButton("Red Component");
    operationButtonsPanel.add(redComponentButton);

    greenComponentButton = new JButton("Green Component");
    operationButtonsPanel.add(greenComponentButton);

    blueComponentButton = new JButton("Blue Component");
    operationButtonsPanel.add(blueComponentButton);

    horizontalFlipButton = new JButton("Horizontal Flip");
    operationButtonsPanel.add(horizontalFlipButton);

    verticalFlipButton = new JButton("Vertical Flip");
    operationButtonsPanel.add(verticalFlipButton);

    blurButton = new JButton("Blur");
    operationButtonsPanel.add(blurButton);

    sharpenButton = new JButton("Sharpen");
    operationButtonsPanel.add(sharpenButton);

    sepiaButton = new JButton("Sepia");
    operationButtonsPanel.add(sepiaButton);

    lumaComponentButton = new JButton("Greyscale");
    operationButtonsPanel.add(lumaComponentButton);

    colorCorrectButton = new JButton("Color Correct");
    operationButtonsPanel.add(colorCorrectButton);
  }

  private void setupAdjustLevelsPanel() {
    JPanel adjustLevelsPanel = new JPanel();
    adjustLevelsPanel.setBorder(BorderFactory.createTitledBorder("Adjust Levels"));
    adjustLevelsPanel.setLayout(new FlowLayout());
    mainPanel.add(adjustLevelsPanel);


    adjustLevelsPanel.add(new JLabel("Black value"));
    blackValueField = new JTextField(10);
    adjustLevelsPanel.add(blackValueField);

    adjustLevelsPanel.add(Box.createHorizontalStrut(15));

    adjustLevelsPanel.add(new JLabel("Mid value"));
    midValueField = new JTextField(10);
    adjustLevelsPanel.add(midValueField);

    adjustLevelsPanel.add(Box.createHorizontalStrut(15));

    adjustLevelsPanel.add(new JLabel("White value"));
    whiteValueField = new JTextField(10);
    adjustLevelsPanel.add(whiteValueField);

    adjustLevelsPanel.add(Box.createHorizontalStrut(15));

    levelsAdjustButton = new JButton("Levels Adjust");
    adjustLevelsPanel.add(levelsAdjustButton);
  }

  private void setupCompressBoxesPanel() {
    JPanel compressPanel = new JPanel();
    compressPanel.setBorder(BorderFactory.createTitledBorder("Compress"));
    compressPanel.setLayout(new FlowLayout());
    mainPanel.add(compressPanel);


    compressPanel.add(new JLabel("Percentage"));
    compressPercentageField = new JTextField(10);
    compressPanel.add(compressPercentageField);

    compressPanel.add(Box.createHorizontalStrut(15));

    compressButton = new JButton("Compress");
    compressPanel.add(compressButton);
  }

  private void setupSplitViewPanel() {
    JPanel splitViewPanel = new JPanel();
    splitViewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
    splitViewPanel.setLayout(new FlowLayout());
    mainPanel.add(splitViewPanel);

    splitViewPanel.add(new JLabel("Percentage"));
    splitPercentageField = new JTextField(10);
    splitViewPanel.add(splitPercentageField);

    splitViewPanel.add(Box.createHorizontalStrut(15));

    splitViewPanel.add(Box.createHorizontalStrut(15));

    splitToggleButton = new JToggleButton("Preview", false);
    splitViewPanel.add(splitToggleButton);

    undoButton = new JButton("Undo");
    splitViewPanel.add(undoButton);
  }

  /**
   * Add operation listeners to the graphical user interface.
   *
   * @param operations the controller object that has the operations
   */
  @Override
  public void addOps(IGraphicsController operations) {
    fileOpenButton.addActionListener(evt -> operations.openFile());
    fileSaveButton.addActionListener(evt -> {
      if (isPreview) {
        showErrorMessage("Image is in preview mode! Exit preview mode to save image.");
      } else {
        operations.saveFile();
      }
    });

    redComponentButton.addActionListener(evt -> operations.performRedComponentOperation());
    greenComponentButton.addActionListener(evt -> operations.performGreenComponentOperation());
    blueComponentButton.addActionListener(evt -> operations.performBlueComponentOperation());
    horizontalFlipButton.addActionListener(evt -> operations.performHorizontalFlipOperation());
    verticalFlipButton.addActionListener(evt -> operations.performVerticalFlipOperation());

    blurButton.addActionListener(evt -> {
      if (splitToggleButton.isSelected()) {
        previewOperation = "blur";
        operations.performSplitViewOperation(previewOperation, splitPercentage);
        isPreview = true;
      } else {
        operations.performBlurOperation();
      }

    });

    sharpenButton.addActionListener(evt -> {
      if (splitToggleButton.isSelected()) {
        previewOperation = "sharpen";
        operations.performSplitViewOperation(previewOperation, splitPercentage);
        isPreview = true;
      } else {
        operations.performSharpenOperation();
      }
    });

    sepiaButton.addActionListener(evt -> {
      if (splitToggleButton.isSelected()) {
        previewOperation = "sepia";
        operations.performSplitViewOperation(previewOperation, splitPercentage);
        isPreview = true;
      } else {
        operations.performSepiaOperation();
      }
    });

    lumaComponentButton.addActionListener(evt -> {
      if (splitToggleButton.isSelected()) {
        previewOperation = "grayscale";
        operations.performSplitViewOperation(previewOperation, splitPercentage);
        isPreview = true;
      } else {
        operations.performGreyscaleOperation();
      }
    });

    colorCorrectButton.addActionListener(evt -> {
      if (splitToggleButton.isSelected()) {
        previewOperation = "color-correct";
        operations.performSplitViewOperation(previewOperation, splitPercentage);
        isPreview = true;
      } else {
        operations.performColorCorrectionOperation();
      }
    });

    compressButton.addActionListener(evt -> {
      try {
        String compressPercentage = compressPercentageField.getText();
        operations.compressImage(compressPercentage);
      } catch (NumberFormatException e) {
        showErrorMessage("Enter a valid percentage for compression.");
      }
    });

    levelsAdjustButton.addActionListener(evt -> {
      try {
        String[] levels = new String[3];
        levels[0] = blackValueField.getText();
        levels[1] = midValueField.getText();
        levels[2] = whiteValueField.getText();

        if (splitToggleButton.isSelected()) {
          previewOperation = "levels-adjust";
          operations.performSplitViewOperation(previewOperation, splitPercentage, levels);
          isPreview = true;
        }
        operations.adjustLevels(levels);
      } catch (Exception e) {
        showErrorMessage("Enter valid values for adjust levels.");
      }
    });

    splitToggleButton.addActionListener(evt -> {
      if (splitToggleButton.isSelected()) {
        try {
          splitPercentage = splitPercentageField.getText();
        } catch (Exception e) {
          showErrorMessage("Enter valid values for percentage.");
        }
      } else {
        isPreview = false;
        operations.revertToOriginalImage();
      }
    });

    undoButton.addActionListener(evt -> operations.revertToOriginalImage());
  }

  /**
   * Display the application graphical user interface.
   */
  @Override
  public void makeVisible() {
    setVisible(true);
  }

  /**
   * Show a message in case an error is encountered.
   *
   * @param error the error encountered
   */
  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(
            this,
            error,
            "Error",
            JOptionPane.ERROR_MESSAGE);
  }
}
