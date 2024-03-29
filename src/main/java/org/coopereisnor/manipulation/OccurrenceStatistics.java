package org.coopereisnor.manipulation;

import org.coopereisnor.animeApplication.singleton.ListContainer;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Episode;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.utility.UtilityMethods;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

// this class contains methods that return information given an ArrayList of Occurrences (could be made with less duplicate code)
public class OccurrenceStatistics {

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

    public static LocalDate getEarliestAiredDate(ArrayList<Occurrence> occurrences) {
        LocalDate earliestDate = LocalDate.MAX;
        for(Occurrence occurrence : occurrences){
            if(occurrence.getAiredStartDate() == null) return null;
            else if(occurrence.getAiredStartDate().isBefore(earliestDate)){
                earliestDate = occurrence.getAiredStartDate();
            }
        }
        return earliestDate;
    }

    public static ArrayList<Occurrence> getListOfOccurrences(ArrayList<Pair> pairs){
        ArrayList<Occurrence> onlyOccurrences = new ArrayList<>();

        for(Pair pair : pairs){
            onlyOccurrences.add(pair.occurrence());
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
        counts.sort(Comparator.comparingInt((Count o) -> o.collection().size()).reversed());
        return counts;
    }

    // wildcard related
    public static Wildcard getNewestOccurrence(ArrayList<Pair> pairs){
        Pair pair = null;
        for(Pair currentPair : pairs){
            if(currentPair.occurrence().getAiredStartDate() != null){
                if(pair == null || currentPair.occurrence().getAiredStartDate().isAfter(pair.occurrence().getAiredStartDate())){
                    pair = currentPair;
                }
            }
        }
        String resultString = pair != null ? pair.occurrence().getName() +" (" +pair.occurrence().getPremieredSeason() +" " +pair.occurrence().getPremieredYear() +")" : "";
        return new Wildcard("Newest Occurrence: ", resultString, pair);
    }

    public static Wildcard getMostRecentlyStartedOccurrence(ArrayList<Pair> pairs){
        Pair pair = null;
        for(Pair currentPair : pairs){
            if(currentPair.occurrence().getStartedWatching() != null){
                if(pair == null || currentPair.occurrence().getStartedWatching().isAfter(pair.occurrence().getStartedWatching())){
                    pair = currentPair;
                }
            }
        }
        String resultString = pair != null ? pair.occurrence().getName() +" (" +UtilityMethods.getAsFormattedDate(pair.occurrence().getStartedWatching()) +")" : "";
        return new Wildcard("Most Recently Started Occurrence: ", resultString, pair);
    }
}
