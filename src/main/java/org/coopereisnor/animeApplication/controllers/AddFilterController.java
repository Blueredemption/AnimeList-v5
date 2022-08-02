package org.coopereisnor.animeApplication.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.manipulation.Tag;
import org.coopereisnor.settingsDao.SettingsDao;

import java.util.ArrayList;

public class AddFilterController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    private final ArrayList<Tag> currentTags = new ArrayList<>();

    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private ComboBox<String> attributeComboBox;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private Button addButton;
    @FXML
    private FlowPane flowPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button cancelButton;
    @FXML
    private Button commitButton;

    @FXML
    public void initialize() {
        addActionListeners();
        populateFilterComboBox();
        initializeContainers();
    }



    private void addActionListeners(){
        filterComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            populateAttributeComboBox();
            validateAdd();
        });

        attributeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            validateAdd();
        });

        toggleButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue) toggleButton.setText("Include");
            else toggleButton.setText("Exclude");
            validateAdd();
        }));

        addButton.setOnAction(actionEvent -> {
            Tag newTag = getNewTag();
            currentTags.add(newTag);
            addNewTagButton(newTag);
            validateAdd();
        });

        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        commitButton.setOnAction(actionEvent -> {
            singletonDao.getListContainer().setTags(currentTags);
            application.changeScene(singletonDao.getListFXML());
            ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close();
        });
    }

    private void populateFilterComboBox(){
        ObservableList<String> filterStrings = FXCollections.observableArrayList("Type", "Status", "Season", "Genre", "Theme", "Rating", "Source", "Studio", "Producer", "Licensor", "Watch Status", "Language");
        filterComboBox.setItems(filterStrings);

    }

    private void initializeContainers(){
        filterComboBox.getSelectionModel().select(0);
        attributeComboBox.getSelectionModel().select(0);
        toggleButton.setSelected(true);
        Common.setFasterScrollBar(scrollPane);

        for(Tag tag : singletonDao.getListContainer().getTags()){
            currentTags.add(tag);
            addNewTagButton(tag);
        }
    }

    private void populateAttributeComboBox(){
        switch (filterComboBox.getValue()) {
            case "Type" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getTypes()));
            case "Status" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getStatuses()));
            case "Season" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getSeasons()));
            case "Genre" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getGenres()));
            case "Theme" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getThemes()));
            case "Rating" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getRatings()));
            case "Source" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getSources()));
            case "Studio" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getStudios()));
            case "Producer" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getProducers()));
            case "Licensor" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getLicensors()));
            case "Watch Status" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getWatchStatuses()));
            case "Language" -> attributeComboBox.setItems(FXCollections.observableArrayList(singletonDao.getFilterContainer().getLanguages()));
            default -> System.out.println("Reached Default in AddFilterController populateAttributeComboBox");
        }
    }

    private void validateAdd(){
        addButton.setDisable(!(filterComboBox.getValue() != null && attributeComboBox.getValue() != null && !checkCurrentTags(getNewTag())));
    }

    private Tag getNewTag(){
        return new Tag(filterComboBox.getValue(), attributeComboBox.getValue(), toggleButton.isSelected());
    }

    private boolean checkCurrentTags(Tag newTag){
        return currentTags.contains(newTag);
    }

    private void addNewTagButton(Tag tag){
        Button tagButton = new Button(tag.getAttribute());
        tagButton.setPrefHeight(25);
        tagButton.setUserData(tag);
        tagButton.setId(tag.isType() ? "filterButtonWant" : "filterButtonAvoid");
        tagButton.setOnAction(actionEvent -> {
            currentTags.remove((Tag)tagButton.getUserData());
            flowPane.getChildren().remove(tagButton);
            validateAdd();
        });
        flowPane.getChildren().add(tagButton);
    }
}
