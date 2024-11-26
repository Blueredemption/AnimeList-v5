package org.coopereisnor.malScrape;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class MalParser extends AbstractParser {
  private static final String WEBSITE_URL = "https://myanimelist.net/anime/";

  private final String link;
  private final Document jSoupDocument;
  private final String jSoupTitle;
  private final String jSoupHtml;

  public MalParser(String link) throws IOException {
    super(WEBSITE_URL);

    this.link = link;
    jSoupDocument = parseLinkIntoDocument(link);
    jSoupTitle = jSoupDocument.title();
    jSoupHtml = jSoupDocument.html();
  }

  @Override
  String getName() {
    Element nameElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(English:)");
    String name = nameElement != null ? nameElement.text().replace("English:", "").trim() : "";
    if(name.isEmpty()) {
      return jSoupTitle.replace("- MyAnimeList.net", "").trim();
    }
    return name;
  }

  @Override
  URL getUrl() throws MalformedURLException {
    return new URL(link);
  }

  @Override
  ImageIcon getImageIcon() throws IOException {
    ImageIcon imageIcon = getImageIcon(jSoupHtml);
    if(imageIcon == null) {
      return new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/Default.png")));
    }
    return imageIcon;
  }

  @Override
  String getType() {
    Element typeElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Type:)");
    return typeElement != null ? typeElement.text().replace("Type:", "").trim() : "";
  }

  @Override
  int getEpisodes() {
    Element episodesElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Episodes:)");
    if (episodesElement != null) {
      try {
        String episodesText = episodesElement.text().replace("Episodes:", "").trim();
        return Integer.parseInt(episodesText);
      } catch (NumberFormatException numberFormatException) {
        return 0;
      }
    }
    return 0;
  }

  @Override
  String getStatus() {
    Element statusElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Status:)");
    return statusElement != null ? statusElement.text().replace("Status:", "").trim() : "";
  }

  @Override
  LocalDate getAiredStartDate() {
    Element airedElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Aired:)");
    if (airedElement != null) {
      try {
        String airedText = airedElement.text().replace("Aired:", "").trim();
        String[] dates = airedText.split(" to ");
        if (dates.length > 0 && !dates[0].isBlank()) {
          return LocalDate.parse(dates[0].trim(), DateTimeFormatter.ofPattern("MMM d, yyyy"));
        }
      } catch (DateTimeParseException dateTimeParseException) {
        return null;
      }
    }
    return null;
  }

  @Override
  LocalDate getAiredEndDate() {
    Element airedElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Aired:)");
    if (airedElement != null) {
      try {
        String airedText = airedElement.text().replace("Aired:", "").trim();

        if(!airedText.contains("to")) {
          return getAiredStartDate();
        }

        String[] dates = airedText.split(" to ");
        if (dates.length > 1 && !dates[1].isBlank()) {
          return LocalDate.parse(dates[1].trim(), DateTimeFormatter.ofPattern("MMM d, yyyy"));
        }
      } catch (DateTimeParseException dateTimeParseException) {
        return null;
      }
    }
    return null;


  }

  @Override
  String getPremieredSeason() {
    Element premieredElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Premiered:)");
    if (premieredElement != null) {
      String premieredText = premieredElement.text().replace("Premiered:", "").trim();
      if(premieredText.contains("?")) {
        return "";
      }
      return premieredText.split(" ")[0];
    }
    return "";
  }

  @Override
  int getPremieredYear() {
    Element premieredElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Premiered:)");
    if (premieredElement != null) {
      String premieredText = premieredElement.text().replace("Premiered:", "").trim();
      try {
        return Integer.parseInt(premieredText.split(" ")[1]);
      } catch (Exception e) {
        return -1;
      }
    }
    return -1;
  }

  @Override
  List<String> getProducers() {
    Element producersElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Producers:), div.spaceit_pad:contains(Producer:)");
    if (producersElement != null) {
      List<String> producers = producersElement.select("a").eachText();
      producers.removeIf(producer -> producer.equalsIgnoreCase("add some"));
      return producers;
    }
    return List.of();
  }

  @Override
  List<String> getLicensors() {
    Element licensorsElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Licensors:), div.spaceit_pad:contains(Licensor:)");
    if (licensorsElement != null) {
      List<String> licensors = licensorsElement.select("a").eachText();
      licensors.removeIf(licensor -> licensor.equalsIgnoreCase("add some"));
      return licensors;
    }
    return List.of();
  }

  @Override
  List<String> getStudios() {
    Element studiosElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Studios:), div.spaceit_pad:contains(Studio:)");
    if (studiosElement != null) {
      List<String> studios = studiosElement.select("a").eachText();
      studios.removeIf(studio -> studio.equalsIgnoreCase("add some"));
      return studios;
    }
    return List.of();
  }

  @Override
  String getSource() {
    Element sourceElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Source:)");
    return sourceElement != null ? sourceElement.text().replace("Source:", "").trim() : "";
  }

  @Override
  List<String> getGenres() {
    Element genresElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Genres:), div.spaceit_pad:contains(Genre:)");
    if (genresElement != null) {
      List<String> genres = genresElement.select("a").eachText();
      genres.removeIf(genre -> genre.equalsIgnoreCase("add some"));
      return genres;
    }
    return List.of();
  }

  @Override
  List<String> getThemes() {
    Element themesElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Themes:), div.spaceit_pad:contains(Theme:)");
    if (themesElement != null) {
      List<String> themes = themesElement.select("a").eachText();
      themes.removeIf(theme -> theme.equalsIgnoreCase("add some"));
      return themes;
    }
    return List.of();
  }

  @Override
  int getDuration() {
    Element durationElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Duration:)");
    if (durationElement != null) {
      try {
        String durationText = durationElement.text().replace("Duration:", "").trim();
        if (durationText.contains("hr")) {
          return Integer.parseInt(durationText.split("hr")[0].trim()) * 3600 + Integer.parseInt(durationText.split("hr")[1].replaceAll("[^0-9]", "").trim()) * 60;
        } else if (durationText.contains("sec")) {
          return Integer.parseInt(durationText.replaceAll("[^0-9]", "").trim());
        } else {
          return Integer.parseInt(durationText.replaceAll("[^0-9]", "").trim()) * 60;
        }
      } catch(NumberFormatException numberFormatException) {
        return -1;
      }
    }
    return -1;
  }

  @Override
  String getRating() {
    Element ratingElement = jSoupDocument.selectFirst("div.spaceit_pad:contains(Rating:)");
    String trimmedString = ratingElement != null ? ratingElement.text()
        .replace("Rating:", "")
        .replace("All Ages", "")
        .replace("Children", "")
        .replace("- Teens 13 or older", "")
        .replace("(violence & profanity)", "")
        .replace("Mild Nudity", "")
        .replace("Hentai", "")
        .replace(" ", "")
        .trim() : "";

    if(trimmedString.endsWith("-")) {
      return trimmedString.substring(0, trimmedString.length() - 1);
    }

    return trimmedString;
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
}
