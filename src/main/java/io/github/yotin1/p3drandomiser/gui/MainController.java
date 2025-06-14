package io.github.yotin1.p3drandomiser.gui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import io.github.yotin1.p3drandomiser.Randomiser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class MainController {

    // private String node = "io.github.yotin1.p3drandomiser.gui.MainController";
    private String initialDirectory;

    @FXML
    private ScrollPane root;

    @FXML
    private CheckBox randomiseWild;
    @FXML
    private CheckBox randomiseStatic;
    @FXML
    private CheckBox randomiseRoaming;

    @FXML
    private CheckBox randomiseTrainers;
    @FXML
    private CheckBox rivalKeepStarter;
    @FXML
    private CheckBox rivalStarterEvolves;

    @FXML
    private CheckBox randomiseStarters;
    @FXML
    private CheckBox randomiseTrades;
    @FXML
    private CheckBox randomiseGifts;
    @FXML
    private CheckBox randomiseGameCorner;

    @FXML
    private CheckBox learnHMs;

    @FXML
    private CheckBox gen1;
    @FXML
    private CheckBox gen2;
    @FXML
    private CheckBox gen3;
    @FXML
    private CheckBox gen4;
    @FXML
    private CheckBox gen5;
    @FXML
    private CheckBox gen6;
    @FXML
    private CheckBox gen7;
    @FXML
    private CheckBox gen8;
    @FXML
    private CheckBox regionalForms;

    @FXML
    private CheckBox hgssMusic;
    @FXML
    private CheckBox overworldSprites;

    @FXML
    private TextField p3DDirectory;
    @FXML
    private Button p3DDirectoryBtn;
    @FXML
    private TextField gamemodeName;
    @FXML
    private TextField seed;

    @FXML
    protected void initialize() {

        loadSettings();
    }

    public void shutdown() {

        saveSettings();
    }

    protected void saveSettings() {

        Preferences settings = Preferences.userRoot().node(this.getClass().getName());
        settings.put("p3DDirectory", initialDirectory);
    }

    protected void loadSettings() {

        Preferences settings = Preferences.userRoot().node(this.getClass().getName());
        initialDirectory = settings.get("p3DDirectory", null);
        p3DDirectory.setText(initialDirectory);
    }

    @FXML
    protected void trainerCheckBoxes() {
        rivalKeepStarter.setDisable(!randomiseTrainers.isSelected());
        if (rivalKeepStarter.isDisabled()) {
            rivalKeepStarter.setSelected(false);
        }
        rivalStarterEvolves.setDisable(!rivalKeepStarter.isSelected());
        if (rivalStarterEvolves.isDisabled()) {
            rivalStarterEvolves.setSelected(false);
        }
    }

    @FXML
    protected void enableRivalStarterEvolves() {
        rivalStarterEvolves.setDisable(!rivalKeepStarter.isSelected());
        if (rivalStarterEvolves.isDisabled()) {
            rivalStarterEvolves.setSelected(false);
        }
    }

    @FXML
    protected void selectDirectory(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (initialDirectory != null) {
            directoryChooser.setInitialDirectory(new File(initialDirectory));
        }
        File directoryFile = directoryChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());
        if (directoryFile != null) {
            initialDirectory = directoryFile.toString();
            p3DDirectory.setText(initialDirectory);
        }

    }

    @FXML
    protected void randomise() throws IOException {

        Map<String, Boolean> checkBoxes = new HashMap<String, Boolean>();
        checkBoxes.put("randomiseWild", randomiseWild.isSelected());
        checkBoxes.put("randomiseStatic", randomiseStatic.isSelected());
        checkBoxes.put("randomiseRoaming", randomiseStatic.isSelected());
        checkBoxes.put("randomiseTrainers", randomiseTrainers.isSelected());
        checkBoxes.put("rivalKeepStarter", rivalKeepStarter.isSelected());
        checkBoxes.put("rivalStarterEvolves", rivalStarterEvolves.isSelected());
        checkBoxes.put("randomiseStarters", randomiseStarters.isSelected());
        checkBoxes.put("randomiseTrades", randomiseTrades.isSelected());
        checkBoxes.put("randomiseGifts", randomiseGifts.isSelected());
        checkBoxes.put("randomiseGameCorner", randomiseGameCorner.isSelected());
        checkBoxes.put("learnHMs", learnHMs.isSelected());
        checkBoxes.put("gen1", gen1.isSelected());
        checkBoxes.put("gen2", gen2.isSelected());
        checkBoxes.put("gen3", gen3.isSelected());
        checkBoxes.put("gen4", gen4.isSelected());
        checkBoxes.put("gen5", gen5.isSelected());
        checkBoxes.put("gen6", gen6.isSelected());
        checkBoxes.put("gen7", gen7.isSelected());
        checkBoxes.put("gen8", gen8.isSelected());
        checkBoxes.put("regionalForms", regionalForms.isSelected());
        checkBoxes.put("hgssMusic", hgssMusic.isSelected());
        checkBoxes.put("overworldSprites", overworldSprites.isSelected());
        checkBoxes.forEach((key, value) -> {
            System.out.println(key + value);
        });

        // Randomiser.run(checkBoxes, gamemodeName.getText(), seed.getText());
    }
}
