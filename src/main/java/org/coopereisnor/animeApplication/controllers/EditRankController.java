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

import java.util.ArrayList;

public class EditRankController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,animeDao.getCollection().size());
    private final int initialRank = anime.getRank();


    @FXML
    private Spinner spinner;
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

        valueFactory.setValue(initialRank);

        spinner.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                endEvent(event);
            }
        } );

        confirmButton.setOnAction(this::endEvent);
    }


    private void endEvent(Event event) {
        anime.setRank(valueFactory.getValue());
        shiftRanks();
        application.changeScene("anime.fxml");
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    public void shiftRanks(){
        ArrayList<Anime> collection = animeDao.getCollection();
        collection.sort(Anime.SORT_BY_RANK);
        collection.remove(anime);
        collection.add(valueFactory.getValue() - 1, anime);
        for(int i = 0; i < collection.size(); i++){
            if(collection.get(i).getRank() != i + 1){
                collection.get(i).setRank(i + 1);
                animeDao.save(collection.get(i));
            }
        }
        animeDao.save(anime);
    }
}