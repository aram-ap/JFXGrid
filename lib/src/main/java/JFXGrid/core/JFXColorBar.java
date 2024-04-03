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

import JFXGrid.util.Style;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Rectangle;

public class JFXColorBar extends Axis {
    private JFXGrid grid;
    private LinearGradient gradient;
    private Rectangle gradientBar;
    private Style style;
    public JFXColorBar(JFXGrid grid, Align align) {
        super(align);
        this.grid = grid;
    }

    public void updateGradient() {

    }

    public void update() {
        super.update();

        var stylizer = grid.getStylizer();
        if(style != stylizer.getStyle()) {
            style = stylizer.getStyle();
            updateGradient();
        }
    }
}
