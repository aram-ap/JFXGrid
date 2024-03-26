package JFXGrid.data;

import com.sun.javafx.collections.UnmodifiableListSet;
import org.ojalgo.matrix.MatrixR032;

import java.util.List;

public class JFXDataset implements Data {
    private DataChunk currentChunk;
    private int numFrames;
    private int numRows;
    private int numColumns;

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
    public void setCurrentChunk(DataChunk dataChunk) {
        currentChunk = dataChunk;
        numFrames = dataChunk.size();
    }

    /**
     * @return The number of frames contained in the dataset
     */
    public int getNumFrames() {
        return numFrames;
    }


    /**
     * Sets the number of rows
     * @param rows
     */
    public void setNumRows(int rows) {
        if(rows < 0) {
            throw new IllegalArgumentException("Rows cannot be less than 0!");
        }
        this.numRows = rows;
    }

    /**
     * Sets the number of columns
     * @param columns
     */
    public void setNumColumns(int columns) {
        if(columns < 0) {
            throw new IllegalArgumentException("Columns cannot be less than 0!");
        }
        this.numColumns = columns;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    /**
     * Returns a list of MatrixR032
     * @return
     */
    public UnmodifiableListSet<MatrixR032> getUnmodifiableCache() {
        if(currentChunk == null) {
            return new UnmodifiableListSet<>(List.of(new MatrixR032[0]));
        }
        return currentChunk.toUnmodifiableList();
    }

    /**
     * Gets the current frame in the chunk
     * @return MatrixR023
     */
    @Override
    public MatrixR032 get() {
        if(currentChunk == null) {
            return null;
        }
        return currentChunk.getCurr();
    }

    /**
     * Returns the number of items in the chunk
     * @return
     */
    public int size() {
        return numFrames;
    }

    /**
     * Removes references to data chunks and calls System.gc()
     */
    @Override
    public void clearData() {
        currentChunk = null;
        System.gc();
    }

    /**
     * Steps data chunk by one frame
     */
    @Override
    public MatrixR032 stepForward() {
        if(currentChunk == null) {
            return null;
        }

        return currentChunk.stepForward();
    }

    /**
     * Steps data chunk back by one frame
     */
    @Override
    public MatrixR032 stepBack() {
        if(currentChunk == null) {
            return null;
        }

        return currentChunk.stepBack();
    }
}
