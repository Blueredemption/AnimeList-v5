package org.coopereisnor.animeApplication.singleton;


import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.settingsDao.SettingsDao;

public final class SingletonDao {

    private AnimeDao animeDao;
    private SettingsDao settingsDao;
    private Application application;

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
}
