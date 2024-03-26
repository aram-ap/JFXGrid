//MIT License
//
//Copyright (c) 2024 Aram Aprahamian
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.
package JFXGrid.core;

import JFXGrid.renderer.AxisRenderer;
import JFXGrid.util.ResizableCanvas;
import javafx.css.Styleable;
import javafx.scene.layout.BorderPane;

public class Axis extends BorderPane implements Styleable {
    public enum Type {
        X_AXIS,
        Y_AXIS,
        Z_AXIS,
        UNASSIGNED
    }

    public enum Align {
        Left,
        Right,
        Up,
        Down
    }

    private Align labelAlignment = Align.Left;
    private double minVal = 0;
    private double maxVal = 1;
    private int numTicks = 2;
    private double tickLength = 15d;
    private double axisSize = 30d;
    private double tickLabelDistance = 20;
    private boolean isSwitched = false;
    private final AxisRenderer renderer;
    private ResizableCanvas canvas;

    public Axis(Align alignment) {
        getStyleClass().add("axis-bar");
        this.labelAlignment = alignment;
        canvas = new ResizableCanvas();
        renderer = new AxisRenderer(this);
        canvas = new ResizableCanvas();
        canvas.getStyleClass().add("axis-canvas");
        setCenter(canvas);
    }

    public ResizableCanvas getCanvas() {
        return canvas;
    }

    public void updateSize() {
        var size = tickLabelDistance + axisSize + tickLength;
        switch (labelAlignment) {
            case Up, Down -> {
                canvas.resize(canvas.getWidth(), size);
            }
            case Left, Right -> {
                canvas.resize(size, canvas.getHeight());
            }
            default -> {
                return;
            }
        }
        if (!canvas.getStyleClass().contains("axis-canvas")) {
            canvas.getStyleClass().add("axis-canvas");
        }

        setCenter(canvas);
        setPrefSize(canvas.getWidth(), canvas.getHeight());
        renderer.forceRender();
    }

    public void forceUpdate() {
        renderer.forceRender();
    }

    public void update() {
        forceUpdate();
    }

    public double getMinVal() {
        return minVal;
    }

    public void setMinVal(double newVal) {
        minVal = newVal;
    }

    public double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(double newVal) {
        maxVal = newVal;
    }

    public AxisRenderer getRenderer() {
        return renderer;
    }

    public double getNumTicks() {
        return numTicks;
    }

    public void setNumTicks(int numTicks) {
        this.numTicks = numTicks;
        forceUpdate();
    }

    public double getTickLength() {
        return tickLength;
    }

    public void setTickLength(double tickLength) {
        this.tickLength = tickLength;
        forceUpdate();
    }

    public Align getLabelAlignment() {
        return labelAlignment;
    }

    public void setLabelAlignment(Align labelAlignment) {
        this.labelAlignment = labelAlignment;
        forceUpdate();
    }

    public double getAxisSize() {
        return axisSize;
    }

    public void setAxisSize(double axisSize) {
        this.axisSize = axisSize;

        forceUpdate();
    }

    public double getTickLabelDistance() {
        return tickLabelDistance;
    }

    public void setTickLabelDistance(double tickLabelDistance) {
        this.tickLabelDistance = tickLabelDistance;
        forceUpdate();
    }

    public boolean isSwitched() {
        return isSwitched;
    }

    public void setSwitched(boolean switched) {
        isSwitched = switched;
    }

}
