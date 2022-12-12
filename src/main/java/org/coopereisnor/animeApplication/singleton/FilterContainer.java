package org.coopereisnor.animeApplication.singleton;

import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FilterContainer {
    private final SingletonDao singletonDao = SingletonDao.getInstance();

    public static final double MAX_THREADS = 12;

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

    public FilterContainer(){
        System.out.println("Filter Container");

        allPairs = AnimeAggregate.getPairs(singletonDao.getAnimeDao().getCollection());

        Executors.newSingleThreadExecutor().execute(() -> {
            ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool((int)MAX_THREADS);
            try{
                executor.execute(() -> {
                    types = AnimeAggregate.getTypes(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    statuses = AnimeAggregate.getStatuses(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    seasons = AnimeAggregate.getSeasons(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    genres = AnimeAggregate.getGenres(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    themes = AnimeAggregate.getThemes(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    ratings = AnimeAggregate.getRatings(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    sources = AnimeAggregate.getSources(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    studios = AnimeAggregate.getStudios(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    producers = AnimeAggregate.getProducers(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    licensors = AnimeAggregate.getLicensors(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    watchStatuses = AnimeAggregate.getWatchStatuses(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    languages = AnimeAggregate.getLanguages(allPairs);
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });

                executor.shutdown();
                if(!executor.awaitTermination(60, TimeUnit.SECONDS)){
                    System.out.println("Executor has timed out before all processes are finished"); // todo logging
                }
            }catch(InterruptedException ex){
                System.out.println("InterruptedException in StatisticsContainer"); // todo logging
            }

            isComplete();
        });
    }

    public void isComplete(){
        System.out.println("Filter Container Complete");
        singletonDao.updateProgressBar(getClass(), 0);
        singletonDao.setFilterContainer(this);
        singletonDao.updateStatistics();
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
}
