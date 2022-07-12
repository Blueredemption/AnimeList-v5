package org.coopereisnor.animeDao;

import javax.swing.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Occurrence implements java.io.Serializable {
    private URL url;
    private String name;
    private String type; // Season, OVA, Movie, Other?
    private int episodes;
    private String status;
    private LocalDate airedStartDate;
    private LocalDate airedEndDate;
    private String premieredSeason;
    private int premieredYear;
    private final ArrayList<String> licensors;
    private final ArrayList<String> producers;
    private final ArrayList<String> studios;
    private String source;
    private final ArrayList<String> genres;
    private final ArrayList<String> themes;
    private int duration; // seconds
    private String rating;
    private LocalDate startedWatching;
    private LocalDate finishedWatching;

    private URL imageURL;
    private ImageIcon imageIcon;
    private final ArrayList<Episode> episodesWatched;
    private boolean favorite;
    private boolean tracked;
    private double score;
    private String language;
    private String watchStatus;
    private String notes;

    public Occurrence(){ // constructor
        url = null;
        name = null;
        type = null;
        episodes = -1;
        status = null;
        airedStartDate = null;
        airedEndDate = null;
        premieredSeason = null;
        premieredYear = -1;
        licensors = new ArrayList<>();
        producers = new ArrayList<>();
        studios = new ArrayList<>();
        source = null;
        genres = new ArrayList<>();
        themes = new ArrayList<>();
        duration = -1;
        rating = null;
        startedWatching = null;
        finishedWatching = null;

        watchStatus = null;
        imageURL = null;
        imageIcon = null;
        episodesWatched = new ArrayList<>();
        favorite = false;
        tracked = true;
        score = -1;
        language = null;
        notes = "";
    }

    // getters
    public URL getUrl(){
        return url;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getEpisodes() {
        return episodes;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getAiredStartDate() {
        return airedStartDate;
    }

    public LocalDate getAiredEndDate() {
        return airedEndDate;
    }

    public String getPremieredSeason() {
        return premieredSeason;
    }

    public int getPremieredYear() {
        return premieredYear;
    }

    public ArrayList<String> getLicensors() {
        return (ArrayList)licensors.clone();
    }

    public ArrayList<String> getProducers() {
        return (ArrayList)producers.clone();
    }

    public ArrayList<String> getStudios() {
        return (ArrayList) studios.clone();
    }

    public String getSource() {
        return source;
    }

    public ArrayList<String> getGenres() {
        return (ArrayList)genres.clone();
    }

    public ArrayList<String> getThemes() {
        return (ArrayList)themes.clone();
    }

    public int getDuration() {
        return duration;
    }

    public String getRating() {
        return rating;
    }

    public LocalDate getStartedWatching() {
        return startedWatching;
    }

    public LocalDate getFinishedWatching() {
        return finishedWatching;
    }

    public URL getImageURL(){
        return imageURL;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public ArrayList<Episode> getEpisodesWatched() {
        return (ArrayList)episodesWatched.clone();
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isTracked(){
        return tracked;
    }

    public double getScore() {
        return score;
    }

    public String getLanguage() {
        return language;
    }

    public String getWatchStatus() {
        return watchStatus;
    }

    public String getNotes() {
        return notes;
    }

    // setters
    public void setUrl(URL url) {this.url = url;}

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAiredStartDate(LocalDate airedStartDate) {
        this.airedStartDate = airedStartDate;
    }

    public void setAiredEndDate(LocalDate airedEndDate) {
        this.airedEndDate = airedEndDate;
    }

    public void setPremieredSeason(String premieredSeason) {
        this.premieredSeason = premieredSeason;
    }

    public void setPremieredYear(int premieredYear) {
        this.premieredYear = premieredYear;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setStartedWatching(LocalDate startedWatching) {
        this.startedWatching = startedWatching;
    }

    public void setFinishedWatching(LocalDate finishedWatching) {
        this.finishedWatching = finishedWatching;
    }

    public void setImageURL(URL imageURL){
        this.imageURL = imageURL;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setTracked(boolean tracked){
        this.tracked = tracked;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setWatchStatus(String watchStatus) {
        this.watchStatus = watchStatus;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // static methods for parameter groups
    public static String[] getTypes(){
        return new String[]{"Season", "Special", "Movie", "Other"};
    }

    public static String[] getStatuses(){
        return new String[]{"Not Yet Aired", "Airing", "Finished Airing", "Paused", "Other"};
    }

    public static String[] getWatchStatuses(){
        return new String[]{"Plan to Watch", "Watching", "Paused", "Dropped"};
    }

    public static String[] getPremieredSeasons(){
        return new String[]{"Winter", "Spring", "Summer", "Fall", "Other"};
    }

    public static String[] getSources(){
        return new String[]{"Manga", "Light novel", "Other"};
    }

    public static String[] getRatings(){
        return new String[]{"G", "PG", "PG-13", "R-17+", "R+, Other"};
    }

    public static String[] getLanguages(){
        return new String[]{"Subbed", "Dubbed", "Other"};
    }

    // adders

    public void addLicensor(String licensor) {
        licensors.add(licensor);
    }

    public void addProducer(String producer) {
        producers.add(producer);
    }

    public void addStudio(String studio) {
        studios.add(studio);
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public void addTheme(String theme) {
        themes.add(theme);
    }

    public void addEpisodeWatched(Episode episode){
        episodesWatched.add(episode);
    }

    // removers
    public void removeLicensor(String licensor) {
        licensors.remove(licensor);
    }

    public void removeProducer(String producer) {
        producers.remove(producer);
    }

    public void removeStudio(String studio) {
        producers.remove(studio);
    }

    public void removeGenre(String genre) {
        genres.remove(genre);
    }

    public void removeTheme(String theme) {
        themes.remove(theme);
    }

    public void removeEpisodeWatched(Episode episode){
        episodesWatched.remove(episode);
    }

    // debug
    public String toString(){
        String returnString = "";
        returnString += "Name: " +name +"\n";
        returnString += "Type: " +type +"\n";
        returnString += "Episodes: " +episodes +"\n";
        returnString += "Status: " +status +"\n";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String startString = airedStartDate == null ? null : airedStartDate.format(formatter);
        String endString = airedEndDate == null ? null : airedEndDate.format(formatter);
        returnString += "Aired: " +startString +" to " +endString +"\n";

        returnString += "Premiered: " +premieredSeason +" " +premieredYear +"\n";
        returnString += "Producers: " +producers +"\n";
        returnString += "Licensors: " +licensors +"\n";
        returnString += "Studios: " +studios +"\n";
        returnString += "Source: " +source +"\n";
        returnString += "Genres: " +genres +"\n";
        returnString += "Themes: " +themes+"\n";
        returnString += "Duration: " +duration/60 +"\n";
        returnString += "Rating: " +rating +"\n";
        returnString += "MAL URL: " +url.toString() +"\n";
        returnString += "Image URL: " +imageURL.toString() +"\n";
        return returnString;
    }
}
