package JFXGrid.data;

import java.nio.ByteBuffer;
import java.nio.InvalidMarkException;

/**
 * A buffer for holding Dataset frames
 * @author aram-ap
 */
public class DataBuffer{
    //A buffer's capacity is the number of elements it contains. The capacity of a buffer is never negative and never changes.
    private int capacity;
    //A buffer's limit is the index of the first element that should not be read or written. A buffer's limit is never negative and is never greater than its capacity.
    private int limit;
    //A buffer's position is the index of the next element to be read or written. A buffer's position is never negative and is never greater than its limit.
    private int position;
    private int mark;

    public DataBuffer(int capacity) {
        if(capacity > 0) {
            this.capacity = capacity;
        } else {
            this.capacity = 10000;
        }
    }

    /**
     * Sets this buffer's position.  If the mark is defined and larger than the
     * new position then it is discarded.
     *
     * @param newPosition The new position value; must be non-negative
     *                    and no larger than the current limit
     * @return This buffer
     * @throws IllegalArgumentException If the preconditions on {@code newPosition} do not hold
     */
    public DataBuffer position(int newPosition) {
        this.position = position;
        return this;
    }

    /**
     * Sets this buffer's limit.  If the position is larger than the new limit
     * then it is set to the new limit.  If the mark is defined and larger than
     * the new limit then it is discarded.
     *
     * @param newLimit The new limit value; must be non-negative
     *                 and no larger than this buffer's capacity
     * @return This buffer
     * @throws IllegalArgumentException If the preconditions on {@code newLimit} do not hold
     */
    public DataBuffer limit(int newLimit) {
        //TODO: ADD PRECONDITIONS
        this.limit = newLimit;
        return this;
    }

    /**
     * Sets this buffer's mark at its position.
     *
     * @return This buffer
     */
    public DataBuffer mark() {
        mark = position;
        return this;
    }

    /**
     * Resets this buffer's position to the previously-marked position.
     *
     * <p> Invoking this method neither changes nor discards the mark's
     * value. </p>
     *
     * @return This buffer
     * @throws InvalidMarkException If the mark has not been set
     */
    public DataBuffer reset() {
        return this;
    }

    /**
     * Clears this buffer.  The position is set to zero, the limit is set to
     * the capacity, and the mark is discarded.
     *
     * <p> Invoke this method before using a sequence of channel-read or
     * <i>put</i> operations to fill this buffer.  For example:
     *
     * <blockquote><pre>
     * buf.clear();     // Prepare buffer for reading
     * in.read(buf);    // Read data</pre></blockquote>
     *
     * <p> This method does not actually erase the data in the buffer, but it
     * is named as if it did because it will most often be used in situations
     * in which that might as well be the case. </p>
     *
     * @return This buffer
     */
    public DataBuffer clear() {
        return this;
    }

    /**
     * Flips this buffer.  The limit is set to the current position and then
     * the position is set to zero.  If the mark is defined then it is
     * discarded.
     *
     * <p> After a sequence of channel-read or <i>put</i> operations, invoke
     * this method to prepare for a sequence of channel-write or relative
     * <i>get</i> operations.  For example:
     *
     * <blockquote><pre>
     * buf.put(magic);    // Prepend header
     * in.read(buf);      // Read data into rest of buffer
     * buf.flip();        // Flip buffer
     * out.write(buf);    // Write header + data to channel</pre></blockquote>
     *
     * <p> This method is often used in conjunction with the {@link
     * ByteBuffer#compact compact} method when transferring data from
     * one place to another.  </p>
     *
     * @return This buffer
     */
    public DataBuffer flip() {
        return this;
    }

    /**
     * Rewinds this buffer.  The position is set to zero and the mark is
     * discarded.
     *
     * <p> Invoke this method before a sequence of channel-write or <i>get</i>
     * operations, assuming that the limit has already been set
     * appropriately.  For example:
     *
     * <blockquote><pre>
     * out.write(buf);    // Write remaining data
     * buf.rewind();      // Rewind buffer
     * buf.get(array);    // Copy data into array</pre></blockquote>
     *
     * @return This buffer
     */
    public DataBuffer rewind() {
        return this;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean hasArray() {
        return false;
    }

    public Object array() {
        return null;
    }

    public int arrayOffset() {
        return 0;
    }

    public boolean isDirect() {
        return false;
    }

    public DataBuffer slice() {
        return null;
    }

    public DataBuffer slice(int index, int length) {
        return null;
    }

    public DataBuffer duplicate() {
        return null;
    }
}
