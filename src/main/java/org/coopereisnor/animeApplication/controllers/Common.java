package org.coopereisnor.animeApplication.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;

public class Common {
    public static void configureNavigation(GridPane gridPane, Class cls){
        Application application = SingletonDao.getInstance().getApplication();

        for(Node node : gridPane.getChildren()){
            switch (node.getId()) {
                case "homeButton" -> {
                    if(!cls.equals(HomeController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("home.fxml", "Home"));
                }
                case "listButton" -> {
                    if(!cls.equals(ListController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene(SingletonDao.getInstance().getListFXML(), "My List"));
                }
                case "newButton" -> {
                    /* Popup */
                }
                case "statisticsButton" -> {
                    if(!cls.equals(StatisticsController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("statistics.fxml", "Statistics"));
                }
                case "timelineButton" -> {
                    if(!cls.equals(TimelineController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("timeline.fxml", "Timeline"));
                }
                case "notesButton" -> {
                    if(!cls.equals(NotesController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("notes.fxml", "Notes"));
                }
                case "settingsButton" -> {
                    if(!cls.equals(SettingsController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("settings.fxml", "Settings"));
                }
            }
        }

        requestProperFocus(gridPane,cls);
    }

    // ensures correct button has focus
    public static void requestProperFocus(GridPane gridPane, Class cls){
        for(Node node : gridPane.getChildren()){
            switch (node.getId()) {
                case "homeButton" -> {
                    if(cls.equals(HomeController.class)) Platform.runLater(node::requestFocus);
                }
                case "listButton" -> {
                    if(cls.equals(ListController.class)) Platform.runLater(node::requestFocus);
                }
                case "newButton" -> {
                    /* Popup */
                }
                case "statisticsButton" -> {
                    if(cls.equals(StatisticsController.class)) Platform.runLater(node::requestFocus);
                }
                case "timelineButton" -> {
                    if(cls.equals(TimelineController.class)) Platform.runLater(node::requestFocus);
                }
                case "notesButton" -> {
                    if(cls.equals(NotesController.class)) Platform.runLater(node::requestFocus);
                }
                case "settingsButton" -> {
                    if(cls.equals(SettingsController.class)) Platform.runLater(node::requestFocus);
                }
            }
        }
    }

    public static void setFasterScrollBar(ScrollPane scrollPane) {
        scrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY();
            double contentHeight = scrollPane.getContent().getBoundsInLocal().getHeight();
            double scrollPaneHeight = scrollPane.getHeight();
            double diff = contentHeight - scrollPaneHeight;
            if (diff < 1) diff = 1;
            double vvalue = scrollPane.getVvalue();
            scrollPane.setVvalue(vvalue + -deltaY/diff);
        });
    }

    public static void configureListImageButtons(Button listViewButton, Button imageViewButton) {
        SingletonDao singletonDao = SingletonDao.getInstance();
        Application application = singletonDao.getApplication();

        EventHandler<ActionEvent> listClick = event -> {
            if(!singletonDao.getListFXML().equals("list.fxml")){
                singletonDao.setListFXML("list.fxml");
                application.changeScene(SingletonDao.getInstance().getListFXML(), "My List");
            }
        };

        listViewButton.setOnAction(listClick);

        EventHandler<ActionEvent> imageClick = event -> {
            if(!singletonDao.getListFXML().equals("listImages.fxml")){
                singletonDao.setListFXML("listImages.fxml");
                application.changeScene(SingletonDao.getInstance().getListFXML(), "My List");
            }
        };

        imageViewButton.setOnAction(imageClick);
    }
}
