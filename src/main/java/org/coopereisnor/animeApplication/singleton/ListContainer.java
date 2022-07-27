package org.coopereisnor.animeApplication.singleton;

import javafx.collections.FXCollections;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.manipulation.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ListContainer {
    private final AnimeDao animeDao;

    private String sortBy = "Added";
    private String order = "Descending";
    private String search = "";
    private ArrayList<Tag> tags = new ArrayList<>(); // starts as empty

    private ArrayList<Anime> filteredAndSortedAnime = null;
    private ArrayList<Pair> filteredAndSortedOccurrences = null;

    public ListContainer(AnimeDao animeDao){
        this.animeDao = animeDao;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
        update();
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
        silentUpdate();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
        update();
    }

    public ArrayList<Anime> getFilteredAndSortedAnime() {
        return filteredAndSortedAnime;
    }

    public ArrayList<Pair> getFilteredAndSortedOccurrences() {
        return filteredAndSortedOccurrences;
    }



    // update methods
    public void silentUpdate(){
        System.out.println("Hatsune Miku");
        // it is important that occurrences are filtered first because anime are filtered using the occurrences list
        filteredAndSortedOccurrences = sortPairs(filterPairs(AnimeAggregate.getPairs(animeDao.getCollection())));
        filteredAndSortedAnime = sortAnime(filterAnime(animeDao.getCollection()));

        if(order.equals("Descending")){
            Collections.reverse(filteredAndSortedAnime);
            Collections.reverse(filteredAndSortedOccurrences);
        }
    }

    public void update(){
        silentUpdate();
        new StatisticsContainer();
    }

    public ArrayList<Anime> filterAnime(ArrayList<Anime> collection){
        ArrayList<Anime> toRemove = new ArrayList<>();
        for(Anime anime : collection){
            if(!containsAtLeastOneOccurrence(anime)) toRemove.add(anime);
        }
        collection.removeAll(toRemove);
        return collection;
    }

    public ArrayList<Anime> sortAnime(ArrayList<Anime> anime){
        return sortAnime(anime, sortBy);
    }

    public ArrayList<Anime> sortAnime(ArrayList<Anime> anime, String sort){
        switch (sort) {
            case "Added" -> anime.sort(Anime.SORT_BY_ADDED);
            case "Score" -> anime.sort(Anime.SORT_BY_SCORE);
            case "Name" -> anime.sort(Anime.SORT_BY_NAME);
            case "Rank" -> anime.sort(Anime.SORT_BY_RANK);
            case "Started" -> anime.sort(Anime.SORT_BY_STARTED_WATCHING_DATE);
            case "Finished" -> anime.sort(Anime.SORT_BY_FINISHED_WATCHING_DATE);
            case "Eps. Watched" -> anime.sort(Anime.SORT_BY_EPISODES_WATCHED);
            case "Eps. Total" -> anime.sort(Anime.SORT_BY_EPISODES_TOTAL);
            case "Year" -> anime.sort(Anime.SORT_BY_YEAR);
            case "Progress" -> anime.sort(Anime.SORT_BY_PROGRESS);
            default -> System.out.println("Reached Default in ListContainer sortAnime");
        }
        return anime;
    }

    public ArrayList<Pair> filterPairs(ArrayList<Pair> pairs){
        ArrayList<Pair> toRemove = new ArrayList<>();
        for(Tag tag : tags){
            for(Pair pair : pairs){
                if(getFilterApply(pair, tag)) toRemove.add(pair);
            }
        }

        pairs.removeAll(toRemove);
        return pairs;
    }

    public ArrayList<Pair> sortPairs(ArrayList<Pair> pairs){
        switch (sortBy) {
            case "Added" -> pairs.sort(Pair.SORT_BY_ADDED);
            case "Score" -> pairs.sort(Pair.SORT_BY_SCORE);
            case "Name" -> pairs.sort(Pair.SORT_BY_NAME);
            case "Rank" -> pairs.sort(Pair.SORT_BY_RANK);
            case "Started" -> pairs.sort(Pair.SORT_BY_STARTED_WATCHING_DATE);
            case "Finished" -> pairs.sort(Pair.SORT_BY_FINISHED_WATCHING_DATE);
            case "Eps. Watched" -> pairs.sort(Pair.SORT_BY_EPISODES_WATCHED);
            case "Eps. Total" -> pairs.sort(Pair.SORT_BY_EPISODES_TOTAL);
            case "Year" -> pairs.sort(Pair.SORT_BY_YEAR);
            case "Progress" -> pairs.sort(Pair.SORT_BY_PROGRESS);
            default -> System.out.println("Reached Default in ListContainer sortPairs");
        }
        return pairs;
    }

    public boolean getFilterApply(Pair pair, Tag tag){
        boolean returnValue;
        switch(tag.getFilter()){
            case "Type" -> returnValue = pair.getOccurrence().getType().equals(tag.getAttribute());
            case "Status" -> returnValue = pair.getOccurrence().getStatus().equals(tag.getAttribute());
            case "Season" -> returnValue = pair.getOccurrence().getPremieredSeason().equals(tag.getAttribute());
            case "Genres" -> returnValue = Arrays.asList(pair.getOccurrence().getGenres()).contains(tag.getAttribute());
            case "Theme" -> returnValue = Arrays.asList(pair.getOccurrence().getThemes()).contains(tag.getAttribute());
            case "Rating" -> returnValue = pair.getOccurrence().getRating().equals(tag.getAttribute());
            case "Source" -> returnValue = pair.getOccurrence().getSource().equals(tag.getAttribute());
            case "Studio" -> returnValue = Arrays.asList(pair.getOccurrence().getStudios()).contains(tag.getAttribute());
            case "Producer" -> returnValue = Arrays.asList(pair.getOccurrence().getProducers()).contains(tag.getAttribute());
            case "Licensor" -> returnValue = Arrays.asList(pair.getOccurrence().getLicensors()).contains(tag.getAttribute());
            case "Watch Status" -> returnValue = pair.getOccurrence().getWatchStatus().equals(tag.getAttribute());
            case "Language" ->  returnValue = pair.getOccurrence().getLanguage().equals(tag.getAttribute());
            default -> {
                System.out.println("Reached Default in ListContainer filter");
                returnValue = false; // should never happen
            }
        }

        return tag.isType() ^ returnValue;
    }

    public boolean containsAtLeastOneOccurrence(Anime anime){
        boolean contains = false;
        for(Pair pair : filteredAndSortedOccurrences){
            if(anime.getOccurrences().contains(pair.getOccurrence())) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public static ArrayList<Anime> searchedAnime(ArrayList<Anime> collection, String searchString) {
        ArrayList<Anime> returnList = new ArrayList<>();
        String inquiry = searchString.trim();

        for(Anime anime : collection){
            String[] split = anime.getName().toUpperCase().split(" ");
            if(anime.getName().toUpperCase().startsWith(inquiry.toUpperCase())){
                returnList.add(anime);
                continue;
            }
            for (String string : split) {
                if (string.toUpperCase().startsWith(inquiry.toUpperCase())) {
                    returnList.add(anime);
                    break;
                }
            }
        }
        return returnList;
    }

    public static ArrayList<Pair> searchedPairs(ArrayList<Pair> pairs, String searchString) {
        ArrayList<Pair> returnList = new ArrayList<>();
        String inquiry = searchString.trim();

        for(Pair pair : pairs){
            String[] split = pair.getOccurrence().getName().toUpperCase().split(" ");
            if(pair.getOccurrence().getName().toUpperCase().startsWith(inquiry.toUpperCase())){
                returnList.add(pair);
                continue;
            }
            for (String string : split) {
                if (string.toUpperCase().startsWith(inquiry.toUpperCase())) {
                    returnList.add(pair);
                    break;
                }
            }
        }
        return returnList;
    }

}
