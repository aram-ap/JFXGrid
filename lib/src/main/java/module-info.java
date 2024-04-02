module JFXGrid {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires ojalgo;
    requires org.slf4j;
    requires org.apache.commons.lang3;

    exports JFXGrid.core;
    exports JFXGrid.util;
    exports JFXGrid.plugin;
    exports JFXGrid.data;
    exports JFXGrid.events;
    exports JFXGrid.renderer;
}