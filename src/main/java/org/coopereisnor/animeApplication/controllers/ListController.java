package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.statistics.OccurrenceStatistics;

public class ListController {
    private AnimeDao animeDao = SingletonDao.getInstance().getAnimeDao();
    private SettingsDao settings = SingletonDao.getInstance().getSettingsDao();
    private Application application = SingletonDao.getInstance().getApplication();

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox insideScrollPane;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
        Common.setFasterScrollBar(scrollPane);
        loadComponents();


    }

    public void loadComponents(){
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

                double watched = OccurrenceStatistics.getTotalEpisodesWatched(anime.getOccurrences());
                double total = OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences());

                label = new Label(anime.getOccurrences().size() +"");
                label.setMinWidth(60);
                label.setTextOverrun(OverrunStyle.CLIP);
                containerPane.add(label, 2, 0);
                GridPane.setMargin(label, new Insets(0, 5, 0, 5));

                label = new Label(OccurrenceStatistics.getEarliestYear(anime.getOccurrences()) +"");
                label.setMinWidth(80);
                label.setTextOverrun(OverrunStyle.CLIP);
                containerPane.add(label, 3, 0);
                GridPane.setMargin(label, new Insets(0, 5, 0, 5));

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

}
