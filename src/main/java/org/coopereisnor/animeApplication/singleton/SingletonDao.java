package org.coopereisnor.animeApplication.singleton;


import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.settingsDao.SettingsDao;

import java.util.ArrayList;

public final class SingletonDao {

    private AnimeDao animeDao;
    private SettingsDao settingsDao;
    private Application application;

    private String sortBy = "Started";
    //private ArrayList<Tag> activeFilters = new ArrayList<Filter>();
    private ArrayList<Anime> filteredAndSortedAnime = null;
    private ArrayList<Pair> filteredAndSortedOccurrences = null;
    private String listFXML = "list.fxml";
    private String order = "Ascending";
    private String type = "Anime";
    private Anime currentAnime = null;
    private Occurrence currentOccurrence = null;
    private String currentField = "";

    // things that are generated by the multithreaded methods
    //private ArrayList<Tag> genreTags

    private final static SingletonDao INSTANCE = new SingletonDao();

    private SingletonDao() {
        animeDao = new AnimeDao();
        settingsDao = new SettingsDao();

        compileLists();
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

    public String getSortBy() {
        return this.sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getListFXML() {
        return this.listFXML;
    }

    public void setListFXML(String listFXML) {
        this.listFXML = listFXML;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
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

    public ArrayList<Anime> getFilteredAndSortedAnime() {
        return filteredAndSortedAnime;
    }

    public ArrayList<Pair> getFilteredAndSortedOccurrences() {
        return filteredAndSortedOccurrences;
    }

    public String getCurrentField() {
        return currentField;
    }

    public void setCurrentField(String currentField) {
        this.currentField = currentField;
    }

    // compile methods
    public void compileLists(){
        System.out.println("Singleton: Compiling Lists");
        // todo: do this method justice
        // for now these are just as is from the AnimeDao
        filteredAndSortedAnime = animeDao.getCollection();
        filteredAndSortedOccurrences = AnimeAggregate.getPairs(animeDao.getCollection());
    }

    // todo: these methods. They should be utilizing multithreading.
    public void update(boolean statistics, boolean filterTags){
        System.out.println("Singleton: Updating");
        if(statistics) compileStatistics();
        if(filterTags) aggregateFilterTags();
    }

    private void compileStatistics(){

    }

    private void aggregateFilterTags(){

        validateCurrentFilterTags();
    }

    private void validateCurrentFilterTags(){

    }
}
