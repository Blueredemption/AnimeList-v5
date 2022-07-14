package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;

public class StatisticsController {
    private AnimeDao animeDao = SingletonDao.getInstance().getAnimeDao();
    private SettingsDao settings = SingletonDao.getInstance().getSettingsDao();
    private Application application = SingletonDao.getInstance().getApplication();

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
    }

}
