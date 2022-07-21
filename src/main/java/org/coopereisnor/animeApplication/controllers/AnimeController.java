package org.coopereisnor.animeApplication.controllers;

import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;

public class AnimeController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();

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
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
        kagamine();
    }

    public void kagamine(){
        double imageWidth = 314;
        double imageHeight = 450;

        Occurrence focusedOccurrence;
        Tab focusedTab = null; // focusedTab will always be assigned due to the logic flow below

        if(singletonDao.getCurrentOccurrence() != null){
            focusedOccurrence = singletonDao.getCurrentOccurrence();
        } else{
            focusedOccurrence = anime.getOccurrences().get(0);
        }

        nameLabel.setText(anime.getName());

        for(Occurrence occurrence : anime.getOccurrences()){
            Tab tab = new Tab();

            if(occurrence == focusedOccurrence){
                focusedTab = tab;
            }

            tab.setText(occurrence.getName());

            ScrollPane scrollPane = new ScrollPane();
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
            // occurrence information
            createDataLabels(counter++, gridPane, "Type:", occurrence.getType());
            createDataLabels(counter++, gridPane, "Episodes:", occurrence.getEpisodesWatched().size() +"/" +occurrence.getEpisodes() +" Episodes");
            createDataLabels(counter++, gridPane, "Status:", occurrence.getStatus());
            createDataLabels(counter++, gridPane, "Aired Dates:", UtilityMethods.getAsFormattedDate(occurrence.getAiredStartDate()) +" to " +UtilityMethods.getAsFormattedDate(occurrence.getAiredEndDate()));
            createDataLabels(counter++, gridPane, "Premiered:", occurrence.getPremieredSeason() +" " +occurrence.getPremieredYear());
            createDataLabels(counter++, gridPane, "Genres:", UtilityMethods.getAsCommaSeperatedString(occurrence.getGenres()));
            createDataLabels(counter++, gridPane, "Themes:", UtilityMethods.getAsCommaSeperatedString(occurrence.getThemes()));
            createDataLabels(counter++, gridPane, "Durration:", occurrence.getDuration()/60 +" Minutes");
            createDataLabels(counter++, gridPane, "Content Rating:", occurrence.getRating());
            createDataLabels(counter++, gridPane, "", ""); // using as a spacer
            createDataLabels(counter++, gridPane, "Source:", occurrence.getSource());
            createDataLabels(counter++, gridPane, "Studios:", UtilityMethods.getAsCommaSeperatedString(occurrence.getStudios()));
            createDataLabels(counter++, gridPane, "Producers:", UtilityMethods.getAsCommaSeperatedString(occurrence.getProducers()));
            createDataLabels(counter++, gridPane, "Licensors:", UtilityMethods.getAsCommaSeperatedString(occurrence.getLicensors()));
            createDataLabels(counter++, gridPane, "", ""); // using as a spacer
            createDataLabels(counter++, gridPane, "Watch Status:", occurrence.getWatchStatus());
            createDataLabels(counter++, gridPane, "Watch Dates:", UtilityMethods.getAsFormattedDate(occurrence.getStartedWatching()) +" to " +UtilityMethods.getAsFormattedDate(occurrence.getFinishedWatching()));
            createDataLabels(counter++, gridPane, "Language:", occurrence.getLanguage());
            createDataLabels(counter++, gridPane, "Score:", occurrence.getScore() == -1 ? "" : occurrence.getScore() % 1 == 0 || occurrence.getScore() == 0 ? (int)occurrence.getScore() +"" : occurrence.getScore() +"");


            Pane imagePane = new Pane();
            // todo: dynamically select which occurrence to get image from
            BufferedImage bufferedImage = UtilityMethods.toBufferedImage(occurrence.getImageIcon().getImage());
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            BackgroundImage backgroundImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(1.0, 1.0, true, true, false, false));
            imagePane.setBackground(new Background(backgroundImage));
            imagePane.setOnMouseClicked(event -> {
                // todo: popup to change the image
            });

            imagePane.setMinSize(imageWidth, imageHeight);
            imagePane.setPrefSize(imageWidth, imageHeight);
            imagePane.setMaxSize(imageWidth, imageHeight);
            imagePane.setPadding(new Insets(5,0,0,5));
            // set the border todo: use settingsDao
            imagePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
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

            ProgressBar progressBar = new ProgressBar(((double)occurrence.getEpisodesWatched().size())/((double)occurrence.getEpisodes()));
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

    public void createDataLabels(int index, GridPane parent, String textOne, String textTwo){
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.NEVER);

        Label paramLabel = new Label(textOne);
        paramLabel.setPrefHeight(textOne.equals("") ? 15 : 24);
        paramLabel.setMinWidth(150);
        paramLabel.setPrefWidth(150);
        paramLabel.setId("animeDataLabel");
        GridPane.setHgrow(paramLabel, Priority.NEVER);
        parent.add(paramLabel, 0, index);

        Label valueLabel = new Label(textTwo);
        valueLabel.setPrefHeight(paramLabel.getPrefHeight());
        valueLabel.setMaxWidth(Double.MAX_VALUE);
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setId("animeDataLabel");
        GridPane.setHgrow(valueLabel, Priority.ALWAYS);
        parent.add(valueLabel, 1, index);

        gridPane.getRowConstraints().add(rowConstraints);
    }
}
