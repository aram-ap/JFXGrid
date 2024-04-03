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
package JFXGrid.data;

/**
 * The default Dataset implementation for JFXGrid. Once created, this data cannot change and is supposed to be for better memory management/utilization.
 *
 * @author aram-ap
 */
public class JFXDataset implements Data {
    private DataChunk currentChunk;
    private int numFrames;
    private final int numRows;
    private final int numColumns;

    /**
     * default constructor for JFXDataset
     * @param rows number of rows in the grid
     * @param columns number of columns in the grid
     */
    protected JFXDataset(int rows, int columns) {
        this.numColumns = columns;
        this.numRows = rows;
    }

    /**
     * Sets the chunk of data
     * @param dataChunk chunk of data
     */
    public final void setCurrentChunk(DataChunk dataChunk) {
        currentChunk = dataChunk;
        numFrames = dataChunk.size();
    }

    /**
     * @return The number of frames contained in the dataset
     */
    public final int getNumFrames() {
        return numFrames;
    }

    public final int getNumRows() {
        return numRows;
    }

    public final int getNumColumns() {
        return numColumns;
    }

    /**
     * Returns a list of MatrixR032
     * @return
     */
    public double[][] getCache() {
        if(currentChunk == null) {
            return new double[0][];
        }
        return currentChunk.toList();
    }

    /**
     * Gets the current frame in the chunk
     * @return MatrixR023
     */
    @Override
    public double[] get() {
        if(currentChunk == null) {
            return null;
        }

        return currentChunk.get();
    }

    /**
     * Returns the number of items in the chunk
     * @return
     */
    public final int size() {
        return numFrames;
    }

    /**
     * Removes references to data chunks and calls System.gc()
     */
    @Override
    public void clearData() {
        currentChunk.clearData();
        currentChunk = null;
        numFrames = 0;

        System.gc();
    }

    /**
     * Steps data chunk by one frame
     */
    @Override
    public double[] stepForward() {
        if(currentChunk == null) {
            return null;
        }

        return currentChunk.stepForward();
    }

    /**
     * Steps data chunk back by one frame
     */
    @Override
    public double[] stepBack() {
        if(currentChunk == null) {
            return null;
        }

        return currentChunk.stepBack();
    }

    /**
     * Goes to the inserted frame number
     *
     * @param frameNum The frame to go to. Note, values are [1, length]. Inclusive of 1.
     * @return The matrix at the specific frame number. Null if out of bounds.
     */
    @Override
    public double[] gotoFrame(int frameNum) {
        if(frameNum < 1 || frameNum > numFrames) {
            return null;
        }

        return currentChunk.gotoFrame(frameNum);
    }

    /**
     * @return
     */
    @Override
    public int getFrameNum() {
        if(currentChunk == null) {
            return 0;
        }
        return currentChunk.getIndex()+1;
    }
}
