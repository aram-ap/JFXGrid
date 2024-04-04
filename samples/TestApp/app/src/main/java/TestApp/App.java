/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package TestApp;
import JFXGrid.core.JFXGrid;
import JFXGrid.data.JFXDatasetFactory;
import JFXGrid.events.JFXClock;
import JFXGrid.plugin.VideoPlayer;
import JFXGrid.util.Style;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ojalgo.matrix.MatrixR032;
import org.ojalgo.random.Uniform;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        //Here we create the heatmap
        var heatmap = new JFXGrid();

        //Here we initialize the video player plugin
        var player = new VideoPlayer();

        //When adding plugins, its quite easy like this
        heatmap.addPlugin(player);

        //When changing the look of the grid, use the .getStylizer() method and whatever setting you want changed
        heatmap.getStylizer().setStyle(Style.DUOTONE);
        heatmap.getStylizer().setShowLines(false);
        heatmap.setKeepAspect(false);

        //The grid is a subclass of the regular JavaFX Node, so you can do the same types of actions you would do with other nodes.
        heatmap.setPrefHeight(300);
        heatmap.setPrefWidth(300);

//        Here we're using the JFXDatasetFactory to create some sample data.
        int rows = 400, cols = 400;
        var dataFactory = new JFXDatasetFactory(rows, cols);
        for(int i = 0; i < 1000; i++) {
            dataFactory.add(MatrixR032.FACTORY.makeFilled(rows, cols, Uniform.standard()).toRawCopy1D());
        }

        //Uncomment this for a smiley face
        /*
        var matrix = MatrixR032.FACTORY.makeDense(10, 10);
        matrix.set(2, 4, 1);
        matrix.set(2, 6, 1);

        matrix.set(5, 3, 1);
        matrix.set(6, 4, 1);
        matrix.set(6, 5, 1);
        matrix.set(6, 6, 1);
        matrix.set(5,7,1);

        var dataFactory = new JFXDatasetFactory(10, 10).add(matrix.toRawCopy1D());
         */

        //Now we just add the data to the grid
        heatmap.setData(dataFactory.build());

        //This is somewhat redundant as the default fps cap is 100, but this shows that you can set it to a value.
        //This value directly dictates the tick rate of the fixedUpdate() calls
        JFXClock.get().setFpsCap(100);

        //You can add other processes to fixed tick function like this
        //Uncomment this to print out fps
//        JFXClock.get().addFixedTickListener(() -> {
//            if(heatmap.getData().getFrameNum()%7 == 0) {
//                System.out.println("FPS: " + heatmap.getRendererFPS());
//            }
//        });

        //Starts incrementing through the frames
        player.play();

        Scene scene = new Scene(heatmap);

        stage.setScene(scene);
        stage.setWidth(600);
        stage.setHeight(400);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        //We call shutdown to end all running processes.
        JFXGrid.shutdown();
    }
}
