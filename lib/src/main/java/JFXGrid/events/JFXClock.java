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

import JFXGrid.core.JFXHeatmap;

/**
 * This class manages updates. It includes features like setting an FPS Cap, getting delta time in MS and Nano,
 * and will automatically update every JFX Grid given to it.
 *
 * @author aram-ap
 */
public class JFXClock {
    private static JFXClock INSTANCE = new JFXClock();

    //This keeps the fps contained to a maximum value. It's best practice to keep this capped as uncapped use will max out CPU usage.
    //Additionally, your monitor can only display so many frames per second (usually between 60-120 hz), so any higher won't do anything visually.
    private static boolean useFpsCap = true;

    //The fps cap if useFpsCap is enabled
    private static int fpsCap = 100;

    //This just activates/deactivates the clock ticking mechanism.
    private static boolean isRunning = false;

    //This tracks the amount of time passed between each frame. Its important when trying to keep to a specific playback rate.
    private static double deltaTimeMS = System.currentTimeMillis();

    //A more precise version of deltaTimeMS, using nanoseconds instead of milliseconds
    private static long lastTimeNano = System.nanoTime();

    /**
     * Starts up the clock ticking mechanism
     */
    public static void start() {
        if(!isRunning)  {
            isRunning = true;
        }

        run();
    }

    /**
     * Changes whether or not the clock is running
     * @param run true to enable clock ticks
     */
    public static void setRunning(boolean run) {
        if(isRunning != run) {
            isRunning = run;
        }

        run();
    }

    /**
     * @return true if the clock is ticking
     */
    public static boolean isRunning() {
        return isRunning;
    }

    /**
     * Gets the current frame rate based on the time between each frame.
     * @return
     */
    public static float getFps() {
        return (float) (1000/deltaTimeMS);
    }

    /**
     * @return The current fps cap
     */
    public static int getFpsCap() {
        return fpsCap;
    }

    /**
     * @return True if the fps is capped to a certain value
     */
    public static boolean fpsIsCapped() {
        return useFpsCap;
    }

    /**
     * @param isCapped Sets whether or not the fps value is capped.
     */
    public static void setFpsIsCapped(boolean isCapped) {
        useFpsCap = isCapped;
    }

    /**
     * @param fpsCap Sets the value of the fps cap.
     */
    public static void setFpsCap(int fpsCap) {
        if(fpsCap >= 0) {
            JFXClock.fpsCap = fpsCap;
        }
    }

    /**
     * @return time in milliseconds between each frame
     */
    public static double getDeltaTimeMS() {
        return deltaTimeMS;
    }

    /**
     * @return time in nanoseconds between each frame
     */
    public static double getPreciseDeltaTime() {
        return System.nanoTime()-lastTimeNano;
    }


    /**
     * @return current system time in milliseconds
     */
    public static double currTimeMS() {
        return System.currentTimeMillis();
    }

    /**
     * The tick implementation which handles updating each heatmap and plugin.
     * @throws Exception
     */
    private static void tick() throws Exception {
        var currentNano = System.nanoTime();

        deltaTimeMS = ((double) currentNano - lastTimeNano)/1000000d;
        lastTimeNano = currentNano;

        TickListener.tick(INSTANCE);
        JFXProcessManager.processNext();
    }

    /**
     * Initializes the clock ticking mechanism
     */
    private static void run() {
        long minTimeBetweenFramesMS = (long)1000/fpsCap;

        Runnable clockRunnable = () -> {
            while(isRunning) {
                try {
                    if(useFpsCap && minTimeBetweenFramesMS < deltaTimeMS) {
                        tick();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        };

        Thread clockBackgroundThread = new Thread(clockRunnable);
        clockBackgroundThread.run();
    }
}
