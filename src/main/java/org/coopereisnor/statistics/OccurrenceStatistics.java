package org.coopereisnor.statistics;

import org.coopereisnor.animeDao.Episode;
import org.coopereisnor.animeDao.Occurrence;

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


    // utility methods
}
