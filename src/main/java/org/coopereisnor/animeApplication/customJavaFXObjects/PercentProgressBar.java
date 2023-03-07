package org.coopereisnor.animeApplication.customJavaFXObjects;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

public class PercentProgressBar extends StackPane {
    private final int part;
    private final int whole;
    private final double progress;

    private final  Label label = new Label();

    public PercentProgressBar(int part, int whole) {
        this.part = part;
        this.whole = whole;
        this.progress = ((double)part)/((double)Math.max(whole, 1)); // no dividing by zero

        setText(false);
        ProgressBar bar = new ProgressBar();
        bar.setMaxWidth(Double.MAX_VALUE); // allows the progress bar to expand to fill available horizontal space.
        bar.setPrefHeight(35); // this will cause problems if you set the height > value, but I'll cross that bridge if I ever find myself there.
        setOnMouseEntered(mouseEvent -> setText(true));
        setOnMouseExited(mouseEvent -> setText(false));
        label.setId("progressLabel");
        bar.setProgress(progress);
        getChildren().setAll(bar, label);
    }

    // synchronizes the progress indicated with the work done.
    private void setText(boolean hovered) {
        if(hovered){
            label.setText((whole - part) +" Remaining");
        }else{
            label.setText(((int)(progress*100)) +"%");
        }
    }
}
