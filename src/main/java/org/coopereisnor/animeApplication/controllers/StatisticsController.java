package org.coopereisnor.animeApplication.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.manipulation.Wildcard;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.manipulation.Count;
import org.coopereisnor.manipulation.TimeSpentCalculated;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    private Label animeLabel;
    @FXML
    private Label occurrencesLabel;
    @FXML
    private Label episodesWatchedLabel;
    @FXML
    private Label daysLabel;
    @FXML
    private Label hoursLabel;
    @FXML
    private Label minutesLabel;
    @FXML
    private VBox statisticsVBox;

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

        for(int i = Math.min(animeOrderedByRank.size() - 1, 2); i >= 0; i--){
            addImageComponent(UtilityMethods.toBufferedImage(animeOrderedByRank.get(i).getFocusedOccurrence().getImageIcon().getImage()), animeOrderedByRank.get(i), i);
        }

        for(int i = 3; i < (Math.max(animeOrderedByRank.size(), 3)); i++){
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
        GridPane containerPane = getGridPane(24);
        rankedVBox.getChildren().add(containerPane);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.NEVER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);

        containerPane.getColumnConstraints().addAll(col0, col1);
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
        addStatisticsComponentGrid("Type", singletonDao.getStatisticsContainer().getTypeCounts());
        addStatisticsComponentGrid("Status", singletonDao.getStatisticsContainer().getStatusCounts());
        addStatisticsComponentGrid("Season", singletonDao.getStatisticsContainer().getSeasonCounts());
        addStatisticsComponentGrid("Genre", singletonDao.getStatisticsContainer().getGenreCounts());
        addStatisticsComponentGrid("Theme", singletonDao.getStatisticsContainer().getThemeCounts());
        addStatisticsComponentGrid("Rating", singletonDao.getStatisticsContainer().getRatingCounts());
        addStatisticsComponentGrid("Source", singletonDao.getStatisticsContainer().getSourceCounts());
        addStatisticsComponentGrid("Studio", singletonDao.getStatisticsContainer().getStudioCounts());
        addStatisticsComponentGrid("Producer", singletonDao.getStatisticsContainer().getProducerCounts());
        addStatisticsComponentGrid("Licensor", singletonDao.getStatisticsContainer().getLicensorCounts());
        addStatisticsComponentGrid("Watch Status", singletonDao.getStatisticsContainer().getWatchStatusCounts());
        addStatisticsComponentGrid("Language", singletonDao.getStatisticsContainer().getLanguageCounts());

        addStatisticsHeader("Wildcards");
        ArrayList<Wildcard> wildcards = singletonDao.getStatisticsContainer().getWildcards();
        for(Wildcard wildcard : wildcards){
            addWildcardStatistics(wildcard.getDescription(), wildcard.getValue(), mouseEvent -> {
                singletonDao.setCurrentAnime(wildcard.getPair().getAnime(), wildcard.getPair().getOccurrence());
                application.changeScene("anime.fxml");
            });
        }
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

    private void addStatisticsHeader(String header){
        GridPane containerPane = getGridPane(26);
        statisticsVBox.getChildren().add(containerPane);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.ALWAYS);
        containerPane.getColumnConstraints().add(col0);
        Insets insets = new Insets(0, 5, 0, 5);

        Label label = new Label(header);
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(Integer.MAX_VALUE);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setId("animeDataLabel");
        containerPane.add(label, 0, 0);
        GridPane.setMargin(label, insets);
    }

    private void addWildcardStatistics(String description, String result, EventHandler<MouseEvent> eventHandler){
        GridPane containerPane = getGridPane(26);
        statisticsVBox.getChildren().add(containerPane);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.ALWAYS);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);

        containerPane.getColumnConstraints().addAll(col0, col1);
        Insets insets = new Insets(0, 5, 0, 5);

        Label label = new Label(description);
        label.setPrefWidth(Integer.MAX_VALUE);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        containerPane.add(label, 0, 0);
        GridPane.setMargin(label, insets);

        label = new Label(result);
        label.setPrefWidth(Integer.MAX_VALUE);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setOnMouseClicked(eventHandler);
        containerPane.add(label, 1, 0);
        GridPane.setMargin(label, insets);
    }

    private void addStatisticsComponentGrid(String header, ArrayList<Count> counts){
        int columnCount = 3;
        int rowCount = counts.size()/columnCount +((counts.size()%columnCount) == 0 ? 0 : 1);

        addStatisticsHeader(header +" Totals");
        GridPane containerPane = getGridPane((rowCount + 1)*20);
        statisticsVBox.getChildren().add(containerPane);
        Insets insets = new Insets(0, 5, 0, 5);

        for(int i = 0; i < columnCount; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.ALWAYS);
            containerPane.getColumnConstraints().add(columnConstraints);

            Label label = makeSimpleLabel("", mouseEvent -> {});
            containerPane.add(label, i*columnCount, 0);
            GridPane.setMargin(label, insets);

            label = makeSimpleLabel("Anime:", mouseEvent -> {});
            containerPane.add(label, i*columnCount + 1, 0);
            GridPane.setMargin(label, insets);

            label = makeSimpleLabel("Occurrences:", mouseEvent -> {});
            containerPane.add(label, i*columnCount + 2, 0);
            GridPane.setMargin(label, insets);
        }

        for(int i = 0; i < counts.size(); i++){
            Label label = makeSimpleLabel(counts.get(i).value(), mouseEvent -> {});
            containerPane.add(label, (i/rowCount)*columnCount, i%rowCount + 1);
            GridPane.setMargin(label, insets);

            int index = i;
            label = makeSimpleLabel(counts.get(i).collection().size() + "", mouseEvent -> {
                singletonDao.setMiniListItems(counts.get(index).collection());
                Common.popup("miniList.fxml");
            });
            containerPane.add(label, 1 + (i/rowCount)*columnCount, i%rowCount + 1);
            GridPane.setMargin(label, insets);

            label = makeSimpleLabel(counts.get(i).pairs().size() + "", mouseEvent -> {
                singletonDao.setMiniListItems(counts.get(index).pairs());
                Common.popup("miniList.fxml");
            });
            containerPane.add(label, 2 + (i/rowCount)*columnCount, i%rowCount + 1);
            GridPane.setMargin(label, insets);
        }
    }

    private Label makeSimpleLabel(String value, EventHandler<MouseEvent> eventHandler){
        Label label = new Label(value);
        label.setPrefHeight(20);
        label.setPrefWidth(Integer.MAX_VALUE);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setOnMouseClicked(eventHandler);
        return label;
    }

    private void populateTimeSpent(){
        TimeSpentCalculated timeSpentCalculated = singletonDao.getStatisticsContainer().getTimeSpentCalculated();

        animeLabel.setText(timeSpentCalculated.getAnimeCount() +"");
        occurrencesLabel.setText(timeSpentCalculated.getOccurrencesCount() +"");
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
