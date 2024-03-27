package JFXGrid.core;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import java.util.ArrayList;
import java.util.Comparator;

public enum Style {
    DEFAULT (
            1d,
            stopOf(0, Color.BLACK),
            stopOf(0.1, Color.DARKBLUE),
            stopOf(0.2, Color.MEDIUMSLATEBLUE),
            stopOf(0.3, Color.CYAN),
            stopOf( 0.4, Color.GREENYELLOW),
            stopOf( 0.65, Color.YELLOW),
            stopOf(0.8, Color.ORANGE),
            stopOf(0.9, Color.RED),
            stopOf(1, Color.DARKRED)
    ),

    JET (
            1d,
            stopOf(0, Color.DARKBLUE),
            stopOf(0.2d, Color.MEDIUMSLATEBLUE),
            stopOf(0.65d, Color.YELLOW),
            stopOf(1d, Color.DARKRED)
    ),

    GRAYSCALE (
            1d,
            stopOf(0, Color.BLACK),
            stopOf(0.5, Color.GREY),
            stopOf(1, Color.WHITE)
    ),
    DUOTONE (
            1d,
            stopOf(0, Color.SLATEBLUE),
            stopOf(1, Color.CORAL)
    ),
    MONOTONE (
            1d,
            stopOf(0, Color.BLUE),
            stopOf(1, Color.WHITE)
    ),
    DEFAULT_REVERSED (
            1d,
            stopOf(1, Color.BLACK),
            stopOf(0.9, Color.DARKBLUE),
            stopOf(0.8, Color.MEDIUMSLATEBLUE),
            stopOf(0.65, Color.CYAN),
            stopOf( 0.4, Color.GREENYELLOW),
            stopOf( 0.3, Color.YELLOW),
            stopOf(0.2, Color.ORANGE),
            stopOf(0.1, Color.RED),
            stopOf(0, Color.DARKRED)
    ),
    JET_REVERSED (
            1d,
            stopOf(1d, Color.DARKBLUE),
            stopOf(0.65d, Color.MEDIUMSLATEBLUE),
            stopOf(0.2d, Color.YELLOW),
            stopOf(0d, Color.DARKRED)
    ),
    GRAYSCALE_REVERSED (
            1d,
            stopOf(1, Color.BLACK),
            stopOf(0.5, Color.GREY),
            stopOf(0, Color.WHITE)
    ),
    CUSTOM (1d, DEFAULT.getStops().toArray(new Stop[0]));
    private final ArrayList<Stop> stops;
    private double brightness;

    Style(double brightness, Stop... inputStops) {
        this.brightness = brightness;
        stops = new ArrayList<>();
        setColors(inputStops);
    }

    public final ArrayList<Stop> getStops() {
        return stops;
    }

    private void setColors(Stop... customColors) {
        stops.clear();
        for(Stop aStop : customColors) {
            stops.add(aStop);
        }
        stops.sort(Comparator.comparingDouble(Stop::getOffset));
    }

    public static Style makeCustom(Stop... stops) {
        Style custom = Style.CUSTOM;
        custom.setColors(stops);
        return custom;
    }

    public static Stop stopOf(double offset, Color color) {
        return new Stop(offset, color);
    }

}
