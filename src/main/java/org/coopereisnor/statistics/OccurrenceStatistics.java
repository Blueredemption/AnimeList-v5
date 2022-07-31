package org.coopereisnor.statistics;

import javafx.util.converter.LocalDateTimeStringConverter;
import org.coopereisnor.animeDao.Episode;
import org.coopereisnor.animeDao.Occurrence;

import java.time.LocalDate;
import java.util.ArrayList;

// this class contains methods that return information given an ArrayList of Occurrences
public class OccurrenceStatistics {
    // public methods

    public static int getTotalEpisodesWatched(ArrayList<Occurrence> occurrences){
        int total = 0;
        for(Occurrence occurrence : occurrences){
            for(Episode episode : occurrence.getEpisodesWatched()){
                total++;
            }
        }
        return total;
    }

    public static int getTotalEpisodes(ArrayList<Occurrence> occurrences) {
        int total = 0;
        for(Occurrence occurrence : occurrences){
            total += occurrence.getEpisodes();
        }
        return total;
    }

    public static int getTotalTimeInSeconds(ArrayList<Occurrence> occurrences){
        int total = 0;
        for(Occurrence occurrence : occurrences){
            for(Episode episode : occurrence.getEpisodesWatched()){
                total += occurrence.getDuration();
            }
        }
        return total;
    }

    public static int getEarliestYear(ArrayList<Occurrence> occurrences) {
        int earliestYear = Integer.MAX_VALUE;
        for(Occurrence occurrence : occurrences){
            if (earliestYear > occurrence.getPremieredYear() && occurrence.getPremieredYear() > 0) earliestYear = occurrence.getPremieredYear();
        }
        return earliestYear;
    }

    public static LocalDate getEarliestStartedWatching(ArrayList<Occurrence> occurrences){
        LocalDate date = LocalDate.MAX;
        for(Occurrence occurrence : occurrences){
            if (occurrence.getStartedWatching() == null) continue;
            if (occurrence.getStartedWatching().isBefore(date)) date = occurrence.getStartedWatching();
        }
        return date;
    }

    public static LocalDate getLatestFinishedWatching(ArrayList<Occurrence> occurrences){
        LocalDate date = LocalDate.MIN;
        for(Occurrence occurrence : occurrences){
            if (occurrence.getFinishedWatching() == null) continue;
            if (occurrence.getFinishedWatching().isAfter(date)) date = occurrence.getFinishedWatching();
        }
        return date;
    }


    public static LocalDate getLatestEndDate(ArrayList<Occurrence> occurrences) {
        LocalDate latestDate = LocalDate.MIN;
        for(Occurrence occurrence : occurrences){
            if(occurrence.getFinishedWatching() == null) return null;
            else if(occurrence.getFinishedWatching().isAfter(latestDate)){
                latestDate = occurrence.getFinishedWatching();
            }
        }
        return latestDate;
    }

    public static LocalDate getEarliestStartDate(ArrayList<Occurrence> occurrences) {
        LocalDate earliestDate = LocalDate.MAX;
        for(Occurrence occurrence : occurrences){
            if(occurrence.getStartedWatching() == null) return null;
            else if(occurrence.getStartedWatching().isBefore(earliestDate)){
                earliestDate = occurrence.getStartedWatching();
            }
        }
        return earliestDate;
    }
}
