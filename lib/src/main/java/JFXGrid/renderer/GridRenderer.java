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

import JFXGrid.core.JFXGrid;
import JFXGrid.events.JFXProcessManager;
import JFXGrid.util.ImageGenerator;
import JFXGrid.util.ResizableCanvas;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.nio.IntBuffer;


/**
 * The GridRenderer is responsible for drawing and displaying all visual elements of the heatmap. This includes
 * grid lines, the heatmap itself, background, and borders.
 *
 * @author Aram Aprahamian
 */
public class GridRenderer implements Renderer {
    private JFXGrid jfxGrid;

    //This is utilized to prevent over processing, this class will only render if there was something that changed requiring an render.
    protected boolean isDirty = true;

    private long lastFrameNano = System.nanoTime();
    private long lastFrameDelta = 0;


    public GridRenderer(final JFXGrid jfxGrid) {
        this.jfxGrid = jfxGrid;
    }

    protected ResizableCanvas getCanvas() {
        return jfxGrid.getCanvas();
    }

    /**
     * Draws horizontal grid lines
     */
    protected void drawHorLines() {
        int rows;
        var dataset = jfxGrid.getData();
        if(dataset == null) {
            rows = 1;
        } else {
            rows = dataset.getNumRows();
        }

        var canvas = getCanvas();
        var gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1);
        gc.setStroke(Color.WHITESMOKE);

        for (int i = 0; i <= rows; i++) {
            double yVal = (canvas.getHeight() / (double) rows) * i;
            gc.strokeLine(0, yVal, canvas.getWidth(), yVal);
        }
    }

    /**
     * Draws vertical grid lines
     */
    protected void drawVerLines() {
        int cols;
        var dataset = jfxGrid.getData();
        if(dataset == null) {
            cols = 1;
        } else {
            cols = dataset.getNumColumns();
        }

        var canvas = getCanvas();
        var gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1);
        gc.setStroke(Color.WHITESMOKE);

        for (int i = 0; i <= cols; i++) {
            double xVal = (canvas.getWidth() / (double) cols) * i;
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

        getCanvas().getGraphicsContext2D().setImageSmoothing(false);
        getCanvas().getGraphicsContext2D().drawImage(image, 0, 0, getCanvas().getWidth(), getCanvas().getHeight());

    }

    /**
     * Usually unseen, but draws a background rectangle
     */
    protected void drawBackground() {
        var gc = getCanvas().getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1);
        gc.fillRect(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
        gc.strokeRect(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
    }

    /**
     * Calculates the instantaneous framerate using the change of time between frames.
     * @return FPS
     */
    public float getFPS() {
        if(lastFrameDelta == 0) {
            return 0;
        }

        return (float) 1_000_000_000 / lastFrameDelta;
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
     * This is the contained runnable process that is given to the JavaFX thread upon a render call
     */
    private final Runnable renderRunnable = () -> {
        drawBackground();

        if(jfxGrid.getData() != null) {
            int rows, cols;
            double[] matrix = jfxGrid.getData().get();
            rows = jfxGrid.getData().getNumRows();
            cols = jfxGrid.getData().getNumColumns();

            if(matrix.length == 0) {
                return;
            }

            IntBuffer buf = ImageGenerator.getBufferedARGB(rows, cols, matrix, jfxGrid.getStylizer().getColorizer());
            PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(
                    cols, rows, buf, PixelFormat.getIntArgbPreInstance()
            );

            final WritableImage image = new WritableImage(pixelBuffer);

            drawImage(image);
//            pixelBuffer.updateBuffer((val) -> null);
        }

        if(jfxGrid.getStylizer().showLinesEnabled()) {
            drawHorLines();
            drawVerLines();
        }

        var timeNano = System.nanoTime();
        lastFrameDelta = timeNano-lastFrameNano;
        lastFrameNano = timeNano;
    };

    /**
     * Forces a re-render of all visual components.
     */
    @Override
    public void render() {
        if(isDirty) {
            //WritableImage is a JavaFX class and throws a fit when trying to make a writable image outside of the fx thread
            //We add this whole rendering process as an JavaFX task
            JFXProcessManager.addFXTask(renderRunnable);
            isDirty = false;
        }
    }

}
