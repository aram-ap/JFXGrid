//MIT License
//
//Copyright (c) 2024 Aram Aprahamian
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.
package JFXGrid.javafx;

import JFXGrid.core.Axis;
import JFXGrid.core.GridStyler;
import JFXGrid.data.JFXDataset;
import JFXGrid.plugin.Plugin;
import JFXGrid.util.ResizableCanvas;
import com.sun.javafx.collections.UnmodifiableListSet;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The JFXGrid is the javafx-compatible chart/imaging object, designed for high-performance
 * heatmap image viewing, primarily for video-playback purposes of raw sensor data output.
 */
public class JFXGrid extends GridFormatPane {
    private final ResizableCanvas canvas;
    private final ArrayList<Axis> axes = new ArrayList<>();
    private final ArrayList<Plugin> plugins = new ArrayList<>();
    private Optional<WritableImage> imageOptional = Optional.empty();
    private JFXDataset dataset;
    private GridStyler gridStyler;

    public JFXGrid() {
        //Calls the GridFormatPane class and initializes it with this as its center node
        init(this);

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

    public UnmodifiableListSet<Plugin> getPlugins() {
        return new UnmodifiableListSet<>(plugins);
    }

    public UnmodifiableListSet<Axis> getAxes() {
        return new UnmodifiableListSet<>(axes);
    }

    public void addPlugin(Plugin plugin) {
        if(plugin == null)
            return;

        plugin.setParent(this);
        plugins.add(plugin);

        update();
    }

    public void addAxis(Axis axis) {
        if(axis == null) {
            return;
        }

        axis.setParent(this);
        axes.add(axis);

        update();
    }

    public ResizableCanvas getCanvas() {
        return canvas;
    }

    public Optional<WritableImage> getImageOptional() {
        return imageOptional;
    }

    public void setImageOptional(Optional<WritableImage> imageOptional) {
        this.imageOptional = imageOptional;
        update();
    }

    public GridStyler getGridStyler() {
        return gridStyler;
    }

    public void setGridStyler(GridStyler gridStyler) {
        this.gridStyler = gridStyler;
    }
}
