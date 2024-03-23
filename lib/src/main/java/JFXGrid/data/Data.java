package JFXGrid.data;

import javafx.beans.property.SimpleStringProperty;
import org.ojalgo.matrix.Matrix2D;

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

    public int size();
    public void clearData();
}
