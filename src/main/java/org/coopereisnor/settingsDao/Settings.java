package org.coopereisnor.settingsDao;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Settings implements java.io.Serializable {
    public static final ImageIcon DEFAULT_HOME_IMAGE = new ImageIcon(Objects.requireNonNull(Settings.class.getResource("/images/Background.png")));
    public static final Color DEFAULT_BUTTON_COLOR = new Color(50, 50, 50);
    public static final Color DEFAULT_BUTTON_COLOR_HOVERED = new Color(60, 60, 60);
    public static final Color DEFAULT_BUTTON_COLOR_PRESSED = new Color(70, 70, 70);
    public static final Color DEFAULT_BORDER_COLOR = new Color(80, 80, 80);
    public static final Color DEFAULT_BACKGROUND_COLOR_THREE = new Color(50, 50, 50);
    public static final Color DEFAULT_BACKGROUND_COLOR_TWO = new Color(40, 40, 40);
    public static final Color DEFAULT_BACKGROUND_COLOR_ONE = new Color(30, 30, 30);
    public static final Color DEFAULT_TEXT_AREA_COLOR = new Color(60, 60, 60);
    public static final Color DEFAULT_REJECTION_COLOR = new Color(112, 64, 64);
    public static final Color DEFAULT_REJECTION_COLOR_HOVERED = new Color(138, 79, 79);
    public static final Color DEFAULT_REJECTION_COLOR_PRESSED = new Color(157, 90, 90);
    public static final Color DEFAULT_REJECTION_COLOR_BORDER = new Color(136, 81, 81);
    public static final Color DEFAULT_ACCEPTANCE_COLOR = new Color(65, 112, 64);
    public static final Color DEFAULT_ACCEPTANCE_COLOR_HOVERED = new Color(79, 136, 78);
    public static final Color DEFAULT_ACCEPTANCE_COLOR_PRESSED = new Color(91, 155, 90);
    public static final Color DEFAULT_ACCEPTANCE_COLOR_BORDER = new Color(85, 138, 84);
    public static final Color DEFAULT_NEUTRAL_COLOR = new Color(64, 112, 106);
    public static final Color DEFAULT_NEUTRAL_COLOR_HOVERED = new Color(75, 131, 124);
    public static final Color DEFAULT_NEUTRAL_COLOR_PRESSED = new Color(91, 159, 150);
    public static final Color DEFAULT_NEUTRAL_COLOR_BORDER = new Color(83, 141, 134);
    public static final Color DEFAULT_PROGRESS_BAR_COLOR = new Color(50, 50, 50);
    public static final Color DEFAULT_PROGRESS_BAR_COLOR_FULL = new Color(80, 80, 80);
    public static final Color DEFAULT_TEXT_COLOR = new Color(255, 255, 255);
    public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1227, 806);
    public static final int DEFAULT_IMAGES_PER_ROW = 6;

    private ImageIcon homeImage;
    private Color buttonColor;
    private Color buttonColorHovered;
    private Color buttonColorPressed;
    private Color borderColor;
    private Color backgroundColorThree;
    private Color backgroundColorTwo;
    private Color backgroundColorOne;
    private Color textAreaColor;
    private Color rejectionColor;
    private Color rejectionColorHovered;
    private Color rejectionColorPressed;
    private Color rejectionColorBorder;
    private Color acceptanceColor;
    private Color acceptanceColorHovered;
    private Color acceptanceColorPressed;
    private Color acceptanceColorBorder;
    private Color neutralColor;
    private Color neutralColorHovered;
    private Color neutralColorPressed;
    private Color neutralColorBorder;
    private Color progressBarColor;
    private Color progressBarColorFull;
    private Color textColor;
    private String notesString;
    private boolean tracking;
    private String defaultListState;
    private String defaultDataTypeState;
    private String defaultAttributeState;
    private String defaultDirectionState;
    private String defaultAggregateState;
    private boolean defaultTypeState;
    private Dimension dimension;
    private int imagesPerRow;


    public Settings(){ // constructor
        homeImage = DEFAULT_HOME_IMAGE;
        buttonColor = DEFAULT_BUTTON_COLOR;
        buttonColorHovered = DEFAULT_BUTTON_COLOR_HOVERED;
        buttonColorPressed = DEFAULT_BUTTON_COLOR_PRESSED;
        borderColor = DEFAULT_BORDER_COLOR;
        backgroundColorThree = DEFAULT_BACKGROUND_COLOR_THREE;
        backgroundColorTwo = DEFAULT_BACKGROUND_COLOR_TWO;
        backgroundColorOne = DEFAULT_BACKGROUND_COLOR_ONE;
        textAreaColor = DEFAULT_TEXT_AREA_COLOR;
        rejectionColor = DEFAULT_REJECTION_COLOR;
        rejectionColorHovered = DEFAULT_REJECTION_COLOR_HOVERED;
        rejectionColorPressed = DEFAULT_REJECTION_COLOR_PRESSED;
        rejectionColorBorder = DEFAULT_REJECTION_COLOR_BORDER;
        acceptanceColor = DEFAULT_ACCEPTANCE_COLOR;
        acceptanceColorHovered = DEFAULT_ACCEPTANCE_COLOR_HOVERED;
        acceptanceColorPressed = DEFAULT_ACCEPTANCE_COLOR_PRESSED;
        acceptanceColorBorder = DEFAULT_ACCEPTANCE_COLOR_BORDER;
        neutralColor = DEFAULT_NEUTRAL_COLOR;
        neutralColorHovered = DEFAULT_NEUTRAL_COLOR_HOVERED;
        neutralColorPressed = DEFAULT_NEUTRAL_COLOR_PRESSED;
        neutralColorBorder = DEFAULT_NEUTRAL_COLOR_BORDER;
        progressBarColor = DEFAULT_PROGRESS_BAR_COLOR;
        progressBarColorFull = DEFAULT_PROGRESS_BAR_COLOR_FULL;
        textColor = DEFAULT_TEXT_COLOR;
        notesString = "";
        tracking = true;
        defaultListState = "list.fxml"; // list over image
        defaultDataTypeState = "Anime";
        defaultAttributeState = "Added";
        defaultDirectionState = "Descending"; // ascending over descending
        defaultAggregateState = "Month";
        defaultTypeState = true;
        dimension = DEFAULT_WINDOW_SIZE;
        imagesPerRow = DEFAULT_IMAGES_PER_ROW;
    }

    // getters and setters
    public ImageIcon getHomeImage() {
        return homeImage;
    }

    public void setHomeImage(ImageIcon homeImage) {
        this.homeImage = homeImage;
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(Color buttonColor) {
        this.buttonColor = buttonColor;
    }

    public Color getButtonColorHovered() {
        return buttonColorHovered;
    }

    public void setButtonColorHovered(Color buttonColorHovered) {
        this.buttonColorHovered = buttonColorHovered;
    }

    public Color getButtonColorPressed() {
        return buttonColorPressed;
    }

    public void setButtonColorPressed(Color buttonColorPressed) {
        this.buttonColorPressed = buttonColorPressed;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getBackgroundColorThree() {
        return backgroundColorThree;
    }

    public void setBackgroundColorThree(Color backgroundColorThree) {
        this.backgroundColorThree = backgroundColorThree;
    }

    public Color getBackgroundColorTwo() {
        return backgroundColorTwo;
    }

    public void setBackgroundColorTwo(Color backgroundColorTwo) {
        this.backgroundColorTwo = backgroundColorTwo;
    }

    public Color getBackgroundColorOne() {
        return backgroundColorOne;
    }

    public void setBackgroundColorOne(Color backgroundColorOne) {
        this.backgroundColorOne = backgroundColorOne;
    }

    public Color getTextAreaColor() {
        return textAreaColor;
    }

    public void setTextAreaColor(Color textAreaColor) {
        this.textAreaColor = textAreaColor;
    }

    public Color getRejectionColor() {
        return rejectionColor;
    }

    public void setRejectionColor(Color rejectionColor) {
        this.rejectionColor = rejectionColor;
    }

    public Color getRejectionColorHovered() {
        return rejectionColorHovered;
    }

    public void setRejectionColorHovered(Color rejectionColorHovered) {
        this.rejectionColorHovered = rejectionColorHovered;
    }

    public Color getRejectionColorPressed() {
        return rejectionColorPressed;
    }

    public void setRejectionColorPressed(Color rejectionColorPressed) {
        this.rejectionColorPressed = rejectionColorPressed;
    }

    public Color getRejectionColorBorder() {
        return rejectionColorBorder;
    }

    public void setRejectionColorBorder(Color rejectionColorBorder) {
        this.rejectionColorBorder = rejectionColorBorder;
    }

    public Color getAcceptanceColor() {
        return acceptanceColor;
    }

    public void setAcceptanceColor(Color acceptanceColor) {
        this.acceptanceColor = acceptanceColor;
    }

    public Color getAcceptanceColorHovered() {
        return acceptanceColorHovered;
    }

    public void setAcceptanceColorHovered(Color acceptanceColorHovered) {
        this.acceptanceColorHovered = acceptanceColorHovered;
    }

    public Color getAcceptanceColorPressed() {
        return acceptanceColorPressed;
    }

    public void setAcceptanceColorPressed(Color acceptanceColorPressed) {
        this.acceptanceColorPressed = acceptanceColorPressed;
    }

    public Color getAcceptanceColorBorder() {
        return acceptanceColorBorder;
    }

    public void setAcceptanceColorBorder(Color acceptanceColorBorder) {
        this.acceptanceColorBorder = acceptanceColorBorder;
    }

    public Color getNeutralColor() {
        return neutralColor;
    }

    public void setNeutralColor(Color neutralColor) {
        this.neutralColor = neutralColor;
    }

    public Color getNeutralColorHovered() {
        return neutralColorHovered;
    }

    public void setNeutralColorHovered(Color neutralColorHovered) {
        this.neutralColorHovered = neutralColorHovered;
    }

    public Color getNeutralColorPressed() {
        return neutralColorPressed;
    }

    public void setNeutralColorPressed(Color neutralColorPressed) {
        this.neutralColorPressed = neutralColorPressed;
    }

    public Color getNeutralColorBorder() {
        return neutralColorBorder;
    }

    public void setNeutralColorBorder(Color neutralColorBorder) {
        this.neutralColorBorder = neutralColorBorder;
    }

    public Color getProgressBarColor() {
        return progressBarColor;
    }

    public void setProgressBarColor(Color progressBarColor) {
        this.progressBarColor = progressBarColor;
    }

    public Color getProgressBarColorFull() {
        return progressBarColorFull;
    }

    public void setProgressBarColorFull(Color progressBarColorFull) {
        this.progressBarColorFull = progressBarColorFull;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public String getNotesString() {
        return notesString;
    }

    public void setNotesString(String notesString) {
        this.notesString = notesString;
    }

    public boolean isTracking(){
        return tracking;
    }

    public void setTracking(boolean tracking){
        this.tracking = tracking;
    }

    public String getDefaultListState() {
        return defaultListState;
    }

    public void setDefaultListState(String defaultListState) {
        this.defaultListState = defaultListState;
    }

    public String getDefaultDataTypeState() {
        return defaultDataTypeState;
    }

    public void setDefaultDataTypeState(String defaultDataTypeState) {
        this.defaultDataTypeState = defaultDataTypeState;
    }

    public String getDefaultAttributeState() {
        return defaultAttributeState;
    }

    public void setDefaultAttributeState(String defaultAttributeState) {
        this.defaultAttributeState = defaultAttributeState;
    }

    public String getDefaultDirectionState() {
        return defaultDirectionState;
    }

    public void setDefaultDirectionState(String defaultDirectionState) {
        this.defaultDirectionState = defaultDirectionState;
    }

    public String getDefaultAggregateState() {
        return defaultAggregateState;
    }

    public void setDefaultAggregateState(String defaultAggregateState) {
        this.defaultAggregateState = defaultAggregateState;
    }

    public boolean isDefaultTypeState() {
        return defaultTypeState;
    }

    public void setDefaultTypeState(boolean defaultTypeState) {
        this.defaultTypeState = defaultTypeState;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public int getImagesPerRow() {
        return imagesPerRow;
    }

    public void setImagesPerRow(int imagesPerRow) {
        this.imagesPerRow = imagesPerRow;
    }
}
