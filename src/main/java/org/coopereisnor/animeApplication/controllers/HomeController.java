package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

public class HomeController implements Controller{
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();

    @FXML
    private GridPane gridPane;

    @FXML
    private Pane imagePane;

    @FXML
    public void initialize() {
        singletonDao.setCurrentController(this);
        Common.configureNavigation(gridPane, this.getClass());
        applyImage();
    }

    public void applyImage(){
        Common.getImagePaneFor(imagePane, UtilityMethods.toBufferedImage(settingsDao.getSettings().getHomeImage().getImage()));

        imagePane.setOnMouseClicked(mouseEvent -> {
            Common.popup("editHomeImage.fxml");
        });
    }

    @Override
    public ProgressBar getUpdateProgressBar() {
        return null;
    }
}