package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

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
