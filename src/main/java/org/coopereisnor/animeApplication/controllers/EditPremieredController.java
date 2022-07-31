package org.coopereisnor.animeApplication.controllers;

import javafx.application.Platform;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;

import java.time.LocalDate;
import java.util.ArrayList;

public class EditPremieredController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final Occurrence occurrence = singletonDao.getCurrentOccurrence();
    private final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900,2200);

    private final String initialSeason = occurrence.getPremieredSeason();
    private final int initialYear = occurrence.getPremieredYear();

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Spinner<Integer> spinner;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        valueFactory.setWrapAround(true);
        valueFactory.setAmountToStepBy(1);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        comboBox.setItems(FXCollections.observableArrayList("", "Winter", "Spring", "Summer", "Fall"));
        comboBox.setValue(initialSeason);

        valueFactory.setValue(initialYear <= 1900 ? LocalDate.now().getYear() : initialYear);

        spinner.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                endEvent(event);
            }
        } );

        confirmButton.setOnAction(this::endEvent);
    }


    private void endEvent(Event event) {
        occurrence.setPremieredSeason(comboBox.getValue());
        occurrence.setPremieredYear(valueFactory.getValue());
        animeDao.save(anime);
        application.changeScene("anime.fxml", anime.getName());
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
