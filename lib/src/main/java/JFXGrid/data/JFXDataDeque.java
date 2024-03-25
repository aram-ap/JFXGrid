package JFXGrid.data;

import org.ojalgo.matrix.MatrixR032;

public class JFXDataDeque extends JFXDataset{
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
}
