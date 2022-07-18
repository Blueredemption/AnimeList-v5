package org.coopereisnor.animeApplication.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.statistics.OccurrenceStatistics;
import org.coopereisnor.utility.UtilityMethods;

import java.util.Random;

public class ListController {
    private AnimeDao animeDao = SingletonDao.getInstance().getAnimeDao();
    private SettingsDao settingsDao = SingletonDao.getInstance().getSettingsDao();
    private Application application = SingletonDao.getInstance().getApplication();

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private FlowPane imageFlowPane;

    @FXML
    private Button listViewButton;

    @FXML
    private Button imageViewButton;

    @FXML
    private VBox insideScrollPane;

    @FXML
    ToggleButton toggleButton;

    @FXML
    private ComboBox comboBox;

    @FXML
    private FlowPane flowPane;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
        Common.setFasterScrollBar(scrollPane);
        Common.configureListImageButtons(listViewButton, imageViewButton);
        if(SingletonDao.getInstance().getListFXML().equals("list.fxml")) loadListComponents();
        else loadImageComponents();
        loadSortComponents();
        loadFilterComponents();

    }

    public void loadListComponents(){
        int counter = 0;
        for(int i = 0; i < 20; i++){
            for(Anime anime : animeDao.getCollection()){
                counter++;

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

                Label label = new Label(counter +":");
                label.setMinWidth(35);
                label.setTextOverrun(OverrunStyle.CLIP);
                containerPane.add(label, 0, 0);
                GridPane.setMargin(label, new Insets(0, 5, 0, 5));

                label = new Label(anime.getName());
                label.setPrefWidth(Integer.MAX_VALUE);
                label.setTextOverrun(OverrunStyle.ELLIPSIS);
                containerPane.add(label, 1, 0);
                GridPane.setMargin(label, new Insets(0, 5, 0, 5));

                // todo replace this later, just want different numbers for effect right now
                double randomNum = (new Random()).nextDouble(10);
                randomNum = ((double)((int)(randomNum*10)))/10.0;

                anime.setScore(randomNum);
                label = new Label(anime.getScore() % 1 == 0 || anime.getScore() == 0 ? (int)anime.getScore() +"" : anime.getScore() +"");
                label.setMinWidth(70);
                label.setTextOverrun(OverrunStyle.CLIP);
                containerPane.add(label, 2, 0);
                GridPane.setMargin(label, new Insets(0, 5, 0, 5));

                label = new Label(OccurrenceStatistics.getEarliestYear(anime.getOccurrences()) +"");
                label.setMinWidth(80);
                label.setTextOverrun(OverrunStyle.CLIP);
                containerPane.add(label, 3, 0);
                GridPane.setMargin(label, new Insets(0, 5, 0, 5));

                double watched = OccurrenceStatistics.getTotalEpisodesWatched(anime.getOccurrences());
                double total = OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences());

                label = new Label((int)watched + "/" +(int)total);
                label.setMinWidth(90);
                label.setTextOverrun(OverrunStyle.CLIP);
                containerPane.add(label, 4, 0);
                GridPane.setMargin(label, new Insets(0, 5, 0, 5));

                ProgressBar progressBar = new ProgressBar(Math.random()); // watched/total
                progressBar.setMinWidth(125);
                progressBar.setPrefHeight(containerPane.getPrefHeight() - containerPane.getInsets().getBottom() - containerPane.getInsets().getTop() - 4);
                containerPane.add(progressBar, 5, 0);
                GridPane.setMargin(progressBar, new Insets(0, 5, 0, 5));

                Button viewButton = new Button("View");
                viewButton.setMinWidth(100);
                viewButton.setPrefHeight(containerPane.getPrefHeight() - containerPane.getInsets().getBottom() - containerPane.getInsets().getTop());
                containerPane.add(viewButton, 6, 0);
                GridPane.setMargin(viewButton, new Insets(0, 5, 0, 5));

            }
        }
    }

    public void loadImageComponents(){

        for(int i = 0; i < 20; i++) {
            for (Anime anime : animeDao.getCollection()) {
                Pane imagePane = new Pane();
                // todo: dynamically select which occurrence to get image from
                Image image = SwingFXUtils.toFXImage(UtilityMethods.toBufferedImage(anime.getOccurrences().get(0).getImageIcon().getImage()), null);
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
        }
    }

    public void loadSortComponents(){
        // todo: move this array initialization later
        ObservableList<String> sortStrings = FXCollections.observableArrayList("Started", "Score", "Name", "Eps. Watched", "Eps. Total", "Season", "Year", "Progress");
        comboBox.setItems(sortStrings);
        comboBox.setValue(sortStrings.get(0));

        // todo: move this and make this action listener work later and text state stored in singleton
        toggleButton.setText("Descending");

        toggleButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                toggleButton.setText("Ascending");
            } else{
                toggleButton.setText("Descending");
            }
        }));
    }

    public void loadFilterComponents(){

        // todo: make this not this
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
