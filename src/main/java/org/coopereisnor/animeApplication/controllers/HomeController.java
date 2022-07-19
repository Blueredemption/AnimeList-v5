package org.coopereisnor.animeApplication.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

public class HomeController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private GridPane gridPane;

    @FXML
    private Pane imagePane;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
        applyImage();
    }

    public void applyImage(){
        // set the image background on the ImagePane todo: use settingsDao
        Image image = SwingFXUtils.toFXImage(UtilityMethods.toBufferedImage(settingsDao.getSettings().getHomeImage().getImage()), null);
        BackgroundImage backgroundImage= new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        imagePane.setBackground(new Background(backgroundImage));

        // set the border todo: use settingsDao
        imagePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
    }

}