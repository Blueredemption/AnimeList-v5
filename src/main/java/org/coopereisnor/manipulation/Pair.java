package org.coopereisnor.manipulation;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;

import java.time.LocalDate;
import java.util.Comparator;

public record Pair(Anime anime, Occurrence occurrence) {

    // sorting related
    public static Comparator<Pair> SORT_BY_ADDED = (pairOne, pairTwo) -> {
        Long identifierOne = pairOne.occurrence().getIdentifier();
        Long identifierTwo = pairTwo.occurrence().getIdentifier();
        return identifierOne.compareTo(identifierTwo);
    };
    public static Comparator<Pair> SORT_BY_SCORE = (pairOne, pairTwo) -> {
        Double scoreOne = pairOne.occurrence().getScore();
        Double scoreTwo = pairTwo.occurrence().getScore();
        return scoreOne.compareTo(scoreTwo);
    };
    public static Comparator<Pair> SORT_BY_NAME = (pairOne, pairTwo) -> {
        String nameOne = pairOne.occurrence().getName().toUpperCase();
        String nameTwo = pairTwo.occurrence().getName().toUpperCase();
        return nameOne.compareTo(nameTwo);
    };
    public static Comparator<Pair> SORT_BY_RANK = (pairOne, pairTwo) -> {
        Integer rankOne = pairOne.anime().getRank();
        Integer rankTwo = pairTwo.anime().getRank();
        return rankOne.compareTo(rankTwo);
    };
    public static Comparator<Pair> SORT_BY_STARTED_WATCHING_DATE = (pairOne, pairTwo) -> {
        LocalDate dateOne = pairOne.occurrence().getStartedWatching();
        LocalDate dateTwo = pairTwo.occurrence().getStartedWatching();

        if (dateOne == null) dateOne = LocalDate.MIN;
        if (dateTwo == null) dateTwo = LocalDate.MIN;

        return dateOne.compareTo(dateTwo);
    };
    public static Comparator<Pair> SORT_BY_FINISHED_WATCHING_DATE = (pairOne, pairTwo) -> {
        LocalDate dateOne = pairOne.occurrence().getFinishedWatching();
        LocalDate dateTwo = pairTwo.occurrence().getFinishedWatching();

        if (dateOne == null) dateOne = LocalDate.MIN;
        if (dateTwo == null) dateTwo = LocalDate.MIN;

        return dateOne.compareTo(dateTwo);
    };
    public static Comparator<Pair> SORT_BY_EPISODES_WATCHED = (pairOne, pairTwo) -> {
        Integer episodesOne = pairOne.occurrence().getEpisodesWatched().length;
        Integer episodesTwo = pairTwo.occurrence().getEpisodesWatched().length;
        return episodesOne.compareTo(episodesTwo);
    };
    public static Comparator<Pair> SORT_BY_EPISODES_TOTAL = (pairOne, pairTwo) -> {
        Integer episodesOne = pairOne.occurrence().getEpisodes();
        Integer episodesTwo = pairTwo.occurrence().getEpisodes();
        return episodesOne.compareTo(episodesTwo);
    };
    public static Comparator<Pair> SORT_BY_RELEASE_DATE = (pairOne, pairTwo) -> {
        LocalDate dateOne = pairOne.occurrence().getAiredStartDate();
        LocalDate dateTwo = pairTwo.occurrence().getAiredStartDate();

        if (dateOne == null) dateOne = LocalDate.MIN;
        if (dateTwo == null) dateTwo = LocalDate.MIN;

        return dateOne.compareTo(dateTwo);
    };
    public static Comparator<Pair> SORT_BY_PROGRESS = (pairOne, pairTwo) -> {
        Double progressOne = ((double) pairOne.occurrence().getEpisodesWatched().length) / ((double) pairOne.occurrence().getEpisodes());
        Double progressTwo = ((double) pairTwo.occurrence().getEpisodesWatched().length) / ((double) pairTwo.occurrence().getEpisodes());
        return progressOne.compareTo(progressTwo);
    };
}
