module jfxgrid.samples.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires JFXGrid;

    opens jfxgrid.samples.demo to javafx.fxml;
    exports jfxgrid.samples.demo;
}