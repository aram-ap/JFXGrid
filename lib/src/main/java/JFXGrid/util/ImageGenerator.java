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

import java.nio.IntBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Utility class for processing matrices into images
 * @author aram-ap
 */
public abstract class ImageGenerator {

    /**
     * A threaded image processor for converting a matrix into an image
     * @param rows number of rows
     * @param cols number of columns
     * @param matrix Matrix to create image with
     * @param theme ColorTheme for parsing data to colors
     */
    public static Future<IntBuffer> getBufferedARGBFuture(final int rows, final int cols, double[] matrix, Colorizer theme) {
        return CompletableFuture.supplyAsync(() -> getBufferedARGB(rows, cols, matrix, theme));
    }

    /**
     * A threaded image processor for converting a matrix into an image
     * @param rows number of rows in the grid
     * @param cols number of columns in the grid
     * @param matrix Matrix to create image with
     * @param theme ColorTheme for parsing data to colors
     */
    public static IntBuffer getBufferedARGB(final int rows, final int cols, final double[] matrix, Colorizer theme) {
        final IntBuffer buffer = IntBuffer.allocate(rows * cols);
        final int[] pixels = buffer.array();

        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                double val = matrix[x * rows + y];
                pixels[(y * cols) + (x % cols)] = theme.getNearestARGBColor(val);
            }
        }

        return buffer;
    }
}
