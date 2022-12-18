package org.coopereisnor.manipulation;

import javafx.scene.chart.XYChart;
import org.coopereisnor.animeDao.Episode;
import org.coopereisnor.utility.UtilityMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class YearSet{
    private final int year;
    private final ArrayList<XYChart.Series<String, Number>> aggregateOfSeriesByDay;
    private final ArrayList<XYChart.Series<String, Number>> aggregateOfSeriesByWeek;
    private final ArrayList<XYChart.Series<String, Number>> aggregateOfSeriesByMonth;

    public YearSet(int year, ArrayList<Pair> pairs){
        this.year = year;
        aggregateOfSeriesByDay = getAggregateOfSeries(pairs, (episode) -> episode.getWatchDate().getDayOfYear() +"", year);
        aggregateOfSeriesByWeek = getAggregateOfSeries(pairs, (episode) -> episode.getWatchDate().getDayOfYear()/7 + 1 +"", year);
        aggregateOfSeriesByMonth = getAggregateOfSeries(pairs, (episode) -> UtilityMethods.capitalize(episode.getWatchDate().getMonth().toString().toLowerCase()), year);
    }

    private ArrayList<XYChart.Series<String, Number>> getAggregateOfSeries(ArrayList<Pair> pairs, SeriesByInterface seriesByInterface, int year){
        ArrayList<XYChart.Series<String, Number>> aggregateOfSeries = new ArrayList<>();
        for(Pair pair : pairs){
            if(pair.getOccurrence().isTracked()){
                XYChart.Series<String, Number> episodeSeries = new XYChart.Series<>();
                episodeSeries.setName(pair.getOccurrence().getName());
                for(Episode episode : pair.getOccurrence().getEpisodesWatched()){
                    if(episode.getWatchDate().getYear() == year){
                        XYChart.Data<String, Number> data = new XYChart.Data<>(seriesByInterface.columnName(episode), 1);
                        data.setExtraValue(pair);
                        episodeSeries.getData().add(data);
                    }
                }
                aggregateOfSeries.add(episodeSeries);
            }
        }
        return aggregateOfSeries;
    }

    public int getYear(){
        return year;
    }

    public ArrayList<XYChart.Series<String, Number>> getAggregateOfSeriesByDay() {
        return aggregateOfSeriesByDay;
    }

    public ArrayList<XYChart.Series<String, Number>> getAggregateOfSeriesByWeek() {
        return aggregateOfSeriesByWeek;
    }

    public ArrayList<XYChart.Series<String, Number>> getAggregateOfSeriesByMonth() {
        return aggregateOfSeriesByMonth;
    }

    // interface for lambda use
    interface SeriesByInterface {
        String columnName(Episode episode);
    }
}
