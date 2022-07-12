package org.coopereisnor.settingsDao;

import javax.swing.filechooser.FileSystemView;
import java.io.*;

public class SettingsDao {
    File root;
    Settings settings;

    public SettingsDao(){ // constructor
        initialize();
    }

    // Initializes the file structure for the data to be stored on the computer
    private void initialize(){
        root = new File(FileSystemView.getFileSystemView().getHomeDirectory().toPath() +File.separator +"AnimeList-v5" +File.separator +"settings");
        root.mkdirs();
        load(new File(root.toPath() +File.separator +"settings.ser"));
    }

    // loads the settings from storage (if they exist, if they don't it creates one)
    private void load(File file){
        try {
            FileInputStream fileStream = new FileInputStream(file);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            settings = (Settings)objectStream.readObject();
            objectStream.close();
            fileStream.close();
        } catch (IOException | ClassNotFoundException ex) {
            settings = new Settings();
            save();
        }
    }

    // saves an anime from collection into storage
    public void save(){
        try {
            FileOutputStream fileStream = new FileOutputStream(root.toPath() +File.separator +"settings.ser");
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(settings);
            objectStream.close();
            fileStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Settings getSettings(){
        return settings;
    }
}
