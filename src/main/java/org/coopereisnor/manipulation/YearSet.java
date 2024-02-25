package org.coopereisnor.manipulation;

import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import org.coopereisnor.animeDao.Episode;
import org.coopereisnor.utility.UtilityMethods;

import java.util.ArrayList;
import java.util.List;

public class YearSet{
    private final int year;
    private final int count;
    private final ArrayList<Series<String, Number>> aggregateOfSeriesByDay;
    private final ArrayList<Series<String, Number>> aggregateOfSeriesByWeek;
    private final ArrayList<Series<String, Number>> aggregateOfSeriesByMonth;

    public YearSet(int year, ArrayList<Pair> pairs){
        this.year = year;
        this.count = calculateCount(pairs);
        this.aggregateOfSeriesByDay = getAggregateOfSeries(pairs, (episode) -> episode.getWatchDate().getDayOfYear() +"");
        this.aggregateOfSeriesByWeek = getAggregateOfSeries(pairs, (episode) -> episode.getWatchDate().getDayOfYear()/7 + 1 +"");
        this.aggregateOfSeriesByMonth = getAggregateOfSeries(pairs, (episode) -> UtilityMethods.capitalize(episode.getWatchDate().getMonth().toString().toLowerCase()));
    }

    private ArrayList<Series<String, Number>> getAggregateOfSeries(ArrayList<Pair> pairs, SeriesByInterface seriesByInterface){
        ArrayList<Series<String, Number>> aggregateOfSeries = new ArrayList<>();
        for(Pair pair : pairs){
            if(pair.occurrence().isTracked()){
                Series<String, Number> episodeSeries = new Series<>();
                episodeSeries.setName(pair.occurrence().getName());
                for(Episode episode : pair.occurrence().getEpisodesWatched()){
                    if(episode.getWatchDate().getYear() == year){
                        Data<String, Number> data = new Data<>(seriesByInterface.columnName(episode), 1);
                        data.setExtraValue(pair);
                        episodeSeries.getData().add(data);
                    }
                }
                aggregateOfSeries.add(episodeSeries);
            }
        }
        return aggregateOfSeries;
    }

    private int calculateCount(List<Pair> pairs){
        int count = 0;
        for(Pair pair : pairs){
            if(pair.occurrence().isTracked()){
                for(Episode episode : pair.occurrence().getEpisodesWatched()){
                    if(episode.getWatchDate().getYear() == year){
                         count++;
                    }
                }
            }
        }
        return count;
    }

    public int getYear(){
        return year;
    }

    public ArrayList<Series<String, Number>> getAggregateOfSeriesByDay() {
        return aggregateOfSeriesByDay;
    }

    public ArrayList<Series<String, Number>> getAggregateOfSeriesByWeek() {
        return aggregateOfSeriesByWeek;
    }

    public ArrayList<Series<String, Number>> getAggregateOfSeriesByMonth() {
        return aggregateOfSeriesByMonth;
    }

    public int getCount() {
        return count;
    }

    // interface for lambda use
    interface SeriesByInterface {
        String columnName(Episode episode);
    }
}
