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
package JFXGrid.events;

import java.util.ArrayList;

/**
 *  Interface for all objects that utilize the JFXClock tick mechanism.
 *  <br> NOTE: calling init(this) is required for all Tick Listeners that want updates
 *
 * @author Aram Aprahamian
 */
public interface TickListener {
    //Collection of all listeners initialized and listening to the TickListener.
    ArrayList<TickListener> listeners = new ArrayList<>();

    /**
     * Called at a fixed rate, capped the fps given to the clock. Left empty to keep it optional.
     * @param clock
     */
    default void updateFixed(JFXClock clock) { }

    /**
     * Adds a tick listener to the list of listeners.
     * @param listener
     */
    static void init(TickListener listener) {
        listeners.add(listener);
    }

    /**
     * Sends a tick to all tick listeners. Called at every frame.
     */
    static void tick(JFXClock clock) {
        listeners.forEach((listener) -> listener.update(clock));
    }

    /**
     * Sends a fixed time render to all listeners.
     */
    static void tickFixed(JFXClock clock) {
        listeners.forEach((listener -> listener.updateFixed(clock)));
    }

    /**
     * Called at each render cycle.
     * @param clock the JFXClock calling the tick
     */
    void update(JFXClock clock);
}
