package org.coopereisnor.animeApplication.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.coopereisnor.animeApplication.Application;
import org.coopereisnor.animeApplication.singleton.SingletonDao;
import org.coopereisnor.animeDao.Anime;
import org.coopereisnor.animeDao.AnimeDao;
import org.coopereisnor.animeDao.Occurrence;
import org.coopereisnor.malScrape.MALScrape;
import org.coopereisnor.settingsDao.SettingsDao;
import org.coopereisnor.utility.Log;
import org.coopereisnor.utility.UtilityMethods;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class EditHomeImageController {
    private final SingletonDao singletonDao = SingletonDao.getInstance();
    private final AnimeDao animeDao = singletonDao.getAnimeDao();
    private final SettingsDao settingsDao = singletonDao.getSettingsDao();
    private final Application application = singletonDao.getApplication();

    @FXML
    private Pane imagePane;
    @FXML
    private TextField urlTextField;
    @FXML
    private Button fileLocationButton;
    @FXML
    private TextField fileLocationTextField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    private ImageIcon currentImageIcon = settingsDao.getSettings().getHomeImage();

    @FXML
    public void initialize() {
        setImage(currentImageIcon);

        fileLocationButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(fileLocationButton.getScene().getWindow());
            try{
                fileLocationTextField.setText(selectedFile.getAbsolutePath());
            }catch(Exception ex){
                Log.logger.error("File Not Valid!", ex);
            }
            checkFilePath();
        });

        urlTextField.setOnKeyTyped(keyEvent -> {
            try {
                URL currentURL = new URL(urlTextField.getText());
                currentImageIcon = new ImageIcon(ImageIO.read(currentURL));
                setImage(currentImageIcon);
            } catch (MalformedURLException ex) {
                Log.logger.error("URL Not Valid!", ex);
                currentImageIcon = null;
            } catch (IOException ex) {
                Log.logger.error("Image Not Valid!", ex);
                currentImageIcon = null;
            }
            setValid();
        });


        fileLocationTextField.setOnKeyTyped(keyEvent -> {
            checkFilePath();
        });


        cancelButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).close());

        confirmButton.setOnAction(this::endEvent);
    }

    private void setValid(){
        confirmButton.setDisable(currentImageIcon == null);
    }

    private void setImage(ImageIcon imageIcon){
        BufferedImage bufferedImage = UtilityMethods.toBufferedImage(imageIcon.getImage());
        Common.getImagePaneFor(imagePane, bufferedImage);
    }

    private void checkFilePath(){
        try{
            currentImageIcon = new ImageIcon(fileLocationTextField.getText());
            setImage(currentImageIcon);
        }catch(Exception ex){
            Log.logger.error("File Not Valid!", ex);
            currentImageIcon = null;
        }
    }

    private void endEvent(Event event) {
        settingsDao.getSettings().setHomeImage(currentImageIcon);
        settingsDao.save();
        application.changeScene("home.fxml");
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
