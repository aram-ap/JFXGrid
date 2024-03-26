package JFXGrid.data;

import javafx.beans.property.SimpleStringProperty;
import org.ojalgo.matrix.Matrix2D;
import org.ojalgo.matrix.MatrixR032;

public interface Data {
    final SimpleStringProperty datasetName = new SimpleStringProperty("Data");

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
    public MatrixR032 get();

    /**
     * Gets the size of the data set
     * @return size of the data set >= 0
     */
    public int size();

    /**
     * Removes pointers to data held within and calls system to garbage collect
     */
    public void clearData();

    public MatrixR032 stepForward();

    public MatrixR032 stepBack();
}
