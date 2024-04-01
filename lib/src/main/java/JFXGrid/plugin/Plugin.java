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
package JFXGrid.plugin;

import JFXGrid.core.JFXHeatmap;

import java.util.Map;

/**
 * Plugins are useful tools to be utilized by the JFXHeatmap to add extra functionality.
 * @author aram-ap
 */
public interface Plugin {

    /**
     * This initializes all plugin internals and adds the plugin's grid parent object.
     * The parent object is necessary when attaching a plugin to a JFXHeatmap, this is automatically called
     * when adding a plugin into a JFXHeatmap object
     * @param grid Grid to attach plugin into
     */
    public void init(JFXHeatmap grid);

    /**
     * This is called at each update cycle
     */
    public void update();

    /**
     * Properties will be unique to each plugin, its up to each plugin to add its own specific properties.
     * @return Returns null if there is no properties map associated.
     */
    public Map<String, String> getProperties();

    /**
     * Updates any property values that need to be updated throughout the application's lifespan.
     */
    public void updateProperties();
}
