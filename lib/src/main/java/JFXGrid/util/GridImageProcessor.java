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
    public void updateImage() {
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
