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
    private int width = 1280;
    private int height = 800;
    @Override
    public void start(Stage stage) throws IOException {
        SingletonDao.getInstance().setApplication(this);

        this.stage = stage;
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.setWidth(width);
        stage.setHeight(height);

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        File file = Paths.get(SingletonDao.getInstance().getSettingsDao().getRoot().toPath() + File.separator +"test.css").toFile();
        scene.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));

        stage.setTitle(title + "  –  Viewing: Home");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml, String extraText) {
        try{
            Parent pane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
            stage.getScene().setRoot(pane);
            stage.setTitle(title + "  –  Viewing: " +extraText);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}