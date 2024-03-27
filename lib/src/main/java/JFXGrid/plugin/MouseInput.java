package JFXGrid.plugin;

import JFXGrid.javafx.JFXGrid;

import java.util.Map;

public class MouseInput implements Plugin {
    /**
     * This initializes all plugin internals and adds the plugin's grid parent object.
     * The parent object is necessary when attaching a plugin to a JFXGrid, this is automatically called
     * when adding a plugin into a JFXGrid object
     *
     * @param grid Grid to attach plugin into
     */
    @Override
    public void init(JFXGrid grid) {

    }

    /**
     * This is called at each update cycle
     */
    @Override
    public void update() {

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
}
