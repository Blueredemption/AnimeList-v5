package org.coopereisnor.manipulation;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.statistics.OccurrenceStatistics;

import java.time.LocalDate;
import java.util.Comparator;

public class Pair {
    private Anime anime;
    private Occurrence occurrence;

    public Pair(Anime anime, Occurrence occurrence){
        this.anime = anime;
        this.occurrence = occurrence;
    }

    public Anime getAnime() {
        return anime;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    // sorting related
    public static Comparator<Pair> SORT_BY_ADDED = (pairOne, pairTwo) -> {
        Long identifierOne = pairOne.getOccurrence().getIdentifier();
        Long identifierTwo = pairTwo.getOccurrence().getIdentifier();
        return identifierOne.compareTo(identifierTwo);
    };
    public static Comparator<Pair> SORT_BY_SCORE = (pairOne, pairTwo) -> {
        Double scoreOne = pairOne.getOccurrence().getScore();
        Double scoreTwo = pairTwo.getOccurrence().getScore();
        return scoreOne.compareTo(scoreTwo);
    };
    public static Comparator<Pair> SORT_BY_NAME = (pairOne, pairTwo) -> {
        String nameOne = pairOne.getOccurrence().getName().toUpperCase();
        String nameTwo = pairTwo.getOccurrence().getName().toUpperCase();
        return nameOne.compareTo(nameTwo);
    };
    public static Comparator<Pair> SORT_BY_RANK = (pairOne, pairTwo) -> {
        Integer rankOne = pairOne.getAnime().getRank();
        Integer rankTwo = pairTwo.getAnime().getRank();
        return rankOne.compareTo(rankTwo);
    };
    public static Comparator<Pair> SORT_BY_STARTED_WATCHING_DATE = (pairOne, pairTwo) -> {
        LocalDate dateOne = pairOne.getOccurrence().getStartedWatching();
        LocalDate dateTwo = pairTwo.getOccurrence().getStartedWatching();

        if(dateOne == null) dateOne = LocalDate.MIN;
        if(dateTwo == null) dateTwo = LocalDate.MIN;

        return dateOne.compareTo(dateTwo);
    };
    public static Comparator<Pair> SORT_BY_FINISHED_WATCHING_DATE = (pairOne, pairTwo) -> {
        LocalDate dateOne = pairOne.getOccurrence().getFinishedWatching();
        LocalDate dateTwo = pairTwo.getOccurrence().getFinishedWatching();

        if(dateOne == null) dateOne = LocalDate.MIN;
        if(dateTwo == null) dateTwo = LocalDate.MIN;

        return dateOne.compareTo(dateTwo);
    };
    public static Comparator<Pair> SORT_BY_EPISODES_WATCHED = (pairOne, pairTwo) -> {
        Integer episodesOne = pairOne.getOccurrence().getEpisodesWatched().length;
        Integer episodesTwo = pairTwo.getOccurrence().getEpisodesWatched().length;
        return episodesOne.compareTo(episodesTwo);
    };
    public static Comparator<Pair> SORT_BY_EPISODES_TOTAL = (pairOne, pairTwo) -> {
        Integer episodesOne = pairOne.getOccurrence().getEpisodes();
        Integer episodesTwo = pairTwo.getOccurrence().getEpisodes();
        return episodesOne.compareTo(episodesTwo);
    };
    public static Comparator<Pair> SORT_BY_YEAR = (pairOne, pairTwo) -> {
        Integer yearOne = pairOne.getOccurrence().getPremieredYear();
        Integer yearTwo = pairTwo.getOccurrence().getPremieredYear();
        return yearOne.compareTo(yearTwo);
    };
    public static Comparator<Pair> SORT_BY_PROGRESS = (pairOne, pairTwo) -> {
        Double progressOne = ((double)pairOne.getOccurrence().getEpisodesWatched().length)/((double)pairOne.getOccurrence().getEpisodes());
        Double progressTwo = ((double)pairTwo.getOccurrence().getEpisodesWatched().length)/((double)pairTwo.getOccurrence().getEpisodes());
        return progressOne.compareTo(progressTwo);
    };
}
