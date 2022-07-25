package org.coopereisnor.animeApplication.singleton;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StatisticsContainer {
    private final SingletonDao singletonDao = SingletonDao.getInstance();

    private final double MAX_THREADS = 1;

    private final ArrayList<Pair> filteredPairs;

    private String[] testStats;


    double currentPercent = 0.0;

    public StatisticsContainer(){
        System.out.println("Kagamine Len");

        filteredPairs = AnimeAggregate.getPairs(singletonDao.getAnimeDao().getCollection());

        Executor executor = Executors.newFixedThreadPool((int) MAX_THREADS, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
        executor.execute(() -> {
            testStats = new String[5];
            markComplete();
        });
    }

    // getters
    public String[] getTestStats() {
        return testStats;
    }

    public void markComplete(){
        currentPercent += 1.0/MAX_THREADS;

        if(testStats != null){
            singletonDao.setStatisticsContainer(this);
        }

        if(singletonDao.getCurrentController() != null){
            ProgressBar progressBar = singletonDao.getCurrentController().getUpdateProgressBar();
            Platform.runLater(() -> {
                if(progressBar != null){
                    progressBar.setProgress(.5 + (currentPercent/2.0));
                    progressBar.setVisible(testStats == null);
                }
            });
        }
    }
}
