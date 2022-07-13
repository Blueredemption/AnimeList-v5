package org.coopereisnor.animeApplication.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

import javax.swing.*;
import java.util.Objects;

public class HomeController {
    private AnimeDao animeDao = SingletonDao.getInstance().getAnimeDao();
    private SettingsDao settingsDao = SingletonDao.getInstance().getSettingsDao();

    @FXML
    private VBox bottomVBox;
    @FXML
    private Pane imagePane;


    @FXML
    public void initialize() {
        // set the background color
        bottomVBox.setBackground(new Background(new BackgroundFill(Paint.valueOf(String.valueOf(Color.rgb(35, 37, 43))), CornerRadii.EMPTY, Insets.EMPTY)));

        // set the image background on the ImagePane
        Image image = SwingFXUtils.toFXImage(UtilityMethods.toBufferedImage(settingsDao.getSettings().getHomeImage().getImage()), null);
        BackgroundImage backgroundImage= new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        imagePane.setBackground(new Background(backgroundImage));

        imagePane.setBorder(new Border(new BorderStroke(Color.rgb(35, 37, 43).brighter(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));


        // clip the corners on the imagePane


    }
}