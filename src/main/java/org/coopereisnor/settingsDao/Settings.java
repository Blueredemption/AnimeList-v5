package org.coopereisnor.settingsDao;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class Settings implements java.io.Serializable {
    private ImageIcon homeImage;
    private Color textColor;

    public Settings(){ // constructor
        homeImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/Background.png")));
        textColor = Color.BLUE;
    }

    // getters and setters

    // getters
    public ImageIcon getHomeImage() {
        return homeImage;
    }

    public Color getTextColor() {
        return textColor;
    }

    // setters

    public void setHomeImage(ImageIcon homeImage) {
        this.homeImage = homeImage;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
}
