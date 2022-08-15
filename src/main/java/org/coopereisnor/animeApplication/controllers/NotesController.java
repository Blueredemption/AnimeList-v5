package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.settingsDao.SettingsDao;

public class NotesController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();

    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea textArea;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
        Common.setFasterScrollBar(scrollPane);

        textArea.setText(settingsDao.getSettings().getNotesString());
        textArea.setOnKeyTyped(keyEvent -> {
            settingsDao.getSettings().setNotesString(textArea.getText());
            settingsDao.save();
        });
    }
}
