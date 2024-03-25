package JFXGrid.data;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.InvalidMarkException;

/**
 * A buffer for holding Dataset frames
 * @author aram-ap
 */
public class DataBuffer {
    //A buffer's capacity is the number of elements it contains. The capacity of a buffer is never negative and never changes.
    private int capacity;
    //A buffer's limit is the index of the first element that should not be read or written. A buffer's limit is never negative and is never greater than its capacity.
    private int limit;
    private int prevPosition;
    //A buffer's position is the index of the next element to be read or written. A buffer's position is never negative and is never greater than its limit.
    private int position;
    private boolean isReadOnly = false;
    private int mark;

    public DataBuffer(int capacity) {
        if(capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Initial capacity cannot be less than or equal to 0!");
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
        if(newLimit < position) {
            throw new IllegalArgumentException("limit cannot be set before position!");
        } else if (newLimit < 0) {
            throw new IllegalArgumentException("limit cannot be less than 0!");
        }

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
     * <i>getCurr</i> operations.  For example:
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
     * <p> Invoke this method before a sequence of channel-write or <i>getCurr</i>
     * operations, assuming that the limit has already been set
     * appropriately.  For example:
     *
     * <blockquote><pre>
     * out.write(buf);    // Write remaining data
     * buf.rewind();      // Rewind buffer
     * buf.getCurr(array);    // Copy data into array</pre></blockquote>
     *
     * @return This buffer
     */
    public DataBuffer rewind() {
        return this;
    }

    /**
     * Tells whether or not this buffer is read-only.
     *
     * @return {@code true} if, and only if, this buffer is read-only
     */
    public boolean isReadOnly() {
        return false;
    }

    /**
     * Tells whether or not this buffer is backed by an accessible
     * array.
     *
     * <p> If this method returns {@code true} then the {@link #array() array}
     * and {@link #arrayOffset() arrayOffset} methods may safely be invoked.
     * </p>
     *
     * @return {@code true} if, and only if, this buffer
     * is backed by an array and is not read-only
     * @since 1.6
     */
    public boolean hasArray() {
        return false;
    }

    /**
     * Returns the array that backs this
     * buffer&nbsp;&nbsp;<i>(optional operation)</i>.
     *
     * <p> This method is intended to allow array-backed buffers to be
     * passed to native code more efficiently. Concrete subclasses
     * provide more strongly-typed return values for this method.
     *
     * <p> Modifications to this buffer's content will cause the returned
     * array's content to be modified, and vice versa.
     *
     * <p> Invoke the {@link #hasArray hasArray} method before invoking this
     * method in order to ensure that this buffer has an accessible backing
     * array.  </p>
     *
     * @return The array that backs this buffer
     * @throws UnsupportedOperationException If this buffer is not backed by an accessible array
     * @since 1.6
     */
    public Object array() {
        return null;
    }

    /**
     * Returns the offset within this buffer's backing array of the first
     * element of the buffer&nbsp;&nbsp;<i>(optional operation)</i>.
     *
     * <p> If this buffer is backed by an array then buffer position <i>p</i>
     * corresponds to array index <i>p</i>&nbsp;+&nbsp;{@code arrayOffset()}.
     *
     * <p> Invoke the {@link #hasArray hasArray} method before invoking this
     * method in order to ensure that this buffer has an accessible backing
     * array.  </p>
     *
     * @return The offset within this buffer's array
     * of the first element of the buffer
     * @throws UnsupportedOperationException If this buffer is not backed by an accessible array
     * @since 1.6
     */
    public int arrayOffset() {
        return 0;
    }

    /**
     * Tells whether or not this buffer is
     * <a href="ByteBuffer.html#direct"><i>direct</i></a>.
     *
     * @return {@code true} if, and only if, this buffer is direct
     * @since 1.6
     */
    public boolean isDirect() {
        return false;
    }

    /**
     * Creates a new buffer whose content is a shared subsequence of
     * this buffer's content.
     *
     * <p> The content of the new buffer will start at this buffer's current
     * position.  Changes to this buffer's content will be visible in the new
     * buffer, and vice versa; the two buffers' position, limit, and mark
     * values will be independent.
     *
     * <p> The new buffer's position will be zero, its capacity and its limit
     * will be the number of elements remaining in this buffer, its mark will be
     * undefined. The new buffer will be direct if, and only if, this buffer is
     * direct, and it will be read-only if, and only if, this buffer is
     * read-only.  </p>
     *
     * @return The new buffer
     * @since 9
     */
    public Buffer slice() {
        return null;
    }

    /**
     * Creates a new buffer whose content is a shared subsequence of
     * this buffer's content.
     *
     * <p> The content of the new buffer will start at position {@code index}
     * in this buffer, and will contain {@code length} elements. Changes to
     * this buffer's content will be visible in the new buffer, and vice versa;
     * the two buffers' position, limit, and mark values will be independent.
     *
     * <p> The new buffer's position will be zero, its capacity and its limit
     * will be {@code length}, its mark will be undefined. The new buffer will
     * be direct if, and only if, this buffer is direct, and it will be
     * read-only if, and only if, this buffer is read-only.  </p>
     *
     * @param index  The position in this buffer at which the content of the new
     *               buffer will start; must be non-negative and no larger than
     *               {@link #limit() limit()}
     * @param length The number of elements the new buffer will contain; must be
     *               non-negative and no larger than {@code limit() - index}
     * @return The new buffer
     * @throws IndexOutOfBoundsException If {@code index} is negative or greater than {@code limit()},
     *                                   {@code length} is negative, or {@code length > limit() - index}
     * @since 13
     */
    public Buffer slice(int index, int length) {
        return null;
    }

    /**
     * Creates a new buffer that shares this buffer's content.
     *
     * <p> The content of the new buffer will be that of this buffer.  Changes
     * to this buffer's content will be visible in the new buffer, and vice
     * versa; the two buffers' position, limit, and mark values will be
     * independent.
     *
     * <p> The new buffer's capacity, limit, position and mark values will be
     * identical to those of this buffer. The new buffer will be direct if, and
     * only if, this buffer is direct, and it will be read-only if, and only if,
     * this buffer is read-only.  </p>
     *
     * @return The new buffer
     * @since 9
     */
    public Buffer duplicate() {
        return null;
    }
}
