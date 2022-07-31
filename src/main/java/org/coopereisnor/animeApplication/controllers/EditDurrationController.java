package org.coopereisnor.animeApplication.controllers;

import javafx.collections.FXCollections;
import javafx.event.Event;
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

public class EditDurrationController
{
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final Occurrence occurrence = singletonDao.getCurrentOccurrence();
    private final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0 , 300);

    private final int initialDurration = occurrence.getDuration()/60;

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

        valueFactory.setValue(initialDurration <= 0 ? 20 : initialDurration);

        spinner.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                endEvent(event);
            }
        } );

        confirmButton.setOnAction(this::endEvent);
    }


    private void endEvent(Event event) {
        occurrence.setDuration(valueFactory.getValue()*60);
        animeDao.save(anime);
        application.changeScene("anime.fxml", anime.getName());
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
