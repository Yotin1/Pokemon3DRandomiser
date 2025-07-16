module io.github.yotin1.p3drandomiser {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires com.fasterxml.jackson.databind;

    opens io.github.yotin1.p3drandomiser.gui to javafx.fxml;

    exports io.github.yotin1.p3drandomiser.gui;
    exports io.github.yotin1.p3drandomiser;
}
