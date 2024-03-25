package JFXGrid.data;

import com.sun.javafx.collections.UnmodifiableListSet;
import org.ojalgo.matrix.MatrixR032;

import java.util.ArrayList;
import java.util.List;

public class JFXDatasetQueue implements Data {
    private int numFrames;
    private int numChunks;
    private DataChunk currentChunk;
    private DataNode headNode;
    private DataNode tailNode;
    private int numRows;
    private int numColumns;

    protected JFXDatasetQueue(int rows, int columns) {
        this.numColumns = columns;
        this.numRows = rows;
    }

    public int getNumFrames() {
        return numFrames;
    }

    public int getNumChunks() {
        return numChunks;
    }

    public void insert(DataChunk chunk) {
        if(chunk == null) return;

        var node = new DataNode(chunk);

        if(headNode == null) {
            headNode = node;
            tailNode = node;
            currentChunk = node.get();
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
        } else {
            headNode = headNode.getNext();
            headNode.setPrev(null);
        }

        numChunks--;
        numFrames -= node.size();

        return node.get();
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

        return node.get();
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

    public int size() {
        return numFrames;
    }

    @Override
    public void clearData() {
        currentChunk = null;
        headNode = null;
        tailNode = null;
        System.gc();
    }
}
