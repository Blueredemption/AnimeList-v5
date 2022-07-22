package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.malScrape.MALScrape;
import org.coopereisnor.settingsDao.SettingsDao;

public class AddOccurrenceController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private TextField textField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;

    @FXML
    public void initialize() {
        textField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                createOccurrenceAndNavigate(event);
            }
        } );

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        createButton.setOnAction(this::createOccurrenceAndNavigate);
    }

    public void createOccurrenceAndNavigate(Event event){
        Occurrence occurrence;

        if(!textField.getText().trim().equals("")) occurrence = MALScrape.getOccurrenceFromURL(textField.getText().trim());
        else occurrence = new Occurrence();

        singletonDao.getCurrentAnime().addOccurrence(occurrence);

        singletonDao.setCurrentAnime(singletonDao.getCurrentAnime(), occurrence);
        application.changeScene("anime.fxml", singletonDao.getCurrentAnime().getName());
        singletonDao.compileLists();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
