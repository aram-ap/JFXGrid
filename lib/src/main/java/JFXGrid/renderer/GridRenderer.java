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
package JFXGrid.renderer;

import JFXGrid.core.JFXHeatmap;
import JFXGrid.events.JFXProcessManager;
import JFXGrid.plugin.Plugin;
import JFXGrid.util.ImageGenerator;
import JFXGrid.util.ResizableCanvas;
import javafx.application.Platform;
import javafx.scene.image.WritableImage;


/**
 * The GridRenderer is responsible for drawing and displaying all visual elements of the heatmap. This includes
 * grid lines, the heatmap itself, background, and borders.
 *
 * @author Aram Aprahamian
 */
public class GridRenderer implements Renderer {
    private final JFXHeatmap jfxGrid;

    //This is utilized to prevent over processing, this class will only render if there was something that changed requiring an render.
    protected boolean isDirty = true;

    public GridRenderer(final JFXHeatmap jfxGrid) {
        this.jfxGrid = jfxGrid;
    }

    protected ResizableCanvas getCanvas() {
        return jfxGrid.getCanvas();
    }

    /**
     * Draws horizontal grid lines
     */
    protected void drawHorLines() {
        var canvas = getCanvas();
        var gc = canvas.getGraphicsContext2D();
        var dataset = jfxGrid.getDataset();
        var rows = dataset.getNumRows();

        for (int i = 0; i <= rows; i++) {
            double yVal = (canvas.getHeight() / (double) rows) * i;
            gc.strokeLine(0, yVal, canvas.getWidth(), yVal);
        }
    }

    /**
     * Draws vertical grid lines
     */
    protected void drawVerLines() {
        var canvas = getCanvas();
        var gc = canvas.getGraphicsContext2D();

        var dataset = jfxGrid.getDataset();
        var columns = dataset.getNumColumns();

        for (int i = 0; i <= columns; i++) {
            double xVal = (canvas.getWidth() / (double) columns) * i;
            gc.strokeLine(xVal, 0, xVal, canvas.getHeight());
        }
    }

    /**
     * Draws the next image onto the heatmap canvas
     */
    protected void drawImage(WritableImage image) {
        if(image == null) {
            return;
        }

        if (jfxGrid.getImageOptional().isEmpty()) return;
        JFXProcessManager.addFXTask(() -> {
            getCanvas().getGraphicsContext2D().setImageSmoothing(false);
            getCanvas().getGraphicsContext2D().drawImage(image, 0, 0, getCanvas().getWidth(), getCanvas().getHeight());
        });
    }

    /**
     * Usually unseen, but draws a background rectangle
     */
    protected void drawBackground() {
        var gc = getCanvas().getGraphicsContext2D();
        gc.fillRect(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
        gc.strokeRect(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
    }

    /**
     * The 'dirty' variable essentially denotes whether or not the renderer needs updating. We use this so we don't flood
     * the JFXProcessManager with useless runnables.
     * @param dirty
     */
    @Override
    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }

    /**
     * Forces a re-render of all visual components.
     */
    public void render() {
        if(isDirty) {
            JFXProcessManager.addFXTask(() -> {
                WritableImage finalImage = jfxGrid.getImageOptional().get();
                drawBackground();
                drawImage(finalImage);
                drawHorLines();
                drawVerLines();
            });
            isDirty = false;
        }
    }
}
