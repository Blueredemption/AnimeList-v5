package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;

public class TimelineController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private GridPane gridPane;
    @FXML
    private StackedBarChart chart;

    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
    }

}
