package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.malScrape.MALScrape;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.settingsDao.SettingsDao;

import java.util.ArrayList;

public class MiniListController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox listVBox;

    @FXML
    public void initialize() {
        Common.setFasterScrollBar(scrollPane);
        loadListItems(singletonDao.getMiniListItems());
    }

    private void loadListItems(ArrayList<?> listItems){
        for(Object object : listItems){
            addListComponent(object);
        }
    }

    private void addListComponent(Object object){
        Anime anime;
        Occurrence occurrence;
        if(object.getClass().equals(Anime.class)){
            anime = ((Anime)object);
            occurrence = null;
        }else{
            anime = ((Pair)object).getAnime();
            occurrence = ((Pair)object).getOccurrence();
        }
        GridPane containerPane = getGridPane(24);
        listVBox.getChildren().add(containerPane);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        containerPane.getColumnConstraints().add(columnConstraints);
        Insets insets = new Insets(0, 5, 0, 5);

        EventHandler<MouseEvent> eventHandler = event -> {
            singletonDao.setCurrentAnime(anime, occurrence);
            application.changeScene("anime.fxml");
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        };

        Label label = new Label(occurrence == null ? anime.getName() : occurrence.getName());
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setOnMouseClicked(eventHandler);
        containerPane.add(label, 0, 0);
        GridPane.setMargin(label, insets);
    }

    private GridPane getGridPane(int height){
        GridPane gridPane = new GridPane();
        GridPane.setFillHeight(gridPane, true);
        gridPane.setMinWidth(HBox.USE_COMPUTED_SIZE);
        gridPane.setMinHeight(HBox.USE_PREF_SIZE);
        gridPane.setPrefWidth(Double.MAX_VALUE);
        gridPane.setPrefHeight(height);
        gridPane.setMaxWidth(HBox.USE_COMPUTED_SIZE);
        gridPane.setMaxHeight(HBox.USE_COMPUTED_SIZE);
        gridPane.setPadding(new Insets(5,5,5,5));
        gridPane.setId("containerBackground");
        return gridPane;
    }
}

