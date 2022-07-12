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

    public static int getTotalTimeInSeconds(ArrayList<Occurrence> occurrences){
        int total = 0;
        for(Occurrence occurrence : occurrences){
            for(Episode episode : occurrence.getEpisodesWatched()){
                total += occurrence.getDuration();
            }
        }
        return total;
    }



    // utility methods
}
