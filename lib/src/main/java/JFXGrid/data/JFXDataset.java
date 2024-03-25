package JFXGrid.data;

import com.sun.javafx.collections.UnmodifiableListSet;
import org.ojalgo.matrix.MatrixR032;

import java.util.List;

public class JFXDataset implements Data {
    protected DataChunk currentChunk;
    private int numFrames;
    private int numRows;
    private int numColumns;

    protected JFXDataset(int rows, int columns) {
        this.numColumns = columns;
        this.numRows = rows;
    }

    public void setCurrentChunk(DataChunk dataChunk) {
        currentChunk = dataChunk;
        numFrames = dataChunk.size();
    }

    public int getNumFrames() {
        return numFrames;
    }


    public void setNumRows(int rows) {
        if(rows < 0) {
            throw new IllegalArgumentException("Rows cannot be less than 0!");
        }
        this.numRows = rows;
    }

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

    public UnmodifiableListSet<MatrixR032> getUnmodifiableCache() {
        if(currentChunk == null) {
            return new UnmodifiableListSet<>(List.of(new MatrixR032[0]));
        }
        return currentChunk.toUnmodifiableList();
    }

    /**
     * @return
     */
    @Override
    public MatrixR032 get() {
        if(currentChunk == null) {
            return null;
        }
        return currentChunk.getCurr();
    }

    public int size() {
        return numFrames;
    }

    @Override
    public void clearData() {
        currentChunk = null;
        System.gc();
    }
}
