package org.coopereisnor.statistics;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;

import java.util.ArrayList;

public class TimeSpentCalculated {
    private final int animeCount;
    private final int occurrencesCount;
    private final int episodesWatched;
    private final int daysSpent;
    private final int hoursSpent;
    private final int minutesSpent;

    public TimeSpentCalculated(ArrayList<Anime> collection, ArrayList<Occurrence> occurrences){
        animeCount = collection.size();
        occurrencesCount = occurrences.size();
        episodesWatched = OccurrenceStatistics.getTotalEpisodesWatched(occurrences);

        int totalTimeInSeconds = OccurrenceStatistics.getTotalTimeInSeconds(occurrences);

        int minutes = totalTimeInSeconds/60;
        int hours = minutes/60;
        daysSpent = hours/24;
        hoursSpent = hours - daysSpent*24;
        minutesSpent = minutes - hoursSpent*60 - daysSpent*24*60;
    }

    public int getAnimeCount() {
        return animeCount;
    }

    public int getOccurrencesCount() {
        return occurrencesCount;
    }

    public int getEpisodesWatched() {
        return episodesWatched;
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
