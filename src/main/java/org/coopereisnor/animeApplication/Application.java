package org.coopereisnor.animeApplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.singleton.SingletonDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class Application extends javafx.application.Application {
    private static String title = "AnimeList-v5";
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        SingletonDao.getInstance().setApplication(this);

        this.stage = stage;
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.setMaxWidth(3000);
        stage.setMaxHeight(2100);

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        File file = Paths.get(SingletonDao.getInstance().getSettingsDao().getRoot().toPath() + File.separator +"test.css").toFile();
        scene.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));

        stage.setTitle(title + "  –  Home");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml, String extraText) {
        try{
            Parent pane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
            stage.getScene().setRoot(pane);
            stage.setTitle(title + "  –  " +extraText);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}