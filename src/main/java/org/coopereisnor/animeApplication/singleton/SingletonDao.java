package org.coopereisnor.animeApplication.singleton;


import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;

public final class SingletonDao {

    private AnimeDao animeDao;
    private SettingsDao settingsDao;
    private Application application;

    private String sortBy = "Started";
    private String listFXML = "list.fxml";
    private String order = "Ascending";
    private String type = "Anime";
    private Anime currentAnime = null;
    private Occurrence currentOccurrence = null;

    private final static SingletonDao INSTANCE = new SingletonDao();

    private SingletonDao() {
        animeDao = new AnimeDao();
        settingsDao = new SettingsDao();
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

    // sketchy application access
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
}
