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
import java.util.concurrent.atomic.AtomicInteger;

import JFXGrid.core.JFXHeatmap;

/**
 * This class manages updates. It includes features like setting an FPS Cap, getting delta time in MS and Nano,
 * and will automatically render every JFX Grid given to it.
 *
 * @author Aram Aprahamian
 */
public class JFXClock {
    private static JFXClock INSTANCE;

    //The fps cap which is used by the fixedUpdate() call for specific timing purposes
    private AtomicInteger fpsCap = new AtomicInteger(100);

    //This just activates/deactivates the clock ticking mechanism.
    private boolean isRunning = false;

    //This tracks the amount of time passed between each frame. Its important when trying to keep to a specific playback rate.
    private double deltaTimeMS = System.currentTimeMillis();

    //A more precise version of deltaTimeMS, using nanoseconds instead of milliseconds
    private long lastTimeNano = System.nanoTime();

    //This is used to keep fixed render calls working at the correct time
    private double lastFixedTimeMS = System.currentTimeMillis();
    private boolean clockThreadActive = false;
    private ArrayList<Runnable> externalRunnables = new ArrayList<>();

    public static synchronized JFXClock get() {
        if (INSTANCE == null) {
            INSTANCE = new JFXClock();
        }
        return INSTANCE;
    }

    /**
     * Starts up the clock ticking mechanism
     */
    public void start() {
        if(!isRunning)  {
            isRunning = true;
        }

        run();
    }

    /**
     * Changes whether or not the clock is running
     * @param run true to enable clock ticks
     */
    public void setRunning(boolean run) {
        if(isRunning != run) {
            isRunning = run;
            if(clockThreadActive && !run) {
                clockThreadActive = false;
            }
        }

        run();
    }

    /**
     * @return true if the clock is ticking
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Gets the current frame rate based on the time between each frame.
     * @return
     */
    public float getFps() {
        return (float) (1000/deltaTimeMS);
    }

    /**
     * @return The current fps cap
     */
    public int getFpsCap() {
        return fpsCap.get();
    }

    /**
     * @param fps Sets the value of the fps cap.
     */
    public void setFpsCap(int fps) {
        if(fps >= 0) {
            this.fpsCap.set(fps);
        }
    }

    /**
     * @return time in milliseconds between each frame
     */
    public double getDeltaTimeMS() {
        return deltaTimeMS;
    }

    /**
     * @return time in nanoseconds between each frame
     */
    public double getPreciseDeltaTime() {
        return System.nanoTime()-lastTimeNano;
    }


    /**
     * @return current system time in milliseconds
     */
    public double currTimeMS() {
        return System.currentTimeMillis();
    }

    /**
     * Attaches a runnable to this clock's fixed tick.
      * @param runnable
     */
    public void addFixedTickListener(Runnable runnable) {
        externalRunnables.add(runnable);
    }

    /**
     * The tick implementation which handles updating each heatmap and plugin.
     * @throws Exception
     */
    private void tick() throws Exception {
        var currentNano = System.nanoTime();

        deltaTimeMS = ((double) currentNano - lastTimeNano)/1_000_000d;
        lastTimeNano = currentNano;

        TickListener.tick(INSTANCE);
    }

    /**
     * Fixed-time tick calls, called at a rate according to the fps cap
     * @throws Exception
     */
    private void tickFixed() throws Exception {
        TickListener.tickFixed(INSTANCE);
        externalRunnables.forEach(Runnable::run);
        lastFixedTimeMS = System.currentTimeMillis();
    }


    /**
     * Initializes the clock ticking mechanism
     */
    private void run() {
        if(clockThreadActive) {
            return;
        }

        Runnable clockRunnable = () -> {
            long minTimeBetweenFramesMS = (long)1000/INSTANCE.fpsCap.get();
            while(isRunning) {
                try {
                    tick();

                    if(minTimeBetweenFramesMS <= (System.currentTimeMillis() - lastFixedTimeMS)) {
                        minTimeBetweenFramesMS = (long)1000/INSTANCE.fpsCap.get();
                        tickFixed();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        };

        Thread clockBackgroundThread = new Thread(clockRunnable);
        clockBackgroundThread.start();
        clockThreadActive = true;
    }


}
