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

import JFXGrid.events.JFXClock;
import JFXGrid.events.TickListener;
import JFXGrid.renderer.AxisRenderer;
import JFXGrid.util.ResizableCanvas;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * The specific Axis class added to a JFXGrid. Contains alignment properties, max/min values, and other stylized properties.
 *
 * @author aram-ap
 */
public class Axis extends Pane implements TickListener {

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
        Down,
        Center
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
    private JFXGrid parent;
    private ResizableCanvas canvas;

    public Axis(Align alignment) {
        getStyleClass().add("axis-bar");
        this.labelAlignment = alignment;
        renderer = new AxisRenderer(this);
        canvas = new ResizableCanvas();
        canvas.getStyleClass().add("axis-canvas");

//        switch(alignment) {
//            case Up,Down -> setHeight(30);
//            case Left, Right -> setWidth(30);
//        }
        setMinHeight(30);
        setMinWidth(30);

        getChildren().add(canvas);
    }

    /**
     * Called at each render cycle.
     *
     * @param clock the JFXClock calling the tick
     */
    @Override
    public void update(JFXClock clock) {
        update();
    }

    public void setParent(JFXGrid grid) {
        if(grid == null) {
            return;
        }
        this.parent = grid;
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

        setPrefSize(canvas.getWidth(), canvas.getHeight());
        renderer.setDirty(true);
    }

    public void update() {
        renderer.render();
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
        renderer.setDirty(true);
    }

    public double getTickLength() {
        return tickLength;
    }

    public void setTickLength(double tickLength) {
        this.tickLength = tickLength;
        renderer.setDirty(true);
    }

    public Align getLabelAlignment() {
        return labelAlignment;
    }

    public void setLabelAlignment(Align labelAlignment) {
        this.labelAlignment = labelAlignment;
        renderer.setDirty(true);
    }

    public double getAxisSize() {
        return axisSize;
    }

    public void setAxisSize(double axisSize) {
        this.axisSize = axisSize;

        renderer.setDirty(true);
    }

    public double getTickLabelDistance() {
        return tickLabelDistance;
    }

    public void setTickLabelDistance(double tickLabelDistance) {
        this.tickLabelDistance = tickLabelDistance;
        renderer.setDirty(true);
    }

    public boolean isSwitched() {
        return isSwitched;
    }

    public void setSwitched(boolean switched) {
        isSwitched = switched;
    }

}
