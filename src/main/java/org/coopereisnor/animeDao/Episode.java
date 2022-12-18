package org.coopereisnor.animeDao;

import java.time.LocalDate;

public class Episode implements java.io.Serializable {
    private LocalDate watchDate;

    public Episode(){ // constructor
        watchDate = LocalDate.now();
    }

    public LocalDate getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(LocalDate watchDate) {
        this.watchDate = watchDate;
    }
}
