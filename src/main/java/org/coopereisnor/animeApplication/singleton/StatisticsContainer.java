package org.coopereisnor.animeApplication.singleton;

import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StatisticsContainer {
    private final SingletonDao singletonDao = SingletonDao.getInstance();

    public static final double MAX_THREADS = 1;

    private final ArrayList<Pair> filteredPairs;

    // define things to store in container
    private String[] testStats;
    private String[] testStats2; // etc

    public StatisticsContainer(){
        System.out.println("Statistics Container");

        filteredPairs = AnimeAggregate.getPairs(singletonDao.getAnimeDao().getCollection());

        Executors.newSingleThreadExecutor().execute(() -> {
            ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool((int)MAX_THREADS);
            try{
                executor.execute(() -> {
                    // do something
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
    public String[] getTestStats() {
        return testStats;
    }
}
