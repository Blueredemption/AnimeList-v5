package org.coopereisnor.runnable;


import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.malScrape.MALScrape;
import org.coopereisnor.settingsDao.Settings;
import org.coopereisnor.settingsDao.SettingsDao;

public class Main {
    public static void main(String[] args){
        testFileIO();
        //testMALScrape();
    }

    public static void testFileIO(){
        AnimeDao animeDao = new AnimeDao();
        animeDao.createNewAnime();

        int index = 0;
        for(Anime anime : animeDao.getCollection()){
            System.out.println(index++ +") " +anime.getName() +": " +anime.getIdentifier());
        }

        SettingsDao settingsDao = new SettingsDao();
        Settings settings = settingsDao.getSettings();
        System.out.println(settings.getTextColor());
    }

    public static void testMALScrape(){
        Occurrence occurrence = MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/1195/Zero_no_Tsukaima?q=familiar&cat=anime");
        System.out.println(occurrence);
    }
}
