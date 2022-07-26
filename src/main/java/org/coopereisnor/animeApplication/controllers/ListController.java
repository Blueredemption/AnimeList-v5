package org.coopereisnor.animeApplication.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.ListContainer;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.manipulation.Tag;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.statistics.OccurrenceStatistics;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ListController implements Controller{
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane imageFlowPane;
    @FXML
    private ToggleButton viewButton;
    @FXML
    private ToggleButton typeButton;
    @FXML
    private VBox insideScrollPane;
    @FXML
    private ToggleButton orderButton;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button addFilterButton;
    @FXML
    private FlowPane flowPane;
    @FXML
    private TextField searchField;
    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        singletonDao.setCurrentController(this);
        Common.configureNavigation(gridPane, this.getClass());
        Common.setFasterScrollBar(scrollPane);
        loadToggleComponents();
        loadTypeComponents();
        loadSortComponents();
        loadFilterComponents();
    }



    private void loadTypeComponents(){
        typeButton.setText(singletonDao.getType());
        orderButton.setText(singletonDao.getListContainer().getOrder());

        if(singletonDao.getListFXML().equals("list.fxml")){
            loadListComponents();
        }else {
            loadImageComponents();
        }
    }



    private void loadListComponents(){
        // first remove everything to start fresh
        insideScrollPane.getChildren().clear();

        int iterator = 0;
        if(singletonDao.getType().equals("Anime")) {
            for(Anime anime : ListContainer.searchedAnime(singletonDao.getListContainer().getFilteredAndSortedAnime(), searchField.getText())){
                int earliestYear = OccurrenceStatistics.getEarliestYear(anime.getOccurrences());

                String number = ++iterator +":";
                String name = anime.getName();
                String score = anime.getScore() == -1 ? "" :anime.getScore() % 1 == 0 || anime.getScore() == 0 ? (int)anime.getScore() +"" : anime.getScore() +"";
                String year = earliestYear == Integer.MAX_VALUE ? "" : earliestYear +"";
                String episodes = OccurrenceStatistics.getTotalEpisodesWatched(anime.getOccurrences()) + "/" +OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences());
                double progress = ((double)OccurrenceStatistics.getTotalEpisodesWatched(anime.getOccurrences()))/((double)OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences()));
                EventHandler eventHandler = event -> {};

                addListComponents(number, name, score, year, episodes, progress, eventHandler, anime, null);
            }
        }else{
            for(Pair pair : ListContainer.searchedPairs(singletonDao.getListContainer().getFilteredAndSortedOccurrences(), searchField.getText())){
                String number = ++iterator +":";
                String name = pair.getOccurrence().getName();
                String score = pair.getOccurrence().getScore() == -1 ? "" : pair.getOccurrence().getScore() % 1 == 0 || pair.getOccurrence().getScore() == 0 ? (int)pair.getOccurrence().getScore() +"" : pair.getOccurrence().getScore() +"";
                String year = pair.getOccurrence().getPremieredYear() == -1 ? "" : pair.getOccurrence().getPremieredYear() +"";
                String episodes = pair.getOccurrence().getEpisodesWatched().length +"/" +pair.getOccurrence().getEpisodes();
                double progress = ((double)pair.getOccurrence().getEpisodesWatched().length)/((double)pair.getOccurrence().getEpisodes());
                EventHandler eventHandler = event -> {};

                addListComponents(number, name, score, year, episodes, progress, eventHandler, pair.getAnime(), pair.getOccurrence());
            }
        }
    }

    private void addListComponents(String number, String name, String score, String year, String episodes, double progress, EventHandler eventHandler, Anime anime, Occurrence occurrence){
        GridPane containerPane = new GridPane();
        GridPane.setFillHeight(containerPane, true);
        containerPane.setMinWidth(HBox.USE_COMPUTED_SIZE);
        containerPane.setMinHeight(HBox.USE_PREF_SIZE);
        containerPane.setPrefWidth(Double.MAX_VALUE);
        containerPane.setPrefHeight(40);
        containerPane.setMaxWidth(HBox.USE_COMPUTED_SIZE);
        containerPane.setMaxHeight(HBox.USE_COMPUTED_SIZE);
        containerPane.setPadding(new Insets(5,5,5,5));
        containerPane.setId("containerBackground");
        insideScrollPane.getChildren().add(containerPane);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.NEVER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.NEVER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.NEVER);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setHgrow(Priority.NEVER);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setHgrow(Priority.NEVER);
        ColumnConstraints col6 = new ColumnConstraints();
        col6.setHgrow(Priority.NEVER);
        containerPane.getColumnConstraints().addAll(col0, col1, col2, col3, col4, col5, col6);

        Label label = new Label(number);
        label.setMinWidth(35);
        label.setTextOverrun(OverrunStyle.CLIP);
        containerPane.add(label, 0, 0);
        GridPane.setMargin(label, new Insets(0, 5, 0, 5));

        label = new Label(name);
        label.setPrefWidth(Integer.MAX_VALUE);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        containerPane.add(label, 1, 0);
        GridPane.setMargin(label, new Insets(0, 5, 0, 5));

        label = new Label(score);
        label.setMinWidth(70);
        label.setTextOverrun(OverrunStyle.CLIP);
        containerPane.add(label, 2, 0);
        GridPane.setMargin(label, new Insets(0, 5, 0, 5));

        label = new Label(year);
        label.setMinWidth(80);
        label.setTextOverrun(OverrunStyle.CLIP);
        containerPane.add(label, 3, 0);
        GridPane.setMargin(label, new Insets(0, 5, 0, 5));

        label = new Label(episodes);
        label.setMinWidth(90);
        label.setTextOverrun(OverrunStyle.CLIP);
        containerPane.add(label, 4, 0);
        GridPane.setMargin(label, new Insets(0, 5, 0, 5));

        ProgressBar progressBar = new ProgressBar(progress); // watched/total
        progressBar.setMinWidth(125);
        progressBar.setPrefHeight(containerPane.getPrefHeight() - containerPane.getInsets().getBottom() - containerPane.getInsets().getTop() - 4);
        containerPane.add(progressBar, 5, 0);
        GridPane.setMargin(progressBar, new Insets(0, 5, 0, 5));

        Button viewButton = new Button("View");
        viewButton.setOnAction(eventHandler);
        viewButton.setMinWidth(100);
        viewButton.setPrefHeight(containerPane.getPrefHeight() - containerPane.getInsets().getBottom() - containerPane.getInsets().getTop());
        viewButton.setOnAction(actionEvent -> {
            singletonDao.setCurrentAnime(anime, occurrence);
            application.changeScene("anime.fxml", anime.getName());
        });
        containerPane.add(viewButton, 6, 0);
        GridPane.setMargin(viewButton, new Insets(0, 5, 0, 5));
    }



    private void loadImageComponents(){
        // first remove everything to start fresh
        imageFlowPane.getChildren().clear();

        if(singletonDao.getType().equals("Anime")){
            for (Anime anime : ListContainer.searchedAnime(singletonDao.getListContainer().getFilteredAndSortedAnime(), searchField.getText())) {
                addImageComponents(UtilityMethods.toBufferedImage(anime.getFocusedOccurrence().getImageIcon().getImage()), anime, null);
            }
        } else{
            for (Pair pair : ListContainer.searchedPairs(singletonDao.getListContainer().getFilteredAndSortedOccurrences(), searchField.getText())) {
                addImageComponents(UtilityMethods.toBufferedImage(pair.getOccurrence().getImageIcon().getImage()), pair.getAnime(), pair.getOccurrence());
            }
        }
    }

    private void addImageComponents(BufferedImage bufferedImage, Anime anime, Occurrence occurrence){
        double width = 157;
        double height = 225;

        Pane imagePane = Common.getImagePaneFor(null, bufferedImage);
        imagePane.setMinSize(width, height);
        imagePane.setPrefSize(width, height);
        imagePane.setMaxSize(width, height);
        imagePane.setPadding(new Insets(5,0,0,5));
        imagePane.setOnMouseClicked(event -> {
            singletonDao.setCurrentAnime(anime, occurrence);
            application.changeScene("anime.fxml", anime.getName());
        });

        imageFlowPane.getChildren().add(imagePane);
    }



    private void loadToggleComponents(){
        // search area
        searchField.setText(singletonDao.getListContainer().getSearch());
        searchField.setOnKeyTyped(keyEvent -> {
            singletonDao.getListContainer().setSearch(searchField.getText());
            loadTypeComponents();
        });

        // view button
        if(singletonDao.getListFXML().equals("list.fxml")){ // these don't need to updated higher up since the page refreshes
            viewButton.setText("List");
            viewButton.setSelected(true);
        }else{
            viewButton.setText("Image");
            viewButton.setSelected(false);
        }

        viewButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                singletonDao.setListFXML("list.fxml");
            } else{
                singletonDao.setListFXML("listImages.fxml");
            }
            application.changeScene(singletonDao.getListFXML(), "My List");
        }));

        // type button
        typeButton.setSelected(!singletonDao.getType().equals("Anime"));
        typeButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(oldValue){
                singletonDao.setType("Anime");
            } else{
                singletonDao.setType("Occurrence");
            }
            loadTypeComponents();
        }));

        // filter button
        addFilterButton.setOnAction(actionEvent -> {
            Common.popup("addFilter.fxml");
        });
    }

    private void loadSortComponents(){
        ObservableList<String> sortStrings = FXCollections.observableArrayList("Added", "Score", "Name", "Rank", "Started", "Finished", "Eps. Watched", "Eps. Total", "Year", "Progress");
        comboBox.setItems(sortStrings);
        comboBox.setValue(singletonDao.getListContainer().getSortBy());
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            singletonDao.getListContainer().setSortBy(comboBox.getValue());
            loadTypeComponents();
        });

        orderButton.setSelected(!singletonDao.getListContainer().getOrder().equals("Ascending"));
        orderButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(oldValue){
                singletonDao.getListContainer().setOrder("Ascending");
            } else{
                singletonDao.getListContainer().setOrder("Descending");
            }
            loadTypeComponents();
        }));
    }

    private void loadFilterComponents(){
        for(Tag tag : singletonDao.getListContainer().getTags()){
            Button tagButton = new Button(tag.getAttribute());
            tagButton.setPrefHeight(25);
            tagButton.setUserData(tag);
            tagButton.setId(tag.isType() ? "filterButtonWant" : "filterButtonAvoid");
            tagButton.setOnAction(actionEvent -> {
                ArrayList<Tag> currentTags = singletonDao.getListContainer().getTags();
                currentTags.remove((Tag)tagButton.getUserData());
                singletonDao.getListContainer().setTags(currentTags);
                flowPane.getChildren().remove(tagButton);
                loadTypeComponents();
            });
            flowPane.getChildren().add(tagButton);
        }
    }

    @Override
    public ProgressBar getUpdateProgressBar() {
        return progressBar;
    }
}
