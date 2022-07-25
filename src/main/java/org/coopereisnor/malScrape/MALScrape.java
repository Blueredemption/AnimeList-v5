package org.coopereisnor.malScrape;

import org.coopereisnor.animeDao.Occurrence;

import javax.imageio.ImageIO;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MALScrape {

    public static Occurrence getOccurrenceFromURL(String url){
        try {
            Document page = Jsoup.connect(url).get();

            Occurrence occurrence = new Occurrence(System.currentTimeMillis());

            // the first two are easy:
            occurrence.setName(page.title().replace("- MyAnimeList.net", "").trim());
            occurrence.setUrl(new URL(url));

            // the rest I have found by iterating through mostly the raw text
            String plainText = page.wholeText();
            InputStream stream = new ByteArrayInputStream(plainText.getBytes(StandardCharsets.UTF_8));
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String str = br.readLine();

            int count = 0;
            while (str != null) {
                switch(count){
                    case 0:
                        if(str.contains("Type:")) {
                            str = br.readLine();
                            occurrence.setType(removeHeader(str));
                            count++;
                        }
                        break;
                    case 1:
                        if(str.contains("Episodes:")) {
                            str = br.readLine();
                            try{ // if it is a number, pass it along
                                occurrence.setEpisodes(Integer.parseInt(removeHeader(str)));
                            }catch(Exception ex){ // if it doesn't parse (it's not a number), pass in -1.
                                occurrence.setEpisodes(-1);
                            }
                            count++;
                        }
                        break;
                    case 2:
                        if(str.contains("Status:")) {
                            str = br.readLine();
                            occurrence.setStatus(removeHeader(str));
                            count++;
                        }
                        break;
                    case 3:
                        if(str.contains("Aired:")) {
                            str = br.readLine();
                            String aired = removeHeader(str);
                            String[] split = aired.split("to");
                            // if either the start or end date do not parse we want the given date to be set to null instead.
                            try{
                                occurrence.setAiredStartDate(parseDate(split[0]));
                            }catch(Exception ex){
                                occurrence.setAiredStartDate(null);
                            }
                            try{
                                occurrence.setAiredEndDate(parseDate(split[1]));
                            }catch(ArrayIndexOutOfBoundsException ex){
                                occurrence.setAiredEndDate(null);

                                // the "second" count++ is because if there is only one "Aired", "Premiered" doesn't (probably always) exist.
                                count++;
                                // and as a result, there probably won't be any year or season. We don't care if season is empty, but we do care if year is.
                                // so lets get year from the first date, if it exists.
                                if(occurrence.getAiredStartDate() != null) occurrence.setPremieredYear(occurrence.getAiredStartDate().getYear());

                            }catch(ParseException ex){
                                occurrence.setAiredEndDate(null);
                            }
                            count++;
                        }
                        break;
                    case 4:
                        if(str.contains("Premiered:")) {
                            str = br.readLine();
                            String premiered = removeHeader(str);
                            String[] split = premiered.split(" ");
                            if(split[0].trim().equals("?")){
                                occurrence.setPremieredSeason(null);
                                occurrence.setPremieredYear(-1);
                            }else{
                                occurrence.setPremieredSeason(split[0].trim());
                                occurrence.setPremieredYear(Integer.parseInt(split[1].trim()));
                            }
                            count++;
                        }
                        break;
                    case 5:
                        if(str.contains("Producers:")||str.contains("Producer:")) {
                            str = br.readLine();
                            for(String string : getEntries(str)){
                                occurrence.addProducer(string);
                            }
                            count++;
                        }
                        break;
                    case 6:
                        if(str.contains("Licensors:")||str.contains("Licensor:")) {
                            str = br.readLine();
                            for(String string : getEntries(str)){
                                occurrence.addLicensor(string);
                            }
                            count++;
                        }
                        break;
                    case 7:
                        if(str.contains("Studios:")||str.contains("Studio:")) {
                            str = br.readLine();
                            for(String string : getEntries(str)){
                                occurrence.addStudio(string);
                            }
                            count++;
                        }
                        break;
                    case 8:
                        if(str.contains("Source:")) {
                            str = br.readLine();
                            occurrence.setSource(removeHeader(str));
                            count++;
                        }
                        break;
                    case 9:
                        if(str.contains("Genres:")||str.contains("Genre:")) {
                            str = br.readLine();
                            for(String string : getGenreThemeEntries(str)){
                                occurrence.addGenre(string);
                            }
                            count++;
                        }
                        break;
                    case 10:
                        if(str.contains("Themes:")||str.contains("Theme:")){
                            str = br.readLine();
                            for(String string : getGenreThemeEntries(str)){
                                occurrence.addTheme(string);
                            }
                        } else if(str.contains("Duration:")) {
                            str = br.readLine();
                            occurrence.setDuration(convertDurationToSeconds(str));
                            count++;
                        }
                        break;
                    case 11:
                        if(str.contains("Rating:")) {
                            str = br.readLine();
                            if(str.contains("R - 17+")){
                                occurrence.setRating("R-17+");
                            } else{
                                occurrence.setRating(removeHeader(str).split(" - ")[0].trim());
                            }
                            count++;
                        }
                        break;
                }
                str = br.readLine();
            }

            // The final thing we need to do is get the image for the anime, but we need to go through the html source to get the url for it.
            String htmlSource = page.html();
            stream = new ByteArrayInputStream(htmlSource.getBytes(StandardCharsets.UTF_8));
            br = new BufferedReader(new InputStreamReader(stream));
            str = br.readLine();

            while (str != null) {
                if(str.contains("og:image")){
                    String imageURL = str.replace("<meta property=\"og:image\" content=\"","").replace("\"/>","").replace("\">","");
                    // we want the biggest version, so we have to add an "l" before the .blah
                    imageURL = imageURL.substring(0, imageURL.lastIndexOf(".")) +"l" +imageURL.substring(imageURL.lastIndexOf("."));
                    URL urlObject = new URL(imageURL);
                    occurrence.setImageURL(urlObject);
                    occurrence.setImageIcon(new ImageIcon(ImageIO.read(urlObject)));
                    break; // found it, no need to look anymore.
                }
                str = br.readLine();
            }
            return occurrence;

        } catch (IOException ex) {
            ex.printStackTrace();
            return new Occurrence(System.currentTimeMillis()); // if it fails just return an empty occurrence
        }
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
