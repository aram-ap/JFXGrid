package JFXGrid.javafx;

import JFXGrid.JFXGrid;
import JFXGrid.core.Axis;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public abstract class GridFormatPane extends GridPane {
    protected JFXGrid grid;

    protected GridFormatPane() {  }

    protected void init(JFXGrid grid) {
        if(grid == null)
            return;
        this.grid = grid;
        arrange();
    }

    private void arrange() {
        if(grid == null) {
            return;
        }

        var axes = grid.getAxes();
        for(var a : axes) {
            var orientation = a.getLabelAlignment();
            addNode(a, orientation);
        }
        addNode(grid, Axis.Align.Center);
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
            }
            default -> { }
        }

        if(!contains(xPos, yPos)) {
            add(node, xPos, yPos);
        }
    }
}
