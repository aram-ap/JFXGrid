module JFXGrid {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires ojalgo;
    requires org.slf4j;

    exports JFXGrid.core;
    exports JFXGrid.util;
    exports JFXGrid.plugin;
    exports JFXGrid.data;
    exports JFXGrid.events;
    exports JFXGrid.renderer;
}