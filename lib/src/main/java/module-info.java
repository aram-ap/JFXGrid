module JFXGrid {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.desktop;

    exports JFXGrid;
    exports JFXGrid.core;
    exports JFXGrid.util;
    exports JFXGrid.javafx;
    exports JFXGrid.plugin;
    exports JFXGrid.data;
    exports JFXGrid.events;
    exports JFXGrid.renderer;
}