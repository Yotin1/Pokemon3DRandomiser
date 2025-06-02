module io.github.yotin1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens io.github.yotin1 to javafx.fxml;
    exports io.github.yotin1;
}
