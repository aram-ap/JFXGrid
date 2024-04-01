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

import org.ojalgo.matrix.MatrixR032;

import java.util.Collection;

/**
 * A data chunk is a collection of MatrixR032 frames with a set size that can be stepped
 * through
 */
public class DataChunk {
    private MatrixR032[] frames;
    private int currentFrame = -1;
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
    protected DataChunk(int capacity, int uid) {
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
    public MatrixR032 getCurrentFrame() {
        if(currentFrame < 0) {
            currentFrame = 0;
        }

        return frames[currentFrame];
    }

    /**
     * @return iterates the current position by one. No action if current position is at the end;
     */
    public MatrixR032 stepForward() {
        if(currentFrame == capacity - 1) {
            return null;
        }

        return frames[++currentFrame];
    }

    /**
     * Sets the current frame # to 0
     * @return the first frame of the chunk
     */
    public MatrixR032 setFrameFront() {
        currentFrame = 0;
        if(frames == null) {
            return null;
        }

        return frames[currentFrame];
    }

    /**
     * Sets the current frame # to the last frame
     * @return the last frame of the chunk
     */
    public MatrixR032 setFrameLast() {
        if(frames == null) {
            return null;
        }
        currentFrame = frames.length - 1;
        return frames[currentFrame];
    }

    /**
     * @return iterates the current position back by one. No action if current position is 0
     */
    public MatrixR032 stepBack() {
        if(currentFrame <= 0) {
            return null;
        }

        return frames[--currentFrame];
    }

    /**
     * @return returns true if no elements are in the chunk
     */
    public boolean isEmpty() {
        return capacity == 0;
    }

    /**
     * @return returns true if the current frame # is not at the end
     */
    public boolean hasNext() {
        return currentFrame != capacity-1;
    }

    /**
     * @return true if the current frame # is not 0
     */
    public boolean hasPrev() {
        return currentFrame != 0;
    }

    /**
     * @return the current index of the chunk
     */
    public int getIndex() {
        return currentFrame;
    }

    /**
     * @return the amount of frames in the chunk
     */
    public int size() {
        return numItems;
    }

    /**
     * @return The list of frames in this chunk
     */
    public MatrixR032[] toList() {
        return frames;
    }
}
