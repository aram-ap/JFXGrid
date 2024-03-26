# JFXGrid (In-dev)
## A Fast and Customizable Heatmap Display For JavaFX 💥🚀
JFXGrid is a heatmap imaging library with focus on performance optimized real-time data visualization of Matrices at over 60 Hz update rates. This library was made as a solution to the ChartFx default heatmap implementation which I found unsuitable for displaying data at video-playback speeds (> 60 FPS). 

---

### How does it work?
- `JFXGrid` is the default Node which contains X/Y axis, data label, mouse pointer tools, and the heatmap image itself.
- Both the JFXGrid and Axis contain `Renderer` classes, which handle all drawing and visual updates.
- `Colorizer` is the class responsible for colored themes and turning data values into color. 
- `DataChunk` is the custom data group which contains a collection of data frames, and a pointer for going through the chunk in a timeline.
- `JFXDataset` is the encapsulating dataset which holds a DataChunk, and all the resources available to play the captured data in real time.
- `JFXDataDeque` is a type of dataset used for dynamic chunk loading. Its primary use is to reduce memory usage, enable binary data saving/parsing, and eventually asynchronous playback. 
- `JFXDatasetFactory` creates the dataset and imports data into the dataset. 
- `ImageGenerator` is a utility class that takes a dataset and colorizer and turns it into a bitmap image.
- `Plugin` objects are plug-in utilities that enable other functionality such as exporting data, zooming in and out, getting mouse cursor location and associalted values, averaging multiple frames together, and playing the frames in video playback

### Code Examples (still IN-DEV, so these are mostly non functional (for now):
Basics (32 x 32 grid): 
```
  Pane root = new Pane();
  JFXGrid grid = new JFXGrid(32, 32);
  root.getChildren().add(grid);
```
Adding data (based on previous example):
```
  //This library relies on the high speed performance of Oj-Algo, but has simple double[][] functionality as well.
  MatrixR032 testMatrix = MatrixR032.FACTORY.makeFilled(32, 32, RANDOM);
  DataChunk chunk = new DataChunk();
  chunk.add(testMatrix);

  //We add the 0 as a unique identifier primarily used in conjunction with many other datasets for sorting purposes
  JFXDataset data = new JFXDatasetFactory(0).add(chunk);
```

Stylizer - turning the grid black and white (based on previous examples):
```
  GridStylizer style = grid.getStylizer();
  style.setStyle(Style.MONOCHROME);
```
