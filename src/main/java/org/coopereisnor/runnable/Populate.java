package org.coopereisnor.runnable;


import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.malScrape.MalParser;

import java.io.IOException;

public class Populate {
    public static void main(String[] args) throws IOException {
        addFamiliarOfZero();
        addTestAnime();
        System.exit(0); // program doesn't seem to stop after execution? I know I close the streams...?
    }

    public static void addFamiliarOfZero() throws IOException {
        AnimeDao animeDao = new AnimeDao();

        Anime anime = animeDao.createNewAnime();
        anime.setName("Familiar of Zero");
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/1195/Zero_no_Tsukaima")).getNewOccurrence());
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/1840/Zero_no_Tsukaima__Futatsuki_no_Kishi")).getNewOccurrence());
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/3712/Zero_no_Tsukaima__Princesses_no_Rondo")).getNewOccurrence());
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/5764/Zero_no_Tsukaima__Princesses_no_Rondo_-_Yuuwaku_no_Sunahama")).getNewOccurrence());
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/11319/Zero_no_Tsukaima_F")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        animeDao.save(anime);
    }

    public static void addTestAnime() throws IOException {
        AnimeDao animeDao = new AnimeDao();

        Anime anime = animeDao.createNewAnime();
        anime.setName("Naruto");
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/20/Naruto")).getNewOccurrence());
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/1735/Naruto__Shippuuden")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.setName("Hunter x Hunter");
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/11061/Hunter_x_Hunter_2011?q=Hunter&cat=anime")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.setName("That Time I Got Reincarnated as a Slime");
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/37430/Tensei_shitara_Slime_Datta_Ken")).getNewOccurrence());
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/39551/Tensei_shitara_Slime_Datta_Ken_2nd_Season")).getNewOccurrence());
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/41487/Tensei_shitara_Slime_Datta_Ken_2nd_Season_Part_2")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/20507/Noragami")).getNewOccurrence());
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/30503/Noragami_Aragoto")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Noragami");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/205/Samurai_Champloo?q=samura&cat=anime")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Samurai Champloo");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/853/Ouran_Koukou_Host_Club")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Ouran High School Host Club");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/22789/Barakamon")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Barakamon");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/486/Kino_no_Tabi__The_Beautiful_World")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Kino's Journey");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/68/Black_Cat_TV")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Black Cat");
        animeDao.save(anime);

        anime = animeDao.createNewAnime();
        anime.addOccurrence(new MalParser(("https://myanimelist.net/anime/6/Trigun")).getNewOccurrence());
        anime.getOccurrences().get(0).setFocused(true);
        anime.setName("Trigun");
        animeDao.save(anime);
    }
}
