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
import JFXGrid.events.JFXClock;
import JFXGrid.events.TickListener;

import java.util.Map;

/**
 * This averaging plugin essentially obtains the average over (n) amount of frames,
 * which replaces the regular single frame view. Mostly used when trying to observe large
 * frame caches that are generally sparse in data.
 * @author aram-ap
 */
public class Averaging implements Plugin{

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
     * Called at each render cycle.
     *
     * @param clock the JFXClock calling the tick
     */
    @Override
    public void update(JFXClock clock) {

    }
}
