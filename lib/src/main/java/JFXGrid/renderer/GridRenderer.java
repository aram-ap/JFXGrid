package JFXGrid.renderer;

import JFXGrid.javafx.JFXGrid;
import JFXGrid.util.ResizableCanvas;


public class GridRenderer implements Renderer {
    private final JFXGrid jfxGrid;

    public GridRenderer(final JFXGrid jfxGrid) {
        this.jfxGrid = jfxGrid;
    }

    protected ResizableCanvas getCanvas() {
        return jfxGrid.getCanvas();
    }

    protected void drawHorLines() {
        var canvas = getCanvas();
        var gc = canvas.getGraphicsContext2D();
        var dataset = jfxGrid.getDataset();
        var rows = dataset.getNumRows();

        for (int i = 0; i <= rows; i++) {
            double yVal = (canvas.getHeight() / (double) rows) * i;
            gc.strokeLine(0, yVal, canvas.getWidth(), yVal);
        }
    }

    protected void drawVerLines() {
        var canvas = getCanvas();
        var gc = canvas.getGraphicsContext2D();

        var dataset = jfxGrid.getDataset();
        var columns = dataset.getNumColumns();

        for (int i = 0; i <= columns; i++) {
            double xVal = (canvas.getWidth() / (double) columns) * i;
            gc.strokeLine(xVal, 0, xVal, canvas.getHeight());
        }
    }

    public void drawImage() {
        if (jfxGrid.getImageOptional().isEmpty()) return;
        var image = jfxGrid.getImageOptional().get();
        getCanvas().getGraphicsContext2D().setImageSmoothing(false);
        getCanvas().getGraphicsContext2D().drawImage(image, 0, 0, getCanvas().getWidth(), getCanvas().getHeight());
    }

    protected void drawGradient() {
    }

    protected void drawBackground() {
        var gc = getCanvas().getGraphicsContext2D();
        gc.fillRect(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
        gc.strokeRect(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
    }

    public void forceRender() {
        drawBackground();
        drawImage();
        drawHorLines();
        drawVerLines();
        drawGradient();
    }
}
