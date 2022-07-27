package org.coopereisnor.animeApplication.controllers;

import javafx.event.ActionEvent;
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
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.image.BufferedImage;

public class AnimeController implements Controller{
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private Occurrence selectedOccurrence = null;

    @FXML
    private GridPane gridPane;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label nameLabel;
    @FXML
    private TabPane tabPane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label rankLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private ToggleButton focusedButton;
    @FXML
    private Button generalStatisticsButton;
    @FXML
    private ToggleButton trackingButton;
    @FXML
    private Button newOccurrenceButton;

    @FXML
    public void initialize() {
        singletonDao.setCurrentController(this);
        Common.configureNavigation(gridPane, this.getClass());
        configureAnimeActions();
        kagamine();
        configureToggleActions();
    }

    public void configureAnimeActions(){
        nameLabel.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.SECONDARY){

            }
        });

        previousButton.setOnAction(actionEvent -> {
            Anime nextAnime = Common.getNextAnime(anime, false);
            singletonDao.setCurrentAnime(nextAnime, null);
            application.changeScene("anime.fxml", nextAnime.getName());
        });

        nextButton.setOnAction(actionEvent -> {
            Anime nextAnime = Common.getNextAnime(anime, true);
            singletonDao.setCurrentAnime(nextAnime, null);
            application.changeScene("anime.fxml", nextAnime.getName());
        });

        generalStatisticsButton.setOnAction(actionEvent -> {
            // todo: popup with general statistics and a way to view and modify episode objects
        });

        rankLabel.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                Common.popup("editRank.fxml");
            }
        });

        scoreLabel.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.SECONDARY){
                singletonDao.setCurrentField("AnimeScore");
                Common.popup("editScore.fxml");
            }
        });

        newOccurrenceButton.setOnAction(actionEvent -> {
            singletonDao.setCurrentField("Occurrence");
            Common.popup("addNew.fxml");
        });
    }

    public void configureToggleActions(){
        focusedButton.setSelected(selectedOccurrence.isFocused());
        focusedButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!oldValue){
                if(!selectedOccurrence.isFocused()){
                    selectedOccurrence.setFocused(true);
                    for(Occurrence occurrence : anime.getOccurrences()){
                        if(occurrence != selectedOccurrence) occurrence.setFocused(false);
                    }
                    animeDao.save(anime);
                }
            }else{ // if oldValue == true
                if(selectedOccurrence.isFocused()){
                    focusedButton.setSelected(true);
                }
            }
        }));

        trackingButton.setSelected(selectedOccurrence.isTracked());
        trackingButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            selectedOccurrence.setTracked(newValue);
            animeDao.save(anime);
            singletonDao.update();
        }));
    }

    public void kagamine(){
        double imageWidth = 314;
        double imageHeight = 450;

        // the anime level information todo: add actions for these two
        nameLabel.setText(anime.getName());
        rankLabel.setText("Rank:   " +(anime.getRank() > animeDao.getCollection().size() ? "" : anime.getRank()));
        scoreLabel.setText("Overall Score:   " +(anime.getScore() == -1 ? "" : anime.getScore() % 1 == 0 || anime.getScore() == 0 ? (int)anime.getScore() +"" : anime.getScore() +""));


        Occurrence focusedOccurrence;
        Tab focusedTab = null; // focusedTab will always be assigned due to the logic flow below

        // this logic flow tracks which tab+occurrence is first shown
        if(singletonDao.getCurrentOccurrence() != null){
            focusedOccurrence = singletonDao.getCurrentOccurrence();
        } else{
            focusedOccurrence = anime.getFocusedOccurrence() != null ? anime.getFocusedOccurrence() : anime.getOccurrences().get(0);
        }
        selectedOccurrence = focusedOccurrence;
        singletonDao.setCurrentAnime(anime, selectedOccurrence);


        // now we add a tab corresponding to each occurrence
        for(Occurrence occurrence : anime.getOccurrences()){
            Tab tab = new Tab();

            // the tab holds a reference of the occurrence for identification
            tab.setUserData(occurrence);

            // the tab will make sure the controller knows which occurrence is active with this action listener
            tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    selectedOccurrence = (Occurrence)tab.getUserData();
                    singletonDao.setCurrentAnime(anime, selectedOccurrence);

                    // based on the new selected occurrence, we need to change what the toggle buttons are displaying
                    // these won't fire events because the event handlers have yet to be added to the buttons in this workflow
                    focusedButton.setSelected(selectedOccurrence.isFocused());
                    trackingButton.setSelected(selectedOccurrence.isTracked());
                }
            });

            if(occurrence == focusedOccurrence){
                focusedTab = tab;
            }

            tab.setText(occurrence.getName());

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setId("tabPaneScrollPane");
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            tab.setContent(scrollPane);

            VBox vBox = new VBox();
            vBox.setFillWidth(true);
            vBox.setMaxWidth(Double.MAX_VALUE);
            scrollPane.setContent(vBox);
            Common.setFasterScrollBar(scrollPane);

            HBox hBox = new HBox();
            hBox.setPadding(new Insets(5,5,5,5));
            hBox.setFillHeight(true);
            hBox.setMaxWidth(Double.MAX_VALUE);
            hBox.setMaxHeight(Double.MAX_VALUE);
            vBox.getChildren().add(hBox);

            GridPane gridPane = new GridPane();
            GridPane.setFillWidth(gridPane, true);
            gridPane.setPadding(new Insets(5,5,5,5));
            HBox.setHgrow(gridPane, Priority.ALWAYS);
            hBox.getChildren().add(gridPane);

            ColumnConstraints columnConstraints = new ColumnConstraints();
            ColumnConstraints columnConstraints2 = new ColumnConstraints();
            gridPane.getColumnConstraints().addAll(columnConstraints, columnConstraints2);

            int counter = 0;
            // occurrence information // todo add actions for all of these
            createDataLabels(counter++, gridPane, "Type:",
                    occurrence.getType(),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Episodes:",
                    occurrence.getEpisodes() == 0 && occurrence.getEpisodesWatched().length == 0 ? "" : occurrence.getEpisodesWatched().length +"/" +occurrence.getEpisodes() +" Episodes",
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Status:",
                    occurrence.getStatus(),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Aired Dates:",
                    (occurrence.getAiredStartDate() == null && occurrence.getAiredEndDate() == null) ?
                    "" : UtilityMethods.getAsFormattedDate(occurrence.getAiredStartDate()) +" to " +UtilityMethods.getAsFormattedDate(occurrence.getAiredEndDate()),
                    mouseEvent -> {
                if(mouseEvent.getButton() == MouseButton.SECONDARY){
                        singletonDao.setCurrentField("AiredDates");
                        Common.popup("editDates.fxml");
                    }});
            createDataLabels(counter++, gridPane, "Premiered:",
                    occurrence.getPremieredYear() == -1 ? "" : occurrence.getPremieredSeason() +" " +occurrence.getPremieredYear(),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Genres:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getGenres()),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Themes:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getThemes()),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Durration:",
                    occurrence.getDuration() > 0 ? occurrence.getDuration()/60 +" Minutes" : "",
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Content Rating:",
                    occurrence.getRating(),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "", "", mouseEvent -> { }); // using as a spacer
            createDataLabels(counter++, gridPane, "Source:",
                    occurrence.getSource(),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Studios:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getStudios()),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Producers:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getProducers()),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Licensors:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getLicensors()),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "", "", mouseEvent -> { }); // using as a spacer
            createDataLabels(counter++, gridPane, "Watch Status:",
                    occurrence.getWatchStatus(),
                    mouseEvent -> { });
            createDataLabels(counter++, gridPane, "Watch Dates:",
                    (occurrence.getStartedWatching() == null && occurrence.getFinishedWatching() == null) ?
                            "" : UtilityMethods.getAsFormattedDate(occurrence.getStartedWatching()) +" to " +UtilityMethods.getAsFormattedDate(occurrence.getFinishedWatching()),
                    mouseEvent -> {
                        if(mouseEvent.getButton() == MouseButton.SECONDARY){
                            singletonDao.setCurrentField("WatchDates");
                            Common.popup("editDates.fxml");
                        }
                    });
            createDataLabels(counter++, gridPane, "Language:",
                    occurrence.getLanguage(),
                    mouseEvent -> { });
            createDataLabels(counter, gridPane, "Score:",
                    occurrence.getScore() == -1 ? "" : occurrence.getScore() % 1 == 0 || occurrence.getScore() == 0 ? (int)occurrence.getScore() +"" : occurrence.getScore() +"",
                    mouseEvent -> {
                        if(mouseEvent.getButton() == MouseButton.SECONDARY){
                            singletonDao.setCurrentField("OccurrenceScore");
                            Common.popup("editScore.fxml");
                        }
                    });


            BufferedImage bufferedImage = UtilityMethods.toBufferedImage(occurrence.getImageIcon().getImage());
            Pane imagePane = Common.getImagePaneFor(null, bufferedImage);
            imagePane.setMinSize(imageWidth, imageHeight);
            imagePane.setPrefSize(imageWidth, imageHeight);
            imagePane.setMaxSize(imageWidth, imageHeight);
            imagePane.setPadding(new Insets(5,0,0,5));
            imagePane.setOnMouseClicked(event -> {
                // todo: popup to change the image
            });

            hBox.getChildren().add(imagePane);


            // below the data and images
            GridPane gridPane2 = new GridPane();
            GridPane.setFillWidth(gridPane2, true);
            gridPane2.setPadding(new Insets(0,5,0,5));
            //gridPane2.setId("containerBackground");
            HBox.setHgrow(gridPane2, Priority.ALWAYS);
            vBox.getChildren().add(gridPane2);

            ColumnConstraints columnConstraints3 = new ColumnConstraints();
            ColumnConstraints columnConstraints4 = new ColumnConstraints();
            gridPane.getColumnConstraints().addAll(columnConstraints3, columnConstraints4);

            Label notesLabel = new Label("");
            GridPane.setHgrow(notesLabel, Priority.ALWAYS);
            notesLabel.setId("animeDataLabel");
            notesLabel.setPrefHeight(20);
            notesLabel.setPadding(new Insets(0,0,0,5));
            gridPane2.add(notesLabel, 0, 0);

            ProgressBar progressBar = new ProgressBar(((double)occurrence.getEpisodesWatched().length)/((double)occurrence.getEpisodes()));
            progressBar.setPrefWidth(imageWidth);
            progressBar.setPrefHeight(20);
            gridPane2.add(progressBar, 1, 0);

            // text area
            TextArea textArea = new TextArea();
            textArea.setBackground(Background.EMPTY);
            textArea.setPadding(new Insets(5,5,5,5));
            textArea.setPrefHeight(0);
            VBox.setVgrow(textArea, Priority.ALWAYS);
            vBox.getChildren().add(textArea);



            tabPane.getTabs().add(tab);
        }



        tabPane.getSelectionModel().select(focusedTab);
    }

    public void createDataLabels(int index, GridPane parent, String textOne, String textTwo, EventHandler<MouseEvent> eventHandler){
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.NEVER);

        Label paramLabel = new Label(textOne);
        paramLabel.setPrefHeight(textOne.equals("") ? 15 : 24);
        paramLabel.setMinWidth(150);
        paramLabel.setPrefWidth(150);
        paramLabel.setId("animeDataLabel");
        paramLabel.setOnMouseClicked(eventHandler);
        GridPane.setHgrow(paramLabel, Priority.NEVER);
        parent.add(paramLabel, 0, index);

        Label valueLabel = new Label(textTwo);
        valueLabel.setPrefHeight(paramLabel.getPrefHeight());
        valueLabel.setMaxWidth(Double.MAX_VALUE);
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setId("animeDataLabel");
        valueLabel.setOnMouseClicked(eventHandler);
        GridPane.setHgrow(valueLabel, Priority.ALWAYS);
        parent.add(valueLabel, 1, index);

        gridPane.getRowConstraints().add(rowConstraints);
    }

    @Override
    public ProgressBar getUpdateProgressBar() {
        return progressBar;
    }
}
