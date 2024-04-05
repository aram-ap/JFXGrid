# JFXGrid (In-dev)
## A Fast and Customizable Heatmap Charting Library For JavaFX 💥🚀
JFXGrid is a heatmap imaging library with focus on performance-optimized real-time data visualization of Matrices at over 
60 Hz update rates. This library was made as a solution to the [ChartFx](https://github.com/fair-acc/chart-fx) default heatmap implementation which I found
unsuitable for displaying data at video-playback speeds (> 60 FPS). 

<figure>
  <img src="docs/photos/JFXGrid0-0-1.jpeg" alt="JFXGrid example" width=1200/>
</figure>


>**READ:** <br>
>This plugin is very much in the alpha stages of development, while I've been building up the framework of this library,
many features have not been implemented yet
or are just very premature.
---

### Why?
While developing an application for processing and visualizing LiDAR sensor information, I came into an issue where the current JavaFX Charting libraries available simply didn't have the performance target and other major requirements that I needed. My major points of issue with the available libraries (likely skill issues on my end) came to this:
- Libraries like [ChartFX](https://github.com/fair-acc/chart-fx) are beautiful with many features. However, its heatmap implementation didn't offer what I needed for my use case.
- Issues rendering with speeds > 30fps (it is designed for 25fps so totally within spec).
- Issues visually, such as refusing to display pixels as squares (this was so frustrating I decided to make this).
- When doing matrix mathematics I typically utilize OjAlgo matrices, so I implemented features to use it with ease.

>#### Note:
>- [ChartFX](https://github.com/fair-acc/chart-fx/) is an amazing library with lots of functionality and performance, and I would recommend it over this in most other circumstances.
>- There are some similarities with this library and ChartFX. I took inspiration for general structures and design patterns like separate data classes, data factories, and renderers. However, nearly all the code within this library is self-written.
>- This is currently in development while I port the code over from the application I originally made.
>- Going through this code, you're going to find areas where I haven't commented, likely some bugs, and some messy areas. I'm continuously growing this, so if you have any suggestions or have any questions, feel free to contact me or add a pull request!

---

### Installing: 
#### Temporary installation process:
First, download this github repo:
```
    git clone https://www.github.com/aram-ap/JFXGrid
```
go to the folder location of the library 
```
    cd /path/to/repo/JFXGrid
```
then run
```
    ./gradlew publishToMavenLocal
```

#### Gradle:
In your build.gradle, add this:
```
dependencies {
  implementation 'io.github.aram-ap:jfxgrid:0.0.1'
}
```
--- 

### How does it work?
- **Primary:**
  - `JFXGrid` is the default Node which contains X/Y axis, data label, mouse pointer tools, and the heatmap image itself.
  - `Plugin` objects are plug-in utilities that enable other functionality such as exporting data, zooming in and out, getting mouse cursor location and associated values, averaging multiple frames together, and playing the frames in video playback.
  - `Renderer` handles drawing onto displayed elements. It is split up two primary renderers: AxisRenderer and GridRenderer.
  - `Style` is where we bring in the specific color gradients used in visualizations.
  - `JFXDatasetFactory` creates the dataset and imports data into the dataset.
  - `JFXDataset` is the encapsulating dataset which holds a DataChunk, and all the resources available to play the captured data in real time.
  - `JFXDataDeque` is the child class of the JFXDataSet. Its purpose is to reduce memory utilization, allow for dynamic chunk loading/reloading Chunking to/from the local filesystem, and all other features of the default JFXDataSet class.
- **Other Javafx Nodes:**
  - `Axis` the node containing the canvas used for displaying axis lines.
  - `JFXColorBar` the node containing both an axis and linear gradient for showcasing values.
- **Background (typically not touched by the user):** 
  - `DataChunk` is the custom data group which contains a collection of data frames, and a pointer for going through the chunk in a timeline.
  - `ImageGenerator` is a utility class that takes a dataset and colorizer and turns it into a bitmap image.
  - `JFXClock` is utilized in plugins allowing for timed updates. It includes functionalities such as obtaining delta time in ms and ns and capping frames per second.
  - `TickListener` is an interface utilized by the JFXGrid and plugins for timing purposes. It keeps separate timer thread which calls at each frame cycle. It also contains a fixed update call that maintains constant timing which is especially helpful for data playback at a specific framerate.
  - `JFXProcessManager` is the background process manager which synchronizes all background worker thread runnables used in JFXGrid.
- **Plugins:**
  - `VideoPlayer` is the primary plugin that allows for playing frames in video-format. It handles frame iteration, timing, pausing, and playing.
  - `Zoomer` allows instantaneous resizing of the grid.
  - `Exporter` handles file output and screenshots
  - `Marquee` is a tool used for zooming into specific points and disabling specific elements
  - `MouseInput` is a plugin built into many plugins that allow mouse inputs to cause actions
  - `Accumulator` is an image processing tool which takes multiple frames and averages them into a single frame. Best used when you're dealing with sparse matrices.

### Code Examples:
The basics (32 x 32 grid): 
```
  JFXGrid grid = new JFXGrid(32, 32); //The central JFXGrid javafx node
  Pane root = new Pane(grid);
```
Adding data (based on previous example):
```
  //OjAlgo's matrices are used in this as an alternative to using double[][] arrays. Regular double[][] arrays are the standard though. 
  MatrixR032 testMatrix = MatrixR032.FACTORY.makeFilled(32, 32, Uniform.standard());
  
  //Here we initialize the factory which builds our datasets with 32 rows, 32 columns
  JFXDatasetFactory dataFactory = new JFXDatasetFactory(32, 32); 
  dataFactory.add(testMatrix);
  
  //Call the .build() method after you have added all the data you want.
  grid.setData(dataFactory.build()); 
```

Stylizer - turning the grid black and white (based on previous examples):
```
  GridStylizer style = grid.getStylizer();
  style.setStyle(Style.DUOTONE);
```
Plugins - adding video playback functionality:
```
  //Here we initialize the video player plugin
  int rows = 32, cols = 32;
  VideoPlayer player = new VideoPlayer();
  
  //All you have to do is add the plugin into the JFXGrid. Simple :)
  grid.addPlugin(player); 
  
  //The clock automatically defaults to 100 FPS, but setting this here allows you to change video playback to whatever frame rate you want.
  JFXClock.get().setFpsCap(100);
  
  //Here we make 1000 matrix frames all randomized. 
  JFXDatasetFactory videoFactory = new JFXDatasetFactory(rows, cols);
  for(int i = 0; i < 1000; i++) {
    videoFactory.add(MatrixR032.FACTORY.makeFilled(rows, cols, Uniform.standard())); 
  }
  
  //Note that the data factories need to be built with the .build() method before processing it as a dataset
  grid.setData(videoFactory.build());
  
  //Starts the video player.
  player.play();
```
