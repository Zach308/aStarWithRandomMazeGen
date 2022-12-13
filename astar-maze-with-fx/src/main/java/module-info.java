module com.amazefx {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    opens com.amazefx.GUI to javafx.fxml;
    exports com.amazefx.GUI;
}
