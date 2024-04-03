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

import JFXGrid.events.JFXClock;
import JFXGrid.core.JFXGrid;
import JFXGrid.events.TickListener;

import java.util.HashMap;
import java.util.Map;

/**
 * VideoPlayer is an essential tool that provides video playback capabilities.
 * It manages updates, framerates, and data handling.
 * @author aram-ap
 */
public class VideoPlayer implements Plugin{
    private final Map<String, String> properties = new HashMap<>();
    private JFXGrid grid;
    private final JFXClock clock = new JFXClock();
    private boolean isPlaying = false;
    private long framenum;
    private long maxFrameNum;
    private double frameRateHZ;
    private double playbackSpeed = 1;
    private String units = "";

    public void GridPlayer() { }

    /**
     * This initializes all plugin internals and adds the plugin's grid parent object.
     * The parent object is necessary when attaching a plugin to a JFXGrid, this is automatically called
     * when adding a plugin into a JFXGrid object
     *
     * @param grid Grid to attach plugin into
     */
    @Override
    public void init(JFXGrid grid) {
        this.grid = grid;

        TickListener.init(this);
        properties.put("plugin", VideoPlayer.class.getName());
        updateProperties();
    }

    /**
     * Properties will be unique to each plugin, its up to each plugin to add its own specific properties.
     *
     * @return Returns null if there is no properties map associated.
     */
    @Override
    public Map<String, String> getProperties() {
        updateProperties();
        return properties;
    }

    /**
     * Updates any property values that need to be updated throughout the application's lifespan.
     */
    @Override
    public void updateProperties() {
        properties.put("frame", String.valueOf(framenum));
    }

    /**
     * Sets the current frame number
     * @param num the frame number to go to
     * @throws IllegalArgumentException if given an input < 0
     */
    public void setFrameNum(long num) {
        if(num < 0) {
            throw new IllegalArgumentException("Cannot set frame numbers less than 0!");
        } else if (num > maxFrameNum) {
            framenum = maxFrameNum;
        }

    }

    /**
     * Iterates through frames according to playback speed.
     */
    public void play() {
        JFXClock.get(); //We use this to ensure that the clock is ticking while we play frames.
        isPlaying = true;
    }

    /**
     * Sets playback speed to 0
     */
    public void pause() {

    }

    /**
     * Increases frame number by one
     */
    public void increment() {
        if(grid == null || grid.getData() == null) {
            return;
        }

        grid.getData().stepForward();
        framenum = grid.getData().getFrameNum();
    }

    /**
     * Decreases frame number by one
     */
    public void decrement() {
        if(grid == null || grid.getData() == null) {
            return;
        }

        grid.getData().stepBack();
        framenum = grid.getData().getFrameNum();
    }

    /**
     * Sets the playback speed in relation to the given frame rate (e.g. 1.5 -> 150% the speed, 0.5 -> 50% speed, -1 -> backwards)
     * @param speed framerate multiplier
     */
    public void setPlaybackSpeed(double speed) {
        this.playbackSpeed = speed;
    }

    /**
     * Used when dealing with framerates with data that was shot with a specific/unconventional framerate that you want to be played back with
     * its original speed (e.g. 250MHz, 1Hz, 0.05Hz, ...)
     *
     * NOTE: For speeds above a computer display's rated framerate (usually around 60 Hz), frames WILL be skipped.
     * @param hz how many frames played each second. Set to a value <= 0 for playback proportional to computer processing time.
     */
    public void setFrameRateHz(double hz) {
        this.frameRateHZ = hz;
    }

    /**
     * Each frame's time value (e.g. S -> seconds, Ms -> Milliseconds, (insert here))
     * @param timeUnit
     */
    public void setFrameUnit(String timeUnit) {
        if(timeUnit == null)
            return;

        units = timeUnit;
    }

    /**
     * Called at each render cycle.
     *
     * @param clock the JFXClock calling the tick
     */
    @Override
    public void update(JFXClock clock) {
        if(grid == null) {
            return;
        }

        var dataset = grid.getData();
        if(dataset == null) {
            return;
        }
    }

    @Override
    public void updateFixed(JFXClock clock) {
        if(isPlaying) {
            increment();
        }
    }
}
