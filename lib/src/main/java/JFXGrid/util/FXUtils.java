package JFXGrid.util;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Originally from https://www.github.com/fair-acc/chart-fx

/**
 * Small tool to execute/call JavaFX GUI-related code from potentially non-JavaFX thread (equivalent to old:
 * SwingUtilities.invokeLater(...) ... invokeAndWait(...) tools)
 *
 * @author rstein
 */
public final class FXUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FXUtils.class);

    public static void assertJavaFxThread() {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("access JavaFX from non-JavaFX thread - please fix");
        }
    }

    /**
     * If you run into any situation where all of your scenes end, the thread managing all of this will just peter out.
     * To prevent this from happening, add this line:
     */
    public static void keepJavaFxAlive() {
        Platform.setImplicitExit(false);
    }

    /**
     * Invokes a Runnable in JFX Thread and waits while it's finished. Like SwingUtilities.invokeAndWait does for EDT.
     *
     * @author hendrikebbers
     * @author rstein
     * @param function Runnable function that should be executed within the JavaFX thread
     * @throws Exception if a exception is occurred in the run method of the Runnable
     */
    public static void runAndWait(final Runnable function) throws Exception {
        if (Platform.isFxApplicationThread()) {
            function.run();
        } else {
            try {
                CompletableFuture.runAsync(function, Platform::runLater).get();
            } catch (ExecutionException ex) {
                throw unwrapExecutionException(ex);
            }
        }
    }

    /**
     * Invokes a Runnable in JFX Thread and waits while it's finished. Like SwingUtilities.invokeAndWait does for EDT.
     *
     * @author hendrikebbers
     * @author rstein
     * @param function Supplier function that should be executed within the JavaFX thread
     * @param <R> generic for return type
     * @return function result of type R
     * @throws Exception if a exception is occurred in the run method of the Runnable
     */
    public static <R> R runAndWait(final Supplier<R> function) throws Exception {
        try {
            return Platform.isFxApplicationThread() ? function.get() : CompletableFuture.supplyAsync(function, Platform::runLater).get();
        } catch (ExecutionException ex) {
            throw unwrapExecutionException(ex);
        }
    }

    /**
     * Invokes a Runnable in JFX Thread and waits while it's finished. Like SwingUtilities.invokeAndWait does for EDT.
     *
     * @author hendrikebbers, original author
     * @author rstein, extension to Function, Supplier, Runnable
     * @param argument function argument
     * @param function transform function that should be executed within the JavaFX thread
     * @param <T> generic for argument type
     * @param <R> generic for return type
     * @return function result of type R
     * @throws Exception if an exception occurred in the run method of the Runnable
     */
    public static <T, R> R runAndWait(final T argument, final Function<T, R> function) throws Exception {
        try {
            return Platform.isFxApplicationThread() ? function.apply(argument) : CompletableFuture.supplyAsync(() -> function.apply(argument), Platform::runLater).get();
        } catch (ExecutionException ex) {
            throw unwrapExecutionException(ex);
        }
    }

    private static Exception unwrapExecutionException(ExecutionException ex) {
        // Unwrap original cause to match previous unit tests
        if (ex.getCause() instanceof Exception) {
            return (Exception) ex.getCause();
        }
        return ex;
    }

    public static void runFX(final Runnable run) {
        // FXUtils.keepJavaFxAlive();
        if (Platform.isFxApplicationThread()) {
            run.run();
        } else {
            Platform.runLater(run);
        }
    }

    public static boolean waitForFxTicks(final Scene scene, final int nTicks) {
        return waitForFxTicks(scene, nTicks, -1);
    }

    public static boolean waitForFxTicks(final Scene scene, final int nTicks, final long timeoutMillis) { // NOPMD
        if (Platform.isFxApplicationThread()) {
            for (int i = 0; i < nTicks; i++) {
                Platform.requestNextPulse();
            }
            return true;
        }
        final Timer timer = new Timer("FXUtils-thread", true);
        final AtomicBoolean run = new AtomicBoolean(true);
        final AtomicInteger tickCount = new AtomicInteger(0);
        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();

        final Runnable tickListener = () -> {
            if (tickCount.incrementAndGet() >= nTicks) {
                lock.lock();
                try {
                    run.getAndSet(false);
                    condition.signal();
                } finally {
                    run.getAndSet(false);
                    lock.unlock();
                }
            }
            Platform.requestNextPulse();
        };

        lock.lock();
        try {
            FXUtils.runAndWait(() -> scene.addPostLayoutPulseListener(tickListener));
        } catch (final Exception e) {
            // cannot occur: tickListener is always non-null and
            // addPostLayoutPulseListener through 'runaAndWait' always executed in JavaFX thread
            LOGGER.atError().setCause(e).log("addPostLayoutPulseListener interrupted");
        }
        try {
            Platform.requestNextPulse();
            if (timeoutMillis > 0) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        LOGGER.atWarn().log("FXUtils::waitForTicks(..) interrupted by timeout");

                        lock.lock();
                        try {
                            run.getAndSet(false);
                            condition.signal();
                        } finally {
                            run.getAndSet(false);
                            lock.unlock();
                        }
                    } }, timeoutMillis);
            }
            while (run.get()) {
                condition.await();
            }
        } catch (final InterruptedException e) {
            LOGGER.atError().setCause(e).log("await interrupted");
        } finally {
            lock.unlock();
            timer.cancel();
        }
        try {
            FXUtils.runAndWait(() -> scene.removePostLayoutPulseListener(tickListener));
        } catch (final Exception e) {
            // cannot occur: tickListener is always non-null and
            // removePostLayoutPulseListener through 'runaAndWait' always executed in JavaFX thread
            LOGGER.atError().setCause(e).log("removePostLayoutPulseListener interrupted");
        }

        return tickCount.get() >= nTicks;
    }

    // Similar to internal Pane::setConstraint
    public static <NODE extends Node> NODE setConstraint(NODE node, Object key, Object value) {
        if (value == null) {
            node.getProperties().remove(key);
        } else {
            Object old = node.getProperties().put(key, value);
            if (Objects.equals(old, value)) {
                return node; // No changes -> no need to force a layout
            }
        }
        if (node.getParent() != null) {
            node.getParent().requestLayout();
        }
        return node;
    }

    // Similar to nternal Pane::getConstraint
    public static Object getConstraint(Node node, Object key) {
        if (node.hasProperties()) {
            Object value = node.getProperties().get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public static <T extends Object> List<T> sizedList(List<T> list, int desiredSize, Supplier<T> constructor) {
        int delta = desiredSize - list.size();
        if (delta == 0) {
            return list;
        }
        while (delta > 0) {
            list.add(constructor.get());
            delta--;
        }
        while (delta < 0) {
            list.remove(list.size() - 1);
            delta++;
        }
        return list;
    }

    /**
     * Binds the managed property to visible and returns the visibility
     * property. The effect is similar to removing the node from the SceneGraph
     * when it is not visible.
     *
     * @param node node
     * @return visibility property
     */
    public static BooleanProperty bindManagedToVisible(Node node) {
        node.managedProperty().bind(node.visibleProperty());
        return node.visibleProperty();
    }

    public static ObservableBooleanValue getShowingBinding(Node node) {
        BooleanProperty showing = new SimpleBooleanProperty();
        Runnable update = () -> showing.set(Optional.ofNullable(node.getScene()).flatMap(scene -> Optional.ofNullable(scene.getWindow())).map(Window::isShowing).orElse(false));
        update.run(); // initial value

        ChangeListener<Boolean> onShowingChange = (obs, old, value) -> {
            update.run();
        };

        ChangeListener<Window> onWindowChange = (obs, old, value) -> {
            if (old != null)
                old.showingProperty().removeListener(onShowingChange);
            if (value != null)
                value.showingProperty().addListener(onShowingChange);
            update.run();
        };

        node.sceneProperty().addListener((obs, old, value) -> {
            if (old != null)
                old.windowProperty().removeListener(onWindowChange);
            if (value != null)
                value.windowProperty().addListener(onWindowChange);
            update.run();
        });

        return showing;
    }

    /**
     * Utility method for registering pre-layout and post-layout hooks.
     * Each JavaFX tick is executed in phases:
     * <p>
     * 1) animations/timers, e.g., Platform.runLater()
     * 2) pre-layout hook
     * 3) CSS styling pass (styling etc. gets updated)
     * 4) layout pass (layoutChildren)
     * 5) post-layout hook
     * 6) update bounds
     * 7) copy dirty node changes to the rendering thread
     * <p>
     * Drawing inside layout children is problematic because
     * the layout may be recursive and can result in many
     * unnecessary drawing operations.
     * <p>
     * The layout hooks will be executed every time there is a pulse,
     * (e.g. renders or mouse press events), but they do not trigger
     * a pulse by themselves.
     * <p>
     * This class registers actions as soon as a Scene is available,
     * and unregisters them when the Scene is removed. Note that the
     * Scene is set during the CSS phase, so the first execution is
     * triggered immediately (still before CSS application).
     */
    public static void registerLayoutHooks(Node node, Runnable preLayoutAction, Runnable postLayoutAction) {
        node.sceneProperty().addListener((observable, oldScene, scene) -> {
            // Remove from the old scene
            if (oldScene != null) {
                oldScene.removePreLayoutPulseListener(preLayoutAction);
                oldScene.removePostLayoutPulseListener(postLayoutAction);
            }

            // Register when the scene changes. The scene reference gets
            // set at the beginning of the CSS phase, so the registration
            // already missed this pulse. However, since the CSS hasn't
            // been applied yet, we can get a similar effect by forcing
            // a manual run now.
            if (scene != null) {
                scene.addPreLayoutPulseListener(preLayoutAction);
                scene.addPostLayoutPulseListener(postLayoutAction);
                preLayoutAction.run();
            }
        });
    }

    public static Group createUnmanagedGroup() {
        final Group group = new Group();
        group.setManaged(false);
        group.relocate(0, 0);
        return group;
    }
}