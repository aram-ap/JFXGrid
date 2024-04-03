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

/**
 * <h3>JFXDataDeque</h3>
 * <hr>
 * <p>
 *     &emsp The JFXDataDeque is the child class of the JFXDataSet. Its purpose is to reduce memory utilization, allow for dynamic chunk loading/reloading
 *     Chunking to/from the local filesystem, and all other features of the default JFXDataSet class.
 *     When should you use this? <br>
 * </p>
 * <b>Use this if any of these apply:</b> <br>
 * <ul>
 *     <li>Your program utilizes <b>large datasets</b> that would otherwise collect and grow too large in memory.</li>
 *     <li>Your program <b>asynchronously reads data</b> such as reading from a network socket or when reading sensor data live</li>
 *     <li>You want datasets processed and saved onto your filesystem</li>
 * </ul>
 *
 * <h3>More notes on this class:</h3>
 * <hr>
 * <p>
 *     &emsp This class work through loading chunks in a at-need basis. There's a reference to the middle / 'current' chunk.
 *     Having this middle chunk as the 'current' chunk, with a couple chunks in front + behind gives a little more
 *     leeway for more jumps in frame number. <br>
 *     &emsp As a chunk has been moved through, this object will attempt to load the next chunk and append it to the end of
 *     the list before it moves the current chunk pointer to the next. Of course, if there are no more chunks left
 *     to load, it'll just move to the next reference and keep playing until the last frame. <br>
 *     &emsp Each time a chunk is loaded, it is placed into the queue according to its UID. This UID 'can' be changed,
 *     but it should usually be used as the number indicating the frame # of the first matrix in the chunk. <br>
 *     &emsp On default, chunk process calls are put into a separate worker thread, but if needed, can be put on the
 *     default JavaFX thread. <br>
 * </p>
 * @author @aram-ap
 */
public class JFXDataDeque extends JFXDataset implements Data{
    private int numFrames; //The maximum frame number of the very last chunk
    private int numChunks; //A number indicating the total length of the chunk queue
    private DataChunk currentChunk; //A reference to the current (usually middle) chunk of the list.
    private DataNode headNode; //The very front of the queue.
    private DataNode tailNode; //The very end of the queue.
    private DataNode currentNode; //The encapsulating node of the current data chunk. Just contains references to the data chunk, next node, and previous node

    /**
     * The default constructor for the JFXDataDeque. Made private as to require the use of the JFXDatasetFactory when
     * creating the Deque objects
     * @param rows number of rows of each matrix
     * @param columns number of columns of each matrix
     */
    protected JFXDataDeque(int rows, int columns) {
        super(rows, columns);
    }

    /**
     * @return the current length of loaded chunks
     */
    public int getNumChunks() {
        return numChunks;
    }

    /**
     * Inserts a new data chunk into the queue. Sets the chunk node in the correct order of priority
     * @param chunk
     */
    protected void insert(DataChunk chunk) {
        if(chunk == null)
            return;

        var node = new DataNode(chunk);

        if(chunk.uid > headNode.getChunk().uid) {
            insertFirst(chunk);
        } else if (chunk.uid < tailNode.getChunk().uid) {
            insertLast(chunk);
        } else {
            var tempPointer = headNode;
            for(int i = 0; i < numChunks; i++) {
                if(tempPointer.getChunk().uid <= chunk.uid && tempPointer.getNext().getChunk().uid > chunk.uid) {
                    break;
                }
                tempPointer = tempPointer.getNext();
            }

            node.setNext(tempPointer.getNext());
            node.setPrev(tempPointer);
            tempPointer.getNext().setPrev(node);
            tempPointer.setNext(node);

            numFrames += chunk.size();
            numChunks++;
        }
    }

    /**
     * Inserts the chunk as the last node. Primarily is used when loading up the node with the highest UID / frame number
     * @param chunk
     */
    protected void insertLast(DataChunk chunk) {
        if(chunk == null) return;

        var node = new DataNode(chunk);

        if(headNode == null) {
            headNode = node;
            tailNode = node;
            currentChunk = node.getChunk();
            currentNode = node;
        } else {
            tailNode.setNext(node);
            node.setPrev(tailNode);
            tailNode = node;
        }

        numChunks++;
        numFrames += chunk.size();
    }

    /**
     * Inserts the chunk as the first node. Primarily is used when loading up the node with the lowest UID / frame number.
     * @param chunk
     */
    protected void insertFirst(DataChunk chunk) {
        if(chunk == null) return;

        var node = new DataNode(chunk);
        if(headNode == null || tailNode == null) {
            headNode = node;
            tailNode = node;
            currentChunk = node.getChunk();
            currentNode = node;
        } else {
            headNode.setPrev(node);
            node.setNext(headNode);
            headNode = node;
        }

        numChunks++;
        numFrames += chunk.size();
    }

    /**
     * This removes the first chunk node. Usually occurs when newer chunks have been loaded / when the current chunk
     * moves too far away from the first chunk as a form of memory management.
     * @return the chunk removed from the queue
     */
    protected DataChunk deleteFirst() {
        if(headNode == null) {
            return null;
        }

        var node = headNode;
        if(headNode == tailNode) {
            headNode = null;
            tailNode = null;
            currentNode = null;
            currentChunk = null;
        } else {
            headNode = headNode.getNext();
            headNode.setPrev(null);
        }

        numChunks--;
        numFrames -= node.size();

        return node.getChunk();
    }

    /**
     * Removes the last chunk in the queue. Usually occurs when the current chunk node is moved back 1+, such as going
     * to the first frame of a multi-chunk queue.
     * @return the last chunk that was removed from the queue.
     */
    protected DataChunk deleteLast() {
        if(tailNode == null) {
            return null;
        }

        var node = tailNode;
        if(headNode == tailNode) {
            headNode = null;
            tailNode = null;
        } else {
            tailNode = tailNode.getPrev();
            tailNode.setNext(null);
        }

        numChunks--;
        numFrames -= node.size();

        return node.getChunk();
    }

    /**
     * Steps to the next node
     * @return the next node's data chunk
     */
    public DataChunk stepNodeForward() {
        if(currentNode == null) {
            return null;
        }

        if(currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
            currentChunk = currentNode.getChunk();
        }

        return currentNode.getChunk();
    }

    /**
     * Steps to the previous node node
     * @return the previous node's data chunk
     */
    public DataChunk stepNodeBackward() {
        if(currentNode == null) {
            return null;
        }

        if(currentNode.getPrev() != null) {
            currentNode = currentNode.getPrev();
            currentChunk = currentNode.getChunk();
        }

        return currentNode.getChunk();
    }

    /**
     * Moves the current node pointer to the indicated frameNum. Will automatically switch chunk nodes while traversing
     * frames.
     * @param frameNum the frame number being moved to.
     * @throws IllegalArgumentException for entered frame numbers less than 0
     */
    public void moveTo(int frameNum) {
        if(frameNum < 0) {
            throw new IllegalArgumentException("Frame number cannot be less than 0!");
        }
        var temp = headNode;
        var chunkFound = false;
        for(int i = 0; i < numChunks; i++) {
            if(temp.getChunk().uid <= frameNum && temp.getNext().getChunk().uid > frameNum) {
                break;
            }
            temp = temp.getNext();
        }

        if(chunkFound) {
            currentNode = temp;
        }
    }

    /**
     * @return the current node's current frame.
     */
    @Override
    public double[][] get() {
        if(headNode == null) {
            return null;
        }
        return currentNode.getChunk().get();
    }

    /**
     * Removes all chunk node references before calling the garbage collector.
     */
    @Override
    public void clearData() {
        currentChunk = null;
        headNode = null;
        tailNode = null;
        System.gc();
    }

    /**
     * Moves the current frame number up by one. If there's no more frames in the current chunk, it'll try to move onto the next chunk before loading its first frame.
     * It won't step if its at its last frame already.
     * @return the next frame
     */
    @Override
    public double[][] stepForward() {
        if(currentChunk == null) {
            return null;
        }

        if(currentChunk.hasNext()) {
            return currentChunk.stepForward();
        } else if (currentNode != tailNode) {
            stepNodeForward();
            currentChunk.setFrameFront();
        }

        return currentChunk.get();
    }

    /**
     * Moves the current frame number back by one. If the chunk's frame pointer is already 0, then it'll try to step to
     * the previous chunk
     * @return the previous frame
     */
    @Override
    public double[][] stepBack() {
        if(currentNode == null) {
            return null;
        }

        if(currentChunk.hasPrev()) {
            return currentChunk.stepBack();
        } else if (currentNode != headNode) {
            stepNodeBackward();
            currentChunk.setFrameLast();
        }
        return currentChunk.get();
    }

    /**
     * Gets the current frame number. This is inclusive of the current chunk's UID / frame start value.
     * @return
     */
    @Override
    public int getFrameNum() {
        return currentChunk.uid + currentChunk.getIndex();
    }
}
