package org.coopereisnor.statistics;

import org.coopereisnor.animeApplication.singleton.ListContainer;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Episode;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.manipulation.Tag;
import org.coopereisnor.manipulation.Wildcard;
import org.coopereisnor.utility.UtilityMethods;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

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

    public static ArrayList<Occurrence> getListOfOccurrences(ArrayList<Pair> occurrences){
        ArrayList<Occurrence> onlyOccurrences = new ArrayList<>();

        for(Pair pair : occurrences){
            onlyOccurrences.add(pair.getOccurrence());
        }

        return onlyOccurrences;
    }

    public static ArrayList<Count> getCounts(ListContainer listContainer, ArrayList<Pair> pairs, ArrayList<Anime> collection, String filter, String[] attributes){
        ArrayList<Count> counts = new ArrayList<>();
        for(String attribute : attributes){
            Tag tag = new Tag(filter, attribute, false);
            ArrayList<Pair> filteredPairs = listContainer.getPairs((ArrayList<Pair>) pairs.clone(), tag);
            ArrayList<Anime> filteredAnime = listContainer.getAnime((ArrayList<Anime>) collection.clone(), filteredPairs);
            counts.add(new Count(attribute, filteredAnime, filteredPairs));
        }
        counts.sort(Comparator.comparingInt((Count o) -> o.getAnime().size()).reversed());

        return counts;
    }

    // wildcard related
    public static Wildcard getNewestOccurrence(ArrayList<Pair> pairs){
        Pair pair = null;

        for(Pair currentPair : pairs){
            if(currentPair.getOccurrence().getAiredStartDate() != null){
                if(pair == null || currentPair.getOccurrence().getAiredStartDate().isAfter(pair.getOccurrence().getAiredStartDate())){
                    pair = currentPair;
                }
            }
        }

        if(pair == null){
            return new Wildcard("Newest Occurrence: ", "", null);
        }else{
            return new Wildcard("Newest Occurrence: ",
                    pair.getOccurrence().getName() +" (" +pair.getOccurrence().getPremieredSeason() +" " +pair.getOccurrence().getPremieredYear() +")",
                    pair);
        }
    }

    public static Wildcard getMostRecentlyStartedOccurrence(ArrayList<Pair> pairs){
        Pair pair = null;

        for(Pair currentPair : pairs){
            if(currentPair.getOccurrence().getStartedWatching() != null){
                if(pair == null || currentPair.getOccurrence().getStartedWatching().isAfter(pair.getOccurrence().getStartedWatching())){
                    pair = currentPair;
                }
            }
        }

        if(pair == null){
            return new Wildcard("Most Recently Started Occurrence: ", "", null);
        }else{
            return new Wildcard("Most Recently Started Occurrence: ",
                    pair.getOccurrence().getName() +" (" +UtilityMethods.getAsFormattedDate(pair.getOccurrence().getStartedWatching()) +")",
                    pair);
        }
    }
}
