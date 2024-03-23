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

public class JFXDataset implements Data{
    private int numFrames;
    private MatrixR032[] frameCache;
    protected JFXDataset() { }

    protected void addCache(MatrixR032[] cache) {
        this.frameCache = cache;
        this.numFrames = cache.length;
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
