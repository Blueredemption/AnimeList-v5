package org.coopereisnor.manipulation;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Episode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class ConfigurePlots {
    public static final String[] MONTH_CATEGORIES = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String[] WEEK_CATEGORIES = Arrays.stream(IntStream.rangeClosed(1, 52).toArray()).mapToObj(String::valueOf).toArray(String[]::new);
    public static final String[] DAY_CATEGORIES = Arrays.stream(IntStream.rangeClosed(1, 366).toArray()).mapToObj(String::valueOf).toArray(String[]::new);

    private final ArrayList<YearSet> yearSets;

    public ConfigurePlots(ArrayList<Anime> collection){
        ArrayList<Pair> pairs = AnimeAggregate.getPairs(collection); // we need them unsorted, hence the seemingly redundant collection -> pairs when we could just pass in the pairs.
        ArrayList<Episode> episodes = aggregateEpisodes(pairs);
        LocalDate firstDate = episodes.isEmpty() ? LocalDate.now() : episodes.get(0).getWatchDate();
        LocalDate lastDate = episodes.isEmpty() ? LocalDate.now() : episodes.get(episodes.size() - 1).getWatchDate();
        yearSets = aggregateYearSets(pairs, firstDate, lastDate);
    }

    private ArrayList<Episode> aggregateEpisodes(ArrayList<Pair> pairs){
        ArrayList<Episode> episodes = new ArrayList<>();
        for(Pair pair : pairs){
            if(pair.getOccurrence().isTracked()){
                episodes.addAll(Arrays.asList(pair.getOccurrence().getEpisodesWatched()));
            }
        }
        episodes.sort(Comparator.comparing(Episode::getWatchDate));

        return episodes;
    }

    private ArrayList<YearSet> aggregateYearSets(ArrayList<Pair> pairs, LocalDate firstDate, LocalDate lastDate){
        ArrayList<YearSet> yearSets = new ArrayList<>();
        for(int i = firstDate.getYear(); i <= lastDate.getYear(); i++){
            yearSets.add(new YearSet(i, pairs));
        }
        return yearSets;
    }

    public ArrayList<YearSet> getYearSets() {
        return yearSets;
    }

    public ArrayList<Integer> getYears(){
        ArrayList<Integer> years = new ArrayList<>();
        for(YearSet yearSet : yearSets){
            years.add(yearSet.getYear());
        }
        return years;
    }
}
