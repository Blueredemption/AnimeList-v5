package org.coopereisnor.manipulation;

import org.coopereisnor.animeDao.Anime;

import java.util.ArrayList;

public record Count(String value, ArrayList<Anime> collection, ArrayList<Pair> pairs) {
}
