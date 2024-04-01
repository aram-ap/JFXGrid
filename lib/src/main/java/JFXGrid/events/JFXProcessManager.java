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

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class JFXProcessManager implements TickListener{
    private static final ExecutorService imageThread;
    private static PriorityQueue<Runnable> processQueue;
    private static final JFXProcessManager processManager = new JFXProcessManager();


    static {
        imageThread = Executors.newSingleThreadExecutor();
        processQueue = new PriorityQueue<>();
        TickListener.init(processManager);
    }

    public static final void addTask(Runnable runnable, int priority) {
        processQueue.add(runnable);
        imageThread.execute(runnable);
    }

    public static void processNext() throws Exception {

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
