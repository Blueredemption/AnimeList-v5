package org.coopereisnor.animeApplication.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.UtilityMethods;

import java.awt.image.BufferedImage;

public class AnimeController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();
    private final Anime anime = singletonDao.getCurrentAnime();

    @FXML
    private GridPane gridPane;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label nameLabel;
    @FXML
    private TabPane tabPane;


    @FXML
    public void initialize() {
        Common.configureNavigation(gridPane, this.getClass());
        kagamine();
    }

    public void kagamine(){
        Occurrence focusedOccurrence;
        Tab focusedTab = null; // focusedTab will always be assigned due to the logic flow below

        if(singletonDao.getCurrentOccurrence() != null){
            focusedOccurrence = singletonDao.getCurrentOccurrence();
        } else{
            focusedOccurrence = anime.getOccurrences().get(0);
        }

        nameLabel.setText(anime.getName());

        for(Occurrence occurrence : anime.getOccurrences()){
            Tab tab = new Tab();

            if(occurrence == focusedOccurrence){
                focusedTab = tab;
            }

            tab.setText(occurrence.getName());

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            tab.setContent(scrollPane);

            HBox hBox = new HBox();
            hBox.setPadding(new Insets(5,5,5,5));
            hBox.setFillHeight(true);
            scrollPane.setContent(hBox);
            Common.setFasterScrollBar(scrollPane);

            Pane imagePane = new Pane();
            // todo: dynamically select which occurrence to get image from
            BufferedImage bufferedImage = UtilityMethods.toBufferedImage(occurrence.getImageIcon().getImage());
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            BackgroundImage backgroundImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(1.0, 1.0, true, true, false, false));
            imagePane.setBackground(new Background(backgroundImage));
            imagePane.setOnMouseClicked(event -> {
                // todo: popup to change the image
            });

            double width = 314;
            double height = 450;
            imagePane.setMinSize(width, height);
            imagePane.setPrefSize(width, height);
            imagePane.setMaxSize(width, height);
            imagePane.setPadding(new Insets(5,0,0,5));
            // set the border todo: use settingsDao
            imagePane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
            hBox.getChildren().add(imagePane);

            tabPane.getTabs().add(tab);
        }

        tabPane.getSelectionModel().select(focusedTab);
    }
}
