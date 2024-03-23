package JFXGrid.util;

import javafx.scene.canvas.Canvas;

public class ResizableCanvas extends Canvas {
    public void resize(double width, double height) {
        if(width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Tried to resize canvas to invalid configuration, width: " + width + ", height: " + height);
        }

        this.setWidth(width);
        this.setHeight(height);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double maxHeight(double width) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double maxWidth(double height) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double minWidth(double height) {
        return 1D;
    }

    @Override
    public double minHeight(double width) {
        return 1D;
    }
}
