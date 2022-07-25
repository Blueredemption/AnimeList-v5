package org.coopereisnor.animeApplication.singleton;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FilterContainer {
    private final SingletonDao singletonDao = SingletonDao.getInstance();

    private final double MAX_THREADS = 12;

    private final ArrayList<Pair> allPairs;

    private String[] types;
    private String[] statuses;
    private String[] seasons;
    private String[] genres;
    private String[] themes;
    private String[] ratings;
    private String[] sources;
    private String[] studios;
    private String[] producers;
    private String[] licensors;
    private String[] watchStatuses;
    private String[] languages;

    double currentPercent = 0.0;

    public FilterContainer(){
        System.out.println("Kagamine Rin");

        allPairs = AnimeAggregate.getPairs(singletonDao.getAnimeDao().getCollection());

        Executor executor = Executors.newFixedThreadPool((int) MAX_THREADS, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        executor.execute(() -> {
            types = AnimeAggregate.getTypes(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            statuses = AnimeAggregate.getStatuses(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            seasons = AnimeAggregate.getSeasons(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            genres = AnimeAggregate.getGenres(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            themes = AnimeAggregate.getThemes(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            ratings = AnimeAggregate.getRatings(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            sources = AnimeAggregate.getSources(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            studios = AnimeAggregate.getStudios(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            producers = AnimeAggregate.getProducers(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            licensors = AnimeAggregate.getLicensors(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            watchStatuses = AnimeAggregate.getWatchStatuses(allPairs);
            checkComplete();
        });
        executor.execute(() -> {
            languages = AnimeAggregate.getLanguages(allPairs);
            checkComplete();
        });

    }

    // getters
    public String[] getTypes() {
        return types;
    }

    public String[] getStatuses() {
        return statuses;
    }

    public String[] getSeasons() {
        return seasons;
    }

    public String[] getGenres() {
        return genres;
    }

    public String[] getThemes() {
        return themes;
    }

    public String[] getRatings() {
        return ratings;
    }

    public String[] getSources() {
        return sources;
    }

    public String[] getStudios() {
        return studios;
    }

    public String[] getProducers() {
        return producers;
    }

    public String[] getLicensors() {
        return licensors;
    }

    public String[] getWatchStatuses() {
        return watchStatuses;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void checkComplete(){
        currentPercent += 1.0/MAX_THREADS;

        if(types != null && statuses != null && seasons != null && genres != null && themes != null && ratings != null && sources != null && studios != null &&
        producers != null && licensors != null && watchStatuses != null && languages != null){
            singletonDao.setFilterContainer(this);
            singletonDao.updateStatistics();
        }

        if(singletonDao.getCurrentController() != null){
            ProgressBar progressBar = singletonDao.getCurrentController().getUpdateProgressBar();
            Platform.runLater(() -> {
                if(progressBar != null){
                    progressBar.setProgress(currentPercent/2.0);
                    progressBar.setVisible(true);
                }
            });
        }
    }
}
