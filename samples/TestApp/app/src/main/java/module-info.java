module TestApp{
    requires javafx.fxml;
    requires javafx.controls;
    requires ojalgo;
    requires org.slf4j;
    requires JFXGrid;

    opens TestApp to javafx.fxml, javafx.controls, JFXGrid;
    exports TestApp;
}