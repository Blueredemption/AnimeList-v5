package org.coopereisnor.malScrape;

import org.coopereisnor.animeDao.Occurrence;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.coopereisnor.utility.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MALScrape {

    // checks the link to see if it is a mal anime link
    public static boolean checkLink(String url){
        return url.contains("https://myanimelist.net/anime/");
    }

    public static Occurrence getOccurrenceFromURL(String url){
        try{
            return parseWebpage(getWebpage(url), url);
        } catch (IOException ex) {
            Log.logger.error("Failure in MalScrape.java", ex);
            return new Occurrence(System.currentTimeMillis());
        }
    }

    public static void refreshOccurrenceFromURL(String url, Occurrence oldOccurrence){
        if(!checkLink(url)) return; // we don't want to refresh with an empty anime

        Occurrence newOccurrence = getOccurrenceFromURL(url);

        oldOccurrence.setUrl(newOccurrence.getUrl());

        if(newOccurrence.getEpisodes() > oldOccurrence.getEpisodes()){
            oldOccurrence.setEpisodes(newOccurrence.getEpisodes());
        }

        oldOccurrence.setStatus(newOccurrence.getStatus());

        oldOccurrence.setAiredStartDate(newOccurrence.getAiredStartDate());
        oldOccurrence.setAiredEndDate(newOccurrence.getAiredEndDate());
        oldOccurrence.setPremieredSeason(newOccurrence.getPremieredSeason());
        oldOccurrence.setPremieredYear(newOccurrence.getPremieredYear());

        for(String producer : newOccurrence.getProducers()){
            oldOccurrence.addProducer(producer);
        }

        for(String studio : newOccurrence.getStudios()){
            oldOccurrence.addStudio(studio);
        }

        for(String licensor : newOccurrence.getLicensors()){
            oldOccurrence.addLicensor(licensor);
        }

        for(String genre : newOccurrence.getGenres()){
            oldOccurrence.addGenre(genre);
        }

        for(String theme : newOccurrence.getThemes()){
            oldOccurrence.addTheme(theme);
        }

        oldOccurrence.setImageIcon(newOccurrence.getImageIcon());
    }

    // retrieves the webpage
    private static Document getWebpage(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    private static Occurrence parseWebpage(Document webpage, String url) throws IOException {
        Occurrence occurrence = new Occurrence(System.currentTimeMillis());

        // the name of the occurrence comes from the webpage title
        occurrence.setName(webpage.title().replace("- MyAnimeList.net", "").trim());
        // where the url comes from is obvious lol
        occurrence.setUrl(new URL(url));

        // the image link is retrieved from the html of the webpage
        ImageIcon imageIcon = getImageIcon(webpage.html());
        if(imageIcon != null) occurrence.setImageIcon(imageIcon);

        // the rest of the attributes are retrieved from the raw text of the webpage

        String webpageString = webpage.wholeText();

        // type
        String typeString = getAttributeString(webpageString, "Type:", null);
        if(typeString != null){
            occurrence.setType(removeHeader(typeString));
        }

        // episodes
        typeString = getAttributeString(webpageString, "Episodes:", null);
        if(typeString != null){
            try{
                occurrence.setEpisodes(Integer.parseInt(removeHeader(typeString)));
            }catch(Exception ex){
                Log.logger.error("Could not parse Episodes string value",ex);
            }
        }

        // status
        typeString = getAttributeString(webpageString, "Status:", null);
        if(typeString != null){
            occurrence.setStatus(removeHeader(typeString));
        }

        // aired
        typeString = getAttributeString(webpageString, "Aired:", null);
        if(typeString != null){
            String aired = removeHeader(typeString);
            String[] split = aired.split("to");

            try{
                occurrence.setAiredStartDate(parseDate(split[0]));
            }catch(Exception ex){
                Log.logger.error("Could not apply AiredStartDate", ex);
            }

            try{
                occurrence.setAiredEndDate(parseDate(split[1]));
            }catch(ParseException ex){
                Log.logger.error("Could not apply AiredEndDate", ex);
            }catch(ArrayIndexOutOfBoundsException ex){
                occurrence.setAiredEndDate(occurrence.getAiredStartDate());
                Log.logger.error("Index out of bounds exception, setting airedEndDate to airedStartDate", ex);
            }
        }

        // premiered
        typeString = getAttributeString(webpageString, "Premiered:", null);
        if(typeString != null){
            String premiered = removeHeader(typeString);
            String[] split = premiered.split(" ");
            if(split[0].trim().equals("?")){
                // do nothing
            }else{
                occurrence.setPremieredSeason(split[0].trim());
                occurrence.setPremieredYear(Integer.parseInt(split[1]== null ? "" : split[1].trim()));
            }
        }else{
            if(occurrence.getAiredStartDate() != null) occurrence.setPremieredYear(occurrence.getAiredStartDate().getYear());
        }

        // producers
        typeString = getAttributeString(webpageString, "Producers:", "Producer:");
        if(typeString != null) {
            for(String string : getEntries(typeString)){
                occurrence.addProducer(string);
            }
        }

        // licensors
        typeString = getAttributeString(webpageString, "Licensors:", "Licensor:");
        if(typeString != null) {
            for(String string : getEntries(typeString)){
                occurrence.addLicensor(string);
            }
        }

        // studios
        typeString = getAttributeString(webpageString, "Studios:", "Studio:");
        if(typeString != null) {
            for(String string : getEntries(typeString)){
                occurrence.addStudio(string);
            }
        }

        // source
        typeString = getAttributeString(webpageString, "Source:", null);
        if(typeString != null){
            occurrence.setSource(removeHeader(typeString));
        }

        // genres
        typeString = getAttributeString(webpageString, "Genres:", "Genre:");
        if(typeString != null) {
            for(String string : getGenreThemeEntries(typeString)){
                occurrence.addGenre(string);
            }
        }

        // themes
        typeString = getAttributeString(webpageString, "Themes:", "Theme:");
        if(typeString != null) {
            for(String string : getGenreThemeEntries(typeString)){
                occurrence.addTheme(string);
            }
        }

        // duration
        typeString = getAttributeString(webpageString, "Duration:", null);
        if(typeString != null){
            occurrence.setDuration(convertDurationToSeconds(typeString));
        }

        // rating
        typeString = getAttributeString(webpageString, "Rating:", null);
        if(typeString != null){
            if(typeString.contains("R - 17+")){
                occurrence.setRating("R-17+");
            } else{
                occurrence.setRating(removeHeader(typeString).split(" - ")[0].trim());
            }
        }

        return occurrence;
    }

    private static ImageIcon getImageIcon(String htmlSource) throws IOException {
        ImageIcon imageIcon = null;

        InputStream stream = new ByteArrayInputStream(htmlSource.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line = br.readLine();

        while (line != null) {
            if(line.contains("og:image")){
                String imageURL = line.replace("<meta property=\"og:image\" content=\"","").replace("\"/>","").replace("\">","");
                // we want the biggest version, so we have to add an "l" before the .blah (this is a mal thing)
                imageURL = imageURL.substring(0, imageURL.lastIndexOf(".")) +"l" +imageURL.substring(imageURL.lastIndexOf("."));
                URL urlObject = new URL(imageURL);
                imageIcon = new ImageIcon(ImageIO.read(urlObject));
                break; // found it, no need to look anymore.
            }
            line = br.readLine();
        }

        return imageIcon;
    }

    private static String getAttributeString(String webpageString, String attributeStringOne, String attributeStringTwo) throws IOException {
        InputStream stream = new ByteArrayInputStream(webpageString.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line = br.readLine();
        boolean typeFound = false; // we want the searches to not catch anything before Type: (Status seems to get caught somewhere before the information portion)

        if(attributeStringOne != null && attributeStringTwo != null){
            while (line != null) {
                if (!typeFound) if(line.contains("Type:")) typeFound = true;
                else {
                    line = br.readLine();
                    continue;
                }

                if(line.contains(attributeStringOne)||line.contains(attributeStringTwo)){
                    return br.readLine();
                }
                line = br.readLine();
            }
        }else if(attributeStringOne != null){
            while (line != null) {
                if (!typeFound) if(line.contains("Type:")) typeFound = true;
                else {
                    line = br.readLine();
                    continue;
                }

                if(line.contains(attributeStringOne)){
                    return br.readLine();
                }
                line = br.readLine();
            }
        }

        return null;
    }

    private static String removeHeader(String string){
        return string.substring(string.indexOf(":") + 1).trim();
    }

    private static String[] getEntries(String string){
        if(string.contains("None found, add some")){ // in the special case where there aren't any entries, we don't want the junk.
            return new String[0];
        } else{
            String[] entries = removeHeader(string).split(",");
            for(int i = 0; i < entries.length; i++){
                entries[i] = entries[i].trim();
            }
            return entries;
        }
    }

    private static String[] getGenreThemeEntries(String string){
        String[] entries = getEntries(string);
        for(int i = 0; i < entries.length; i++){
            entries[i] = entries[i].substring(0, (entries[i].length()/2));
        }
        return entries;
    }

    private static int convertDurationToSeconds(String string){
        String str = string.replaceAll("[^0-9]", "");
        if(str.equals("")){
            return -1;
        }else if(string.contains("hr")){
            return (int)(Double.parseDouble(str.substring(0, 1))*60*60) + (int)(Double.parseDouble(str.substring(1))*60);
        }else if(string.contains("sec")){
            return (int)(Double.parseDouble(str.substring(0, 1)));
        }else{
            return (int)(Double.parseDouble(str)*60);
        }
    }

    private static LocalDate parseDate(String dateString) throws ParseException {
        DateFormat df = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        Date date = df.parse(dateString.replace(",","").trim());
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
