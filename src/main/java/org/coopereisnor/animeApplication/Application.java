package org.coopereisnor.animeApplication;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.controllers.Common;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.statistics.AnimeStatistics;
import org.coopereisnor.utility.Log;
import org.coopereisnor.utility.UtilityMethods;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;


public class Application extends javafx.application.Application {
    private static String title = "AnimeList-v5";
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        int height;
        int width;

        if(System.getProperty("os.name").toUpperCase().contains("WINDOWS")){
            width = 1227;
            height = 826;
        }else{
            width = 1212;
            height = 798;
        }


        SingletonDao.getInstance().setApplication(this);
        SingletonDao.getInstance().update();

        // statisticsContainer is the last thing on its own thread to be initialized. Everything needs to have AN instance before launching.
        while(SingletonDao.getInstance().getStatisticsContainer() == null){
            try {
                Thread.sleep(1); // I don't like waiting using loops, but since this is just when launching the program I'll let it fly.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // with all the initialization over, we can create the GUI:
        this.stage = stage;
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("/images/Icon.png"))));

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home.fxml"));

        // load in the font needed
        Font.loadFont(Objects.requireNonNull(getClass().getResource("/css/fonts/Roboto-Regular.ttf")).toExternalForm(), 10);
        Font.loadFont(Objects.requireNonNull(getClass().getResource("/css/fonts/Roboto-Bold.ttf")).toExternalForm(), 10);

        Scene scene = new Scene(fxmlLoader.load());
        File file = Paths.get(SingletonDao.getInstance().getSettingsDao().getRoot().toPath() + File.separator +"application.css").toFile();
        scene.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml) {
        try{
            Parent pane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
            stage.getScene().setRoot(pane);
        }catch(IOException ex){
            Log.logger.error("IOException", ex);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}