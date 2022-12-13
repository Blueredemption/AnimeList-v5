package org.coopereisnor.animeApplication.singleton;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.statistics.OccurrenceStatistics;
import org.coopereisnor.statistics.TimeSpentCalculated;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StatisticsContainer {
    private final SingletonDao singletonDao = SingletonDao.getInstance();

    public static final double MAX_THREADS = 2;

    private final ArrayList<Anime> anime;
    private final ArrayList<Pair> occurrences;

    private ArrayList<Anime> animeOrderedByRank;
    private TimeSpentCalculated timeSpentCalculated;

    public StatisticsContainer(){
        System.out.println("Statistics Container");

        anime = (ArrayList<Anime>)singletonDao.getListContainer().getFilteredAndSortedAnime().clone(); // cloned because the order is manipulated
        occurrences = (ArrayList<Pair>)singletonDao.getListContainer().getFilteredAndSortedOccurrences().clone();

        Executors.newSingleThreadExecutor().execute(() -> {
            ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool((int)MAX_THREADS);
            try{
                executor.execute(() -> {
                    animeOrderedByRank = singletonDao.getListContainer().sortAnime(anime, "Rank");
                    singletonDao.updateProgressBar(getClass(), executor.getActiveCount());
                });
                executor.execute(() -> {
                    timeSpentCalculated = new TimeSpentCalculated(anime, OccurrenceStatistics.getListOfOccurrences(occurrences));
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
        System.out.println("Statistics Container Complete");
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
}
