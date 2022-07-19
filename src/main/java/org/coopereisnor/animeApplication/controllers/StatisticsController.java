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
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
    }

}
