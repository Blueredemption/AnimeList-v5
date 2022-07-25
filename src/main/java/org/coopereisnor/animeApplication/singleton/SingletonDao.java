package org.coopereisnor.animeApplication.singleton;


import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.controllers.Controller;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.settingsDao.SettingsDao;

import java.util.ArrayList;

public final class SingletonDao {

    private final AnimeDao animeDao;
    private final SettingsDao settingsDao;
    private Application application;
    private Controller currentController = null;

    private String listFXML = "list.fxml";
    private String type = "Anime";
    private Anime currentAnime = null;
    private Occurrence currentOccurrence = null;
    private String currentField = "";

    private final ListContainer listContainer;
    private FilterContainer filterContainer;
    private StatisticsContainer statisticsContainer;

    private final static SingletonDao INSTANCE = new SingletonDao();

    private SingletonDao() {
        animeDao = new AnimeDao();
        settingsDao = new SettingsDao();
        listContainer = new ListContainer(animeDao);
    }

    public static SingletonDao getInstance(){
        return INSTANCE;
    }

    public AnimeDao getAnimeDao(){
        return this.animeDao;
    }

    public SettingsDao getSettingsDao() {
        return this.settingsDao;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Application getApplication(){
        return this.application;
    }

    public Controller getCurrentController() {
        return currentController;
    }

    public void setCurrentController(Controller currentController) {
        this.currentController = currentController;
    }

    public String getListFXML() {
        return this.listFXML;
    }

    public void setListFXML(String listFXML) {
        this.listFXML = listFXML;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Anime getCurrentAnime() {
        return currentAnime;
    }

    public Occurrence getCurrentOccurrence(){
        return currentOccurrence;
    }

    public void setCurrentAnime(Anime currentAnime, Occurrence occurrence) {
        this.currentAnime = currentAnime;
        this.currentOccurrence = occurrence;
    }

    public String getCurrentField() {
        return currentField;
    }

    public void setCurrentField(String currentField) {
        this.currentField = currentField;
    }

    public ListContainer getListContainer() {
        return listContainer;
    }

    public FilterContainer getFilterContainer() {
        return filterContainer;
    }

    public StatisticsContainer getStatisticsContainer() {
        return statisticsContainer;
    }

    // compile methods
    public void update(){
        updateFilters();
    }

    public void updateFilters(){
        new FilterContainer();
    }

    // this method is only called by the filterContainer itself
    public void setFilterContainer(FilterContainer filterContainer){
        this.filterContainer = filterContainer;
    }

    public void updateStatistics(){
        listContainer.update(); // the list container starts the update process for statistics once it is complete
    }

    // this method is only called by the statisticsContainer itself
    public void setStatisticsContainer(StatisticsContainer statisticsContainer){
        this.statisticsContainer = statisticsContainer;
    }
}
