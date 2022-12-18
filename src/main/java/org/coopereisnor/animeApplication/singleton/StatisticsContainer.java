package org.coopereisnor.animeApplication.singleton;

import org.coopereisnor.Program;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.manipulation.*;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StatisticsContainer {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final FilterContainer filterContainer = singletonDao.getFilterContainer();

    public static final double MAX_THREADS = 15;

    private final ArrayList<Anime> collection;
    private final ArrayList<Pair> pairs;

    private ArrayList<Anime> animeOrderedByRank;
    private TimeSpentCalculated timeSpentCalculated;
    private ArrayList<Count> typeCounts;
    private ArrayList<Count> statusCounts;
    private ArrayList<Count> seasonCounts;
    private ArrayList<Count> genreCounts;
    private ArrayList<Count> themeCounts;
    private ArrayList<Count> ratingCounts;
    private ArrayList<Count> sourceCounts;
    private ArrayList<Count> studioCounts;
    private ArrayList<Count> producerCounts;
    private ArrayList<Count> licensorCounts;
    private ArrayList<Count> watchStatusCounts;
    private ArrayList<Count> languageCounts;
    private final ArrayList<Wildcard> wildcards = new ArrayList<>();
    private ConfigurePlots configurePlots;

    public StatisticsContainer(){
        Program.logger.debug("Statistics Container");

        collection = (ArrayList<Anime>)singletonDao.getListContainer().getFilteredAndSortedAnime().clone(); // cloned because the order is manipulated
        pairs = (ArrayList<Pair>)singletonDao.getListContainer().getFilteredAndSortedOccurrences().clone();

        Executors.newSingleThreadExecutor().execute(() -> {
            ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool((int)MAX_THREADS);
            try{
                executor.execute(() -> {
                    animeOrderedByRank = singletonDao.getListContainer().sortAnime(collection, "Rank");
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    timeSpentCalculated = new TimeSpentCalculated(collection, OccurrenceStatistics.getListOfOccurrences(pairs));
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    typeCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Type", filterContainer.getTypes());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    statusCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Status", filterContainer.getStatuses());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    seasonCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Season", filterContainer.getSeasons());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    genreCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Genre", filterContainer.getGenres());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    themeCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Theme", filterContainer.getThemes());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    ratingCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Rating", filterContainer.getRatings());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    sourceCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Source", filterContainer.getSources());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    studioCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Studio", filterContainer.getStudios());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    producerCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Producer", filterContainer.getProducers());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    licensorCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Licensor", filterContainer.getLicensors());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    watchStatusCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Watch Status", filterContainer.getWatchStatuses());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    languageCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Language", filterContainer.getLanguages());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    wildcards.add(AnimeStatistics.getNewestAnime((ArrayList<Anime>) collection.clone())); // clones are to prevent concurrent modification
                    wildcards.add(OccurrenceStatistics.getNewestOccurrence((ArrayList<Pair>) pairs.clone()));
                    wildcards.add(AnimeStatistics.getOldestAnime((ArrayList<Anime>) collection.clone()));
                    wildcards.add(AnimeStatistics.getMostRecentlyStartedAnime((ArrayList<Anime>) collection.clone()));
                    wildcards.add(OccurrenceStatistics.getMostRecentlyStartedOccurrence((ArrayList<Pair>) pairs.clone()));
                    wildcards.add(AnimeStatistics.getFirstStartedAnime((ArrayList<Anime>) collection.clone()));
                    wildcards.add(AnimeStatistics.getLongestAnime((ArrayList<Anime>) collection.clone()));
                    wildcards.add(AnimeStatistics.getShortestAnime((ArrayList<Anime>) collection.clone()));
                    wildcards.add(AnimeStatistics.getLongestSpan((ArrayList<Anime>) collection.clone()));
                    wildcards.add(AnimeStatistics.getShortestSpan((ArrayList<Anime>) collection.clone()));
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    configurePlots = new ConfigurePlots(collection);
                });

                executor.shutdown();
                if(!executor.awaitTermination(60, TimeUnit.SECONDS)){
                    Program.logger.error("Executor has timed out before all processes are finished");
                }
            }catch(InterruptedException ex){
                Program.logger.error("InterruptedException", ex);
            }

            isComplete();
        });
    }

    public void isComplete(){
        Program.logger.debug("Statistics Container Complete");
        singletonDao.updateProgressBar(getClass(), 0);
        singletonDao.setStatisticsContainer(this);
    }

    // getters
    public ArrayList<Anime> getAnimeOrderedByRank() {
        return animeOrderedByRank;
    }

    public TimeSpentCalculated getTimeSpentCalculated() {
        return timeSpentCalculated;
    }

    public SingletonDao getSingletonDao() {
        return singletonDao;
    }

    public ArrayList<Count> getTypeCounts() {
        return typeCounts;
    }

    public ArrayList<Count> getStatusCounts() {
        return statusCounts;
    }

    public ArrayList<Count> getSeasonCounts() {
        return seasonCounts;
    }

    public ArrayList<Count> getGenreCounts() {
        return genreCounts;
    }

    public ArrayList<Count> getThemeCounts() {
        return themeCounts;
    }

    public ArrayList<Count> getRatingCounts() {
        return ratingCounts;
    }

    public ArrayList<Count> getSourceCounts() {
        return sourceCounts;
    }

    public ArrayList<Count> getStudioCounts() {
        return studioCounts;
    }

    public ArrayList<Count> getProducerCounts() {
        return producerCounts;
    }

    public ArrayList<Count> getLicensorCounts() {
        return licensorCounts;
    }

    public ArrayList<Count> getWatchStatusCounts() {
        return watchStatusCounts;
    }

    public ArrayList<Count> getLanguageCounts() {
        return languageCounts;
    }

    public ArrayList<Wildcard> getWildcards() {
        return wildcards;
    }

    public ConfigurePlots getConfigurePlots() {
        return configurePlots;
    }
}
