package JFXGrid.data;

import JFXGrid.core.Axis;
import JFXGrid.plugin.Plugin;
import com.sun.javafx.collections.UnmodifiableListSet;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import org.ojalgo.matrix.Matrix2D;
import org.ojalgo.matrix.MatrixR032;

import java.util.ArrayList;
import java.util.List;

public class JFXDataset implements Data {
    private int numFrames;
    private MatrixR032[] frameCache;
    private int numRows;
    private int numColumns;

    protected JFXDataset() {
        this.numRows = 0;
        this.numColumns = 0;
    }

    protected void addCache(MatrixR032[] cache) {
        this.frameCache = cache;
        this.numFrames = cache.length;
    }

    protected void setNumRows(int rows) {
        this.numRows = rows;
    }

    protected void setNumColumns(int columns) {
        this.numColumns = columns;
    }

    public int rowsDim() {
        return numRows;
    }

    public int columnsDim() {
        return numColumns;
    }

    public UnmodifiableListSet<MatrixR032> getUnmodifiableCache() {
        return new UnmodifiableListSet<MatrixR032>(List.of(frameCache));
    }

    public int size() {
        return numFrames;
    }

    @Override
    public void clearData() {
        frameCache = null;
    }
}
