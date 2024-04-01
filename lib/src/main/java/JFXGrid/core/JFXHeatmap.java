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
package JFXGrid.core;

import JFXGrid.data.JFXDataset;
import JFXGrid.events.JFXClock;
import JFXGrid.events.TickListener;
import JFXGrid.plugin.Plugin;
import JFXGrid.renderer.GridRenderer;
import JFXGrid.util.GridStyler;
import JFXGrid.util.ResizableCanvas;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The JFXHeatmap is the javafx-compatible chart/imaging object, designed for high-performance
 * heatmap image viewing, primarily for video-playback purposes of raw sensor data output.
 *
 * @author aram-ap
 */
public class JFXHeatmap extends GridFormatPane implements TickListener {
    //The canvas for which the grid will be displayed on
    private final ResizableCanvas canvas;

    //The set of axes, will not have any added unless you specifically add axes
    private final ArrayList<Axis> axes = new ArrayList<>();

    //The set of plugins
    private final ArrayList<Plugin> plugins = new ArrayList<>();

    //Contains all the code for drawing the grid itself
    private final GridRenderer gridRenderer;

    //Responsible for specific style configurations like colors, default line sizes, chart sizes, tick marks, etc...
    private final GridStyler gridStyler;
    //The current dataset displayed on the grid
    private JFXDataset dataset;

    /**
     * Default constructor for JFXHeatmap class.
     * Adds the "jfx-grid" css style class.
     */
    public JFXHeatmap() {
        //Calls the GridFormatPane class and initializes it with this as its center node
        init(this);

        TickListener.init(this);

        getStyleClass().add("jfx-grid");
        gridStyler = new GridStyler();
        canvas = new ResizableCanvas();
        gridRenderer = new GridRenderer(this);
    }

    /**
     * Sets the specific dataset used in this grid
     * @param newDataset The dataset, created by JFXDatasetFactory, to add to this grid
     */
    public void setDataset(JFXDataset newDataset) {
        this.dataset = dataset;
    }

    public JFXDataset getDataset() {
        return dataset;
    }

    /**
     * @return list of plugins
     */
    public ArrayList<Plugin> getPlugins() {
        return plugins;
    }

    /**
     * @return list of axes
     */
    public ArrayList<Axis> getAxes() {
        return axes;
    }

    /**
     * Adds a plugin and initializes it
     * @param plugin The plugin to add
     */
    public void addPlugin(Plugin plugin) {
        if(plugin == null)
            return;

        plugin.init(this);
        plugins.add(plugin);
    }

    /**
     * Adds a collection of plugins and initializes them.
     * @param plugins Set of plugins to add
     */
    public void addPlugins(Plugin... plugins) {
        if(plugins == null) {
            return;
        }

        for(var plug : plugins) {
            if(plug == null)
                continue;

            plug.init(this);
            this.plugins.add(plug);
        }
    }

    /**
     * Adds an axis and initializes it. It WILL NOT add the axis if an axis of its type already exists.
     * @param axis
     */
    public void addAxis(Axis axis) {
        if(axis == null) {
            return;
        }

        axis.setParent(this);
        axes.add(axis);
    }

    public ResizableCanvas getCanvas() {
        return canvas;
    }

    public GridStyler getGridStyler() {
        return gridStyler;
    }

    public Optional<WritableImage> getImageOptional() {
        return Optional.empty();
    }

    /**
     * Called at each update cycle.
     *
     * @param clock the JFXClock calling the tick
     */
    @Override
    public void update(JFXClock clock) {

    }
}
