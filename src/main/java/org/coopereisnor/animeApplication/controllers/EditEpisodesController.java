package org.coopereisnor.animeApplication.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Episode;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class EditEpisodesController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final Occurrence occurrence = singletonDao.getCurrentOccurrence();
    private final Episode[] currentEpisodes = occurrence.getEpisodesWatched();

    private int maxValue = 10000;

    @FXML
    private Spinner<Integer> episodesTotalSpinner;
    @FXML
    private Label episodesWatchedLabel;
    @FXML
    private Spinner<Integer> addEpisodesSpinner;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button addButton;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    private final ChangeListener episodesTotalListener = (observableValue, o, t1) -> revalidateAddSpinner();
    private final ChangeListener addEpisodesListener = (observableValue, o, t1) -> revalidateTotalSpinner();

    public void initialize() {
        episodesTotalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxValue));
        episodesTotalSpinner.getValueFactory().setValue(occurrence.getEpisodes());

        datePicker.setValue(LocalDate.now());

        initializeEpisodes();
        revalidateEpisodes();

        addEpisodesSpinner.getValueFactory().setValue(1);
        addEpisodesSpinner.getValueFactory().setWrapAround(true);

        addButton.setOnAction(actionEvent -> {
            for(int i = 0; i < addEpisodesSpinner.getValueFactory().getValue(); i++){
                addEpisodeElement(datePicker.getValue());
            }
            revalidateEpisodes();
        });

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        confirmButton.setOnAction(this::endAction);
    }

    private void initializeEpisodes(){
        for(int i = 0; i < currentEpisodes.length; i++){
            addEpisodeElement(currentEpisodes[i].getWatchDate());
        }
    }

    private void revalidateEpisodes(){
        revalidateAddSpinner();
        revalidateTotalSpinner();
    }

    private void revalidateAddSpinner(){
        listeners(false);
        addEpisodesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, episodesTotalSpinner.getValueFactory().getValue() - flowPane.getChildren().size()));
        addEpisodesSpinner.getValueFactory().setWrapAround(true);
        addEpisodesSpinner.setDisable(episodesTotalSpinner.getValueFactory().getValue() - flowPane.getChildren().size() == 0);
        if(!addEpisodesSpinner.isDisable()) addEpisodesSpinner.getValueFactory().setValue(1);
        listeners(true);
        episodesWatchedLabel.setText("Episodes Watched: " +flowPane.getChildren().size());
        addButton.setDisable(episodesTotalSpinner.getValueFactory().getValue() == flowPane.getChildren().size());
    }

    private void revalidateTotalSpinner(){
        listeners(false);
        int oldValue = episodesTotalSpinner.getValueFactory().getValue();
        episodesTotalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(flowPane.getChildren().size(), maxValue));
        episodesTotalSpinner.getValueFactory().setValue(oldValue);
        addEpisodesSpinner.setDisable(episodesTotalSpinner.getValueFactory().getValue() - flowPane.getChildren().size() == 0);
        listeners(true);
        episodesWatchedLabel.setText("Episodes Watched: " +flowPane.getChildren().size());
        addButton.setDisable(episodesTotalSpinner.getValueFactory().getValue() == flowPane.getChildren().size());
    }

    private void listeners(boolean value){
        if(value){
            addEpisodesSpinner.valueProperty().addListener(addEpisodesListener);
            episodesTotalSpinner.valueProperty().addListener(episodesTotalListener);
        }else{
            addEpisodesSpinner.valueProperty().removeListener(addEpisodesListener);
            episodesTotalSpinner.valueProperty().removeListener(episodesTotalListener);
        }
    }

    private void addEpisodeElement(LocalDate date){
        GridPane containerPane = new GridPane();
        GridPane.setFillHeight(containerPane, true);
        containerPane.setMinWidth(HBox.USE_COMPUTED_SIZE);
        containerPane.setMinHeight(HBox.USE_PREF_SIZE);
        containerPane.setPrefWidth(397);
        containerPane.setPrefHeight(HBox.USE_COMPUTED_SIZE);
        containerPane.setMaxWidth(HBox.USE_COMPUTED_SIZE);
        containerPane.setMaxHeight(HBox.USE_COMPUTED_SIZE);
        containerPane.setPadding(new Insets(2,2,2,2));
        flowPane.getChildren().add(containerPane);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.ALWAYS);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.NEVER);
        containerPane.getColumnConstraints().addAll(col0, col1);

        DatePicker episodeDatePicker = new DatePicker(date);
        episodeDatePicker.setPrefWidth(Integer.MAX_VALUE);
        episodeDatePicker.setEditable(false);
        containerPane.add(episodeDatePicker, 0, 0);
        GridPane.setMargin(episodeDatePicker, new Insets(0, 0, 0, 0));
        containerPane.setUserData(episodeDatePicker);

        Button deleteButton = new Button("Delete");
        deleteButton.setMinWidth(70);
        deleteButton.setId("toggleButtonColored");
        deleteButton.setTextOverrun(OverrunStyle.CLIP);
        deleteButton.setOnAction(actionEvent -> {
            flowPane.getChildren().remove(containerPane);
            revalidateEpisodes();
        });
        containerPane.add(deleteButton, 1, 0);
        GridPane.setMargin(deleteButton, new Insets(0, 0, 0, 5));
    }

    private void endAction(Event event){
        try {
            occurrence.setEpisodes(episodesTotalSpinner.getValueFactory().getValue());
            for(Episode episode : currentEpisodes){
                occurrence.removeEpisodeWatched(episode);
            }
            for(Node node : flowPane.getChildren()){
                Episode newEpisode = new Episode();
                newEpisode.setWatchDate(((DatePicker)node.getUserData()).getValue());
                occurrence.addEpisodeWatched(newEpisode);
            }
            animeDao.save(anime);
            application.changeScene("anime.fxml");
            singletonDao.update();
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        }catch(Exception ex){
            System.out.println("Null Pointer Exception voiding endAction in EditEpisodesController");
        }
    }
}
