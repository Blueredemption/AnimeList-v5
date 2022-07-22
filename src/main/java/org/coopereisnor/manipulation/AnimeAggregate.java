package org.coopereisnor.manipulation;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;

import java.util.ArrayList;

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
}
