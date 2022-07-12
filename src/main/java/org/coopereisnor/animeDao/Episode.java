package org.coopereisnor.animeDao;

import java.time.LocalDate;

public class Episode implements java.io.Serializable {
    private LocalDate watchDate;
    private boolean favorite;
    private String name;

    public Episode(){ // constructor
        watchDate = LocalDate.now();
        favorite = false;
        name = "";
    }

    // getters
    public LocalDate getWatchDate() {
        return watchDate;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getName() {
        return name;
    }

    // setters
    public void setWatchDate(LocalDate watchDate) {
        this.watchDate = watchDate;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setName(String name) {
        this.name = name;
    }
}
