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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * The default dataset builder class for JFXGrid. It manages inputting data into the chunks and allows datasets to be immutable.
 * @author aram-ap
 */
public class JFXDatasetFactory extends JFXDataset {
    public enum DataType {
        Single_Chunk,
        Batch_Chunking
    }

    private DataType type = DataType.Single_Chunk;
    private final ArrayList<double[][]> frames = new ArrayList<>();
    private final ArrayList<DataChunk> chunks = new ArrayList<>();

    public JFXDatasetFactory(int rows, int cols) {
        super(rows, cols);
    }

    public JFXDatasetFactory add(MatrixR032 matrix) {
        if(matrix != null) {
            add(matrix.toRawCopy2D());
        }
        return this;
    }

    public JFXDatasetFactory add(double[][] matrix) {
        if(matrix != null) {
            frames.add(matrix);
        }

        return this;
    }

    public JFXDatasetFactory addAll(double[][][] matrices) {
        if(matrices == null || matrices.length == 0) {
            return this;
        }

        int length = matrices.length;
        for(int i = 0; i<length; i++) {
            frames.add(matrices[i]);
        }

        return this;
    }

    public JFXDatasetFactory addAll(MatrixR032[] matrices) {
        if(matrices == null) {
            return this;
        }

        for(var matrix :matrices) {
            frames.add(matrix.toRawCopy2D());
        }

        return this;
    }

    public JFXDatasetFactory addAll(Collection<double[][]> matrices) {
        if(matrices == null) {
            return this;
        }

        frames.addAll(matrices);
        return this;
    }

    /**
     * Creates the JFXDataset. Defaults to the regular JFXDataset class unless called by the
     * @return
     */
    public JFXDataset build() {
        JFXDataset dataset;
        if(chunks.isEmpty()) {
            chunks.add(new DataChunk(frames, 0));
        }

        if(type == DataType.Single_Chunk) {
            dataset = new JFXDataset(getNumRows(), getNumColumns());
            dataset.setCurrentChunk(chunks.get(0));
        } else {
            dataset = new JFXDataDeque(getNumRows(), getNumColumns());
            JFXDataDeque deque = (JFXDataDeque) dataset;
            chunks.forEach(deque::insertLast);
        }

        return dataset;
    }

    /**
     * Sets the type of returned Dataset type. Will default to JFXDataset 'Single_Chunk', but can be modified to 'Batch_Chunking' for the JFXDataDeque type
     * @param type type of datatype to set
     * @return
     */
    public JFXDatasetFactory setDataType(DataType type) {
        this.type = type;
        return this;
    }
}
