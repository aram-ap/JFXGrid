package JFXGrid.data;

import org.ojalgo.matrix.MatrixR032;

import java.util.ArrayList;
import java.util.List;

public class JFXDatasetFactory extends JFXDataset{
    private ArrayList<MatrixR032> frames;

    public JFXDatasetFactory() {
        frames = new ArrayList<>();
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

    public JFXDatasetFactory addAll(double[][][] matrices) {
        if(matrices == null || matrices.length == 0) {
            return this;
        }

        for(double[][] d : matrices) {
            frames.add(MatrixR032.FACTORY.rows(d));
        }

        return this;
    }

    public JFXDataset build() {
        JFXDataset dataset = new JFXDataset();
        dataset.addCache(frames.toArray(new MatrixR032[0]));
        dataset.setNumColumns(frames.get(0).getColDim());
        dataset.setNumRows(frames.get(0).getRowDim());
        frames = null;
        return dataset;
    }
}
