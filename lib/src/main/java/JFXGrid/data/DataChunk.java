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

import java.util.Arrays;
import java.util.Collection;

/**
 * A data chunk is a collection of MatrixR032 frames with a set size that can be stepped
 * through
 *
 * @author Aram Aprahamian
 */
public class DataChunk implements Data {
    //The collection of frames held in the chunk
    private double[][][] frames;

    //The internal frame pointer that keeps track of the current matrix being shown
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
        this.frames = new double[capacity][][];
        this.capacity = capacity;
    }
    /**
     * Default constructor for DataChunk
     * @param frames the pre-determined frames to add into the chunk
     */
    protected DataChunk(double[][][] frames, int uid) {
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
    protected DataChunk(Collection<double[][]> frames, int uid) {
        if(frames == null)
            throw new IllegalArgumentException("Frames cannot be null!");

        this.uid = uid;
        this.frames = frames.toArray(new double[0][][]);
        this.capacity = this.frames.length;
        numItems = this.frames.length;
    }

    /**
     * @return iterates the current position by one. No action if current position is at the end;
     */
    public double[][] stepForward() {
        if(currentFrame == capacity - 1) {
            return frames[currentFrame];
        }

        return frames[++currentFrame];
    }

    /**
     * Sets the current frame # to 0
     * @return the first frame of the chunk
     */
    public double[][] setFrameFront() {
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
    public double[][] setFrameLast() {
        if(frames == null) {
            return null;
        }
        currentFrame = frames.length - 1;
        return frames[currentFrame];
    }

    /**
     * @return iterates the current position back by one. No action if current position is 0
     */
    public double[][] stepBack() {
        if(currentFrame <= 0) {
            return null;
        }

        return frames[--currentFrame];
    }

    /**
     * Goes to the inserted frame number
     *
     * @param frameNum The frame to go to. Note, values are [1, length]. Inclusive of 1.
     * @return The matrix at the specific frame number. Null if out of bounds.
     */
    @Override
    public double[][] gotoFrame(int frameNum) {
        if(frameNum < 1 || frameNum > numItems) {
            return null;
        }
        this.currentFrame = frameNum - 1;
        return frames[currentFrame];
    }

    /**
     * @return 
     */
    @Override
    public int getFrameNum() {
        return currentFrame + 1;
    }

    /**
     * @return returns true if no elements are in the chunk
     */
    public boolean isEmpty() {
        return capacity <= 0 || numItems <= 0;
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
     * Gets the current matrix
     *
     * @return MatrixR032, null if empty
     */
    @Override
    public double[][] get() {
        if(isEmpty()) {
            return null;
        }
        return frames[currentFrame];
    }

    /**
     * @return the amount of frames in the chunk
     */
    public int size() {
        return numItems;
    }

    /**
     * Removes pointers to data held within and calls system to garbage collect
     * Note. This is permanent. Values will have to be reinitialized if wanted again.
     * Sets frames to null, number items to 0, and frame number to 0.
     */
    @Override
    public void clearData() {
        frames = null;
        numItems = 0;
        currentFrame = -1;
    }

    /**
     * @return The list of frames in this chunk
     */
    public double[][][] toList() {
        return frames;
    }

    /**
     * @return an array of 2d double arrays
     */
    public double[][][] toListPrimitive() {
        double[][][] arr = new double[numItems][][];

        for(int i = 0; i < numItems; i++) {
            arr[i] = frames[i];
        }

        return arr;
    }

    @Override
    public String toString() {
        return "DataChunk{" +
                "frames=" + Arrays.toString(frames) +
                ", currentFrame=" + currentFrame +
                ", capacity=" + capacity +
                ", numItems=" + numItems +
                ", uid=" + uid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(o == null || !(o instanceof DataChunk chunk)) {
            return false;
        }

        return numItems == chunk.numItems && Arrays.equals(this.toListPrimitive(), chunk.toListPrimitive());
    }
}
