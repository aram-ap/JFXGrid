package JFXGrid.events;

import java.util.ArrayList;

/**
 *  Interface for all objects that utilize the JFXClock tick mechanism
 */
public interface TickListener {
    ArrayList<TickListener> listeners = new ArrayList<>();
    JFXClock clock = new JFXClock();

    /**
     * Adds a tick listener to the list of listeners.
     * @param listener
     */
    static void init(TickListener listener) {
        listeners.add(listener);
    }

    /**
     * Sends a tick to all tick listeners.
     */
    static void tick() {
        listeners.forEach((listener) -> listener.update(clock));
    }

    /**
     * Called at each update cycle.
     * @param clock the JFXClock calling the tick
     */
    void update(JFXClock clock);
}
