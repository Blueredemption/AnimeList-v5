package org.coopereisnor.animeDao;

import java.util.ArrayList;

public class Anime implements java.io.Serializable {
    private final String identifier;
    private final ArrayList<Occurrence> occurrences;
    private double score;
    private int rank;

    private String name;

    public Anime(String identifier){ // constructor
        this.identifier = identifier;
        this.occurrences = new ArrayList<>();
        this.score = -1;
        this.name = "New Anime";
        this.rank = Integer.MAX_VALUE;
    }

    public String getIdentifier(){
        return identifier;
    }

    // non final variables

    // getters
    public String getName() {
        return name;
    }

    public double getScore(){
        return score;
    }

    public int getRank(){
        return rank;
    }

    public Occurrence getFocusedOccurrence(){
        for(Occurrence occ : occurrences){
            if(occ.isFocused()) return occ;
        }
        return null;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setScore(double score){
        this.score = score;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    // occurrence related
    public void addOccurrence(Occurrence occurrence){
        occurrences.add(occurrence);
    }

    public void removeOccurrence(Occurrence occurrence){
        occurrences.remove(occurrence);
    }

    public ArrayList<Occurrence> getOccurrences(){
        return (ArrayList)occurrences.clone();
    }

}
