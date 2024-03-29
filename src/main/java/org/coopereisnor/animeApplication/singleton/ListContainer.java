package org.coopereisnor.animeApplication.singleton;

import org.coopereisnor.Program;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.manipulation.AnimeAggregate;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.manipulation.Tag;
import org.coopereisnor.settingsDao.SettingsDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ListContainer {
    private final AnimeDao animeDao;

    private String sortBy;
    private String order;
    private String aggregate;
    private boolean type;

    private String search = "";
    private ArrayList<Tag> tags = new ArrayList<>(); // starts as empty

    private ArrayList<Anime> filteredAndSortedAnime = null;
    private ArrayList<Pair> filteredAndSortedOccurrences = null;

    public ListContainer(AnimeDao animeDao, SettingsDao settingsDao){
        this.animeDao = animeDao;

        sortBy = settingsDao.getSettings().getDefaultAttributeState();
        order = settingsDao.getSettings().getDefaultDirectionState();
        aggregate = settingsDao.getSettings().getDefaultAggregateState();
        type = settingsDao.getSettings().isDefaultTypeState();
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

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(String aggregate) {
        this.aggregate = aggregate;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
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
        Program.logger.debug("Silent Update");
        // it is important that occurrences are filtered first because anime are filtered using the occurrences list
        filteredAndSortedOccurrences = sortPairs(filterPairs(AnimeAggregate.getPairs(animeDao.getCollection())));
        filteredAndSortedAnime = sortAnime(filterAnime(animeDao.getCollection()));

        if(order.equals("Descending")){
            Collections.reverse(filteredAndSortedAnime);
            Collections.reverse(filteredAndSortedOccurrences);
        }
    }

    public void update(){
        Program.logger.debug("Update");
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

    public ArrayList<Anime> sortAnime(ArrayList<Anime> collection){
        return sortAnime(collection, sortBy);
    }

    public ArrayList<Anime> sortAnime(ArrayList<Anime> collection, String sort){
        switch (sort) {
            case "Added" -> collection.sort(Anime.SORT_BY_ADDED);
            case "Score" -> collection.sort(Anime.SORT_BY_SCORE);
            case "Name" -> collection.sort(Anime.SORT_BY_NAME);
            case "Rank" -> collection.sort(Anime.SORT_BY_RANK);
            case "Started" -> collection.sort(Anime.SORT_BY_STARTED_WATCHING_DATE);
            case "Finished" -> collection.sort(Anime.SORT_BY_FINISHED_WATCHING_DATE);
            case "Eps. Watched" -> collection.sort(Anime.SORT_BY_EPISODES_WATCHED);
            case "Eps. Total" -> collection.sort(Anime.SORT_BY_EPISODES_TOTAL);
            case "Released" -> collection.sort(Anime.SORT_BY_RELEASE_DATE);
            case "Progress" -> collection.sort(Anime.SORT_BY_PROGRESS);
            default -> Program.logger.error("Reached Default in ListContainer sortAnime : " +sort);
        }
        return collection;
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
            case "Released" -> pairs.sort(Pair.SORT_BY_RELEASE_DATE);
            case "Progress" -> pairs.sort(Pair.SORT_BY_PROGRESS);
            default -> Program.logger.error("Reached Default in ListContainer sortPairs : " +sortBy);
        }
        return pairs;
    }

    public boolean getFilterApply(Pair pair, Tag tag){
        boolean returnValue;
        switch(tag.getFilter()){
            case "Type" -> returnValue = pair.occurrence().getType().equals(tag.getAttribute());
            case "Status" -> returnValue = pair.occurrence().getStatus().equals(tag.getAttribute());
            case "Season" -> returnValue = pair.occurrence().getPremieredSeason().equals(tag.getAttribute());
            case "Genre" -> returnValue = Arrays.asList(pair.occurrence().getGenres()).contains(tag.getAttribute());
            case "Theme" -> returnValue = Arrays.asList(pair.occurrence().getThemes()).contains(tag.getAttribute());
            case "Rating" -> returnValue = pair.occurrence().getRating().equals(tag.getAttribute());
            case "Source" -> returnValue = pair.occurrence().getSource().equals(tag.getAttribute());
            case "Studio" -> returnValue = Arrays.asList(pair.occurrence().getStudios()).contains(tag.getAttribute());
            case "Producer" -> returnValue = Arrays.asList(pair.occurrence().getProducers()).contains(tag.getAttribute());
            case "Licensor" -> returnValue = Arrays.asList(pair.occurrence().getLicensors()).contains(tag.getAttribute());
            case "Watch Status" -> returnValue = pair.occurrence().getWatchStatus().equals(tag.getAttribute());
            case "Language" ->  returnValue = Arrays.asList(pair.occurrence().getLanguages()).contains(tag.getAttribute());
            default -> {
                Program.logger.error("Reached Default in ListContainer filter : " +tag.getFilter());
                returnValue = false; // should never happen
            }
        }

        return tag.isType() ^ returnValue;
    }

    public boolean containsAtLeastOneOccurrence(Anime anime){
        return containsAtLeastOneOccurrence(anime, filteredAndSortedOccurrences);
    }

    public boolean containsAtLeastOneOccurrence(Anime anime, ArrayList<Pair> pairs){
        boolean contains = false;
        for(Pair pair : pairs){
            if(anime.getOccurrences().contains(pair.occurrence())) {
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
            String[] split = pair.occurrence().getName().toUpperCase().split(" ");
            if(pair.occurrence().getName().toUpperCase().startsWith(inquiry.toUpperCase())){
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

    // methods used by to calculate statistics (hijacked above methods for counting instead of explicit filtering)
    public ArrayList<Anime> getAnime(ArrayList<Anime> collection, ArrayList<Pair> pairs){
        ArrayList<Anime> toKeep = new ArrayList<>();
        for(Anime anime : collection){
            if(containsAtLeastOneOccurrence(anime, pairs)) toKeep.add(anime);
        }
        return toKeep;
    }

    public ArrayList<Pair> getPairs(ArrayList<Pair> pairs, Tag tag){
        ArrayList<Pair> toKeep = new ArrayList<>();
        for(Pair pair : pairs){
            if(getFilterApply(pair, tag)) toKeep.add(pair);
        }
        return toKeep;
    }
}
