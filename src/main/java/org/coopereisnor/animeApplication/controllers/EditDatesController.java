package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;

public class EditDatesController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final Occurrence occurrence = singletonDao.getCurrentOccurrence();


    @FXML
    private Label labelOne;
    @FXML
    private Label labelTwo;
    @FXML
    private DatePicker datePickerOne;
    @FXML
    private DatePicker datePickerTwo;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        if(singletonDao.getCurrentField().equals("AiredDates")) {
            labelOne.setText("Started Airing:");
            labelTwo.setText("Finished Airing:");
        }else if(singletonDao.getCurrentField().equals("WatchDates")){
            labelOne.setText("Started Watching:");
            labelTwo.setText("Finished Watching:");
        }
    }
}
