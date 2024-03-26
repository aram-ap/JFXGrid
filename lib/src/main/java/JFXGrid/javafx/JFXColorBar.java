package JFXGrid.javafx;

import JFXGrid.core.Axis;
import JFXGrid.core.GridStyler.Style;
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

        var stylizer = grid.getGridStyler();
        if(style != stylizer.getStyle()) {
            style = stylizer.getStyle();
            updateGradient();
        }
    }
}
