package org.coopereisnor.animeApplication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.singleton.SingletonDao;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.setMaxWidth(3000);
        stage.setMaxHeight(2100);

        SingletonDao.getInstance().setApplication(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("AnimeList-v5");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml) {
        try{
            Parent pane = FXMLLoader.load(Objects.requireNonNull(Application.class.getResource(fxml)));
            stage.getScene().setRoot(pane);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}