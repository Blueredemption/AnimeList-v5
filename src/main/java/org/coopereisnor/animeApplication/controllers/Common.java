package org.coopereisnor.animeApplication.controllers;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.utility.UtilityMethods;

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
                    if(!cls.equals(HomeController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("home.fxml"));
                }
                case "listButton" -> {
                    // want to allow refresh
                    ((Button)node).setOnAction(actionEvent -> application.changeScene(SingletonDao.getInstance().getListFXML()));
                }
                case "newButton" -> {
                    Common.configureNewAnimePopup(node);
                }
                case "statisticsButton" -> {
                    // want to allow refresh
                    ((Button)node).setOnAction(actionEvent -> application.changeScene("statistics.fxml"));
                }
                case "timelineButton" -> {
                    if(!cls.equals(TimelineController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("timeline.fxml"));
                }
                case "notesButton" -> {
                    if(!cls.equals(NotesController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("notes.fxml"));
                }
                case "settingsButton" -> {
                    if(!cls.equals(SettingsController.class)) ((Button)node).setOnAction(actionEvent -> application.changeScene("settings.fxml"));
                }
                default -> System.out.println("Reached Default in Common configureNavigation");
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
                default -> System.out.println("Reached Default in Common requestProperFocus");
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

        // set the border
        workingPane.setBorder(new Border(new BorderStroke(UtilityMethods.convertColor(SingletonDao.getInstance().getSettingsDao().getSettings().getBorderColor()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        return workingPane;
    }

    public static Anime getNextAnime(Anime currentAnime, boolean right){
        ArrayList<Anime> workingCollection = SingletonDao.getInstance().getListContainer().getFilteredAndSortedAnime();
        if(workingCollection.size() == 0){
            return currentAnime;
        }else{
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
    }


    // popups
    public static void configureNewAnimePopup(Node node){
        ((Button)node).setOnAction(actionEvent -> {
            SingletonDao.getInstance().setCurrentField("Anime");
            popup("addNew.fxml");
        });
    }

    public static final KeyCombination escape = new KeyCodeCombination(KeyCode.ESCAPE);
    public static void popup(String fxml){
        try {
            final Stage popup = new Stage();
            popup.setResizable(false);

            popup.initModality(Modality.APPLICATION_MODAL); // makes it act like JDialog

            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml))));
            File file = Paths.get(SingletonDao.getInstance().getSettingsDao().getRoot().toPath() + File.separator +"application.css").toFile();
            scene.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));
            popup.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
                if(escape.match(event)) popup.close();
            });

            popup.setScene(scene);
            popup.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
