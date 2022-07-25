package org.coopereisnor.animeDao;

import org.coopereisnor.statistics.OccurrenceStatistics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class Anime implements java.io.Serializable {
    private final long identifier;
    private final ArrayList<Occurrence> occurrences;
    private double score;
    private int rank;

    private String name;

    public Anime(String identifier){ // constructor
        this.identifier = Long.parseLong(identifier);
        this.occurrences = new ArrayList<>();
        this.score = -1;
        this.name = "New Anime";
        this.rank = Integer.MAX_VALUE;
    }

    public long getIdentifier(){
        return identifier;
    }

    // non final variables

    // getters
    public String getName() {
        return name;
    }

    public double getScore(){
        return score;
    }

    public int getRank(){
        return rank;
    }

    public Occurrence getFocusedOccurrence(){
        for(Occurrence occ : occurrences){
            if(occ.isFocused()) return occ;
        }
        return null;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setScore(double score){
        this.score = score;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    // occurrence related
    public void addOccurrence(Occurrence occurrence){
        occurrences.add(occurrence);
    }

    public void removeOccurrence(Occurrence occurrence){
        occurrences.remove(occurrence);
    }

    public ArrayList<Occurrence> getOccurrences(){
        return (ArrayList)occurrences.clone();
    }

    // sorting related
    public static Comparator<Anime> SORT_BY_ADDED = (animeOne, animeTwo) -> {
        Long identifierOne = animeOne.getIdentifier();
        Long identifierTwo = animeTwo.getIdentifier();
        return identifierOne.compareTo(identifierTwo);
    };
    public static Comparator<Anime> SORT_BY_SCORE = (animeOne, animeTwo) -> {
        Double scoreOne = animeOne.getScore();
        Double scoreTwo = animeTwo.getScore();
        return scoreOne.compareTo(scoreTwo);
    };
    public static Comparator<Anime> SORT_BY_NAME = (animeOne, animeTwo) -> {
        String nameOne = animeOne.getName().toUpperCase();
        String nameTwo = animeTwo.getName().toUpperCase();
        return nameOne.compareTo(nameTwo);
    };
    public static Comparator<Anime> SORT_BY_RANK = (animeOne, animeTwo) -> {
        Integer rankOne = animeOne.getRank();
        Integer rankTwo = animeTwo.getRank();
        return rankOne.compareTo(rankTwo);
    };
    public static Comparator<Anime> SORT_BY_STARTED_WATCHING_DATE = (animeOne, animeTwo) -> {
        LocalDate dateOne = OccurrenceStatistics.getEarliestStartedWatching(animeOne.getOccurrences());
        LocalDate dateTwo = OccurrenceStatistics.getEarliestStartedWatching(animeTwo.getOccurrences());
        return dateOne.compareTo(dateTwo);
    };
    public static Comparator<Anime> SORT_BY_FINISHED_WATCHING_DATE = (animeOne, animeTwo) -> {
        LocalDate dateOne = OccurrenceStatistics.getLatestFinishedWatching(animeOne.getOccurrences());
        LocalDate dateTwo = OccurrenceStatistics.getLatestFinishedWatching(animeTwo.getOccurrences());
        return dateOne.compareTo(dateTwo);
    };
    public static Comparator<Anime> SORT_BY_EPISODES_WATCHED = (animeOne, animeTwo) -> {
        Integer episodesOne = OccurrenceStatistics.getTotalEpisodesWatched(animeOne.getOccurrences());
        Integer episodesTwo = OccurrenceStatistics.getTotalEpisodesWatched(animeTwo.getOccurrences());
        return episodesOne.compareTo(episodesTwo);
    };
    public static Comparator<Anime> SORT_BY_EPISODES_TOTAL = (animeOne, animeTwo) -> {
        Integer episodesOne = OccurrenceStatistics.getTotalEpisodes(animeOne.getOccurrences());
        Integer episodesTwo = OccurrenceStatistics.getTotalEpisodes(animeTwo.getOccurrences());
        return episodesOne.compareTo(episodesTwo);
    };
    public static Comparator<Anime> SORT_BY_YEAR = (animeOne, animeTwo) -> {
        Integer yearOne = OccurrenceStatistics.getEarliestYear(animeOne.getOccurrences());
        Integer yearTwo = OccurrenceStatistics.getEarliestYear(animeTwo.getOccurrences());
        return yearOne.compareTo(yearTwo);
    };
    public static Comparator<Anime> SORT_BY_PROGRESS = (animeOne, animeTwo) -> {
        Double progressOne = ((double)OccurrenceStatistics.getTotalEpisodesWatched(animeOne.getOccurrences()))/((double)OccurrenceStatistics.getTotalEpisodes(animeOne.getOccurrences()));
        Double progressTwo = ((double)OccurrenceStatistics.getTotalEpisodesWatched(animeTwo.getOccurrences()))/((double)OccurrenceStatistics.getTotalEpisodes(animeTwo.getOccurrences()));
        return progressOne.compareTo(progressTwo);
    };
}
