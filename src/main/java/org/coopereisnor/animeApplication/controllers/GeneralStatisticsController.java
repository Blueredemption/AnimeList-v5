package org.coopereisnor.animeApplication.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.statistics.AnimeStatistics;
import org.coopereisnor.statistics.OccurrenceStatistics;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;

public class GeneralStatisticsController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();
    private final Occurrence occurrence = singletonDao.getCurrentOccurrence();

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
        // todo grab totalTimeInSeconds from StatisticsContainer when that is completed
        int totalTimeInSeconds = AnimeStatistics.getTotalTimeInSeconds(singletonDao.getListContainer().getFilteredAndSortedAnime());
        int animeTimeInSeconds = OccurrenceStatistics.getTotalTimeInSeconds(anime.getOccurrences());

        // todo make sure this is correctly represented when "real" data exists
        int minutes = animeTimeInSeconds/60;
        int hours = minutes/60;
        int displayedDays = hours/24;
        int displayedHours = hours - displayedDays*24;
        int displayedMinutes = minutes - displayedHours*60 - displayedDays*24*60;

        numberOfDaysLabel.setText(displayedDays +" Days");
        numberOfHoursLabel.setText(displayedHours + " Hours");
        numberOfMinutesLabel.setText(displayedMinutes +" Minutes");

        spanOfDaysLabel.setText(getSpanOfDays());

        double ratio = ((double)animeTimeInSeconds)/((double)totalTimeInSeconds);
        double percent = ((double)((int)((ratio*100)*100.0)))/100.0;

        percentOfTotalLabel.setText(percent +"%");
    }

    private String getSpanOfDays(){
        LocalDate start = OccurrenceStatistics.getEarliestStartDate(anime.getOccurrences());
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
