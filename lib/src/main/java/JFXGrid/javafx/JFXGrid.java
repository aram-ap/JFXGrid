package JFXGrid.javafx;

import JFXGrid.core.Axis;
import JFXGrid.core.GridStyler;
import JFXGrid.data.JFXDataset;
import JFXGrid.plugin.Plugin;
import JFXGrid.util.ResizableCanvas;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The JFXGrid is the javafx-compatible chart/imaging object, designed for high-performance
 * heatmap image viewing, primarily for video-playback purposes of raw sensor data output.
 */
public class JFXGrid extends Pane {
    private Optional<WritableImage> image = Optional.empty();
    private final ResizableCanvas canvas;
    private final ArrayList<Axis> axes;
    private final ArrayList<Plugin> plugins;
    private GridStyler gridStyler;


    public JFXGrid() {
        super();
        getStyleClass().add("grid-chart");
        gridStyler = new GridStyler();
        axes = new ArrayList<>();
        plugins = new ArrayList<>();
        canvas = new ResizableCanvas();
    }

    public void update() {

    }

    public ArrayList<Plugin> getPlugins() {
        return plugins;
    }

    public ArrayList<Axis> getAxes() {
        return axes;
    }

    @Override
    public void setHeight(double v) {
        super.setHeight(v);
    }

    @Override
    public void setWidth(double v) {
        super.setWidth(v);
    }
}
