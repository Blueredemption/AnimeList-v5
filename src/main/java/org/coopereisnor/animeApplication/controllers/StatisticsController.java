package org.coopereisnor.animeApplication.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.customJavaFXObjects.PercentProgressBar;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeApplication.singleton.StatisticsContainer;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.statistics.TimeSpentCalculated;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class StatisticsController implements Controller {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();



    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private HBox rankedHBox;
    @FXML
    private ScrollPane rankedScrollPane;
    @FXML
    private VBox rankedVBox;
    @FXML
    private Label animeWatchedLabel;
    @FXML
    private Label episodesWatchedLabel;
    @FXML
    private Label daysLabel;
    @FXML
    private Label hoursLabel;
    @FXML
    private Label minutesLabel;

    @FXML
    public void initialize() {
        singletonDao.setCurrentController(this);
        Common.configureNavigation(gridPane, this.getClass());
        Common.setFasterScrollBar(rankedScrollPane);
        Common.setFasterScrollBar(scrollPane);

        loadTopRankedAnime();
        loadStatistics();
        populateTimeSpent();
    }

    private void loadTopRankedAnime(){
        ArrayList<Anime> animeOrderedByRank = singletonDao.getStatisticsContainer().getAnimeOrderedByRank();

        for(int i = Math.min(animeOrderedByRank.size(), 2); i >= 0; i--){ //todo: test how this method works with low i values
            addImageComponent(UtilityMethods.toBufferedImage(animeOrderedByRank.get(i).getFocusedOccurrence().getImageIcon().getImage()), animeOrderedByRank.get(i), i);
        }

        for(int i = (Math.min(animeOrderedByRank.size(), 3)); i < (Math.max(animeOrderedByRank.size(), 3)); i++){
            addListComponent(animeOrderedByRank.get(i), i + 1);
        }
    }

    private void addImageComponent(BufferedImage bufferedImage, Anime anime, int index){
        double width = 141;
        double height = 202;

        Pane imagePane = Common.getImagePaneFor(null, bufferedImage, UtilityMethods.medalColors[index]);
        imagePane.setMinSize(width, height);
        imagePane.setPrefSize(width, height);
        imagePane.setMaxSize(width, height);
        imagePane.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                singletonDao.setCurrentAnime(anime, null);
                application.changeScene("anime.fxml");
            }
        });

        rankedHBox.getChildren().add(imagePane);
    }

    private void addListComponent(Anime anime, int index){
        GridPane containerPane = new GridPane();
        GridPane.setFillHeight(containerPane, true);
        containerPane.setMinWidth(HBox.USE_COMPUTED_SIZE);
        containerPane.setMinHeight(HBox.USE_PREF_SIZE);
        containerPane.setPrefWidth(Double.MAX_VALUE);
        containerPane.setPrefHeight(24);
        containerPane.setMaxWidth(HBox.USE_COMPUTED_SIZE);
        containerPane.setMaxHeight(HBox.USE_COMPUTED_SIZE);
        containerPane.setPadding(new Insets(5,5,5,5));
        containerPane.setId("containerBackground");
        rankedVBox.getChildren().add(containerPane);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.NEVER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();

        containerPane.getColumnConstraints().addAll(col0, col1, col2);
        Insets insets = new Insets(0, 5, 0, 5);

        EventHandler<MouseEvent> eventHandler = event -> {
            singletonDao.setCurrentAnime(anime, null);
            application.changeScene("anime.fxml");
        };

        int i = 0;
        Label label = new Label(index +":");
        label.setMinWidth(35);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setOnMouseClicked(eventHandler);
        containerPane.add(label, i++, 0);
        GridPane.setMargin(label, insets);

        label = new Label(anime.getName());
        label.setPrefWidth(Integer.MAX_VALUE);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setOnMouseClicked(eventHandler);
        containerPane.add(label, i++, 0);
        GridPane.setMargin(label, insets);
    }

    private void loadStatistics(){

    }

    private void populateTimeSpent(){
        TimeSpentCalculated timeSpentCalculated = singletonDao.getStatisticsContainer().getTimeSpentCalculated();

        animeWatchedLabel.setText(timeSpentCalculated.getAnimeWatched() +"");
        episodesWatchedLabel.setText(timeSpentCalculated.getEpisodesWatched() +"");
        daysLabel.setText(timeSpentCalculated.getDaysSpent() +"");
        hoursLabel.setText(timeSpentCalculated.getHoursSpent() +"");
        minutesLabel.setText(timeSpentCalculated.getMinutesSpent() +"");
    }

    @Override
    public ProgressBar getUpdateProgressBar() {
        return progressBar;
    }
}
