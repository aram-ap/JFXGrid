# JFXGrid
## A Fast and Customizable Heatmap Display For JavaFX 💥🚀
JFXGrid is a heatmap imaging library with focus on performance optimized real-time data visualization of Matrices at over 60 Hz update rates. This library was made as a solution to the ChartFx default heatmap implementation which I found unsuitable for displaying data at video-playback speeds (> 60 FPS). 

---

### How does it work?
- `JFXGrid` is the default Node which contains X/Y axis, data label, mouse pointer tools, and the heatmap image itself.
- Both the JFXGrid and Axis contain `Renderer` classes, which handle all drawing and visual updates.
- `Colorizer` is the class responsible for colored themes and turning data values into color. 
- `DataChunk` is the custom data group which contains a collection of data frames, and a pointer for going through the chunk in a timeline.
- `JFXDataset` is a custom encapsulating dataset,
- `ImageGenerator` is a newly implemented class that takes a dataset and colorizer and turns it into an image.
