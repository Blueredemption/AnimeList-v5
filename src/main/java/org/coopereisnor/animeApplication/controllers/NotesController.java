package org.coopereisnor.animeApplication.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

import java.util.Set;

public class NotesController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();

    @FXML
    private HBox innerHBox;
    @FXML
    private GridPane gridPane;
    @FXML
    private TextArea textArea;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());

        innerHBox.setBackground(new Background(Common.makeBackgroundImage(Common.makeFilteredImage(UtilityMethods.toBufferedImage(settingsDao.getSettings().getHomeImage().getImage())))));
        Common.setBorder(innerHBox);
        textArea.setUserData(true);
        textArea.setId("transparent");
        textArea.setText(settingsDao.getSettings().getNotesString());
        textArea.setOnKeyTyped(keyEvent -> {
            settingsDao.getSettings().setNotesString(textArea.getText());
            settingsDao.save();
        });
        Common.setFasterScrollBar(textArea);
    }
}
