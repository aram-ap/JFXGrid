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
 * The JFXDataDeque is a dataset implementation which intends to allow chunking of data and reduced memory utilization.
 */
public class JFXDataDeque extends JFXDataset implements Data{
    private int numFrames;
    private int numChunks;
    private DataChunk currentChunk;
    private DataNode headNode;
    private DataNode tailNode;
    private DataNode currentNode;

    protected JFXDataDeque(int rows, int columns) {
        super(rows, columns);
    }

    public int getNumChunks() {
        return numChunks;
    }

    public void insert(DataChunk chunk) {
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

    public void insertLast(DataChunk chunk) {
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
    public void insertFirst(DataChunk chunk) {
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

    public DataChunk deleteFirst() {
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

    public DataChunk deleteLast() {
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

    public MatrixR032 stepFrame() {
        if(currentChunk == null) {
            return null;
        }

        if(currentChunk.hasNext()) {
            return currentChunk.stepForward();
        } else if (currentNode != tailNode) {
            currentNode = currentNode.getNext();
            currentNode.setPrev(null);
            currentChunk = currentNode.getChunk();
        }

        return currentChunk.getCurr();
    }

    /**
     * Moves the current node pointer to the indicated frameNum
     * @param frameNum
     */
    public void moveTo(int frameNum) {
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
     * @return
     */
    @Override
    public MatrixR032 get() {
        if(headNode == null) {
            return null;
        }
        return currentNode.getChunk().getCurr();
    }

    @Override
    public void clearData() {
        currentChunk = null;
        headNode = null;
        tailNode = null;
        System.gc();
    }

    @Override
    public MatrixR032 stepForward() {
        if(currentNode == null) {
            return null;
        }

        if(currentNode.getChunk().hasNext()) {
        } else if (currentNode.getChunk().hasNext() && currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
        }
        return currentNode.stepForward();
    }

    @Override
    public MatrixR032 stepBack() {
        if(currentNode == null) {
            return null;
        }

        var val = currentNode.stepBack();
        if(val == null) {
            if(currentNode.getPrev() != null) {
                currentNode = currentNode.getPrev();
            }
        }

        return val;
    }

    @Override
    public long getFrameNum() {
        return currentChunk.uid + currentChunk.getIndex();
    }
}
