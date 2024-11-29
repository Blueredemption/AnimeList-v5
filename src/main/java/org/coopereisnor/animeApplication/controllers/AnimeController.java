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
import org.coopereisnor.animeApplication.customJavaFXObjects.PercentProgressBar;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Episode;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

public class AnimeController implements Controller{
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
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
        populateControls();
        configureToggleActions();
    }

    public void configureAnimeActions(){
        previousButton.setOnAction(actionEvent -> {
            Anime nextAnime = Common.getNextAnime(anime, false);
            singletonDao.setCurrentAnime(nextAnime, null);
            application.changeScene("anime.fxml");
        });

        nextButton.setOnAction(actionEvent -> {
            Anime nextAnime = Common.getNextAnime(anime, true);
            singletonDao.setCurrentAnime(nextAnime, null);
            application.changeScene("anime.fxml");
        });

        generalStatisticsButton.setOnAction(actionEvent -> {
            Common.popup("generalStatistics.fxml");
        });

        rankLabel.setOnMouseClicked(mouseEvent -> {
            Common.popup("editRank.fxml");
        });

        scoreLabel.setOnMouseClicked(mouseEvent -> {
            singletonDao.setCurrentField("AnimeScore");
            Common.popup("editScore.fxml");
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
            if(selectedOccurrence.isTracked() != newValue){
                selectedOccurrence.setTracked(newValue);
                animeDao.save(anime);
                singletonDao.update();
            }
        }));
    }

    public void populateControls(){
        // the anime level information
        nameLabel.setText(anime.getName());
        nameLabel.setOnMouseClicked(mouseEvent -> {
            Common.popup("editAnime.fxml");
        });

        rankLabel.setText("Rank:   " +(anime.getRank() > animeDao.getCollection().size() ? "" : anime.getRank()));
        scoreLabel.setText("Overall Score:   " +(anime.getScore() == -1 ? "" : anime.getScore() == 10 || anime.getScore() == 0 ? (int)anime.getScore() +"" : anime.getScore() +""));


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

            Label nameLabel = new Label(occurrence.getName());
            nameLabel.setOnMouseClicked(mouseEvent -> {
                if((mouseEvent.getButton() == MouseButton.SECONDARY) && tab.isSelected()){
                    Common.popup("editOccurrence.fxml");
                }
            });
            tab.setGraphic(nameLabel);

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
            // occurrence information //
            createDataLabels(counter++, gridPane, "Type:",
                    occurrence.getType(),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Type");
                        Common.popup("editTag.fxml");
                    });
            createDataLabelsAndAddEpisodeButton(counter++, gridPane, "Episodes:",
                    occurrence.getEpisodes() == 0 && occurrence.getEpisodesWatched().length == 0 ? "" : occurrence.getEpisodesWatched().length +" / " +occurrence.getEpisodes() +" Episodes",
                    mouseEvent -> {
                        Common.popup("editEpisodes.fxml");
                    },
                    occurrence);
            createDataLabels(counter++, gridPane, "Status:",
                    occurrence.getStatus(),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Status");
                        Common.popup("editTag.fxml");
                    });
            createDataLabels(counter++, gridPane, "Aired Dates:",
                    UtilityMethods.getDatesString(occurrence.getAiredStartDate(), occurrence.getAiredEndDate()),
                    mouseEvent -> {
                        singletonDao.setCurrentField("AiredDates");
                        Common.popup("editDates.fxml");
                    });
            createDataLabels(counter++, gridPane, "Premiered:",
                    occurrence.getPremieredYear() <= 1900 ? "" : occurrence.getPremieredSeason() +" " +occurrence.getPremieredYear(),
                    mouseEvent -> {
                        Common.popup("editPremiered.fxml");
                    });
            createDataLabels(counter++, gridPane, "Genres:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getGenres()),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Genres");
                        Common.popup("editTags.fxml");
                    });
            createDataLabels(counter++, gridPane, "Themes:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getThemes()),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Themes");
                        Common.popup("editTags.fxml");
                    });
            createDataLabels(counter++, gridPane, "Duration:",
                    occurrence.getDuration() > 0 ? occurrence.getDuration()/60 +" Minutes" : "",
                    mouseEvent -> {
                        Common.popup("editDuration.fxml");
                    });
            createDataLabels(counter++, gridPane, "Content Rating:",
                    occurrence.getRating(),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Content Rating");
                        Common.popup("editTag.fxml");
                    });
            createDataLabels(counter++, gridPane, "", "", mouseEvent -> { }); // using as a spacer
            createDataLabels(counter++, gridPane, "Source:",
                    occurrence.getSource(),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Source");
                        Common.popup("editTag.fxml");
                    });
            createDataLabels(counter++, gridPane, "Studios:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getStudios()),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Studios");
                        Common.popup("editTags.fxml");
                    });
            createDataLabels(counter++, gridPane, "Producers:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getProducers()),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Producers");
                        Common.popup("editTags.fxml");
                    });
            createDataLabels(counter++, gridPane, "Licensors:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getLicensors()),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Licensors");
                        Common.popup("editTags.fxml");
                    });
            createDataLabels(counter++, gridPane, "", "", mouseEvent -> { }); // using as a spacer
            createDataLabels(counter++, gridPane, "Watch Status:",
                    occurrence.getWatchStatus(),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Watch Status");
                        Common.popup("editTag.fxml");
                    });
            createDataLabels(counter++, gridPane, "Watch Dates:",
                    UtilityMethods.getDatesString(occurrence.getStartedWatching(), occurrence.getFinishedWatching()),
                    mouseEvent -> {
                        singletonDao.setCurrentField("WatchDates");
                        Common.popup("editDates.fxml");
                    });
            createDataLabels(counter++, gridPane, "Language:",
                    UtilityMethods.getAsCommaSeperatedString(occurrence.getLanguages()),
                    mouseEvent -> {
                        singletonDao.setCurrentField("Languages");
                        Common.popup("editTags.fxml");
                    });
            createDataLabels(counter, gridPane, "Score:",
                    occurrence.getScore() == -1 ? "" : occurrence.getScore() == 10 || occurrence.getScore() == 0 ? (int)occurrence.getScore() +"" : occurrence.getScore() +"",
                    mouseEvent -> {
                        singletonDao.setCurrentField("OccurrenceScore");
                        Common.popup("editScore.fxml");
                    });

            BufferedImage bufferedImage = UtilityMethods.toBufferedImage(occurrence.getImageIcon().getImage());
            Pane imagePane = Common.getImagePaneFor(null, bufferedImage);
            imagePane.setPadding(new Insets(5,0,0,5));
            imagePane.setOnMouseClicked(mouseEvent -> {
                Common.popup("editImage.fxml");
            });

            hBox.getChildren().add(imagePane);

            // below the data and images
            GridPane gridPane2 = new GridPane();
            GridPane.setFillWidth(gridPane2, true);
            gridPane2.setPadding(new Insets(0,5,0,5));
            HBox.setHgrow(gridPane2, Priority.ALWAYS);
            vBox.getChildren().add(gridPane2);

            ColumnConstraints columnConstraints3 = new ColumnConstraints();
            ColumnConstraints columnConstraints4 = new ColumnConstraints();
            gridPane.getColumnConstraints().addAll(columnConstraints3, columnConstraints4);

            Label notesLabel = new Label("Notes:");
            GridPane.setHgrow(notesLabel, Priority.ALWAYS);
            notesLabel.setId("animeDataLabel");
            notesLabel.setPrefHeight(20);
            notesLabel.setPadding(new Insets(0,0,0,5));
            gridPane2.add(notesLabel, 0, 0);

            PercentProgressBar percentProgressBar  = new PercentProgressBar(occurrence.getEpisodesWatched().length,occurrence.getEpisodes());
            percentProgressBar.setPrefHeight(20);
            gridPane2.add(percentProgressBar, 1, 0);

            // text area
            TextArea textArea = new TextArea();
            textArea.setBackground(Background.EMPTY);
            textArea.setPadding(new Insets(5,5,5,5));
            textArea.setWrapText(true);
            textArea.setText(occurrence.getNotes());
            textArea.setOnKeyTyped(keyEvent -> saveNotes(occurrence, textArea));
            textArea.setUserData(true);
            Common.setFasterScrollBar(textArea);
            VBox.setVgrow(textArea, Priority.ALWAYS);
            vBox.getChildren().add(textArea);

            // dynamic
            tabPane.widthProperty().addListener((observableValue, oldWidth, newWidth) -> {
                resizeComponents(newWidth.intValue(), percentProgressBar, imagePane, textArea);
            });

            tabPane.getTabs().add(tab);
        }

        tabPane.getSelectionModel().select(focusedTab);
    }

    private void resizeComponents(int newWidth, PercentProgressBar percentProgressBar, Pane imagePane, TextArea textArea) {
        double imageRatio = 225d / 157d;
        double windowRatio = 2d/7.5d;
        double width = windowRatio * newWidth;
        double height = width * imageRatio;

        imagePane.setMinSize(width, height);
        imagePane.setPrefSize(width, height);
        imagePane.setMaxSize(width, height);

        textArea.setMinHeight(height / 6);

        percentProgressBar.setPrefWidth(width);
    }

    private void saveNotes(Occurrence occurrence, TextArea textArea){
        occurrence.setNotes(textArea.getText());
        animeDao.save(anime);
    };

    public void createDataLabels(int index, GridPane parent, String textOne, String textTwo, EventHandler<MouseEvent> eventHandler){
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.NEVER);

        Label paramLabel = new Label(textOne);
        paramLabel.setPrefHeight(textOne.equals("") ? 0 : 23);
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

    public void createDataLabelsAndAddEpisodeButton(int index, GridPane parent, String textOne, String textTwo, EventHandler<MouseEvent> eventHandler, Occurrence occurrence){
        createDataLabels(index, parent, textOne, textTwo, eventHandler);

        Button addButton = new Button("+");
        addButton.setMinWidth(25);
        addButton.setPrefWidth(25);
        if(occurrence.getEpisodesWatched().length != occurrence.getEpisodes()){
            addButton.setOnAction(actionEvent -> {
                // create and add episode object
                Episode newEpisode = new Episode();
                LocalDate localDate = LocalDate.now();
                newEpisode.setWatchDate(localDate);
                occurrence.addEpisodeWatched(newEpisode);

                // perform changes that might need to happen to startDate and/or endDate
                EditEpisodesController.applyStartAndEndConditionally(occurrence);

                // save anime, refresh page, run update function
                animeDao.save(anime);
                application.changeScene("anime.fxml");
                singletonDao.update();
            });
        }else{
            addButton.setVisible(false);
        }

        parent.add(addButton, 2, index);
    }

    @Override
    public ProgressBar getUpdateProgressBar() {
        return progressBar;
    }
}
