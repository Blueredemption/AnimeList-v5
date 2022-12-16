package org.coopereisnor.animeApplication.singleton;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.manipulation.Wildcard;
import org.coopereisnor.statistics.AnimeStatistics;
import org.coopereisnor.statistics.Count;
import org.coopereisnor.statistics.OccurrenceStatistics;
import org.coopereisnor.statistics.TimeSpentCalculated;
import org.coopereisnor.utility.Log;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StatisticsContainer {
    private final SingletonDao singletonDao = SingletonDao.getInstance();

    public static final double MAX_THREADS = 14;

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

    public StatisticsContainer(){
        Log.logger.debug("Statistics Container");

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
                    typeCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Type", singletonDao.getFilterContainer().getTypes());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    statusCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Status", singletonDao.getFilterContainer().getStatuses());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    seasonCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Season", singletonDao.getFilterContainer().getSeasons());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    genreCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Genre", singletonDao.getFilterContainer().getGenres());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    themeCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Theme", singletonDao.getFilterContainer().getThemes());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    ratingCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Rating", singletonDao.getFilterContainer().getRatings());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    sourceCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Source", singletonDao.getFilterContainer().getSources());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    studioCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Studio", singletonDao.getFilterContainer().getStudios());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    producerCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Producer", singletonDao.getFilterContainer().getProducers());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    licensorCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Licensor", singletonDao.getFilterContainer().getLicensors());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    watchStatusCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Watch Status", singletonDao.getFilterContainer().getWatchStatuses());
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    languageCounts = OccurrenceStatistics.getCounts(singletonDao.getListContainer(), pairs, collection, "Language", singletonDao.getFilterContainer().getLanguages());
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

                executor.shutdown();
                if(!executor.awaitTermination(60, TimeUnit.SECONDS)){
                    Log.logger.error("Executor has timed out before all processes are finished");
                }
            }catch(InterruptedException ex){
                Log.logger.error("InterruptedException", ex);
            }

            isComplete();
        });
    }

    public void isComplete(){
        Log.logger.debug("Statistics Container Complete");
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
}
