package org.coopereisnor.manipulation;

import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;

public class Pair {
    private Anime anime;
    private Occurrence occurrence;

    public Pair(Anime anime, Occurrence occurrence){
        this.anime = anime;
        this.occurrence = occurrence;
    }

    public Anime getAnime() {
        return anime;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }
}
