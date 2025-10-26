## Instructions

Utilize the following commands to interact with the application. Each command is accompanied by a brief description and an example for clarity.

1. **Loading the Image**: `load filepath image_name`.
    - **Description**: Load an image from a specified file path and assign it a name within the application.
    - **Example**: Load `fox.jpg` and name it 'fox': `load res/fox.jpg fox`.

2. **Compressing the Image**: `compress percentage imagename destimagename`.
    - **Description**: Reduce the size of an image by a specified percentage.
    - **Example**: Compress the 'fox' image by 70%: `compress 70 fox fox_compressed`.

3. **Histogram of the Image**: `histogram image_name dest_name`.
    - **Description**: Generate histograms for the Red, Green, and Blue (RGB) components of an image.
    - **Example**: Create RGB histograms for 'fox': `histogram fox fox_histogram`.

4. **Color Correct an Image**: `color-correct image_name image_dest_name`.
    - **Description**: Perform color correction on an image by aligning its histogram peaks.
    - **Example**: Color correct 'fox': `color-correct fox fox_color_corrected`.

5. **Adjust Levels of an Image**: `levels-adjust image_name dest_name black mid white`.
    - **Description**: Adjust the black, mid-tone, and white levels of an image.
    - **Example**: Adjust levels of 'fox': `levels-adjust fox fox_level_adjusted 5 10 15`.

6. **Brightening the Image**: `brighten value image_name dest_name`.
    - **Description**: Increase the brightness of an image by a specified value.
    - **Example**: Brighten 'fox' by 10 units: `brighten 10 fox fox-brighter`.

7. **Flipping the Image Vertically**: `vertical-flip image_name dest_name`.
    - **Description**: Flip an image vertically.
    - **Example**: Vertically flip 'fox': `vertical-flip fox fox-vertical`.

8. **Flipping the Image Horizontally**: `horizontal-flip image_name dest_name`.
    - **Description**: Flip an image horizontally.
    - **Example**: Horizontally flip the vertically flipped 'fox': `horizontal-flip fox-vertical fox-vertical-horizontal`.

9. **Convert to Greyscale using Value Component**: `value-component image_name dest_name`.
    - **Description**: Convert an image to greyscale using its value component.
    - **Example**: Convert 'fox' to greyscale: `value-component fox fox-greyscale`.

10. **Saving the Image**: `save filepath image_name`.
    - **Description**: Save the modified image to a specified file path.
    - **Examples**:
        - Save the brightened 'fox' image: `save res/fox-brighter.jpg fox-brighter`.
        - Save the greyscale 'fox' image: `save res/fox-gs.jpg fox-greyscale`.

11. **Overwrite the Image**: `load new_filepath image_name`.
    - **Description**: Overwrite an existing image reference with a new image from a different file path.
    - **Example**: Overwrite 'fox' with a new image: `load res/upper.jpg fox`.

12. **RGB Split for Red Tint**: `rgb-split image_name red_dest green_dest blue_dest`.
    - **Description**: Split an image into its RGB components.
    - **Example**: Split 'fox' into RGB components: `rgb-split fox fox-red fox-green fox-blue`.

13. **Brighten Only the Red Component**: `brighten value red_image_name red_dest_name`.
    - **Description**: Brighten only the red component of an image.
    - **Example**: Brighten the red component of 'fox' by 50 units: `brighten 50 fox-red fox-red`.

14. **Combine RGB with Brightened Red for Red Tint**: `rgb-combine dest_name red_image green_image blue_image`.
    - **Description**: Combine RGB components into a single image, using a modified red component for a tint effect.
    - **Example**: Create a red-tinted 'fox' image: `rgb-combine fox-red-tint fox-red fox-green fox-blue`.

15. **Save the Red Tinted Image**: `save filepath tinted_image_name`.
    - **Description**: Save the red-tinted image to a specified file path.
    - **Example**: Save the red-tinted 'fox' image: `save res/fox-red-tint.jpg fox-red-tint`.
