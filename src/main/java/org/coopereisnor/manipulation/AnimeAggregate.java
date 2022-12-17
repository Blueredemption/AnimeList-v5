package org.coopereisnor.manipulation;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.utility.UtilityMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

// this class is for methods that collect various things (like all existing genres, for example)
public class AnimeAggregate {

    public static ArrayList<Pair> getPairs(ArrayList<Anime> collection){
        ArrayList<Pair> pairs = new ArrayList<>();
        for(Anime anime : collection){
            for(Occurrence occurrence : anime.getOccurrences()){
                pairs.add(new Pair(anime, occurrence));
            }
        }
        return pairs;
    }

    public static String[] getTypes(ArrayList<Pair> allPairs){
        LinkedHashSet<String> types = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            types.add(pair.getOccurrence().getType());
        }

        return UtilityMethods.convertToStringArray(types);
    }

    public static String[] getStatuses(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> statuses = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            statuses.add(pair.getOccurrence().getStatus());
        }

        return UtilityMethods.convertToStringArray(statuses);
    }

    public static String[] getSeasons(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> seasons = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            seasons.add(pair.getOccurrence().getPremieredSeason());
        }

        return UtilityMethods.convertToStringArray(seasons);
    }

    public static String[] getGenres(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> genres = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            genres.addAll(Arrays.asList(pair.getOccurrence().getGenres()));
        }

        return UtilityMethods.convertToStringArray(genres);
    }

    public static String[] getThemes(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> themes = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            themes.addAll(Arrays.asList(pair.getOccurrence().getThemes()));
        }

        return UtilityMethods.convertToStringArray(themes);
    }

    public static String[] getRatings(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> ratings = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            ratings.add(pair.getOccurrence().getRating());
        }

        return UtilityMethods.convertToStringArray(ratings);
    }

    public static String[] getSources(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> sources = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            sources.add(pair.getOccurrence().getSource());
        }

        return UtilityMethods.convertToStringArray(sources);
    }


    public static String[] getStudios(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> studios = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            studios.addAll(Arrays.asList(pair.getOccurrence().getStudios()));
        }

        return UtilityMethods.convertToStringArray(studios);
    }

    public static String[] getProducers(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> producers = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            producers.addAll(Arrays.asList(pair.getOccurrence().getProducers()));
        }

        return UtilityMethods.convertToStringArray(producers);
    }

    public static String[] getLicensors(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> licensors = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            licensors.addAll(Arrays.asList(pair.getOccurrence().getLicensors()));
        }

        return UtilityMethods.convertToStringArray(licensors);
    }

    public static String[] getWatchStatuses(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> watchStatuses = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            watchStatuses.add(pair.getOccurrence().getWatchStatus());
        }

        return UtilityMethods.convertToStringArray(watchStatuses);
    }

    public static String[] getLanguages(ArrayList<Pair> allPairs) {
        LinkedHashSet<String> languages = new LinkedHashSet<>();

        for(Pair pair : allPairs){
            languages.addAll(Arrays.asList(pair.getOccurrence().getLanguages()));
        }

        return UtilityMethods.convertToStringArray(languages);
    }
}
