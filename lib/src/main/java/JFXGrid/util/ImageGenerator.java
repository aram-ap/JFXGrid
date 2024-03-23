package JFXGrid.util;

import JFXGrid.core.Colorizer;
import org.ojalgo.matrix.MatrixR032;

import java.nio.IntBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ImageGenerator {
    private static boolean isThreaded = false;

    public static IntBuffer initImageGeneratorThread(final double width, final double height, MatrixR032 matrix, Colorizer colorizer) {
        return null;
    }
    /**
     * A threaded image processor for converting a matrix into an image
     * @param width Width in pixels
     * @param height Height in pixels
     * @param matrix Matrix to create image with
     * @param theme ColorTheme for parsing data to colors
     */
    public static Future<IntBuffer> getBufferedArgbAsync(final double width, final double height, MatrixR032 matrix, Colorizer theme) {
        return CompletableFuture.supplyAsync(() -> getBufferedARGB(width, height, matrix, theme));
    }

    /**
     * A threaded image processor for converting a matrix into an image
     * @param width Width in pixels
     * @param height Height in pixels
     * @param matrix Matrix to create image with
     * @param theme ColorTheme for parsing data to colors
     */
    public static IntBuffer getBufferedARGB(final double width, final double height, MatrixR032 matrix, Colorizer theme) {
        final IntBuffer buffer = IntBuffer.allocate((int) (width * height));
        final int[] pixels = buffer.array();

        AtomicBoolean processIsComplete = new AtomicBoolean(false);
        if(isThreaded) {
            return initImageGeneratorThread(width, height, matrix, theme);
        } else {
            final double numRows = matrix.getRowDim();
            final double numCols = matrix.getColDim();
            for(int y = 0; y < height; y++) {
                int pointY = (int)(y / height * numRows);

                for(int x = 0; x < width; x++) {
                    int pointX = (int)(x / width * numCols);
                    double val = matrix.get(pointY, pointX);
                    pixels[(int) ((x % width) + (y * width))] = theme.getNearestARGBColor(val);
                }
            }
        }

        return buffer;
    }
}
