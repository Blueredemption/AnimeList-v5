package org.coopereisnor.animeApplication.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;

import java.net.MalformedURLException;
import java.net.URL;

public class EditAnimeController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final Occurrence occurrence = singletonDao.getCurrentOccurrence();

    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField urlTextField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    public void initialize() {

    }
}
