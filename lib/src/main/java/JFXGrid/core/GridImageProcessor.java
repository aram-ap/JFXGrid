package JFXGrid.core;

import JFXGrid.events.JFXProcessManager;
import JFXGrid.javafx.JFXGrid;
import JFXGrid.util.ImageGenerator;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.nio.IntBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GridImageProcessor {
    private static final ExecutorService imageThread = Executors.newFixedThreadPool(1);
    private WritableImage image;
    private JFXGrid grid;
    private double width, height;

    public GridImageProcessor(JFXGrid grid, int width, int height) {
        this.grid = grid;
        this.width = width;
        this.height = height;
    }

    public WritableImage getImage() {
        return image;
    }

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
            JFXProcessManager.addTask(imageCall, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
