package org.coopereisnor.manipulation;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.utility.UtilityMethods;

import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

// this class contains methods that return information given an ArrayList of Anime (could be made with less duplicate code)
public class AnimeStatistics {

    public static int getTotalTimeInSeconds(ArrayList<Anime> collection){
        int total = 0;
        for(Anime anime : collection){
            total += OccurrenceStatistics.getTotalTimeInSeconds(anime.getOccurrences());
        }
        return total;
    }

    // wildcard related
    private static Occurrence getOldestOccurrence(Anime anime){
        Occurrence toReturnOccurrence = null;
        for(Occurrence occurrence : anime.getOccurrences()){
            if(occurrence.getAiredStartDate() != null){
                if(toReturnOccurrence == null || occurrence.getAiredStartDate().isAfter(toReturnOccurrence.getAiredStartDate())){
                    toReturnOccurrence = occurrence;
                }
            }
        }
        return toReturnOccurrence;
    }

    private static Occurrence getNewestOccurrence(Anime anime){
        Occurrence toReturnOccurrence = null;
        for(Occurrence occurrence : anime.getOccurrences()){
            if(occurrence.getAiredStartDate() != null){
                if(toReturnOccurrence == null || occurrence.getAiredStartDate().isBefore(toReturnOccurrence.getAiredStartDate())){
                    toReturnOccurrence = occurrence;
                }
            }
        }
        return toReturnOccurrence;
    }

    public static Wildcard getNewestAnime(ArrayList<Anime> collection) {
        Pair pair = null;
        for(Anime anime : collection){
            Occurrence occurrence = getNewestOccurrence(anime);
            if(occurrence != null && occurrence.getAiredStartDate() != null){
                if(pair == null || occurrence.getAiredStartDate().isAfter(pair.occurrence().getAiredStartDate())){
                    pair = new Pair(anime, occurrence);
                }
            }
        }
        String resultString = pair != null ? pair.anime().getName() +" (" +pair.occurrence().getPremieredSeason() +" " +pair.occurrence().getPremieredYear() +")" : "";
        return new Wildcard("Newest Anime: ", resultString, pair);
    }
    public static Wildcard getOldestAnime(ArrayList<Anime> collection) {
        Pair pair = null;
        for(Anime anime : collection){
            Occurrence occurrence = getOldestOccurrence(anime);
            if(occurrence != null && occurrence.getAiredStartDate() != null){
                if(pair == null || occurrence.getAiredStartDate().isBefore(pair.occurrence().getAiredStartDate())){
                    pair = new Pair(anime, occurrence);
                }
            }
        }
        String resultString = pair != null ? pair.anime().getName() +" (" +pair.occurrence().getPremieredSeason() +" " +pair.occurrence().getPremieredYear() +")" : "";
        return new Wildcard("Oldest Anime: ", resultString, pair);
    }

    private static Occurrence getFirstStarted(Anime anime){
        Occurrence toReturnOccurrence = null;
        for(Occurrence occurrence : anime.getOccurrences()){
            if(occurrence.getStartedWatching() != null){
                if(toReturnOccurrence == null || occurrence.getStartedWatching().isBefore(toReturnOccurrence.getStartedWatching())){
                    toReturnOccurrence = occurrence;
                }
            }
        }
        return toReturnOccurrence;
    }

    public static Wildcard getMostRecentlyStartedAnime(ArrayList<Anime> collection) {
        Pair pair = null;
        for(Anime anime : collection){
            Occurrence occurrence = getFirstStarted(anime);
            if(occurrence != null && occurrence.getStartedWatching() != null){
                if(pair == null || occurrence.getStartedWatching().isAfter(pair.occurrence().getStartedWatching())){
                    pair = new Pair(anime, occurrence);
                }
            }
        }
        String resultString = pair != null ? pair.anime().getName() +" (" +UtilityMethods.getAsFormattedDate(pair.occurrence().getStartedWatching()) +")" : "";
        return new Wildcard("Most Recently Started Anime: ", resultString, pair);
    }

    public static Wildcard getFirstStartedAnime(ArrayList<Anime> collection) {
        Pair pair = null;
        for(Anime anime : collection){
            Occurrence occurrence = getFirstStarted(anime);
            if(occurrence != null && occurrence.getStartedWatching() != null){
                if(pair == null || occurrence.getStartedWatching().isBefore(pair.occurrence().getStartedWatching())){
                    pair = new Pair(anime, occurrence);
                }
            }
        }
        String resultString = pair != null ? pair.anime().getName() +" (" + UtilityMethods.getAsFormattedDate(pair.occurrence().getStartedWatching()) +")" : "";
        return new Wildcard("First (Recorded) Started Anime: ", resultString, pair);
    }

    public static Wildcard getLongestAnime(ArrayList<Anime> collection){
        Anime keepAnime = null;
        int keepEpisodes = Integer.MIN_VALUE;
        for(Anime anime : collection){
            int totalEpisodes = OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences());
            if(totalEpisodes > keepEpisodes){
               keepAnime = anime;
               keepEpisodes = totalEpisodes;
            }
        }
        String resultString = keepAnime != null ? keepAnime.getName() +" (" + keepEpisodes +" Episodes)" : "";
        return new Wildcard("Longest Anime: ", resultString, keepAnime != null ? new Pair(keepAnime, null) : null);
    }

    public static Wildcard getShortestAnime(ArrayList<Anime> collection){
        Anime keepAnime = null;
        int keepEpisodes = Integer.MAX_VALUE;
        for(Anime anime : collection){
            int totalEpisodes = OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences());
            if(totalEpisodes < keepEpisodes){
                keepAnime = anime;
                keepEpisodes = totalEpisodes;
            }
        }
        String resultString = keepAnime != null ? keepAnime.getName() +" (" + keepEpisodes +" Episodes)" : "";
        return new Wildcard("Shortest Anime: ", resultString, keepAnime != null ? new Pair(keepAnime, null) : null);
    }

    public static Wildcard getLongestSpan(ArrayList<Anime> collection){
        Anime keepAnime = null;
        long keepDays = Integer.MIN_VALUE;
        for(Anime anime : collection){
            LocalDate start = OccurrenceStatistics.getEarliestStartedWatching(anime.getOccurrences());
            LocalDate end = OccurrenceStatistics.getLatestEndDate(anime.getOccurrences());
            if(start == null || end == null) continue;
            long totalDays = DAYS.between(start, end);
            if(totalDays > keepDays){
                keepAnime = anime;
                keepDays = totalDays;
            }
        }
        String resultString = keepAnime != null ? keepAnime.getName() +" (" + keepDays +" Days)" : "";
        return new Wildcard("Longest Span: ", resultString, keepAnime != null ? new Pair(keepAnime, null) : null);
    }

    public static Wildcard getShortestSpan(ArrayList<Anime> collection){
        Anime keepAnime = null;
        long keepDays = Integer.MAX_VALUE;
        for(Anime anime : collection){
            LocalDate start = OccurrenceStatistics.getEarliestStartedWatching(anime.getOccurrences());
            LocalDate end = OccurrenceStatistics.getLatestEndDate(anime.getOccurrences());
            if(start == null || start == LocalDate.MAX || end == null) continue;
            long totalDays = DAYS.between(start, end);
            if(totalDays < keepDays){
                keepAnime = anime;
                keepDays = totalDays;
            }
        }
        String resultString = keepAnime != null ? keepAnime.getName() +" (" + keepDays +" Days)" : "";
        return new Wildcard("Shortest Span: ", resultString, keepAnime != null ? new Pair(keepAnime, null) : null);
    }
}
