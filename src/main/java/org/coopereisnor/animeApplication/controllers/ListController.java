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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.statistics.OccurrenceStatistics;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

public class ListController {
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
    private FlowPane flowPane;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
        Common.setFasterScrollBar(scrollPane);
        loadToggleComponents();
        loadTypeComponents();
        loadSortComponents();
        loadFilterComponents();
    }



    public void loadTypeComponents(){
        typeButton.setText(singletonDao.getType());
        orderButton.setText(singletonDao.getOrder());

        if(singletonDao.getListFXML().equals("list.fxml")){
            loadListComponents();
        }else {
            loadImageComponents();
        }
    }



    public void loadListComponents(){
        // first remove everything to start fresh
        insideScrollPane.getChildren().clear();

        int iterator = 0;
        if(singletonDao.getType().equals("Anime")) {
            for(Anime anime : animeDao.getCollection()){
                // this is temp until I implement scores
                double randomNum = (new Random()).nextDouble(10);
                randomNum = ((double)((int)(randomNum*10)))/10.0;
                anime.setScore(randomNum);

                String number = ++iterator +":";
                String name = anime.getName();
                String score = anime.getScore() % 1 == 0 || anime.getScore() == 0 ? (int)anime.getScore() +"" : anime.getScore() +"";
                String year = OccurrenceStatistics.getEarliestYear(anime.getOccurrences()) +"";
                String episodes = OccurrenceStatistics.getTotalEpisodesWatched(anime.getOccurrences()) + "/" +OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences());
                double progress = Math.random();
                EventHandler eventHandler = event -> {};

                addListComponents(number, name, score, year, episodes, progress, eventHandler);
            }
        }else{
            for(Anime anime : animeDao.getCollection()){
                for(Occurrence occurrence : anime.getOccurrences()){
                    String number = ++iterator +":";
                    String name = occurrence.getName();
                    String score = occurrence.getScore() +"";
                    String year = occurrence.getPremieredYear() +"";
                    String episodes = occurrence.getEpisodesWatched().size() +"/" +occurrence.getEpisodes();
                    double progress = Math.random();
                    EventHandler eventHandler = event -> {};

                    addListComponents(number, name, score, year, episodes, progress, eventHandler);
                }
            }
        }
    }

    public void addListComponents(String number, String name, String score, String year, String episodes, double progress, EventHandler eventHandler){
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
        containerPane.add(viewButton, 6, 0);
        GridPane.setMargin(viewButton, new Insets(0, 5, 0, 5));
    }



    public void loadImageComponents(){
        // first remove everything to start fresh
        imageFlowPane.getChildren().clear();

        if(singletonDao.getType().equals("Anime")){
            for (Anime anime : animeDao.getCollection()) {
                addImageComponents(UtilityMethods.toBufferedImage(anime.getOccurrences().get(0).getImageIcon().getImage()));
            }
        } else{
            for (Anime anime : animeDao.getCollection()) {
                for(Occurrence occurrence : anime.getOccurrences()){
                    addImageComponents(UtilityMethods.toBufferedImage(occurrence.getImageIcon().getImage()));
                }
            }
        }
    }

    public void addImageComponents(BufferedImage bufferedImage){
        Pane imagePane = new Pane();
        // todo: dynamically select which occurrence to get image from
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        imagePane.setBackground(new Background(backgroundImage));
        imagePane.setOnMouseClicked(event -> System.out.println("Image Clicked!"));

        double width = 157;
        double height = 225;
        imagePane.setMinSize(width, height);
        imagePane.setPrefSize(width, height);
        imagePane.setMaxSize(width, height);
        imagePane.setPadding(new Insets(5,0,0,5));

        imageFlowPane.getChildren().add(imagePane);

        // set the border todo: use settingsDao
        imagePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

    }





    public void loadToggleComponents(){
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
                singletonDao.setType("Occurrences");
            }
            loadTypeComponents();
        }));
    }

    public void loadSortComponents(){
        // todo: move array initialization later and store the selection in singleton
        // todo: make combobox impact the order of elements
        // todo: make order impact the order of elements

        ObservableList<String> sortStrings = FXCollections.observableArrayList("Started", "Score", "Name", "Eps. Watched", "Eps. Total", "Year", "Progress");
        comboBox.setItems(sortStrings);
        comboBox.setValue(sortStrings.get(0));

        orderButton.setSelected(!singletonDao.getOrder().equals("Ascending"));
        orderButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(oldValue){
                singletonDao.setOrder("Ascending");
            } else{
                singletonDao.setOrder("Descending");
            }
            loadTypeComponents();
        }));
    }

    public void loadFilterComponents(){
        // todo: make actually filter the elements and have them stored in singleton

        for(int i = 0; i < 20; i++){
            Button filterButton = new Button((Math.random() +"").substring(0, 1 + (int)(Math.random() * ((8 - 1) + 1))));
            filterButton.setPrefHeight(25);
            filterButton.setId(i%2 == 0 ? "filterButtonWant" : "filterButtonAvoid");
            flowPane.getChildren().add(filterButton);
        }


        // todo: if (filterCount > 1) add, else do nothing.
        Button filterButton = new Button("Clear All");
        filterButton.setPrefHeight(25);
        filterButton.setId("filterButtonClear");
        flowPane.getChildren().add(filterButton);
    }
}
