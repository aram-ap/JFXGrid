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
package JFXGrid.renderer;

import JFXGrid.core.Axis;
import JFXGrid.core.Colorizer;
import JFXGrid.renderer.Renderer;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;

public class AxisRenderer implements Renderer{
    private final Axis axis;
    private static Colorizer colorTheme;

    public AxisRenderer(Axis axis) {
        this.axis = axis;
        if(colorTheme == null) colorTheme = new Colorizer();
    }

    protected void drawTickMarks() {
        var canvas = axis.getCanvas();
        var gc = axis.getCanvas().getGraphicsContext2D();
        double min, max;
        max = axis.getMaxVal();
        min = axis.getMinVal();
        final String format = "%.0f";

//        gc.setLineWidth();
//        gc.setFontSmoothingType(FontSmoothingType.GRAY);
        gc.setFill(gc.getStroke());

        double x1, x2, y1, y2;
        switch(axis.getLabelAlignment()) {
            case Up -> {
                y1 = canvas.getHeight()-axis.getTickLength();
                y2 = canvas.getHeight();

                for(int i = 0; i < axis.getNumTicks(); i++) {
                    double xVal = (canvas.getWidth()/(axis.getNumTicks() -1)) * i;
                    double tickVal = getTickValue(max, min, (int) axis.getNumTicks(), i, axis.isSwitched());

                    String labelVal = String.format(format,tickVal);

                    gc.strokeLine(xVal, y1, xVal, y2);
                    gc.setTextBaseline(VPos.TOP);

                    //Change alignment at edges to make sure text doesn't get clipped out
                    if (i == axis.getNumTicks() - 1) {
                        gc.setTextAlign(TextAlignment.RIGHT);
                    } else if (i != 0) {
                        gc.setTextAlign(TextAlignment.CENTER);
                    } else {
                        gc.setTextAlign(TextAlignment.LEFT);
                    }

                    gc.strokeText(labelVal, xVal, canvas.getHeight()-axis.getAxisSize()-axis.getTickLabelDistance());
                }
            }
            case Down -> {
                y1 = 0;
                y2 = axis.getTickLength();

                for(int i = 0; i < axis.getNumTicks(); i++) {
                    double xVal = (canvas.getWidth()/(axis.getNumTicks() -1)) * i;
                    double tickVal = getTickValue(max, min, (int) axis.getNumTicks(), i, axis.isSwitched());
                    String labelVal = String.format(format,tickVal);

                    gc.setTextBaseline(VPos.BOTTOM);

                    gc.strokeLine(xVal, y1, xVal, y2);

                    //Change alignment at edges to make sure text doesn't get clipped out
                    if (i == axis.getNumTicks() - 1) {
                        gc.setTextAlign(TextAlignment.RIGHT);
                    } else if (i != 0) {
                        gc.setTextAlign(TextAlignment.CENTER);
                    } else {
                        gc.setTextAlign(TextAlignment.LEFT);
                    }

                    gc.strokeText(labelVal, xVal, axis.getTickLength());
                }
            }
            case Left -> {
                x1 = canvas.getWidth() - axis.getTickLength();
                x2 = canvas.getWidth();

                for(int i = 0; i < axis.getNumTicks(); i++) {
                    double yVal = (canvas.getHeight()/(axis.getNumTicks() -1)) * i;
                    double tickVal = getTickValue(max, min, (int) axis.getNumTicks(), i, axis.isSwitched());

                    String labelVal = String.format(format,tickVal);
                    gc.strokeLine(x1, yVal, x2, yVal);
                    gc.setTextAlign(TextAlignment.RIGHT);
                    //Change alignment at edges to make sure text doesn't get clipped out
                    if(i == 0) {
                        yVal += gc.getFont().getSize()/2;
                        gc.setTextBaseline(VPos.CENTER);
                    } else {
                        gc.setTextBaseline(VPos.BOTTOM);
                    }


                    gc.strokeText(labelVal, canvas.getWidth()-axis.getAxisSize()-axis.getTickLabelDistance(), yVal);
                }
            }
            case Right -> {
                x1 = 0;
                x2 = axis.getTickLength();

                for(int i = 0; i < axis.getNumTicks(); i++) {
                    double yVal = (canvas.getHeight()/(axis.getNumTicks() -1)) * i;
                    double tickVal = getTickValue(max, min, (int) axis.getNumTicks(), i, axis.isSwitched());
                    String labelVal = String.format(format,tickVal);

                    gc.strokeLine(x1, yVal, x2, yVal);
                    gc.setTextAlign(TextAlignment.LEFT);

                    //Change alignment at edges to make sure text doesn't get clipped out
                    if(i == 0) {
                        yVal += gc.getFont().getSize()/2;
                    }

                    if (i == axis.getNumTicks() - 1) {
                        gc.setTextBaseline(VPos.BOTTOM);
                    } else {
                        gc.setTextBaseline(VPos.CENTER);
                    }

                    gc.strokeText(labelVal, axis.getTickLength(), yVal);
                }
            }
            default -> {
            }
        }
    }

    protected double getTickValue(double max, double min, int numTicks, int index, boolean isSwitched) {
        double tickVal;

        //Check if the numbers have to be upside down
        if(isSwitched) {
            tickVal = Math.abs((max) - (((double) index /((double)numTicks-1d)) * (max-min)));
        } else {
            tickVal = Math.floor((double) (index) /(numTicks-1) * (max-min));
        }
        return tickVal;
    }

    protected void drawLine() {
        var canvas = axis.getCanvas();
        var gc = axis.getCanvas().getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double x1, x2, y1, y2;

        switch(axis.getLabelAlignment()) {
            case Up ->  {
                y1 = canvas.getHeight() - axis.getTickLength()/2d;
                y2 = y1;
                x1 = 0;
                x2 = canvas.getWidth();
            }
            case Down -> {
                y1 = axis.getTickLength()/2d;
                y2 = y1;
                x1 = 0;
                x2 = canvas.getWidth();

            }
            case Left -> {
                x1 = canvas.getWidth() - axis.getTickLength()/2d;
                x2 = x1;
                y1 = 0;
                y2 = canvas.getHeight();
            }
            case Right -> {
                x1 = axis.getTickLength()/2d;
                x2 = x1;
                y1 = 0;
                y2 = canvas.getHeight();
            }
            default -> {
                return;
            }
        }

//        gc.setLineWidth(colorTheme.getLineWidth());
//        gc.setStroke(colorTheme.getLineColor());
        gc.strokeLine(x1, y1, x2, y2);
    }

    public void update() {

    }
    public void forceRender() {
        drawLine();
        drawTickMarks();
    }
}
