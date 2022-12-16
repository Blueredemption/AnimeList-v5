package org.coopereisnor.animeApplication.controllers;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.coopereisnor.Program;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Common {
    public static void configureNavigation(GridPane gridPane, Class<?> cls){
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
                default -> Program.logger.error("Reached Default in Common configureNavigation");
            }
        }

        requestProperFocus(gridPane,cls);
    }

    // ensures correct button has focus
    public static void requestProperFocus(GridPane gridPane, Class<?> cls){
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
                default -> Program.logger.error("Reached Default in Common requestProperFocus");
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

    public static void setFasterScrollBar(TextArea textArea) {
        // what a fucking pain in the ass this is. So apparently textArea has its own scrollPane that can't be accessed unless the css is loaded
        // which is after the Platform.runLater() scope. I have a listener for the first scroll event on the textArea to add a listener to the scrollPane inside it.
        // the textArea only fires a scrollEvent once (maybe when I first apply the text?), so I don't have to worry about the first event firing more than once, but I put a
        // flag in there anyway using the setUserData property where the textArea is initialized just to be safe. Really hacky code to get around this issue, I don't like it.
        if((boolean)textArea.getUserData()){
            textArea.setUserData(false);
            textArea.setOnScroll(scrollEvent -> {
                ScrollPane scrollPane = (ScrollPane) textArea.lookup(".scroll-pane");
                setFasterScrollBar(scrollPane);
            });
        }
    }

    public static Pane getImagePaneFor(Pane imagePane, BufferedImage bufferedImage){
        Pane workingPane = imagePane != null ? imagePane : new Pane();
        workingPane.setBackground(new Background(makeBackgroundImage(bufferedImage)));
        setBorder(workingPane);
        return workingPane;
    }

    public static Pane getImagePaneFor(Pane imagePane, BufferedImage bufferedImage, Color color){
        Pane workingPane = imagePane != null ? imagePane : new Pane();
        workingPane.setBackground(new Background(makeBackgroundImage(bufferedImage)));
        setBorder(workingPane, color);
        return workingPane;
    }

    public static void setBorder(Pane pane){
        setBorder(pane, SingletonDao.getInstance().getSettingsDao().getSettings().getBorderColor());
    }

    public static void setBorder(Pane pane, Color color){
        pane.setBorder(new Border(new BorderStroke(UtilityMethods.convertColor(color), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
    }

    public static BackgroundImage makeBackgroundImage(BufferedImage bufferedImage){
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        return new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
    }

    public static BufferedImage makeFilteredImage(BufferedImage baseImage) {
        BufferedImage overlayImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = overlayImage.createGraphics();
        Color color = SingletonDao.getInstance().getSettingsDao().getSettings().getTextAreaColor();
        g2d.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 230));
        g2d.clearRect(0, 0, baseImage.getWidth(), baseImage.getHeight());
        g2d.dispose();

        BufferedImage combinedImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = combinedImage.getGraphics();
        g.drawImage(baseImage, 0, 0, null);
        g.drawImage(overlayImage, 0, 0, null);
        g.dispose();

        return combinedImage;
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
            popup.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("/images/Icon.png"))));

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
