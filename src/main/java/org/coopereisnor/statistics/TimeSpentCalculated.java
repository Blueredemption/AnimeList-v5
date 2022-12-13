package org.coopereisnor.statistics;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;

import java.util.ArrayList;

public class TimeSpentCalculated {
    private final int animeWatched;
    private final int episodesWatched;
    private final int totalTimeInSeconds;
    private final int daysSpent;
    private final int hoursSpent;
    private final int minutesSpent;

    public TimeSpentCalculated(ArrayList<Anime> anime, ArrayList<Occurrence> occurrences){
        animeWatched = anime.size();
        episodesWatched = OccurrenceStatistics.getTotalEpisodesWatched(occurrences);

        totalTimeInSeconds = OccurrenceStatistics.getTotalTimeInSeconds(occurrences);

        int minutes = totalTimeInSeconds/60;
        int hours = minutes/60;
        daysSpent = hours/24;
        hoursSpent = hours - daysSpent*24;
        minutesSpent = minutes - hoursSpent*60 - daysSpent*24*60;
    }

    public int getAnimeWatched() {
        return animeWatched;
    }

    public int getEpisodesWatched() {
        return episodesWatched;
    }

    public int getTotalTimeInSeconds(){
        return totalTimeInSeconds;
    }

    public int getDaysSpent() {
        return daysSpent;
    }

    public int getHoursSpent() {
        return hoursSpent;
    }

    public int getMinutesSpent() {
        return minutesSpent;
    }
}
