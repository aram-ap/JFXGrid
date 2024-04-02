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

import org.apache.commons.lang3.ArrayUtils;
import org.ojalgo.matrix.MatrixR032;

import java.nio.IntBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility class for processing matrices into images
 * @author aram-ap
 */
public abstract class ImageGenerator {

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
    public static Future<IntBuffer> getBufferedARGBFuture(final double width, final double height, MatrixR032 matrix, Colorizer theme) {
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

        return buffer;
    }
}
