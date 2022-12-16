package org.coopereisnor.statistics;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.manipulation.Pair;

import java.util.ArrayList;

public class Count {
    private final String value;
    private final ArrayList<Anime> collection;
    private final ArrayList<Pair> pairs;

    public Count(String value, ArrayList<Anime> collection, ArrayList<Pair> pairs){
        this.value = value;
        this.collection = collection;
        this.pairs = pairs;
    }

    public String getValue() {
        return value;
    }

    public ArrayList<Anime> getCollection() {
        return collection;
    }

    public ArrayList<Pair> getPairs() {
        return pairs;
    }
}
