package JFXGrid.core;

import JFXGrid.core.JFXHeatmap;
import JFXGrid.core.Axis;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * This class is utilized by the JFXHeatmap, it arranges the grid and axis accordingly.
 *
 * @author aram-ap
 */
public abstract class GridFormatPane extends GridPane {

    protected GridFormatPane() {  }

    protected void init(JFXHeatmap grid) {
        if(grid == null)
            return;
    }

    public void addNode(Node node, Axis.Align alignment) {
        if(node == null) {
            return;
        }

        //Add to grid (x,y)
        int xPos = 0;
        int yPos = 0;
        switch(alignment) {
            case Up -> {
                xPos = 1;
                yPos = 0;
            }
            case Down -> {
                xPos = 1;
                yPos = 2;
            }
            case Left -> {
               xPos = 0;
               yPos = 1;
            }
            case Right -> {
                xPos = 2;
                yPos = 1;
            }
            case Center -> {
                xPos = 1;
                yPos = 1;
                GridPane.setFillHeight(node, true);
                GridPane.setFillWidth(node, true);
            }
            default -> { }
        }

        if(!contains(xPos, yPos)) {
            add(node, xPos, yPos);
        }
    }
}
