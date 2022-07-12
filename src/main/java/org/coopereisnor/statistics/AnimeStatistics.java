package org.coopereisnor.statistics;

import org.coopereisnor.animeDao.Anime;

import java.util.ArrayList;

// this class contains methods that return information given an ArrayList of Anime
public class AnimeStatistics {
    // public methods

    public static int getTotalEpisodesWatched(ArrayList<Anime> collection){
        int total = 0;
        for(Anime anime : collection){
            total += OccurrenceStatistics.getTotalEpisodesWatched(anime.getOccurrences());
        }
        return total;
    }

    public static int getTotalTimeInSeconds(ArrayList<Anime> collection){
        int total = 0;
        for(Anime anime : collection){
            total += OccurrenceStatistics.getTotalTimeInSeconds(anime.getOccurrences());
        }
        return total;
    }

    // utility methods
}
