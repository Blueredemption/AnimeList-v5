package org.coopereisnor.settingsDao;

import org.coopereisnor.Program;
import org.coopereisnor.utility.UtilityMethods;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingsDao {
    static File root = new File(UtilityMethods.getLocalDataDirectory() +File.separator +"AnimeList-v5" +File.separator +"settings");
    Settings settings;

    public SettingsDao(){ // constructor
        initialize();
    }

    // Initializes the file structure for the data to be stored on the computer
    private void initialize(){
        root.mkdirs();
        load(new File(root.toPath() +File.separator +"settings.ser"));
        writeCSSFile();
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
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                FileOutputStream fileStream = new FileOutputStream(root.toPath() +File.separator +"settings.ser");
                ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
                objectStream.writeObject(settings);
                objectStream.close();
                fileStream.close();
            } catch (IOException ex) {
                Program.logger.error("IOException", ex);
            }
        });
    }

    public Settings getSettings(){
        return settings;
    }

    public File getRoot(){
        return root;
    }

    public void writeCSSFile(){
        try {
            FileWriter writer = new FileWriter(root.toPath() +File.separator +"application.css");

            InputStream stream = getClass().getResourceAsStream("/css/Application.css");
            String cssFile = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

            cssFile = cssFile.replaceFirst("buttonColor", UtilityMethods.getStringOfColor(settings.getButtonColor()));
            cssFile = cssFile.replaceFirst("buttonColorHovered", UtilityMethods.getStringOfColor(settings.getButtonColorHovered()));
            cssFile = cssFile.replaceFirst("buttonColorPressed", UtilityMethods.getStringOfColor(settings.getButtonColorPressed()));
            cssFile = cssFile.replaceFirst("borderColor", UtilityMethods.getStringOfColor(settings.getBorderColor()));
            cssFile = cssFile.replaceFirst("backgroundColorThree", UtilityMethods.getStringOfColor(settings.getBackgroundColorThree()));
            cssFile = cssFile.replaceFirst("backgroundColorTwo", UtilityMethods.getStringOfColor(settings.getBackgroundColorTwo()));
            cssFile = cssFile.replaceFirst("backgroundColorOne", UtilityMethods.getStringOfColor(settings.getBackgroundColorOne()));
            cssFile = cssFile.replaceFirst("textAreaColor", UtilityMethods.getStringOfColor(settings.getTextAreaColor()));
            cssFile = cssFile.replaceFirst("rejectionColor", UtilityMethods.getStringOfColor(settings.getRejectionColor()));
            cssFile = cssFile.replaceFirst("rejectionColorHovered", UtilityMethods.getStringOfColor(settings.getRejectionColorHovered()));
            cssFile = cssFile.replaceFirst("rejectionColorPressed", UtilityMethods.getStringOfColor(settings.getRejectionColorPressed()));
            cssFile = cssFile.replaceFirst("rejectionColorBorder", UtilityMethods.getStringOfColor(settings.getRejectionColorBorder()));
            cssFile = cssFile.replaceFirst("acceptanceColor", UtilityMethods.getStringOfColor(settings.getAcceptanceColor()));
            cssFile = cssFile.replaceFirst("acceptanceColorHovered", UtilityMethods.getStringOfColor(settings.getAcceptanceColorHovered()));
            cssFile = cssFile.replaceFirst("acceptanceColorPressed", UtilityMethods.getStringOfColor(settings.getAcceptanceColorPressed()));
            cssFile = cssFile.replaceFirst("acceptanceColorBorder", UtilityMethods.getStringOfColor(settings.getAcceptanceColorBorder()));
            cssFile = cssFile.replaceFirst("neutralColor", UtilityMethods.getStringOfColor(settings.getNeutralColor()));
            cssFile = cssFile.replaceFirst("neutralColorHovered", UtilityMethods.getStringOfColor(settings.getNeutralColorHovered()));
            cssFile = cssFile.replaceFirst("neutralColorPressed", UtilityMethods.getStringOfColor(settings.getNeutralColorPressed()));
            cssFile = cssFile.replaceFirst("neutralColorBorder", UtilityMethods.getStringOfColor(settings.getNeutralColorBorder()));
            cssFile = cssFile.replaceFirst("progressBarColor", UtilityMethods.getStringOfColor(settings.getProgressBarColor()));
            cssFile = cssFile.replaceFirst("progressBarColorFull", UtilityMethods.getStringOfColor(settings.getProgressBarColorFull()));
            cssFile = cssFile.replaceFirst("textColor", UtilityMethods.getStringOfColor(settings.getTextColor()));

            writer.write(cssFile);
            writer.close();
        } catch (IOException ex) {
            Program.logger.error("IOException", ex);
        }
    }

    // danger zone!
    public static void purge() {
        UtilityMethods.purge(root);
    }
}
