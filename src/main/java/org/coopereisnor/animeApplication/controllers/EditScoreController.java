package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;

public class EditScoreController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final Occurrence occurrence = singletonDao.getCurrentOccurrence();
    private final SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0,10.0);


    @FXML
    private Spinner spinner;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        valueFactory.setWrapAround(true);
        valueFactory.setAmountToStepBy(0.1);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        if(singletonDao.getCurrentField().equals("AnimeScore")) {
            valueFactory.setValue(anime.getScore() >= 0 ? anime.getScore() : 8);
        }else if(singletonDao.getCurrentField().equals("OccurrenceScore")){
            valueFactory.setValue(occurrence.getScore() >= 0 ? occurrence.getScore() : 8);
        }

        spinner.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                if(singletonDao.getCurrentField().equals("AnimeScore")) {
                    animeEnd(event);
                }else if(singletonDao.getCurrentField().equals("OccurrenceScore")){
                    occurrenceEnd(event);
                }
            }
        } );

        confirmButton.setOnAction(event -> {
            if(singletonDao.getCurrentField().equals("AnimeScore")) {
                animeEnd(event);
            }else if(singletonDao.getCurrentField().equals("OccurrenceScore")){
                occurrenceEnd(event);
            }
        });
    }

    private void animeEnd(Event event){
        double score = ((double)((int)(valueFactory.getValue() *10.0)))/10.0;
        anime.setScore(score);
        if(anime.getOccurrences().size() == 1) occurrence.setScore(score);
        commonEnd(event);
    }

    private void occurrenceEnd(Event event){
        double score = ((double)((int)(valueFactory.getValue() *10.0)))/10.0;
        occurrence.setScore(score);
        if(anime.getOccurrences().size() == 1) anime.setScore(score);
        commonEnd(event);
    }

    private void commonEnd(Event event) {
        animeDao.save(anime);
        application.changeScene("anime.fxml");
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
