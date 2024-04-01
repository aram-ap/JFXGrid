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
    public DataChunk getChunk() {
        return val;
    }

    /**
     *
     * @return
     */
    public MatrixR032 get() {
        return val.getCurrentFrame();
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

    /**
     *
     */
    @Override
    public MatrixR032 stepForward() {
        if(val == null)
            return null;

        return val.stepForward();
    }

    /**
     *
     */
    @Override
    public MatrixR032 stepBack() {
        if(val == null) {
            return null;
        }

        return val.stepBack();
    }

    /**
     * @return
     */
    @Override
    public long getFrameNum() {
        return val.getIndex();
    }
}
