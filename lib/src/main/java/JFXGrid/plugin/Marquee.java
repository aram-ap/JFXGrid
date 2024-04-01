package JFXGrid.plugin;

import JFXGrid.core.JFXHeatmap;
import JFXGrid.events.JFXClock;
import JFXGrid.events.TickListener;

import java.util.Map;

/**
 * The marquee tool requires the MouseInput plugin, but allows users to select multiple points at once to focus on or delete.
 * @author aram-ap
 */
public class Marquee implements Plugin{
    /**
     * This initializes all plugin internals and adds the plugin's grid parent object.
     * The parent object is necessary when attaching a plugin to a JFXHeatmap, this is automatically called
     * when adding a plugin into a JFXHeatmap object
     *
     * @param grid Grid to attach plugin into
     */
    @Override
    public void init(JFXHeatmap grid) {
        TickListener.init(this);
    }

    /**
     * Properties will be unique to each plugin, its up to each plugin to add its own specific properties.
     *
     * @return Returns null if there is no properties map associated.
     */
    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    /**
     * Updates any property values that need to be updated throughout the application's lifespan.
     */
    @Override
    public void updateProperties() {

    }

    /**
     * Called at each update cycle.
     *
     * @param clock the JFXClock calling the tick
     */
    @Override
    public void update(JFXClock clock) {

    }
}
