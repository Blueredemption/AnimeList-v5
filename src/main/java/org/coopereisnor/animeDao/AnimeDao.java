package org.coopereisnor.animeDao;

import org.coopereisnor.utility.UtilityMethods;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class AnimeDao {
    static File root = new File(FileSystemView.getFileSystemView().getHomeDirectory().toPath() +File.separator +"AnimeList-v5" +File.separator +"animeObjects");
    ArrayList<Anime> collection;

    public AnimeDao(){ // constructor
        collection = new ArrayList();
        initialize();
    }

    // Initializes the file structure for the data to be stored on the computer
    private void initialize(){
        root.mkdirs();
        loadAll();
    }

    // adds all anime into collection from storage
    private void loadAll(){
        for(String path : Objects.requireNonNull(root.list())){
            load(new File(root.toPath() +File.separator +path));
        }
    }

    // saves all anime from collection into storage
    private void saveAll(){
        for(int i = 0; i < collection.size() - 1; i++){
            save(i);
        }
    }

    // loads an anime from storage
    private void load(File file){
        try {
            FileInputStream fileStream = new FileInputStream(file);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            add((Anime)objectStream.readObject());
            objectStream.close();
            fileStream.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    // saves an anime from collection into storage
    public void save(Anime anime){
        if(collection.contains(anime)){
            save(collection.indexOf(anime));
        } else{
            System.out.println("Anime Does not exist in collection, cannot save!");
        }
    }

    private void save(int index){
        Anime anime = collection.get(index);
        try {
            FileOutputStream fileStream = new FileOutputStream(root.toPath() +File.separator +anime.getIdentifier() +".ser");
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(anime);
            objectStream.close();
            fileStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Anime createNewAnime(){
        Anime anime = new Anime(System.currentTimeMillis() +"");
        add(anime);
        return anime;
    }

    // adds an anime to the collection and storage
    private void add(Anime anime){
        // first we check to see if it is already in the collection before adding it (serialization makes this laborious)
        for(Anime entry : collection){
            if(anime.getIdentifier().equals(entry.getIdentifier())) return; // it is already there, no need to add it.
        }
        // it was not found in the collection, so let's add it.
        collection.add(anime);

        // let's make sure to save this to storage (in most cases, redundant)
        save(collection.indexOf(anime));
    }

    // removes an anime from collection and storage
    public void remove(Anime anime){
        // first we check to see if it is already in the collection before removing it (serialization makes this laborious)
        ArrayList<Anime> tempCollection = new ArrayList<>();
        for(Anime entry : collection){
            if(anime.getIdentifier().equals(entry.getIdentifier())) tempCollection.add(entry) ; // found one! remove when done iterating.
        }
        // remove all found copies (this is a presumptive catch-all for any duplication bug that may arise)
        for(Anime entry : tempCollection){
            collection.remove(entry);
        }

        // now let's delete the anime from storage as well.
        File file = new File(root.toPath() +File.separator +anime.getIdentifier() +".ser");
        file.delete();
    }

    public ArrayList<Anime> getCollection(){
        return (ArrayList)collection.clone();
    }

    // danger zone!
    public static void purge(){
        UtilityMethods.purge(root);
    }
}
