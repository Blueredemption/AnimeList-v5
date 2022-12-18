package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;

public class EditAnimeController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();

    @FXML
    private Button deleteButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private FlowPane flowPane;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    public void initialize() {
        nameTextField.setText(anime.getName());

        nameTextField.setOnKeyTyped(keyEvent -> setValid());

        nameTextField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER && !confirmButton.isDisabled()) {
                endAction(event);
            }
        } );

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        deleteButton.setOnAction(this::deleteEvent);

        confirmButton.setOnAction(this::endAction);

        addOccurrenceOrderObjects();
    }

    private void addOccurrenceOrderObjects(){
        for(Occurrence occurrence : anime.getOccurrences()){
            flowPane.getChildren().add(createOccurrenceOrderObject(occurrence));
        }
    }

    private Label createOccurrenceOrderObject(Occurrence occurrence){
        Label nameLabel = new Label(occurrence.getName());
        nameLabel.setUserData(occurrence);
        nameLabel.setPrefWidth(480);
        nameLabel.setPrefHeight(25);
        nameLabel.setOnMouseClicked(mouseEvent -> {
            Bounds boundsInScene = nameLabel.localToScene(nameLabel.getBoundsInLocal());

            Rectangle2D topHalf = new Rectangle2D(boundsInScene.getMinX(), boundsInScene.getMinY(), boundsInScene.getWidth(), boundsInScene.getHeight()/2);

            int currentIndex = flowPane.getChildren().indexOf(nameLabel);
            if(topHalf.contains(mouseEvent.getSceneX(), mouseEvent.getSceneY())){
                if(currentIndex != 0){
                    flowPane.getChildren().remove(nameLabel);
                    flowPane.getChildren().add(currentIndex - 1, nameLabel);
                }
            }else{
                if(currentIndex != flowPane.getChildren().size() - 1){
                    flowPane.getChildren().remove(nameLabel);
                    flowPane.getChildren().add(currentIndex + 1, nameLabel);
                }
            }
        });
        return nameLabel;
    }

    private void setValid(){
        confirmButton.setDisable(nameTextField.getText().trim().equals(""));
    }

    private void endAction(Event event){
        // remove all occurrences
        while(anime.getOccurrences().size() != 0) anime.removeOccurrence(anime.getOccurrences().get(0));
        // add them back in the desired order
        for(Node node : flowPane.getChildren()) anime.addOccurrence((Occurrence)node.getUserData());

        anime.setName(nameTextField.getText().trim());
        animeDao.save(anime);
        application.changeScene("anime.fxml");
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    private void deleteEvent(Event event){
        animeDao.remove(anime);
        singletonDao.update();
        application.changeScene(singletonDao.getListFXML());
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
