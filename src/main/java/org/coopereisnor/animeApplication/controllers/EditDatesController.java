package org.coopereisnor.animeApplication.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        if(singletonDao.getCurrentField().equals("AiredDates")) {
            labelOne.setText("Started Airing:");
            labelTwo.setText("Finished Airing:");

            if(occurrence.getAiredStartDate() != null) datePickerOne.setValue(occurrence.getAiredStartDate());
            if(occurrence.getAiredEndDate() != null) datePickerTwo.setValue(occurrence.getAiredEndDate());

            confirmButton.setOnAction(actionEvent -> {
                occurrence.setAiredStartDate(datePickerOne.getValue());
                occurrence.setAiredEndDate(datePickerTwo.getValue());
                commonEnd(actionEvent);
            });
        }else if(singletonDao.getCurrentField().equals("WatchDates")){
            labelOne.setText("Started Watching:");
            labelTwo.setText("Finished Watching:");

            if(occurrence.getStartedWatching() != null) datePickerOne.setValue(occurrence.getStartedWatching());
            if(occurrence.getFinishedWatching() != null) datePickerTwo.setValue(occurrence.getFinishedWatching());

            confirmButton.setOnAction(actionEvent -> {
                occurrence.setStartedWatching(datePickerOne.getValue());
                occurrence.setFinishedWatching(datePickerTwo.getValue());
                commonEnd(actionEvent);
            });
        }

    }

    public void commonEnd(ActionEvent actionEvent){
        animeDao.save(anime);
        application.changeScene("anime.fxml", anime.getName());
        ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
    }
}
