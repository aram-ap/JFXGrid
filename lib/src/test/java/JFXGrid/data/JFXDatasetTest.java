package JFXGrid.data;

import org.junit.jupiter.api.Test;
import org.ojalgo.matrix.MatrixR032;
import org.ojalgo.random.Uniform;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JFXDatasetTest {

    @Test
    void size() {
        int rows = 32, cols = 32, numFrames = 1000;
        JFXDataset data = fakeDataBuilder(rows, cols, numFrames);
        assertEquals(numFrames, data.size());
    }

    @Test
    void clearData() {
        int rows = 32, cols = 32, numFrames = 1000;
        JFXDataset data = fakeDataBuilder(rows, cols, numFrames);
        data.clearData();
        assertEquals(0, data.getNumFrames());
        assertEquals(null, data.get());
        assertEquals(0, data.getFrameNum());
    }

    @Test
    void getFrameNum() {
        int numFrames = 1000, refFrameIndex = 8;
        JFXDataset data = fakeDataBuilder(32, 32, numFrames);

        double[] refMatrix = data.gotoFrame(refFrameIndex);
        data.gotoFrame(1);

        int numStepsForward = 10;
        for(int i = 0; i < numStepsForward; i++) {
            data.stepForward();
        }

        assertEquals(numStepsForward+1, data.getFrameNum());

        int numStepsBack = 8;
        for(int i = 0; i < numStepsBack; i++) {
            data.stepBack();
        }

        assertEquals(numStepsForward-numStepsBack+1, data.getFrameNum());

        for(int i = 0; i < (numStepsForward-numStepsBack+4); i++) {
            data.stepBack();
        }

        assertEquals(1, data.getFrameNum());

        for(int i = 0; i < numFrames+5; i++) {
            data.stepForward();
            data.get();
        }

        assertEquals(numFrames, data.getNumFrames());
        assertArrayEquals(refMatrix, data.gotoFrame(refFrameIndex));
    }

    public static JFXDataset fakeDataBuilder(int rows, int cols, int numFrames) {
        JFXDatasetFactory data = new JFXDatasetFactory(rows, cols);

        for(int i = 0; i < numFrames; i++) {
            data.add(MatrixR032.FACTORY.makeFilled(rows, cols, Uniform.standard()));
        }

        return data.build();
    }
}