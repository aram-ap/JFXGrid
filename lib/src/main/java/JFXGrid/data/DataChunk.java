package JFXGrid.data;

import com.sun.javafx.collections.UnmodifiableListSet;
import org.ojalgo.matrix.MatrixR032;

import java.util.Collection;
import java.util.List;

/**
 * A data chunk is a collection of MatrixR032 frames with a set size that can be stepped
 * through
 */
public class DataChunk {
    private MatrixR032[] frames;
    private int current = -1;
    private int capacity;
    private int numItems;

    //UID is used for frame tracking, will usually be assigned the start frame # for sorting the timeline.
    public final int uid;

    protected DataChunk() {
        uid = 0;
    }

    /**
     * Default constructor for DataChunk
     * @param capacity the pre-determined capacity of the chunk
     */
    public DataChunk(int capacity, int uid) {
        if(capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be less than 0!");
        }

        this.uid = uid;
        this.frames = new MatrixR032[capacity];
        this.capacity = capacity;
    }
    /**
     * Default constructor for DataChunk
     * @param frames the pre-determined frames to add into the chunk
     */
    protected DataChunk(MatrixR032[] frames, int uid) {
        if(frames == null) {
            throw new IllegalArgumentException("Frames cannot be null!");
        }

        this.uid = uid;
        this.frames = frames;
        this.capacity = this.frames.length;
        numItems = this.frames.length;
    }

    /**
     * Default constructor for DataChunk
     * @param frames the pre-determined collection of frames to add into the chunk
     */
    protected DataChunk(Collection<MatrixR032> frames, int uid) {
        if(frames == null)
            throw new IllegalArgumentException("Frames cannot be null!");

        this.uid = uid;
        this.frames = frames.toArray(new MatrixR032[0]);
        this.capacity = this.frames.length;
        numItems = this.frames.length;
    }

    /**
     * @return the frame at the current position
     */
    public MatrixR032 getCurr() {
        if(current < 0) {
            current = 0;
        }

        return frames[current];
    }

    /**
     * @return iterates the current position by one. No action if current position is at the end;
     */
    public MatrixR032 stepForward() {
        if(current == capacity - 1) {
            return null;
        }

        return frames[++current];
    }

    /**
     * @return iterates the current position back by one. No action if current position is 0
     */
    public MatrixR032 stepBack() {
        if(current <= 0) {
            return null;
        }

        return frames[--current];
    }

    /**
     * @return returns true if no elements are in the chunk
     */
    public boolean isEmpty() {
        return capacity == 0;
    }

    /**
     * @return returns true if the current position is not at the end
     */
    public boolean hasNext() {
        return current == capacity-1;
    }

    /**
     * @return the current index of the chunk
     */
    public int getIndex() {
        return current;
    }

    /**
     * @return the amount of frames in the chunk
     */
    public int size() {
        return numItems;
    }

    /**
     * @return The unmodifiable list of frames in this chunk
     */
    public UnmodifiableListSet<MatrixR032> toUnmodifiableList() {
        return new UnmodifiableListSet<>(List.of(frames).subList(0, numItems));
    }
}
