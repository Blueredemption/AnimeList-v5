package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.coopereisnor.Program;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.Tag;
import org.coopereisnor.settingsDao.SettingsDao;

import java.util.ArrayList;
import java.util.List;

public class EditTagsController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final Occurrence occurrence = singletonDao.getCurrentOccurrence();

    private final String paramType = singletonDao.getCurrentField();

    @FXML
    private Label paramLabel;
    @FXML
    private TextField paramTextField;
    @FXML
    private Button addButton;
    @FXML
    private FlowPane selectedFlowPane;
    @FXML
    private FlowPane optionsFlowPane;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    private ArrayList<String> currentParams;
    private ArrayList<String> paramOptions;

    @FXML
    public void initialize() {
        setCurrentParams();
        setParamOptions();
        addActionListeners();
        initializeContainers();
    }

    private void setCurrentParams(){
        paramLabel.setText("Edit " +paramType +":");

        switch (paramType) {
            case "Genres" -> currentParams = new ArrayList<>(List.of(occurrence.getGenres()));
            case "Themes" -> currentParams = new ArrayList<>(List.of(occurrence.getThemes()));
            case "Studios" -> currentParams = new ArrayList<>(List.of(occurrence.getStudios()));
            case "Producers" -> currentParams = new ArrayList<>(List.of(occurrence.getProducers()));
            case "Licensors" -> currentParams = new ArrayList<>(List.of(occurrence.getLicensors()));
            default -> Program.logger.error("Reached Default in EditTagsController setCurrentParams");
        }
    }

    private void setParamOptions(){
        switch (paramType) {
            case "Genres" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getGenres()));
            case "Themes" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getThemes()));
            case "Studios" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getStudios()));
            case "Producers" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getProducers()));
            case "Licensors" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getLicensors()));
            default -> Program.logger.error("Reached Default in EditTagsController setParamOptions");
        }

        for(String string : currentParams){
            paramOptions.remove(string);
        }
    }

    private void addActionListeners(){
        paramTextField.setOnKeyPressed( event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                add();
            }
        } );

        addButton.setOnAction(actionEvent -> {
            add();
        });

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        confirmButton.setOnAction(this::endAction);
    }

    private void initializeContainers(){
        for(String string : currentParams){
            addNewTagButton(new Tag(paramType, string, true), selectedFlowPane);
        }

        for(String string : paramOptions){
            addNewTagButton(new Tag(paramType, string, false), optionsFlowPane);
        }
    }

    private void addNewTagButton(Tag tag, FlowPane flowPane){
        Button tagButton = new Button(tag.getAttribute());
        tagButton.setPrefHeight(25);
        tagButton.setUserData(tag);
        tagButton.setId(tag.isType() ? "filterButtonWant" : "filterButtonAvoid");
        tagButton.setOnAction(actionEvent -> {
            if(tagButton.getParent().equals(selectedFlowPane)){
                selectedFlowPane.getChildren().remove(tagButton);
                ((Tag)tagButton.getUserData()).setType(false);
                tagButton.setId(tag.isType() ? "filterButtonWant" : "filterButtonAvoid");
                optionsFlowPane.getChildren().add(tagButton);
            } else{
                optionsFlowPane.getChildren().remove(tagButton);
                ((Tag)tagButton.getUserData()).setType(true);
                tagButton.setId(tag.isType() ? "filterButtonWant" : "filterButtonAvoid");
                selectedFlowPane.getChildren().add(tagButton);
            }
        });
        flowPane.getChildren().add(tagButton);
    }


    private boolean paramAlreadyExists(String param){
        for(Node node : selectedFlowPane.getChildren()){
            if(((Tag)node.getUserData()).getAttribute().equals(param)) {
                // do nothing the tag is already there
                return true;
            }
        }

        for(Node node : optionsFlowPane.getChildren()){
            if(((Tag)node.getUserData()).getAttribute().equals(param)) {
                moveFromOptionsToSelected(node);
                return true;
            }
        }

        return false;
    }

    private void moveFromOptionsToSelected(Node node){
        Button tagButton = (Button)node;
        optionsFlowPane.getChildren().remove(tagButton);
        ((Tag)tagButton.getUserData()).setType(true);
        tagButton.setId("filterButtonWant");
        selectedFlowPane.getChildren().add(tagButton);
    }

    private void add(){
        if(!paramTextField.getText().trim().equals("") && !paramAlreadyExists(paramTextField.getText().trim()))
            addNewTagButton(new Tag(paramType, paramTextField.getText().trim(), true), selectedFlowPane);
    }


    private void endAction(Event event){
        switch (paramType) {
            case "Genres" -> {
                for(Node node : optionsFlowPane.getChildren()) occurrence.removeGenre(((Tag)node.getUserData()).getAttribute());
                for(Node node : selectedFlowPane.getChildren()) occurrence.addGenre(((Tag)node.getUserData()).getAttribute());
            }
            case "Themes" -> {
                for(Node node : optionsFlowPane.getChildren()) occurrence.removeTheme(((Tag)node.getUserData()).getAttribute());
                for(Node node : selectedFlowPane.getChildren()) occurrence.addTheme(((Tag)node.getUserData()).getAttribute());
            }
            case "Studios" -> {
                for(Node node : optionsFlowPane.getChildren()) occurrence.removeStudio(((Tag)node.getUserData()).getAttribute());
                for(Node node : selectedFlowPane.getChildren()) occurrence.addStudio(((Tag)node.getUserData()).getAttribute());
            }
            case "Producers" -> {
                for(Node node : optionsFlowPane.getChildren()) occurrence.removeProducer(((Tag)node.getUserData()).getAttribute());
                for(Node node : selectedFlowPane.getChildren()) occurrence.addProducer(((Tag)node.getUserData()).getAttribute());
            }
            case "Licensors" -> {
                for(Node node : optionsFlowPane.getChildren()) occurrence.removeLicensor(((Tag)node.getUserData()).getAttribute());
                for(Node node : selectedFlowPane.getChildren()) occurrence.addLicensor(((Tag)node.getUserData()).getAttribute());
            }
            default -> Program.logger.error("Reached Default in EditTagsController endAction");
        }
        animeDao.save(anime);
        application.changeScene("anime.fxml");
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
