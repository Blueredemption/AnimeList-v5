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

public class AddNewController {
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
        if(singletonDao.getCurrentField().equals("Anime")){
            initializeAnime();
        }else if(singletonDao.getCurrentField().equals("Occurrence")){
            initializeOccurrence();
        }
    }

    public void initializeAnime(){
        textField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                createAnimeAndNavigate(event);
            }
        } );

        cancelButton.setOnAction(event -> {
            System.out.println("Kagamine");
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        });

        createButton.setOnAction(this::createAnimeAndNavigate);
    }

    public void createAnimeAndNavigate(Event event){
        Occurrence occurrence;

        if(!textField.getText().trim().equals("")) occurrence = MALScrape.getOccurrenceFromURL(textField.getText().trim());
        else occurrence = new Occurrence(System.currentTimeMillis());

        // the first occurrence in an anime is set to focused by default
        occurrence.setFocused(true);

        Anime anime = animeDao.createNewAnime();
        anime.setRank(animeDao.getCollection().size());
        anime.setName(occurrence.getName().equals("New Occurrence") ? "New Anime" : occurrence.getName());
        anime.addOccurrence(occurrence);
        animeDao.save(anime);

        singletonDao.setCurrentAnime(anime, occurrence);
        application.changeScene("anime.fxml", anime.getName());
        singletonDao.update();

        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    public void initializeOccurrence(){
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
        else occurrence = new Occurrence(System.currentTimeMillis());

        Anime anime = singletonDao.getCurrentAnime();
        anime.addOccurrence(occurrence);
        animeDao.save(anime);

        singletonDao.setCurrentAnime(singletonDao.getCurrentAnime(), occurrence);
        application.changeScene("anime.fxml", singletonDao.getCurrentAnime().getName());
        singletonDao.update();

        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
