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

import javafx.application.Platform;

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * The JFXProcessManager handles adding all JFXGrid-esc background worker calls for processing. Ensures all added tasks
 * run in correct order. This also limits the effects background processes may have on the JavaFX thread, keeping the
 * program running smoothly.
 *
 * @author Aram Aprahamian
 */
public class JFXProcessManager implements TickListener {
    private static final ExecutorService workerThread;
    private static PriorityQueue<Runnable> processQueue;
    private static final JFXProcessManager processManager = new JFXProcessManager();


    static {
        workerThread = Executors.newSingleThreadExecutor();
        processQueue = new PriorityQueue<>();
        TickListener.init(processManager);
    }

    /**
     * Adds a runnable task onto the worker thread queue
     * @param runnable
     */
    public static void addTask(Runnable runnable) {
        processQueue.add(runnable);
    }

    /**
     * Adds a task onto the JavaFX thread. Use this for everything related to JavaFX nodes.
     * @param runnable
     */
    public static void addFXTask(Runnable runnable) {
        Platform.runLater(runnable);
    }

    /**
     * Processes the next runnable in the queue
     * @throws Exception
     */
    public static void processNext() throws Exception {
        if(processQueue.isEmpty()) {
            return;
        }

        workerThread.execute(processQueue.poll());
    }

    /**
     * Called at each update cycle.
     *
     * @param clock the JFXClock calling the tick
     */
    @Override
    public void update(JFXClock clock) {
        try {
            processNext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
