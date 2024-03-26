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
import java.util.List;

public class JFXDatasetFactory extends JFXDataset {
    private ArrayList<MatrixR032> frames = new ArrayList<>();

    public JFXDatasetFactory(int rows, int cols) {
        super(rows, cols);
    }

    public JFXDatasetFactory add(MatrixR032 matrix) {
        if(matrix != null) {
            frames.add(matrix);
        }
        return this;
    }

    public JFXDatasetFactory add(double[][] matrix) {
        if(matrix != null) {
            frames.add(MatrixR032.FACTORY.rows(matrix));
        }

        return this;
    }

    public JFXDatasetFactory addAll(MatrixR032[] matrices) {
        if(matrices == null) {
            return this;
        }
        frames.addAll(List.of(matrices));
        return this;
    }

    public JFXDatasetFactory addAll(Collection<MatrixR032> matrices) {
        if(matrices == null) {
            return this;
        }

        frames.addAll(matrices);
        return this;
    }

    public JFXDataset build() {
        JFXDataset dataset = new JFXDataset(getNumRows(), getNumColumns());
        dataset.setCurrentChunk(new DataChunk(frames.toArray(new MatrixR032[0]), 0));
        return dataset;
    }
}
