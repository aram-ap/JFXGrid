package JFXGrid.renderer;

import JFXGrid.core.JFXHeatmap;
import javafx.scene.image.WritableImage;

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
    default WritableImage joinAll(JFXHeatmap grid) {
        return null;
    }
}
