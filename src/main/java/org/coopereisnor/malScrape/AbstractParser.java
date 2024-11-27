package org.coopereisnor.malScrape;

import org.coopereisnor.animeDao.Occurrence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public abstract class AbstractParser {
  private final String websiteLink;
  
  public AbstractParser(String websiteLink) {
    this.websiteLink = websiteLink;
  }
  
  boolean isLinkValid(String link) {
    return link.contains(websiteLink);
  }
  
  Document parseLinkIntoDocument(String link) throws IOException {
    return Jsoup.connect(link).get();
  }

  public Occurrence getNewOccurrence() throws IOException {
    Occurrence occurrence = new Occurrence(System.currentTimeMillis());

    occurrence.setName(getName());
    occurrence.setUrl(getUrl());
    occurrence.setImageIcon(getImageIcon());
    occurrence.setType(getType());
    occurrence.setEpisodes(getEpisodes());
    occurrence.setStatus(getStatus());
    occurrence.setAiredStartDate(getAiredStartDate());
    occurrence.setAiredEndDate(getAiredEndDate());
    occurrence.setPremieredSeason(getPremieredSeason());
    occurrence.setPremieredYear(getPremieredYear());
    getProducers().forEach(occurrence::addProducer);
    getLicensors().forEach(occurrence::addLicensor);
    getStudios().forEach(occurrence::addStudio);
    occurrence.setSource(getSource());
    getGenres().forEach(occurrence::addGenre);
    getThemes().forEach(occurrence::addTheme);
    occurrence.setDuration(getDuration());
    occurrence.setRating(getRating());

    return occurrence;
  }

  public void updateOccurrence(Occurrence oldOccurrence) throws IOException {
    oldOccurrence.setUrl(getUrl());

    if(getEpisodes() > oldOccurrence.getEpisodes()){
      oldOccurrence.setEpisodes(getEpisodes());
    }

    oldOccurrence.setStatus(getStatus());

    oldOccurrence.setAiredStartDate(getAiredStartDate());
    oldOccurrence.setAiredEndDate(getAiredEndDate());
    oldOccurrence.setPremieredSeason(getPremieredSeason());
    oldOccurrence.setPremieredYear(getPremieredYear());

    getProducers().forEach(oldOccurrence::addProducer);
    getLicensors().forEach(oldOccurrence::addLicensor);
    getStudios().forEach(oldOccurrence::addStudio);

    oldOccurrence.setSource(getSource());

    getGenres().forEach(oldOccurrence::addGenre);
    getThemes().forEach(oldOccurrence::addTheme);
  }

  abstract String getName();

  abstract URL getUrl() throws MalformedURLException;

  abstract ImageIcon getImageIcon() throws IOException;

  abstract String getType();

  abstract int getEpisodes();

  abstract String getStatus();

  abstract LocalDate getAiredStartDate();

  abstract LocalDate getAiredEndDate();

  abstract String getPremieredSeason();

  abstract int getPremieredYear();

  abstract List<String> getProducers();

  abstract List<String> getLicensors();

  abstract List<String> getStudios();

  abstract String getSource(); // occurrence source, not webpage source.

  abstract List<String> getGenres();

  abstract List<String> getThemes();
  
  abstract int getDuration();
  
  abstract String getRating();
}
