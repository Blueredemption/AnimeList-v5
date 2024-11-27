package org.coopereisnor.animeApplication.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.customJavaFXObjects.PercentProgressBar;
import org.coopereisnor.animeApplication.singleton.ListContainer;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.manipulation.OccurrenceStatistics;
import org.coopereisnor.manipulation.Pair;
import org.coopereisnor.manipulation.Tag;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ListController implements Controller {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final Application application = singletonDao.getApplication();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();

    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane imageFlowPane;
    @FXML
    private ToggleButton viewButton;
    @FXML
    private ToggleButton typeButton;
    @FXML
    private VBox insideScrollPane;
    @FXML
    private ToggleButton orderButton;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button addFilterButton;
    @FXML
    private FlowPane flowPane;
    @FXML
    private TextField searchField;
    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        singletonDao.setCurrentController(this);
        Common.configureNavigation(gridPane, this.getClass());
        Common.setFasterScrollBar(scrollPane);

        if (imageFlowPane != null) {
            imageFlowPane.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
                resizeImages(newSceneWidth.intValue());
            });
        }

        loadToggleComponents();
        loadTypeComponents();
        loadSortComponents();
        loadFilterComponents();
    }

    private void loadTypeComponents() {
        typeButton.setText(singletonDao.getType());
        orderButton.setText(singletonDao.getListContainer().getOrder());

        if (singletonDao.getListFXML().equals("list.fxml")) {
            loadListComponents();
        } else {
            loadImageComponents();
        }
    }

    private void loadListComponents() {
        // first remove everything to start fresh
        insideScrollPane.getChildren().clear();

        int iterator = 0;
        if (singletonDao.getType().equals("Anime")) {
            for (Anime anime : ListContainer.searchedAnime(singletonDao.getListContainer().getFilteredAndSortedAnime(), searchField.getText())) {
                String number = ++iterator + ":";
                String name = anime.getName();
                String episodes = OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences()) == 0 ? "" : OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences()) + "";
                String season = anime.getFocusedOccurrence().getPremieredSeason();
                String year = anime.getFocusedOccurrence().getPremieredYear() == -1 ? "" : anime.getFocusedOccurrence().getPremieredYear() + "";
                int part = OccurrenceStatistics.getTotalEpisodesWatched(anime.getOccurrences());
                int whole = OccurrenceStatistics.getTotalEpisodes(anime.getOccurrences());

                addListComponent(number, name, episodes, season, year, part, whole, anime, null);
            }
        } else {
            for (Pair pair : ListContainer.searchedPairs(singletonDao.getListContainer().getFilteredAndSortedOccurrences(), searchField.getText())) {
                String number = ++iterator + ":";
                String name = pair.occurrence().getName();
                String episodes = pair.occurrence().getEpisodes() == 0 ? "" : pair.occurrence().getEpisodes() + "";
                String season = pair.occurrence().getPremieredSeason();
                String year = pair.occurrence().getPremieredYear() == -1 ? "" : pair.occurrence().getPremieredYear() + "";
                int part = pair.occurrence().getEpisodesWatched().length;
                int whole = pair.occurrence().getEpisodes();

                addListComponent(number, name, episodes, season, year, part, whole, pair.anime(), pair.occurrence());
            }
        }
    }

    private void addListComponent(String number, String name, String episodes, String season, String year, int part, int whole, Anime anime, Occurrence occurrence) {
        GridPane containerPane = new GridPane();
        GridPane.setFillHeight(containerPane, true);
        containerPane.setMinWidth(HBox.USE_COMPUTED_SIZE);
        containerPane.setMinHeight(HBox.USE_PREF_SIZE);
        containerPane.setPrefWidth(Double.MAX_VALUE);
        containerPane.setPrefHeight(37);
        containerPane.setMaxWidth(HBox.USE_COMPUTED_SIZE);
        containerPane.setMaxHeight(HBox.USE_COMPUTED_SIZE);
        containerPane.setPadding(new Insets(5, 5, 5, 5));
        containerPane.setId("containerBackground");
        insideScrollPane.getChildren().add(containerPane);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.NEVER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.NEVER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.NEVER);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setHgrow(Priority.NEVER);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setHgrow(Priority.NEVER);
        ColumnConstraints col6 = new ColumnConstraints();
        col6.setHgrow(Priority.NEVER);
        ColumnConstraints col7 = new ColumnConstraints();
        col7.setHgrow(Priority.NEVER);
        containerPane.getColumnConstraints().addAll(col0, col1, col2, col3, col4, col5, col6, col7);
        Insets insets = new Insets(0, 5, 0, 5);

        int i = 0;
        Label label = new Label(number);
        label.setMinWidth(35);
        label.setTextOverrun(OverrunStyle.CLIP);
        containerPane.add(label, i++, 0);
        GridPane.setMargin(label, insets);

        label = new Label(name);
        label.setPrefWidth(Integer.MAX_VALUE);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        containerPane.add(label, i++, 0);
        GridPane.setMargin(label, insets);

        label = new Label(season);
        label.setMinWidth(82);
        label.setTextOverrun(OverrunStyle.CLIP);
        containerPane.add(label, i++, 0);
        GridPane.setMargin(label, insets);

        label = new Label(year);
        label.setMinWidth(70);
        label.setTextOverrun(OverrunStyle.CLIP);
        containerPane.add(label, i++, 0);
        GridPane.setMargin(label, insets);

        label = new Label(episodes);
        label.setMinWidth(80);
        label.setTextOverrun(OverrunStyle.CLIP);
        containerPane.add(label, i++, 0);
        GridPane.setMargin(label, insets);

        PercentProgressBar progressBar = new PercentProgressBar(part, whole);
        progressBar.setMinWidth(110);
        progressBar.setPrefHeight(containerPane.getPrefHeight() - containerPane.getInsets().getBottom() - containerPane.getInsets().getTop() - 4);
        containerPane.add(progressBar, i++, 0);
        GridPane.setMargin(progressBar, insets);

        Button viewButton = new Button("View");
        viewButton.setMinWidth(100);
        viewButton.setPrefHeight(containerPane.getPrefHeight() - containerPane.getInsets().getBottom() - containerPane.getInsets().getTop());
        viewButton.setOnAction(actionEvent -> {
            singletonDao.setCurrentAnime(anime, occurrence);
            application.changeScene("anime.fxml");
        });
        containerPane.add(viewButton, i, 0);
        GridPane.setMargin(viewButton, new Insets(0, 5, 0, 5));
    }


    private void loadImageComponents() {
        // first remove everything to start fresh
        imageFlowPane.getChildren().clear();

        if (singletonDao.getType().equals("Anime")) {
            for (Anime anime : ListContainer.searchedAnime(singletonDao.getListContainer().getFilteredAndSortedAnime(), searchField.getText())) {
                addImageComponent(UtilityMethods.toBufferedImage(anime.getFocusedOccurrence().getImageIcon().getImage()), anime, null);
            }
        } else {
            for (Pair pair : ListContainer.searchedPairs(singletonDao.getListContainer().getFilteredAndSortedOccurrences(), searchField.getText())) {
                addImageComponent(UtilityMethods.toBufferedImage(pair.occurrence().getImageIcon().getImage()), pair.anime(), pair.occurrence());
            }
        }

        resizeImages((int) imageFlowPane.getWidth());
    }

    private void addImageComponent(BufferedImage bufferedImage, Anime anime, Occurrence occurrence) {
        Pane imagePane = Common.getImagePaneFor(null, bufferedImage);
        imagePane.setOnMouseClicked(event -> {
            singletonDao.setCurrentAnime(anime, occurrence);
            application.changeScene("anime.fxml");
        });

        imageFlowPane.getChildren().add(imagePane);
    }

    private void resizeImages(int parentSize) {
        int componentsPerRow = settingsDao.getSettings().getImagesPerRow();
        int layoutHorizontalPadding = 5;
        int componentWidth = (parentSize - (layoutHorizontalPadding * (componentsPerRow + 1))) / componentsPerRow;
        int componentMod = parentSize % componentsPerRow;
        double componentHeight = componentWidth * (225d / 157d);
        int pseudoIndex = componentMod >= layoutHorizontalPadding ?
            componentMod - layoutHorizontalPadding :
            componentMod - layoutHorizontalPadding + componentsPerRow;

        List<Node> children = imageFlowPane.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            int chunkFactor = i % componentsPerRow < pseudoIndex ? 1 : 0;
            int childWidth = componentWidth + chunkFactor;
            if (child instanceof Pane) {
                ((Pane) child).setMinSize(childWidth, componentHeight);
                ((Pane) child).setPrefSize(childWidth, componentHeight);
                ((Pane) child).setMaxSize(childWidth, componentHeight);
            }
        }
    }


    private void loadToggleComponents() {
        // search area
        searchField.setText(singletonDao.getListContainer().getSearch());
        searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            singletonDao.getListContainer().setSearch(searchField.getText());
            loadTypeComponents();
        });

        // view button
        if (singletonDao.getListFXML().equals("list.fxml")) { // these don't need to updated higher up since the page refreshes
            viewButton.setText("List");
            viewButton.setSelected(true);
        } else {
            viewButton.setText("Image");
            viewButton.setSelected(false);
        }

        viewButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                singletonDao.setListFXML("list.fxml");
            } else {
                singletonDao.setListFXML("listImages.fxml");
            }
            application.changeScene(singletonDao.getListFXML());
        }));

        // type button
        typeButton.setSelected(!singletonDao.getType().equals("Anime"));
        typeButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue) {
                singletonDao.setType("Anime");
            } else {
                singletonDao.setType("Occurrence");
            }

            loadTypeComponents();
        }));

        // filter button
        addFilterButton.setOnAction(actionEvent -> {
            Common.popup("addFilter.fxml");
        });
    }

    private void loadSortComponents() {
        ObservableList<String> sortStrings = FXCollections.observableArrayList("Added", "Score", "Name", "Rank", "Started", "Finished", "Eps. Watched", "Eps. Total", "Released", "Progress");
        comboBox.setItems(sortStrings);
        comboBox.setValue(singletonDao.getListContainer().getSortBy());
        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            singletonDao.getListContainer().setSortBy(comboBox.getValue());
            loadTypeComponents();
        });

        orderButton.setSelected(!singletonDao.getListContainer().getOrder().equals("Ascending"));
        orderButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (oldValue) {
                singletonDao.getListContainer().setOrder("Ascending");
            } else {
                singletonDao.getListContainer().setOrder("Descending");
            }
            loadTypeComponents();
        }));
    }

    private void loadFilterComponents() {
        for (Tag tag : singletonDao.getListContainer().getTags()) {
            Button tagButton = new Button(tag.getAttribute());
            tagButton.setPrefHeight(25);
            tagButton.setUserData(tag);
            tagButton.setId(tag.isType() ? "filterButtonWant" : "filterButtonAvoid");
            tagButton.setOnAction(actionEvent -> {
                ArrayList<Tag> currentTags = singletonDao.getListContainer().getTags();
                currentTags.remove((Tag) tagButton.getUserData());
                singletonDao.getListContainer().setTags(currentTags);
                flowPane.getChildren().remove(tagButton);
                loadTypeComponents();
            });
            flowPane.getChildren().add(tagButton);
        }
    }

    @Override
    public ProgressBar getUpdateProgressBar() {
        return progressBar;
    }
}
