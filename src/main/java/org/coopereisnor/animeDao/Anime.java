package org.coopereisnor.animeDao;

import java.util.ArrayList;

public class Anime implements java.io.Serializable {
    private final String identifier;
    private final ArrayList<Occurrence> occurrences;
    private double score;

    private String name;

    public Anime(String identifier){ // constructor
        this.identifier = identifier;
        this.occurrences = new ArrayList<>();
        this.score = 0;
        this.name = "New Anime";
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

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setScore(double score){
        this.score = score;
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
