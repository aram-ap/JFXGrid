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

import JFXGrid.JFXGrid;

/**
 * This class manages updates. It includes features like setting an FPS Cap, getting delta time in MS and Nano,
 * and will automatically update every JFX Grid given to it.
 *
 * @author aram-ap
 */
public class JFXClock {
    private static boolean useFpsCap = true;
    private static boolean isRunning = false;
    private static double deltaTimeMS = 0d;
    private static long lastTimeNano = System.nanoTime();
    private static int fpsCap = 100;
    private static final ArrayList<JFXGrid> gridsToClock = new ArrayList<>();

    public static void add(JFXGrid grid) {
        if(grid == null) {
            return;
        }

        gridsToClock.add(grid);
    }

    public static void start() {
        if(!isRunning)  {
            isRunning = true;
        }

        initClock();
    }
    public static void setRunning(boolean run) {
        if(isRunning != run) {
            isRunning = run;
            initClock();
        }
    }

    public static boolean isRunning() {
        return isRunning;
    }

    private static boolean initClock() {
        while(isRunning) {
            try {
                tick();

                if(useFpsCap) {
                    long minTimeBetweenFramesMS = (long)1000/fpsCap;
                    long sleepDuration = (long) (minTimeBetweenFramesMS - deltaTimeMS);

                    if(sleepDuration > 0) {
                        Thread.sleep(minTimeBetweenFramesMS);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static float getFps() {
        return (float) (1000/deltaTimeMS);
    }

    public static int getFpsCap() {
        return fpsCap;
    }

    public static boolean fpsIsCapped() {
        return useFpsCap;
    }

    public static void setFpsIsCapped(boolean isCapped) {
        useFpsCap = isCapped;
    }

    public static void setFpsCap(int fpsCap) {
        if(fpsCap >= 0) {
            JFXClock.fpsCap = fpsCap;
        }
    }

    public static void tick() throws Exception {
        var currentNano = System.nanoTime();

        deltaTimeMS = ((double) currentNano - lastTimeNano)/1000000d;
        lastTimeNano = currentNano;
        gridsToClock.forEach(JFXGrid::update);

        JFXProcessManager.processNext();
    }

    public static double getDeltaTimeMS() {
        return deltaTimeMS;
    }

    public static double getPreciseDeltaTime() {
        return System.nanoTime()-lastTimeNano;
    }
}
