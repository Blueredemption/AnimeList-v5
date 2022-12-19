package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.coopereisnor.Program;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.malScrape.MALScrape;

import java.net.MalformedURLException;
import java.net.URL;

public class EditOccurrenceController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
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

    URL currentURL;

    public void initialize() {
        currentURL = occurrence.getUrl();
        nameTextField.setText(occurrence.getName());
        urlTextField.setText(occurrence.getUrl() == null ? "" : occurrence.getUrl().toString());
        setValid();

        nameTextField.setOnKeyTyped(keyEvent -> setValid());

        nameTextField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER && !confirmButton.isDisabled()) {
                endEvent(event);
            }
        } );

        urlTextField.setOnKeyTyped(keyEvent -> {
            try {
                currentURL = new URL(urlTextField.getText());
            } catch (MalformedURLException ex) {
                currentURL = null;
                Program.logger.error("URL Not Valid!", ex);
            }
            setValid();
        });

        urlTextField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER && !confirmButton.isDisabled()) {
                endEvent(event);
            }
        } );

        deleteButton.setOnAction(event -> {
            singletonDao.setCurrentField("Occurrence");
            singletonDao.setPopupStage(((Stage)((Node)event.getSource()).getScene().getWindow()));
            Common.popup("confirmation.fxml");
        });

        refreshButton.setOnAction(this::refreshEvent);

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        confirmButton.setOnAction(this::endEvent);
    }

    private void setValid(){
        confirmButton.setDisable(false);
        refreshButton.setDisable(currentURL == null || nameTextField.getText().trim().equals(""));
    }

    private void endEvent(Event event){
        occurrence.setName(nameTextField.getText().trim());
        occurrence.setUrl(currentURL);
        animeDao.save(anime);
        application.changeScene("anime.fxml");
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    private void refreshEvent(Event event){
        MALScrape.refreshOccurrenceFromURL(urlTextField.getText().trim(), occurrence);
        animeDao.save(anime);
        application.changeScene("anime.fxml");
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
