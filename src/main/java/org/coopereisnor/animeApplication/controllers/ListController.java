package org.coopereisnor.animeApplication.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;

public class ListController {
    private AnimeDao animeDao = SingletonDao.getInstance().getAnimeDao();
    private SettingsDao settings = SingletonDao.getInstance().getSettingsDao();

    @FXML
    private Label welcomeText;

    @FXML
    public void initialize() {
        welcomeText.setText(animeDao.toString());
    }

    public void onButtonClick(ActionEvent actionEvent) {
        SingletonDao.getInstance().getApplication().changeScene("home.fxml");
    }
}
