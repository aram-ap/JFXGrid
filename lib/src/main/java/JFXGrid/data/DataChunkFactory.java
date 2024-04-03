package JFXGrid.data;

import org.ojalgo.matrix.MatrixR032;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Creates Data Chunks, adding and subtracting frames to those chunks
 */
public class DataChunkFactory extends DataChunk {
    private ArrayList<MatrixR032> frames;
    private int uid;

    /**
     * Default constructor for DataChunkFactory
     * @param frames predetermined frames to add;
     */
    public DataChunkFactory(MatrixR032[] frames, int uid) {
        super();
        this.frames = new ArrayList<MatrixR032>(List.of(frames));
        this.uid = uid;
    }

    /**
     * Default constructor for DataChunkFactory.
     */
    public DataChunkFactory(int uid) {
        super();
        this.frames = new ArrayList<>();
    }

    /**
     * @return the processed DataChunk version
     */
    public DataChunk build() {
        return new DataChunk(frames.toArray(new double[0][][]), uid);
    }

    /**
     * Sets the frame at the data chunk
     * @param index the index to set the matrix
     * @param matrix the matrix to add at the index
     * @throws IllegalArgumentException for given index above the capacity or below 0 or when matrix is null
     */
    public void set(int index, MatrixR032 matrix) {
        if(matrix == null) {
            throw new IllegalArgumentException("Matrix cannot be null!");
        } else if (index < 0) {
            throw new IllegalArgumentException("Index cannot be less than 0!");
        }

        frames.set(index, matrix);
    }

    /**
     * Adds a matrix into the datachunk
     * @param matrix the matrix to add
     * @return returns this
     */
    public DataChunkFactory add(MatrixR032 matrix) {
        if(matrix != null) {
            frames.add(matrix);
        }
        return this;
    }

    /**
     * @param matrices array of Matrices to add to the data chunk
     * @return returns this
     */
    public DataChunkFactory addAll(MatrixR032[] matrices) {
        if (matrices != null){
            addAll(List.of(matrices));
        }

        return this;
    }

    /**
     * @param matrices collection of Matrices to add to the data chunk
     * @return returns this
     */
    public DataChunkFactory addAll(Collection<MatrixR032> matrices) {
        if(matrices != null) {
            frames.addAll(matrices);
        }

        return this;
    }
}
