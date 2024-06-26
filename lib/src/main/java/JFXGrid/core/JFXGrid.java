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
import JFXGrid.events.JFXProcessManager;
import JFXGrid.events.TickListener;
import JFXGrid.plugin.Plugin;
import JFXGrid.renderer.GridRenderer;
import JFXGrid.util.GridStyler;
import JFXGrid.util.ResizableCanvas;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The JFXGrid is the javafx-compatible chart/imaging object, designed for high-performance
 * heatmap image viewing, primarily for video-playback purposes of raw sensor data output.
 *
 * @author Aram Aprahamian (Github: @aram-ap)
 */
public class JFXGrid extends GridFormatPane implements TickListener {
    private static ArrayList<JFXGrid> gridInstances = new ArrayList<>();

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

    //Marks whether or not the aspect ratio of the grid is free to change
    private boolean keepAspect = true;

    private BooleanProperty isDirty = new SimpleBooleanProperty();

    /**
     * Default constructor for JFXGrid class.
     * Adds the "jfx-grid" css style class.
     */
    public JFXGrid() {
        //Calls the GridFormatPane class and initializes it with this as its center node
        TickListener.init(this);

        getStyleClass().add("jfx-grid");
        gridStyler = new GridStyler();
        canvas = new ResizableCanvas();
        gridRenderer = new GridRenderer(this);
        gridRenderer.render();

        //For simplicity sake, we pre-initialize the axes
        var xAxis = new Axis(Axis.Align.Up);
        var yAxis = new Axis(Axis.Align.Left);
        var zAxis = new JFXColorBar(this, Axis.Align.Right);

        axes.addAll(List.of(xAxis, yAxis, zAxis));
        gridInstances.add(this);

        arrange();

        //We add this as insurance that the clock is turned on.
        // JFXClock is a singleton, so it doesn't matter if multiple JFXGrid objects are created.
        JFXClock.get().start();
    }

    /**
     * Sets the specific dataset used in this grid
     * @param newDataset The dataset, created by JFXDatasetFactory, to add to this grid
     */
    public void setData(JFXDataset newDataset) {
        this.dataset = newDataset;
    }

    /**
     * Returns the current Dataset being used
     * @return
     */
    public JFXDataset getData() {
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
        setDirty();
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
        setDirty();
    }

    public ResizableCanvas getCanvas() {
        return canvas;
    }

    /**
     * Gets the GridStyler associated with this grid. Use this to access cosmetic changes.
     * @return
     */
    public GridStyler getStylizer() {
        return gridStyler;
    }

    /**
     * @param aspect True ensures the length:width ratio won't change. False enables free moving/stretching.
     */
    public void setKeepAspect(boolean aspect) {
        this.keepAspect = true;
        setDirty();
    }

    /**
     * @return True if aspect ratio is locked, False if not.
     */
    public boolean getKeepAspect() {
        return keepAspect;
    }

    /**
     * Gets the current FPS count located in the renderer.
     * @return
     */
    public float getRendererFPS() {
        return gridRenderer.getFPS();
    }

    /**
     * Arranges the axes and grid into their respective locations.
     */
    private void arrange() {
        addNode(canvas, Axis.Align.Center);
        for(Axis axis : axes) {
            addNode(axis, axis.getLabelAlignment());
        }
        setDirty();
    }

    /**
     * Queues the grid and axes for a new update
     */
    private void setDirty() {
        gridRenderer.setDirty(true);
        for(Axis axis : axes) {
            axis.getRenderer().setDirty(true);
        }
    }

    /**
     * Called at each render cycle.
     * @param clock the JFXClock calling the tick
     */
    @Override
    public void update(JFXClock clock) {
        gridRenderer.render();
    }

    /**
     * Called at a fixed rate given by the JFXClock. Defaults to 100 hz
     * @param clock
     */
    @Override
    public void updateFixed(JFXClock clock) {
        checkForResize();
    }

    /**
     * Used for resizing the grid and axes when the overall shape of the JFXGrid changes.
     */
    private void checkForResize() {
        var newWidth = this.getWidth();
        var newHeight = this.getHeight();

        if(this.getWidth() != canvas.getWidth() || this.getHeight() != canvas.getHeight()) {
            if(keepAspect) {
                var size = Math.min(newWidth-30, newHeight-30);
                canvas.resize(size, size);
            } else {
                canvas.resize(newWidth-30, newHeight-30);
            }
            setDirty();
        }
    }

    /**
     * Shuts down all running background processes.
     */
    public static void shutdown() {
        JFXClock.get().setRunning(false);
        JFXProcessManager.end();
    }
}
