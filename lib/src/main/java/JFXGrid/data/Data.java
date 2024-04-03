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

import javafx.beans.property.SimpleStringProperty;
import org.ojalgo.matrix.Matrix2D;
import org.ojalgo.matrix.MatrixR032;

/**
 * The default Data interface. Contains the essentials like dataset names, getting the current MatrixR032 matrix, size, and iterating through frames.
 * @author Aram Aprahamian
 */
public interface Data {
    SimpleStringProperty datasetName = new SimpleStringProperty("Data");

    default SimpleStringProperty getDatasetName() {
        return datasetName;
    }

    default void setDatasetName(String name) {
        if(name == null)
            return;

        datasetName.setValue(name);
    }

    /**
     * Gets the current matrix
     * @return MatrixR032, null if empty
     */
    public double[]get();

    /**
     * Gets the size of the data set
     * @return size of the data set >= 0
     */
    public int size();

    /**
     * Removes pointers to data held within and calls system to garbage collect
     */
    public void clearData();

    public double[] stepForward();

    public double[] stepBack();

    /**
     * Goes to the inserted frame number
     * @param frameNum The frame to go to. Note, values are [1, length]. Inclusive of 1.
     * @return The matrix at the specific frame number. Null if out of bounds.
     */
    public double[] gotoFrame(int frameNum);

    public int getFrameNum();
}
