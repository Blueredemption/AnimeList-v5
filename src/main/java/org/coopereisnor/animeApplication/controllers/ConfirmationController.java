package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;

public class ConfirmationController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;

    @FXML
    public void initialize() {
        initializeButtons();
    }

    public void initializeButtons(){
        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());
        deleteButton.setOnAction(this::deleteEvent);
    }

    private void deleteEvent(Event event){
        Anime anime = singletonDao.getCurrentAnime();
        Occurrence occurrence = singletonDao.getCurrentOccurrence();

        if(singletonDao.getCurrentField().equals("Anime")){
            animeDao.remove(anime);
            singletonDao.update();
            application.changeScene(singletonDao.getListFXML());
        }else if(singletonDao.getCurrentField().equals("Occurrence")){
            boolean focused = occurrence.isFocused();
            singletonDao.setCurrentAnime(anime, null);
            anime.removeOccurrence(occurrence);
            if(anime.getOccurrences().size() == 0){
                animeDao.remove(anime);
                singletonDao.update();
                application.changeScene(singletonDao.getListFXML());

            }else{
                if(!anime.getOccurrences().get(0).isFocused()) anime.getOccurrences().get(0).setFocused(focused);
                singletonDao.update();
                application.changeScene("anime.fxml");
            }
        }

        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        singletonDao.getPopupStage().close();
    }
}
