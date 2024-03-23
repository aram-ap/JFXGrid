package JFXGrid.data;

import JFXGrid.core.Axis;
import JFXGrid.plugin.Plugin;
import org.ojalgo.matrix.MatrixR032;

import java.util.ArrayList;
import java.util.List;

public class JFXDatasetFactory extends JFXDataset{
    private ArrayList<MatrixR032> frames;

    public JFXDatasetFactory() {
        frames = new ArrayList<>();
    }

    public JFXDatasetFactory addAll(MatrixR032[] matrices) {
        if(matrices == null) {
            return this;
        }
        frames.addAll(List.of(matrices));
        return this;
    }

    public JFXDataset build() {
        JFXDataset dataset = new JFXDataset();
        dataset.addCache(frames.toArray(new MatrixR032[0]));
        frames = null;
        return dataset;
    }
}
