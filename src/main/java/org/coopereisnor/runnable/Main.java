package org.coopereisnor.runnable;


import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.malScrape.MALScrape;
import org.coopereisnor.settingsDao.Settings;
import org.coopereisnor.settingsDao.SettingsDao;

public class Main {
    public static void main(String[] args){
        //testFileIO();
        //testMALScrape();
        //addFamiliarOfZero();
        //addTestAnime();
        //random();
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

    public static void addFamiliarOfZero(){
        AnimeDao animeDao = new AnimeDao();

        Anime anime = animeDao.createNewAnime();
        anime.setName("Familiar of Zero");
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/1195/Zero_no_Tsukaima"));
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/1840/Zero_no_Tsukaima__Futatsuki_no_Kishi"));
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/3712/Zero_no_Tsukaima__Princesses_no_Rondo"));
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/5764/Zero_no_Tsukaima__Princesses_no_Rondo_-_Yuuwaku_no_Sunahama"));
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/11319/Zero_no_Tsukaima_F"));
        anime.getOccurrences().get(0).setFocused(true);
        animeDao.save(anime);
    }

    public static void addTestAnime(){
        AnimeDao animeDao = new AnimeDao();

        Anime anime = animeDao.createNewAnime();
        anime.setName("Naruto");
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/20/Naruto"));
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/1735/Naruto__Shippuuden"));
        anime.getOccurrences().get(0).setFocused(true);
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.setName("Hunter x Hunter");
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/11061/Hunter_x_Hunter_2011?q=Hunter&cat=anime"));
        anime.getOccurrences().get(0).setFocused(true);
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.setName("That Time I Got Reincarnated as a Slime");
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/37430/Tensei_shitara_Slime_Datta_Ken"));
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/39551/Tensei_shitara_Slime_Datta_Ken_2nd_Season"));
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/41487/Tensei_shitara_Slime_Datta_Ken_2nd_Season_Part_2"));
        anime.getOccurrences().get(0).setFocused(true);
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/20507/Noragami"));
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/30503/Noragami_Aragoto"));
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Noragami");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/205/Samurai_Champloo?q=samura&cat=anime"));
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Samurai Champloo");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/853/Ouran_Koukou_Host_Club"));
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Ouran High School Host Club");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/22789/Barakamon"));
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Barakamon");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/486/Kino_no_Tabi__The_Beautiful_World"));
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Kino's Journey");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/68/Black_Cat_TV"));
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Black Cat");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(MALScrape.getOccurrenceFromURL("https://myanimelist.net/anime/6/Trigun"));
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Trigun");
        animeDao.save(anime);
    }

    public static void random(){

    }
}
