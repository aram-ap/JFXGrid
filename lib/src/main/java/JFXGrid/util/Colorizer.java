package JFXGrid.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

import java.util.ArrayList;

/**
 * The colorizer is a utility tool which handles value-to-color IO, used by ImageGenerator.
 * The colorizer holds a set of colors used to create the color gradients.
 */
public class Colorizer {
    private int[] aRGBColors;
    private int numColorGradations = 255 * 255 * 255;
    private final ArrayList<Stop> stops;
    private double brightness = 1.0;

    public Colorizer() {
        stops = new ArrayList<>() ;
    }

    /**
     * {@code getColorFromValue} takes a double value between {@code 0} and {@code 1} and returns the relative
     * color to that value. The color returned is between the min, mid, and maximum point colors.
     *
     *
     * @return Color object relative to the {@code value} parameter
     */
    public final Color getColorFromValue(double value) {
        if(value > 1) return stops.get(stops.size()-1).getColor();
        else if (value < 0) return stops.get(0).getColor();

        Color returnColor = Color.BLACK;

        for (int i = stops.size() - 1; i >= 0; i--) {
            if (stops.get(i).getOffset() <= value) {
                return interpolateFromTwoColors(stops.get(i + 1).getColor(),
                        stops.get(i).getColor(),
                        brightness,
                        (value - stops.get(i).getOffset()) / (stops.get(i + 1).getOffset() - stops.get(i).getOffset()));
            }
        }
        return returnColor;
    }

    /**
     * {@code interpolateFromTwoColors} takes two colors and a singular value and linearly maps the value between
     * the two colors.
     *
     * @param highColor the color representing {@code 1}
     * @param lowColor  the color representing {@code 0}
     * @param value     the value between {@code 0} and {@code 1}
     * @return the mapped color based on the input parameter
     */
    public static Color interpolateFromTwoColors(Color highColor, Color lowColor, double brightness, double value) {
        if (value > 1.0 || value < 0.0)
            throw new IllegalArgumentException("Value must be between 0 and 1, got " + value + " instead!");

        double red = highColor.getRed() * value + lowColor.getRed() * (1 - value);
        double green = highColor.getGreen() * value + lowColor.getGreen() * (1 - value);
        double blue = highColor.getBlue() * value + lowColor.getBlue() * (1 - value);
        Color color = new Color(
                (red) * brightness,
                (green) * brightness,
                (blue) * brightness,
                1
        );

        return color;
    }

    /**
     * This processes the different color values and maps it onto a list, prevents extra processing and memory usage with
     * the ColorTheme getColorFromValue() function (expensive)
     *
     * @return ARGB color values
     */
    public final int[] processARGBVals(int gradValues) {
        var aRGBColors = new int[gradValues];
        double refVal = 0;
        for (int colorIndex = 0; colorIndex < gradValues; colorIndex++) {
            Color fxColor = getColorFromValue(refVal);

            int alpha = 255;
            int red = (int) (fxColor.getRed() * 255);
            int green = (int) (fxColor.getGreen() * 255);
            int blue = (int) (fxColor.getBlue() * 255);

            aRGBColors[colorIndex] = alpha << 24 | red << 16 | green << 8 | blue;

            refVal += 1.0 / gradValues;
        }
        return aRGBColors;
    }

    /**
     * @param val Value between 0-1
     * @return aRGB color value
     */
    public final int getNearestARGBColor(final double val) {
        if (aRGBColors == null || aRGBColors.length == 0 || val > 1 || val < 0) return 0;
        return aRGBColors[(int) ((aRGBColors.length - 1) * val)];
    }

    public final void setStyleColors(Style style) {
        if(style == null) {
            return;
        }

        stops.clear();
        stops.addAll(style.getStops());
        aRGBColors = processARGBVals(numColorGradations);
    }

    /**
     * @return Number of unique colors in a specific gradient and style
     */
    public final int getNumColorGradations() {
        return numColorGradations;
    }

    /**
     * Num Color Gradations is the number of color shades in a specific Style
     * Default is 16.7M colors == 255 * 255 * 255
     * @param numColorGradations Number of unique colors to set
     */
    public final void setNumColorGradations(int numColorGradations) {
        if(numColorGradations < 0) {
            throw new IllegalArgumentException("Number of color gradations cannot be < 0!");
        }

        this.numColorGradations = numColorGradations;
        this.aRGBColors = processARGBVals(numColorGradations);
    }
}
