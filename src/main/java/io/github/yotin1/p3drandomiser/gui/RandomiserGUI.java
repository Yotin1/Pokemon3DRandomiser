package io.github.yotin1.p3drandomiser.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * An object representing the gui launcher.
 *
 */
public class RandomiserGUI extends Application {

    private static Scene scene;
    // FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RandomiserGUI.class.getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Pokemon 3D Randomiser");
        stage.setOnHidden(e -> controller.shutdown());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}