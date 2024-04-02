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
package JFXGrid.util;

import JFXGrid.core.JFXHeatmap;
import JFXGrid.events.JFXProcessManager;
import JFXGrid.util.ImageGenerator;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.nio.IntBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Contains the code for obtaining a new heatmap image asynchronously
 *
 * @author aram-ap
 */
public class GridImageProcessor {
    private WritableImage image;
    private JFXHeatmap grid;
    private double width, height;

    public GridImageProcessor(JFXHeatmap grid, int width, int height) {
        this.grid = grid;
        this.width = width;
        this.height = height;
    }

    /**
     * @return the last processed image
     */
    public WritableImage getImage() {
        return image;
    }

    /**
     * Asynchronously generates a new image int buffer and pushes it for processing into a WritableImage
     * Uses the JFXProcessManager add task function
     */
    public void getFutureImage() {
        final Future<IntBuffer> futureBuffer = ImageGenerator.getBufferedARGBFuture(
                width, height, grid.getDataset().get(), grid.getGridStyler().getColorizer());

        try {
            IntBuffer buffer = futureBuffer.get();
            Runnable imageCall = () -> {
                while (true) {
                    if(futureBuffer.isDone())
                        break;

                    final PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(
                            (int) width, (int) height, buffer, PixelFormat.getIntArgbPreInstance()
                    );
                    final WritableImage image = new WritableImage(pixelBuffer);
                    pixelBuffer.updateBuffer(b -> null);
                    this.image = image;
                }
            };
            JFXProcessManager.addTask(imageCall);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
