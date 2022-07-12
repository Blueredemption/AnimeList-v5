package org.coopereisnor.settingsDao;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Settings implements java.io.Serializable {
    private ImageIcon homeImage;
    private Color textColor;

    public Settings(){ // constructor
        homeImage = new ImageIcon(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
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
