package JFXGrid.javafx;

import JFXGrid.core.Axis;
import JFXGrid.core.GridStyler;
import JFXGrid.data.JFXDataset;
import JFXGrid.plugin.Plugin;
import JFXGrid.util.ResizableCanvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The JFXGrid is the javafx-compatible chart/imaging object, designed for high-performance
 * heatmap image viewing, primarily for video-playback purposes of raw sensor data output.
 */
public class JFXGrid extends Pane {
    private final ResizableCanvas canvas;
    private final ArrayList<Axis> axes = new ArrayList<>();
    private final ArrayList<Plugin> plugins = new ArrayList<>();
    private Optional<WritableImage> imageOptional = Optional.empty();
    private JFXDataset dataset;
    private GridStyler gridStyler;


    public JFXGrid() {
        super();
        getStyleClass().add("grid-chart");
        gridStyler = new GridStyler();
        canvas = new ResizableCanvas();
    }

    public void setDataset(JFXDataset newDataset) {
        this.dataset = dataset;
        update();
    }

    public JFXDataset getDataset() {
        return dataset;
    }

    public void update() {
        axes.forEach(Axis::update);
        plugins.forEach(Plugin::update);
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

    public ResizableCanvas getCanvas() {
        return canvas;
    }

    public Optional<WritableImage> getImageOptional() {
        return imageOptional;
    }

    public void setImageOptional(Optional<WritableImage> imageOptional) {
        this.imageOptional = imageOptional;
    }
}
