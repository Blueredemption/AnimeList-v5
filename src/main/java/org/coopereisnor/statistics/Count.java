package org.coopereisnor.statistics;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.manipulation.Pair;

import java.util.ArrayList;

public class Count {
    private final String value;
    private final ArrayList<Anime> anime;
    private final ArrayList<Pair> pairs;

    public Count(String value, ArrayList<Anime> anime, ArrayList<Pair> pairs){
        this.value = value;
        this.anime = anime;
        this.pairs = pairs;
    }

    public String getValue() {
        return value;
    }

    public ArrayList<Anime> getAnime() {
        return anime;
    }

    public ArrayList<Pair> getPairs() {
        return pairs;
    }
}
