package org.coopereisnor.animeApplication.controllers;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

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
                    Common.configureNewAnimePopup(node);
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
                    // do nothing
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

    public static Pane getImagePaneFor(Pane imagePane, BufferedImage bufferedImage){
        Pane workingPane = imagePane != null ? imagePane : new Pane();
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        workingPane.setBackground(new Background(backgroundImage));

        // set the border todo: use settingsDao
        workingPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        return workingPane;
    }

    public static Anime getNextAnime(Anime currentAnime, boolean right){
        ArrayList<Anime> workingCollection = SingletonDao.getInstance().getFilteredAndSortedAnime();
        int currentIndex = workingCollection.indexOf(currentAnime);
        int size = workingCollection.size();
        if(currentIndex + 1 == size && right){
            return workingCollection.get(0);
        }else if(currentIndex == 0 && !right){
            return workingCollection.get(size - 1);
        }else{
            return right ? workingCollection.get(currentIndex + 1) : workingCollection.get(currentIndex - 1);
        }
    }


    // popups
    public static void configureNewAnimePopup(Node node){
        ((Button)node).setOnAction(actionEvent -> {
            try {
                final Stage popup = new Stage();
                popup.setResizable(false);

                popup.initModality(Modality.APPLICATION_MODAL); // makes it act like JDialog

                Scene addAnimeScene = new Scene(FXMLLoader.load(Objects.requireNonNull(Application.class.getResource("addAnime.fxml"))));
                File file = Paths.get(SingletonDao.getInstance().getSettingsDao().getRoot().toPath() + File.separator +"test.css").toFile();
                addAnimeScene.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));
                popup.setScene(addAnimeScene);
                popup.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void createNewOccurrencePopup(){
        try {
            final Stage popup = new Stage();
            popup.setResizable(false);

            popup.initModality(Modality.APPLICATION_MODAL); // makes it act like JDialog

            Scene addOccurrenceScene = new Scene(FXMLLoader.load(Objects.requireNonNull(Application.class.getResource("addOccurrence.fxml"))));
            File file = Paths.get(SingletonDao.getInstance().getSettingsDao().getRoot().toPath() + File.separator +"test.css").toFile();
            addOccurrenceScene.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));
            popup.setScene(addOccurrenceScene);
            popup.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
