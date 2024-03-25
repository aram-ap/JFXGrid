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
