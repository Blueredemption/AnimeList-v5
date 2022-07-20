package org.coopereisnor.animeApplication.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.controllers.Common;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.malScrape.MALScrape;
import org.coopereisnor.settingsDao.SettingsDao;

public class AddAnimeController {
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
                createAnimeAndNavigate(event);
            }
        } );

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        createButton.setOnAction(this::createAnimeAndNavigate);
    }

    public void createAnimeAndNavigate(Event event){
        Occurrence occurrence;

        if(!textField.getText().trim().equals("")) occurrence = MALScrape.getOccurrenceFromURL(textField.getText().trim());
        else occurrence = new Occurrence();

        Anime anime = animeDao.createNewAnime();
        anime.setName(occurrence.getName());
        anime.addOccurrence(occurrence);
        animeDao.save(anime);

        singletonDao.setCurrentAnime(anime, occurrence);
        application.changeScene("anime.fxml", anime.getName());
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
