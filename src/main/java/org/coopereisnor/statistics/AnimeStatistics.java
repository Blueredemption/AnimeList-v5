package org.coopereisnor.statistics;

import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.manipulation.Wildcard;
import org.coopereisnor.utility.UtilityMethods;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

import static java.time.temporal.ChronoUnit.DAYS;

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

    // wildcard related
    public static Occurrence getOldestOccurrence(Anime anime){
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

    public static Occurrence getNewestOccurrence(Anime anime){
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
                if(pair == null || occurrence.getAiredStartDate().isAfter(pair.getOccurrence().getAiredStartDate())){
                    pair = new Pair(anime, occurrence);
                }
            }
        }

        if(pair == null){
            return new Wildcard("Newest Anime: ", "", null);
        }else{
            return new Wildcard("Newest Anime: ",
                    pair.getAnime().getName() +" (" +pair.getOccurrence().getPremieredSeason() +" " +pair.getOccurrence().getPremieredYear() +")",
                    pair);
        }
    }
    public static Wildcard getOldestAnime(ArrayList<Anime> collection) {
        Pair pair = null;

        for(Anime anime : collection){
            Occurrence occurrence = getOldestOccurrence(anime);
            if(occurrence != null && occurrence.getAiredStartDate() != null){
                if(pair == null || occurrence.getAiredStartDate().isBefore(pair.getOccurrence().getAiredStartDate())){
                    pair = new Pair(anime, occurrence);
                }
            }
        }

        if(pair == null){
            return new Wildcard("Oldest Anime: ", "", null);
        }else{
            return new Wildcard("Oldest Anime: ",
                    pair.getAnime().getName() +" (" +pair.getOccurrence().getPremieredSeason() +" " +pair.getOccurrence().getPremieredYear() +")",
                    pair);
        }
    }

    public static Occurrence getFirstStarted(Anime anime){
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
                if(pair == null || occurrence.getStartedWatching().isAfter(pair.getOccurrence().getStartedWatching())){
                    pair = new Pair(anime, occurrence);
                }
            }
        }

        if(pair == null){
            return new Wildcard("Most Recently Started Anime: ", "", null);
        }else{
            return new Wildcard("Most Recently Started Anime: ",
                    pair.getAnime().getName() +" (" +UtilityMethods.getAsFormattedDate(pair.getOccurrence().getStartedWatching()) +")",
                    pair);
        }
    }

    public static Wildcard getFirstStartedAnime(ArrayList<Anime> collection) {
        Pair pair = null;

        for(Anime anime : collection){
            Occurrence occurrence = getFirstStarted(anime);
            if(occurrence != null && occurrence.getStartedWatching() != null){
                if(pair == null || occurrence.getStartedWatching().isBefore(pair.getOccurrence().getStartedWatching())){
                    pair = new Pair(anime, occurrence);
                }
            }
        }

        if(pair == null){
            return new Wildcard("First (Recorded) Started Anime: ", "", null);
        }else{
            return new Wildcard("First (Recorded) Started Anime: ",
                    pair.getAnime().getName() +" (" + UtilityMethods.getAsFormattedDate(pair.getOccurrence().getStartedWatching()) +")",
                    pair);
        }
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

        if(keepAnime  == null){
            return new Wildcard("Longest Anime: ", "", null);
        }else{
            return new Wildcard("Longest Anime: ",
                    keepAnime.getName() +" (" + keepEpisodes +" Episodes)",
                    new Pair(keepAnime, null));
        }
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

        if(keepAnime == null){
            return new Wildcard("Shortest Anime: ", "", null);
        }else{
            return new Wildcard("Shortest Anime: ",
                    keepAnime.getName() +" (" + keepEpisodes +" Episodes)",
                    new Pair(keepAnime, null));
        }
    }

    public static Wildcard getLongestSpan(ArrayList<Anime> collection){

        Anime keepAnime = null;
        long keepDays = Integer.MIN_VALUE;
        for(Anime anime : collection){
            LocalDate start = OccurrenceStatistics.getEarliestStartDate(anime.getOccurrences());
            LocalDate end = OccurrenceStatistics.getLatestEndDate(anime.getOccurrences());

            if(start == null || end == null){
                continue;
            }

            long totalDays = DAYS.between(start, end);
            if(totalDays > keepDays){
                keepAnime = anime;
                keepDays = totalDays;
            }
        }

        if(keepAnime == null){
            return new Wildcard("Longest Span: ", "", null);
        }else{
            return new Wildcard("Longest Span: ",
                    keepAnime.getName() +" (" + keepDays +" Days)",
                    new Pair(keepAnime, null));
        }
    }

    public static Wildcard getShortestSpan(ArrayList<Anime> collection){

        Anime keepAnime = null;
        long keepDays = Integer.MAX_VALUE;
        for(Anime anime : collection){
            LocalDate start = OccurrenceStatistics.getEarliestStartDate(anime.getOccurrences());
            LocalDate end = OccurrenceStatistics.getLatestEndDate(anime.getOccurrences());

            if(start == null || end == null){
                continue;
            }

            long totalDays = DAYS.between(start, end);
            if(totalDays < keepDays){
                keepAnime = anime;
                keepDays = totalDays;
            }
        }

        if(keepAnime == null){
            return new Wildcard("Shortest Span: ", "", null);
        }else{
            return new Wildcard("Shortest Span: ",
                    keepAnime.getName() +" (" + keepDays +" Days)",
                    new Pair(keepAnime, null));
        }
    }
}
