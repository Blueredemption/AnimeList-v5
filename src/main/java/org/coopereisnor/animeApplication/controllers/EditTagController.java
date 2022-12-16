package org.coopereisnor.animeApplication.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.Tag;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.Log;

import java.util.ArrayList;
import java.util.List;

public class EditTagController {
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

    private String currentParam;
    private ArrayList<String> paramOptions;

    @FXML
    public void initialize() {
        setCurrentParam();
        setParamOptions();
        addActionListeners();
        initializeContainers();
    }

    private void setCurrentParam(){
        paramLabel.setText("Edit " +paramType +":");

        switch (paramType) {
            case "Type" -> currentParam = occurrence.getType();
            case "Status" -> currentParam = occurrence.getStatus();
            case "Content Rating" -> currentParam = occurrence.getRating();
            case "Source" -> currentParam = occurrence.getSource();
            case "Watch Status" -> currentParam = occurrence.getWatchStatus();
            case "Language" -> currentParam = occurrence.getLanguage();
            default -> Log.logger.error("Reached Default in EditTagController setCurrentParam");
        }
    }

    private void setParamOptions(){
        switch (paramType) {
            case "Type" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getTypes()));
            case "Status" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getStatuses()));
            case "Content Rating" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getRatings()));
            case "Source" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getSources()));
            case "Watch Status" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getWatchStatuses()));
            case "Language" -> paramOptions = new ArrayList<>(List.of(singletonDao.getFilterContainer().getLanguages()));
            default -> Log.logger.error("Reached Default in EditTagController setParamOptions");
        }

        paramOptions.remove(currentParam);
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
        if(!currentParam.equals("")){
            addNewTagButton(new Tag(paramType, currentParam, true), selectedFlowPane);
        }

        for(String string : paramOptions){
            addNewTagButton(new Tag(paramType, string, false), optionsFlowPane);
        }
    }

    private void moveFunction(){
        if(selectedFlowPane.getChildren().size() != 0){
            Button wasSelectedButton = (Button)(selectedFlowPane.getChildren().get(0));
            wasSelectedButton.setId("filterButtonAvoid");
            selectedFlowPane.getChildren().remove(selectedFlowPane.getChildren().get(0));
            optionsFlowPane.getChildren().add(wasSelectedButton);
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
                moveFunction();
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
        moveFunction();
        selectedFlowPane.getChildren().add(tagButton);
    }

    private void add(){
        if(!paramTextField.getText().trim().equals("") && !paramAlreadyExists(paramTextField.getText().trim())){
            moveFunction();
            addNewTagButton(new Tag(paramType, paramTextField.getText().trim(), true), selectedFlowPane);
        }
    }

    private void endAction(Event event){
        String param = "";
        if(selectedFlowPane.getChildren().size() > 0){
            param = ((Tag)selectedFlowPane.getChildren().get(0).getUserData()).getAttribute();
        }

        switch (paramType) {
            case "Type" -> occurrence.setType(param);
            case "Status" -> occurrence.setStatus(param);
            case "Content Rating" -> occurrence.setRating(param);
            case "Source" -> occurrence.setSource(param);
            case "Watch Status" -> occurrence.setWatchStatus(param);
            case "Language" -> occurrence.setLanguage(param);
            default -> Log.logger.error("Reached Default in EditTagController endAction");
        }
        animeDao.save(anime);
        application.changeScene("anime.fxml");
        singletonDao.update();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
