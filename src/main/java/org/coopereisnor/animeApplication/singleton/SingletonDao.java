package org.coopereisnor.animeApplication.singleton;


import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;

public final class SingletonDao {

    private AnimeDao animeDao;
    private SettingsDao settingsDao;
    private Application application;

    private String sortBy = "Started";
    private String listFXML = "list.fxml";
    private String order = "Ascending";
    private String type = "Anime";

    private final static SingletonDao INSTANCE = new SingletonDao();

    private SingletonDao() {
        animeDao = new AnimeDao();
        settingsDao = new SettingsDao();
    }

    public static SingletonDao getInstance(){
        return INSTANCE;
    }

    public AnimeDao getAnimeDao(){
        return INSTANCE.animeDao;
    }

    public SettingsDao getSettingsDao() {
        return INSTANCE.settingsDao;
    }

    // sketchy application access
    public void setApplication(Application application) {
        INSTANCE.application = application;
    }

    public Application getApplication(){
        return INSTANCE.application;
    }

    public String getSortBy() {
        return INSTANCE.sortBy;
    }

    public void setSortBy(String sortBy) {
        INSTANCE.sortBy = sortBy;
    }

    public String getListFXML() {
        return INSTANCE.listFXML;
    }

    public void setListFXML(String listFXML) {
        INSTANCE.listFXML = listFXML;
    }

    public String getOrder() {
        return INSTANCE.order;
    }

    public void setOrder(String order) {
        INSTANCE.order = order;
    }

    public String getType() {
        return INSTANCE.type;
    }

    public void setType(String type) {
        INSTANCE.type = type;
    }
}
