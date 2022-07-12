package org.coopereisnor.animeDao;

import java.util.ArrayList;

public class Anime implements java.io.Serializable {
    private final String identifier;
    private final ArrayList<Occurrence> occurrences;

    private String name;

    public Anime(String identifier){ // constructor
        this.identifier = identifier;
        this.name = "New Anime";
        this.occurrences = new ArrayList<>();
    }

    public String getIdentifier(){
        return identifier;
    }

    // non final variables

    // getters
    public String getName() {
        return name;
    }

    // setters
    public void setName(String name) {
        this.name = name;
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
