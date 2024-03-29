package org.coopereisnor.animeApplication.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.manipulation.AnimeStatistics;
import org.coopereisnor.manipulation.OccurrenceStatistics;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class GeneralStatisticsController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final Anime anime = singletonDao.getCurrentAnime();

    @FXML
    private Label numberOfEpisodesWatchedLabel;
    @FXML
    private Label numberOfDaysLabel;
    @FXML
    private Label numberOfHoursLabel;
    @FXML
    private Label numberOfMinutesLabel;
    @FXML
    private Label spanOfDaysLabel;
    @FXML
    private Label percentOfTotalLabel;


    public void initialize() {
        int episodesWatched = OccurrenceStatistics.getTotalEpisodesWatched(anime.getOccurrences());
        int episodesTotal = OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences());

        int totalTimeInSeconds = AnimeStatistics.getTotalTimeInSeconds(singletonDao.getAnimeDao().getCollection()); // we don't want this to be the filtered list
        int animeTimeInSeconds = OccurrenceStatistics.getTotalTimeInSeconds(anime.getOccurrences());

        int minutes = animeTimeInSeconds/60;
        int hours = minutes/60;
        int displayedDays = hours/24;
        int displayedHours = hours - displayedDays*24;
        int displayedMinutes = minutes - displayedHours*60 - displayedDays*24*60;

        numberOfEpisodesWatchedLabel.setText(episodesWatched +" / " +episodesTotal  +" Episodes");

        numberOfDaysLabel.setText(displayedDays +" Days");
        numberOfHoursLabel.setText(displayedHours + " Hours");
        numberOfMinutesLabel.setText(displayedMinutes +" Minutes");

        spanOfDaysLabel.setText(getSpanOfDays());

        double ratio = ((double)animeTimeInSeconds)/((double)totalTimeInSeconds);
        double percent = ((double)((int)((ratio*100)*100.0)))/100.0;

        percentOfTotalLabel.setText(percent +"%");
    }

    private String getSpanOfDays(){
        LocalDate start = OccurrenceStatistics.getEarliestStartedWatching(anime.getOccurrences());
        LocalDate end = OccurrenceStatistics.getLatestEndDate(anime.getOccurrences());

        if(start == null || end == null){
            return "Unknown Days";
        }

        long daysBetween = DAYS.between(start, end);
        if (daysBetween < 0) return "Timey Wimey";
        else if (daysBetween == 0)  return " < 1 Day";
        else if (daysBetween == 1) return "1 Day";
        return daysBetween +" Days";
    }
}
