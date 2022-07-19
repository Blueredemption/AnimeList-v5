package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.controllers.Common;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;

public class AddAnimeController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();


    @FXML
    public void initialize() {
        // for later
    }
}
