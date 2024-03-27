package JFXGrid.renderer;

import JFXGrid.JFXGrid;
import JFXGrid.core.Axis;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;

/**
 * Default interface for all renderers
 *
 * @author aram-ap
 */
public interface Renderer {
    /**
     * Builds the final chart image
     * @param grid the grid to be displayed
     * @return a final view including all axis and grid laid out
     */
    default WritableImage joinAll(JFXGrid grid) {
        return null;
    }
}
