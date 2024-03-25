package JFXGrid.data;

/**
 * The DataNode is an encapsulating object for DataChunk to allow DataStructures like Deques and Binary Trees
 */
public class DataNode implements Data {
    private DataNode next;
    private DataNode prev;
    private DataChunk val;

    /**
     * The default constructor for DataNode
     * @param data the DataChunk object to attach
     */
    public DataNode(DataChunk data) {
        this.val = data;
    }

    /**
     * @return Returns the attached DataChunk
     */
    public DataChunk get() {
        return val;
    }

    public DataNode getNext() {
        return next;
    }

    public DataNode getPrev() {
        return prev;
    }

    public void setNext(DataNode node) {
        next = node;
    }

    public void setPrev(DataNode node) {
        prev = node;
    }

    /**
     * @return
     */
    @Override
    public int size() {
        if(val == null) return 0;
        return val.size();
    }

    /**
     *
     */
    @Override
    public void clearData() {
        val = null;
    }
}
