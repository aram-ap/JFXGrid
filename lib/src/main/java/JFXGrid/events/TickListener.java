package JFXGrid.events;

import java.util.ArrayList;

/**
 *  Interface for all objects that utilize the JFXClock tick mechanism
 */
public interface TickListener {
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
     * Sends a tick to all tick listeners.
     */
    static void tick(JFXClock clock) {
        listeners.forEach((listener) -> listener.update(clock));
    }

    /**
     * Sends a fixed time update to all listeners.
     */
    static void tickFixed(JFXClock clock) {
        listeners.forEach((listener -> listener.updateFixed(clock)));
    }

    /**
     * Called at each update cycle.
     * @param clock the JFXClock calling the tick
     */
    void update(JFXClock clock);
}
