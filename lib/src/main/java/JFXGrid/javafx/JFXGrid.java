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
