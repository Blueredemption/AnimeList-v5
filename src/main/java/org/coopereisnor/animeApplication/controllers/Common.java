package org.coopereisnor.animeApplication.controllers;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;

public class Common {
    public static void configureNavigation(GridPane gridPane, Class cls){
        Application application = SingletonDao.getInstance().getApplication();

        for(Node node : gridPane.getChildren()){
            switch (node.getId()) {
                case "homeButton" -> {
                    if(!cls.equals(HomeController.class)) ((Button)node).setOnAction(actionEvent -> { application.changeScene("home.fxml", "Home");});
                }
                case "listButton" -> {
                    if(!cls.equals(ListController.class)) ((Button)node).setOnAction(actionEvent -> { application.changeScene("list.fxml", "My List");});
                }
                case "newButton" -> {
                    /* Popup */
                }
                case "statisticsButton" -> {
                    if(!cls.equals(StatisticsController.class)) ((Button)node).setOnAction(actionEvent -> { application.changeScene("statistics.fxml", "Statistics");});
                }
                case "timelineButton" -> {
                    if(!cls.equals(TimelineController.class)) ((Button)node).setOnAction(actionEvent -> { application.changeScene("timeline.fxml", "Timeline");});
                }
                case "notesButton" -> {
                    if(!cls.equals(NotesController.class)) ((Button)node).setOnAction(actionEvent -> { application.changeScene("notes.fxml", "Notes");});
                }
                case "settingsButton" -> {
                    if(!cls.equals(SettingsController.class)) ((Button)node).setOnAction(actionEvent -> { application.changeScene("settings.fxml", "Settings");});
                }
            }
        }
    }
}
